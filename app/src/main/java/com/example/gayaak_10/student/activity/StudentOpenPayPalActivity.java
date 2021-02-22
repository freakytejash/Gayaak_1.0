package com.example.gayaak_10.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.BuildConfig;
import com.example.gayaak_10.adapter.CartAdapter;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityStudentOpenPaypalBinding;
import com.example.gayaak_10.model.paypalinfo.PaypalPaymentInfo;
import com.example.gayaak_10.model.request.CourseSubscriptionRequest;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentOpenPayPalActivity extends AppCompatActivity {
    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = BuildConfig.paypal_client_id;

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private String currencyCode ;
    private ActivityStudentOpenPaypalBinding binding;
    private int totalPrice;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Gayaak")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentOpenPaypalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Locale locale = Locale.getDefault();
        Currency currency = Currency.getInstance(locale);
        currencyCode = "USD" ;

        PayPalPayment thingToBuy = getStuffToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent1 = new Intent(StudentOpenPayPalActivity.this, PaymentActivity.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
    }

    /*
     * This method shows use of optional payment details and item list.
     */
    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amount details
        ArrayList<PayPalItem> payPalItem = new ArrayList<PayPalItem>();
        for (int i = 0; i < App.cartList.size(); i++) {
            payPalItem.add( new PayPalItem(App.cartList.get(i).name,
                    1,
                    new BigDecimal(App.cartList.get(i).price),
                    currencyCode,
                    App.cartList.get(i).courseId.toString()));
        }
        PayPalItem[] simpleArray = new PayPalItem[payPalItem.size()];
        payPalItem.toArray(simpleArray);

        BigDecimal subtotal = PayPalItem.getItemTotal(simpleArray);
        BigDecimal shipping = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal("0.00");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, currencyCode, "Gayaak Course", paymentIntent);
        payment.items(simpleArray).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    /*
     * Add app-provided shipping address to payment
     */
    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
        ShippingAddress shippingAddress =
                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
        paypalPayment.providedShippingAddress(shippingAddress);
    }

    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }


    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(StudentOpenPayPalActivity.this, PayPalProfileSharingActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    protected void displayResultText(String result) {
        Utility.customDialogBoxTextWithSingle(StudentOpenPayPalActivity.this, "Payment Status",result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        JSONObject jsonObject =  confirm.toJSONObject();
                        PaypalPaymentInfo paypalPaymentInfo = new PaypalPaymentInfo(jsonObject);
                        //call api for subscription
                        buySubscription(paypalPaymentInfo.id);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Future Payment code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Profile Sharing code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void buySubscription(String paymentId) {
        for (int i = 0; i < App.cartList.size(); i++) {
            CourseSubscriptionRequest courseSubscriptionRequest = new CourseSubscriptionRequest();

            courseSubscriptionRequest.courseSubscriptionId = 0;
            courseSubscriptionRequest.userId = App.userDataContract.detail.userId;
            courseSubscriptionRequest.courseId = App.cartList.get(i).courseId;
            courseSubscriptionRequest.comment = "";
            courseSubscriptionRequest.price = App.cartList.get(i).price;
            courseSubscriptionRequest.startDate = DateTimeUtility.currentDateTime();
            courseSubscriptionRequest.endDate = DateTimeUtility.currentDateTime();
            courseSubscriptionRequest.paymentMethodId = 1;
            courseSubscriptionRequest.transactionId = paymentId;
            courseSubscriptionRequest.isActive = true;
            courseSubscriptionRequest.createdDate = DateTimeUtility.currentDateTime();
            courseSubscriptionRequest.modifiedDate = DateTimeUtility.currentDateTime();
            courseSubscriptionRequest.createdBy = App.userDataContract.detail.userId;
            courseSubscriptionRequest.modifiedBy = App.userDataContract.detail.userId;
            courseSubscriptionRequest.loginUserId = App.userDataContract.detail.userId;

            Call<DefaultResponse> subscriptionCall = Constant.retrofitServiceHeader.addCourseSubscription(App.userDataContract.detail.userId, courseSubscriptionRequest);
            subscriptionCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body().status){
                            App.cartList.clear();
                            openMyCourse();

                        }
                    }else {
                        Utility.customDialogBoxAuthenticating(StudentOpenPayPalActivity.this, false);

                        Gson gson = new GsonBuilder().create();
                        ErrorPojoClass mError = new ErrorPojoClass();
                        try {
                            mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Utility.customDialogBoxTextWithSingle(StudentOpenPayPalActivity.this, "Something went wrong.", mError.message);

                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {

                }
            });
        }
    }

    private void openMyCourse() {
        new MaterialAlertDialogBuilder(StudentOpenPayPalActivity.this)
                .setTitle("Payment Status")
                .setMessage("PaymentConfirmed from PayPal.")
                .setPositiveButton("Ok", (dialogInterface, i1) -> {
                    //show updating view
                    binding.courseUpdationView.setVisibility(View.VISIBLE);
                    /*App.getSubscribedCourses(() -> {
                        binding.courseUpdationView.setVisibility(View.GONE);
                        Intent intent = new Intent(OpenPayPalActivity.this, MainActivity.class);
                        intent.putExtra("openView", "MyCourse");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
                    });*/
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }


    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
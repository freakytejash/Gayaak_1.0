package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.BuildConfig;
import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentCheckoutBinding;
import com.example.gayaak_10.model.paypalinfo.PaypalPaymentInfo;
import com.example.gayaak_10.model.request.CourseSubscriptionRequest;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.model.TransactionType;
import com.example.gayaak_10.student.model.WalletRechargePlanDataContractList;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment implements View.OnClickListener {

    private FragmentCheckoutBinding binding;
    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = BuildConfig.paypal_client_id;

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private String currencyCode;
    private int totalPrice;
    private WalletRechargePlanDataContractList selectedWalletPlan;
    private String type = "";
    private StudentViewModel viewModel;
    private Context mContext;
    private Integer transactionId = 0;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Gayaak")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    private int payPalTotal = 0;


    public CheckoutFragment(WalletRechargePlanDataContractList selectedPlan, String checkoutType) {
        selectedWalletPlan = selectedPlan;
        type = checkoutType;
    }

    public CheckoutFragment(String checkoutType) {
        type = checkoutType;
    }


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);

        viewModel.getTransactionType().observe(getActivity(), new Observer<TransactionType>() {
            @Override
            public void onChanged(TransactionType transactionType) {
                if (transactionType.detail != null){
                    for (int i = 0; i < transactionType.detail.size(); i++) {
                        if (transactionType.detail.get(i).text.equalsIgnoreCase("Paypal")){
                            transactionId = Integer.valueOf(transactionType.detail.get(i).value);
                        }
                    }
                }
            }
        });


        Locale locale = Locale.getDefault();
        Currency currency = Currency.getInstance(locale);
        currencyCode = "USD";

        binding.ivPaymentBack.setOnClickListener(this);
        binding.btnPayPal.setOnClickListener(this);

        if (type.equalsIgnoreCase("planBuy")) {
            binding.tvPlanName.setText(selectedWalletPlan.name);
            int total = selectedWalletPlan.noOfCoin * selectedWalletPlan.usCurrencyValue;
            binding.tvPlanPrice.setText(total + " USD");

            double amount = Double.parseDouble(String.valueOf(total));
            /*double res = (amount / 100.0f) * 10;
            double subTotal = amount - res;*/
            payPalTotal = (int) (amount - 10);
            binding.tvSubTotal.setText(amount + " USD");
            //binding.tvTax.setText(res+ " USD");
            binding.tvTotal.setText(payPalTotal + " USD");
            binding.layoutPlans.setVisibility(View.VISIBLE);
        } else {
            binding.layoutPlans.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPaymentBack:
                StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
                break;

            case R.id.btnPayPal:
                PayPalPayment thingToBuy = getStuffToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent1 = new Intent(getActivity(), PaymentActivity.class);
                intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent1, REQUEST_CODE_PAYMENT);

                break;
        }
    }

    /*
     * This method shows use of optional payment details and item list.
     */
    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amount details
        ArrayList<PayPalItem> payPalItem = new ArrayList<PayPalItem>();

        if (type.equalsIgnoreCase("courseBuy")) {
            for (int i = 0; i < App.cartList.size(); i++) {
                payPalItem.add(new PayPalItem(App.cartList.get(i).name,
                        1,
                        new BigDecimal(App.cartList.get(i).price),
                        currencyCode, App.cartList.get(i).courseId.toString()));
            }
        } else if (type.equalsIgnoreCase("planBuy")) {
            if (selectedWalletPlan != null) {
                /*total = ((selectedWalletPlan.noOfCoin + selectedWalletPlan.extraCoin)
                 * selectedWalletPlan.usCurrencyValue);*/

                payPalItem.add(new PayPalItem(selectedWalletPlan.name,
                        1, new BigDecimal(payPalTotal), currencyCode,
                        selectedWalletPlan.walletRechargePlanId.toString()));
            }
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        JSONObject jsonObject = confirm.toJSONObject();
                        PaypalPaymentInfo paypalPaymentInfo = new PaypalPaymentInfo(jsonObject);
                        if (type.equalsIgnoreCase("courseBuy")) {
                            for (int i = 0; i < App.cartList.size(); i++) {
                                //call api for subscription
                                buySubscription(paypalPaymentInfo.id);
                            }
                        } else {
                            // update wallet coins
                            updateWalletCoins(selectedWalletPlan, paypalPaymentInfo.id);
                        }


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
                Log.i("ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void updateWalletCoins(WalletRechargePlanDataContractList selectedWalletPlan, String paymentId) {
        viewModel.updateWallet(selectedWalletPlan, paymentId, transactionId).observe(getActivity(), defaultResponse -> getUserProfile(App.userDataContract.detail.userId));
    }

    private void getUserProfile(int userId) {
        viewModel.getUserProfile(userId).observe(getActivity(), userDataProfile -> {
            if (userDataProfile != null) {
                App.userDataContract = userDataProfile;
                SharedPrefsUtil.setUserPreferences(getActivity(), userDataProfile);
            }
            if (App.selectedSessionDetail != null){
                createSession();
            }else {
                StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
            }
        });
    }

    private void createSession() {
        DemoTutorRequest demoTutorRequest = new DemoTutorRequest();
        demoTutorRequest.ScheduleId = App.selectedSessionDetail.ScheduleId1;
        demoTutorRequest.StudentTutorBookingId = 0;
        demoTutorRequest.TutorId = App.selectedSessionDetail.TutorId;
        demoTutorRequest.TutorScheduleId = String.valueOf(App.selectedSessionDetail.TutorScheduleId);
        demoTutorRequest.CourseId = App.selectedSessionDetail.courseId;
        String day=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            day = DateTimeUtility.getDateFromWeekDay(App.selectedSessionDetail.courseSelectedDay);
        }
        //App.selectedSessionDetail.courseSelectedDay + " " + App.selectedSessionDetail.courseSelectedTime;
        demoTutorRequest.Date = DateTimeUtility.convertDateTimeFormate(day, "dd MMMM yyyy", "yyyy-MM-dd") + " " + App.selectedSessionDetail.courseSelectedTime;
        demoTutorRequest.levelid = String.valueOf(App.selectedSessionDetail.levelId);
        demoTutorRequest.liveclasstype = 2;
        demoTutorRequest.classTypeId = 2;
        demoTutorRequest.categoryid = App.selectedSessionDetail.categoryId;

        viewModel.postBookingDemo(App.userDataContract.detail.userId, demoTutorRequest).observe(getActivity(), new Observer<DefaultResponse>() {
            @Override
            public void onChanged(DefaultResponse defaultResponse) {
                Utility.hideLoader();
                Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                Utility.hideLoader();
                Fragment fragmentName = null;
                String userType = SharedPrefsUtil.getStringPreferences(Objects.requireNonNull(getActivity()), "UserType");
                if (userType.equalsIgnoreCase("Free")) {
                    fragmentName = new StudentDemoHomeFragment();
                } else if (userType.equalsIgnoreCase("Paid")) {
                    fragmentName = new StudentRegularFragment();
                }
                StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
            }
        });
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
                    if (response.isSuccessful()) {
                        if (response.body().status) {
                            App.cartList.clear();
                            openMyCourse();
                        }
                    } else {
                        Utility.customDialogBoxAuthenticating(getActivity(), false);

                        Gson gson = new GsonBuilder().create();
                        ErrorPojoClass mError = new ErrorPojoClass();
                        try {
                            mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Utility.customDialogBoxTextWithSingle(getActivity(), "Something went wrong.", mError.message);

                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {

                }
            });
        }
    }


    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    protected void displayResultText(String result) {
        Utility.customDialogBoxTextWithSingle(getActivity(), "Payment Status", result);
    }

    private void openMyCourse() {
        new MaterialAlertDialogBuilder(mContext)
                .setTitle("Payment Status")
                .setMessage("PaymentConfirmed from PayPal.")
                .setPositiveButton("Ok", (dialogInterface, i1) -> {
                    dialogInterface.dismiss();
                    if (App.userDataContract.detail.roleId == 3) {
                        Intent intent1 = new Intent(getActivity(), StudentHomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
                    } else if (App.userDataContract.detail.roleId == 2) {
                        Intent intent1 = new Intent(getActivity(), TutorHomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
                    }
                })
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        mContext.stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
}

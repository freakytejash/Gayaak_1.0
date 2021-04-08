package com.example.gayaak_10.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentWebViewBinding;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.fragment.StudentEbookFragment;
import com.example.gayaak_10.student.fragment.StudentModuleFragment;
import com.example.gayaak_10.student.model.EbookDataContract;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.fragment.TutorCourseModuleFragment;
import com.example.gayaak_10.tutor.fragment.TutorEbookFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebviewFragment extends Fragment {

    private FragmentWebViewBinding binding;
    private String userType = "";
    private EbookDataContract pdfModuleDataContractList;

   /* public WebviewFragment(PDFModuleDataContractList pDFURL, String type) {
        pdfModuleDataContractList = pDFURL;
        userType = type;
    }*/

    public WebviewFragment(EbookDataContract pDFURL, String type) {
        pdfModuleDataContractList = pDFURL;
        userType = type;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWebViewBinding.inflate(getLayoutInflater());
        prepareWebView();


        binding.ivWebViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equalsIgnoreCase("Student")) {
                    StudentHomeActivity.addFragment(new StudentEbookFragment(StudentModuleFragment.moduleId), Constant.COURSE_CATALOG, getActivity());
                } else if (userType.equalsIgnoreCase("Tutor")) {
                    TutorHomeActivity.addFragment(new TutorEbookFragment(TutorCourseModuleFragment.moduleId), Constant.COURSE_CATALOG, getActivity());
                }
            }
        });
        return binding.getRoot();
    }

    private void prepareWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true); // enable javascript


        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d("webview", "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                m1.invoke(settings, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                m2.invoke(settings, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(settings, "/data/data/" + getActivity().getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(settings, 1024 * 1024 * 8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(settings, "/data/data/" + getActivity().getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", new Class[]{Boolean.TYPE});
                m6.invoke(settings, Boolean.TRUE);

                Log.d("webview", "Enabled HTML5-Features");
            } catch (NoSuchMethodException e) {
                Log.e("webview", "Reflection fail", e);
            } catch (InvocationTargetException e) {
                Log.e("webview", "Reflection fail", e);
            } catch (IllegalAccessException e) {
                Log.e("webview", "Reflection fail", e);
            }
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        ProgressDialog progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
        progressBar.setCancelable(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("webview", "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("webview", "Error: " + description);
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
      //  binding.webView.loadData(pdfModuleDataContractList.booklink, "text/html", "UTF-8");
       binding.webView.loadUrl(pdfModuleDataContractList.booklink);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

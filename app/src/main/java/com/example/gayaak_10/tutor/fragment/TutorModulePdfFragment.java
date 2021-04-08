package com.example.gayaak_10.tutor.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentModulePdfBinding;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.adapter.CourseModulePdfAdapter;
import com.example.gayaak_10.student.model.ModuleWithVideo;
import com.example.gayaak_10.student.model.PDFModuleDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;

import java.util.ArrayList;

public class TutorModulePdfFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentModulePdfBinding binding;
    private StudentViewModel viewModel;
    private CourseModuleDetail courseModuleDetail;

    public TutorModulePdfFragment(CourseModuleDetail courseModuleDetail) {
        this.courseModuleDetail = courseModuleDetail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentModulePdfBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);

        getPdfs();

        binding.ivModuleInfoBack.setOnClickListener(this);
        return binding.getRoot();
    }

    private void getPdfs() {
        viewModel.getModulePdf(1,/*courseModuleDetail.moduleId*/App.userDataContract.detail.userId).observe(getActivity(), new Observer<ModuleWithVideo>() {
            @Override
            public void onChanged(ModuleWithVideo moduleWithVideo) {
                if (moduleWithVideo.detail.pDFModuleDataContractList != null && !moduleWithVideo.detail.pDFModuleDataContractList.isEmpty()) {
                    binding.layoutPdf.setVisibility(View.VISIBLE);
                    binding.layoutEmptyLibrary.setVisibility(View.GONE);
                    setModuleView(moduleWithVideo.detail.pDFModuleDataContractList);
                } else {
                    binding.layoutPdf.setVisibility(View.GONE);
                    binding.layoutEmptyLibrary.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setModuleView(ArrayList<PDFModuleDataContractList> detail) {
        binding.rvModulePDF.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CourseModulePdfAdapter adapter = new CourseModulePdfAdapter(getActivity(), detail, new CourseModulePdfAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               /* binding.layoutPdf.setVisibility(View.GONE);
                binding.layoutEmptyLibrary.setVisibility(View.GONE);
                binding.webViewPdf.setVisibility(View.VISIBLE);*/
                // prepareWebView(detail.get(position));

                try {
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(detail.get(position).pDFURL), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intentUrl);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }

            }
        });
        binding.rvModulePDF.setAdapter(adapter);
    }

    private void prepareWebView(PDFModuleDataContractList pdfModuleDataContractList) {
        binding.webViewPdf.requestFocus();
        binding.webViewPdf.getSettings().setJavaScriptEnabled(true);
        String url = pdfModuleDataContractList.pDFURL;
        binding.webViewPdf.loadUrl(url);
        binding.webViewPdf.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        binding.webViewPdf.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    // progressDialog.show();
                }
                if (progress == 100) {
                    //   progressDialog.dismiss();
                }
            }
        });



     /*   binding.webViewPdf.loadUrl(pdfModuleDataContractList.pDFURL);
        binding.webViewPdf.getSettings().setJavaScriptEnabled(true);
        binding.webViewPdf.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webViewPdf.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webViewPdf.getSettings().setDomStorageEnabled(true);
        binding.webViewPdf.getSettings().setLoadWithOverviewMode(true);
        binding.webViewPdf.getSettings().setSupportMultipleWindows(true);
        binding.webViewPdf.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webViewPdf.setHorizontalScrollBarEnabled(true);

        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        };
        binding.webViewPdf.setWebViewClient(webViewClient);*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivModuleInfoBack:
                if (binding.webViewPdf.getVisibility() == View.VISIBLE) {
                    getPdfs();
                    binding.layoutPdf.setVisibility(View.VISIBLE);
                    binding.layoutEmptyLibrary.setVisibility(View.GONE);
                    binding.webViewPdf.setVisibility(View.GONE);
                } else {
                    TutorHomeActivity.addFragment(new TutorCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                }
                break;
        }
    }
}

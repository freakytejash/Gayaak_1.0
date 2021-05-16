package com.example.gayaak_10.student.fragment;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.adapter.NewEBookAdapter;
import com.example.gayaak_10.common.viewmodel.CommonViewModel;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentEbookBinding;
import com.example.gayaak_10.fragment.WebviewFragment;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.model.EbookDataContract;
import com.example.gayaak_10.utility.Utility;

import java.io.File;
import java.util.ArrayList;

public class StudentEbookFragment extends Fragment implements View.OnClickListener {

    private FragmentEbookBinding binding;
    private CommonViewModel viewModel;
    private Integer courseId;
    private ArrayList<EbookDataContract> ebookDataContractArrayList = new ArrayList();

    public StudentEbookFragment(Integer courseId) {
        this.courseId = courseId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEbookBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(CommonViewModel.class);
        binding.tvHeaderBooks.setText("EBooks");
        binding.ivModuleInfoBack.setOnClickListener(this);

        if (!Utility.hasPermissions(getActivity(), Utility.PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), Utility.PERMISSIONS, Utility.PERMISSION_ALL);
        }

       /* viewModel.getModulePdf(courseId, App.userDataContract.detail.userId).observe(getActivity(), new Observer<ModuleWithVideo>() {
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
        });*/

        viewModel.getModuleEbook(courseId).observe(getActivity(), moduleEbooks -> {
            if (moduleEbooks.detail != null && moduleEbooks.detail.courseEbookDataContractList != null){
                for (int i = 0; i < moduleEbooks.detail.courseEbookDataContractList.size(); i++) {
                    if (moduleEbooks.detail.courseEbookDataContractList.get(i).ebookDataContract != null){
                        ebookDataContractArrayList.add(moduleEbooks.detail.courseEbookDataContractList.get(i).ebookDataContract);
                    }
                    if (moduleEbooks.detail.courseEbookDataContractList.size() == ebookDataContractArrayList.size()){
                        setEBookView(ebookDataContractArrayList);
                        break;
                    }
                }
            }else {
                binding.layoutPdf.setVisibility(View.GONE);
                binding.layoutEmptyLibrary.setVisibility(View.VISIBLE);
            }
        });
        return binding.getRoot();
    }

    private void setEBookView(ArrayList<EbookDataContract> ebookDataContractArrayList) {
        binding.layoutPdf.setVisibility(View.VISIBLE);
        binding.layoutEmptyLibrary.setVisibility(View.GONE);
        NewEBookAdapter adapter = new NewEBookAdapter(getActivity(), ebookDataContractArrayList,
                new NewEBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    StudentHomeActivity.addFragment(new WebviewFragment(ebookDataContractArrayList.get(position), "Student"), "WebView", getActivity());
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDownloadEbook(int position) {
                if (!ebookDataContractArrayList.get(position).imageName.isEmpty()){
                    Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                    DownloadFiles(ebookDataContractArrayList.get(position).imageName, ebookDataContractArrayList.get(position).bookName);
// DownloadFiles("https://www.tutorialspoint.com/android/android_tutorial.pdf", pDFModuleDataContractList.get(position).name);

                }else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.rvEbooks.setAdapter(adapter);
    }

/*    private void setModuleView(ArrayList<PDFModuleDataContractList> pDFModuleDataContractList) {
        StudentEbookAdapter adapter = new StudentEbookAdapter(getActivity(), pDFModuleDataContractList, new StudentEbookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    StudentHomeActivity.addFragment(new WebviewFragment(pDFModuleDataContractList.get(position), "Student"), "WebView", getActivity());
                   *//* Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(pDFModuleDataContractList.get(position).pDFURL), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intentUrl);*//*
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDownloadEbook(int position) {
                Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                DownloadFiles(pDFModuleDataContractList.get(position).pDFURL, pDFModuleDataContractList.get(position).name);
// DownloadFiles("https://www.tutorialspoint.com/android/android_tutorial.pdf", pDFModuleDataContractList.get(position).name);

            }
        });
        binding.rvEbooks.setAdapter(adapter);
    }*/


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivModuleInfoBack:
                StudentHomeActivity.addFragment(new StudentCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                break;
        }
    }

    public void DownloadFiles(String url, String name) {
        try {
            Log.e("url", "DownloadFiles: " +url );
            DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            File folder = new File(Environment.getExternalStorageDirectory() + "/Gayaak/Ebook");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(name+".pdf");
            request.setDescription("Downloading..");
           // request.setMimeType("application/pdf");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
           // request.setDestinationInExternalFilesDir(getActivity(), Environment.getExternalStorageDirectory() + "/Gayaak/Ebook", name);
            request.allowScanningByMediaScanner();
            downloadmanager.enqueue(request);

        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }
}

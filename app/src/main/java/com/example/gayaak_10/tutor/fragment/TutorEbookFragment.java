package com.example.gayaak_10.tutor.fragment;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.adapter.NewEBookAdapter;
import com.example.gayaak_10.common.viewmodel.CommonViewModel;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentEbookBinding;
import com.example.gayaak_10.fragment.WebviewFragment;
import com.example.gayaak_10.student.model.EbookDataContract;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;

import java.util.ArrayList;

public class TutorEbookFragment extends Fragment implements View.OnClickListener {

    private FragmentEbookBinding binding;
    private CommonViewModel viewModel;
    private Integer courseId;
    private ArrayList<EbookDataContract> ebookDataContractArrayList = new ArrayList();

    public TutorEbookFragment(Integer courseId) {
        this.courseId = courseId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEbookBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(CommonViewModel.class);
        binding.tvHeaderBooks.setText("EBooks");
        binding.ivModuleInfoBack.setOnClickListener(this);

      /*  viewModel.getModulePdf(courseId, App.userDataContract.detail.userId).observe(getActivity(), new Observer<ModuleWithVideo>() {
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
*/

        viewModel.getModuleEbook(courseId).observe(getActivity(), moduleEBooks -> {
            if (moduleEBooks.detail != null && moduleEBooks.detail.courseEbookDataContractList != null) {
                for (int i = 0; i < moduleEBooks.detail.courseEbookDataContractList.size(); i++) {
                    if (moduleEBooks.detail.courseEbookDataContractList.get(i).ebookDataContract != null) {
                        ebookDataContractArrayList.add(moduleEBooks.detail.courseEbookDataContractList.get(i).ebookDataContract);
                    }
                    if (moduleEBooks.detail.courseEbookDataContractList.size() == ebookDataContractArrayList.size()) {
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
                            TutorHomeActivity.addFragment(new WebviewFragment(ebookDataContractArrayList.get(position), "Tutor"), "WebView", getActivity());
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDownloadEbook(int position) {
                        if (!ebookDataContractArrayList.get(position).imageName.isEmpty()) {
                            Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                            DownloadFiles(ebookDataContractArrayList.get(position).imageName, ebookDataContractArrayList.get(position).bookName);
                            // DownloadFiles("https://www.tutorialspoint.com/android/android_tutorial.pdf", pDFModuleDataContractList.get(position).name);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        binding.rvEbooks.setAdapter(adapter);
    }

   /* private void setModuleView(ArrayList<PDFModuleDataContractList> pDFModuleDataContractList) {
        TutorEbookAdapter adapter = new TutorEbookAdapter(getActivity(), pDFModuleDataContractList , new TutorEbookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TutorHomeActivity.addFragment(new WebviewFragment(pDFModuleDataContractList.get(position), "Tutor"), "WebView", getActivity());
               *//* try
                {
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(pDFModuleDataContractList.get(position).pDFURL), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intentUrl);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getActivity(), "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }*//*
            }

            @Override
            public void onDownload(int position) {
                Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                DownloadFiles(pDFModuleDataContractList.get(position).pDFURL,pDFModuleDataContractList.get(position).name);
            }
        });
        binding.rvEbooks.setAdapter(adapter);
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivModuleInfoBack:
                TutorHomeActivity.addFragment(new TutorCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                break;
        }
    }

    public void DownloadFiles(String url, String name) {
        try {
            DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(name);
            request.setDescription("Downloading..");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
            downloadmanager.enqueue(request);

        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }
}

package com.example.gayaak_10.zoom.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityOpenZoomBinding;
import com.example.gayaak_10.initsdk.InitAuthSDKCallback;
import com.example.gayaak_10.initsdk.InitAuthSDKHelper;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.tutor.model.TodaySessions;
import com.example.gayaak_10.zoom.UserLoginCallback;
import com.example.gayaak_10.zoom.ZoomMeetingUISettingHelper;
import com.example.gayaak_10.zoom.customizedmeetingui.MyMeetingActivity;
import com.example.gayaak_10.zoom.customizedmeetingui.RawDataMeetingActivity;
import com.example.gayaak_10.zoom.customizedmeetingui.SimpleZoomUIDelegate;
import com.example.gayaak_10.zoom.customizedmeetingui.view.MeetingWindowHelper;

import us.zoom.sdk.InMeetingNotificationHandle;
import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;

public class OpenZoomViewActivity extends Activity implements InitAuthSDKCallback,
        MeetingServiceListener, UserLoginCallback.ZoomDemoAuthenticationListener, OnClickListener {

    private final static String TAG = "ZoomSDKExample";
    private ZoomSDK mZoomSDK;
    private LiveClassDataContractList sessionInfo;
    private TodaySessions todaySessions;
    private ActivityOpenZoomBinding binding;

    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenZoomBinding.inflate(getLayoutInflater());
        mZoomSDK = ZoomSDK.getInstance();
        if (mZoomSDK.isLoggedIn()) {
            finish();
            //showEmailLoginUserStartJoinActivity();
            return;
        }
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("sessionInfo")) {
            sessionInfo = (LiveClassDataContractList) getIntent().getSerializableExtra("sessionInfo");
            binding.tvCourseName.setText(sessionInfo.categoryName);
            binding.tvCourseLevel.setText(sessionInfo.LevelName);
            binding.tvCourseTutorName.setText(sessionInfo.tutorName);
            Glide.with(this).load(sessionInfo.TutorProfileImage)
                    .placeholder(getDrawable(R.drawable.ic_profile))
                    .into(binding.ivCourseTutor);
        } else if (getIntent().hasExtra("tutorSessionInfo")) {
            todaySessions = (TodaySessions) getIntent().getSerializableExtra("tutorSessionInfo");
            Constant.todaySessionsData = todaySessions;
            binding.tvCourseName.setText(todaySessions.CategoryName);
            binding.tvCourseLevel.setText(todaySessions.LevelName);
            Glide.with(this).load(todaySessions.StudentProfileImage)
                    .placeholder(getDrawable(R.drawable.ic_profile))
                    .into(binding.ivCourseTutor);
        }

        binding.ivBackZoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        InitAuthSDKHelper.getInstance().initSDK(this, this);

        if (mZoomSDK.isInitialized()) {
            View view = findViewById(R.id.btnSettings);
            if (null != view) {
                view.setVisibility(View.VISIBLE);
            }
            ZoomSDK.getInstance().getMeetingService().addListener(this);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(true);
            binding.progressInitializing.setVisibility(View.INVISIBLE);
            //   binding.btnStartClass.setEnabled(true);
            onClickJoin();
        } else {
            binding.progressInitializing.setVisibility(View.VISIBLE);
            binding.btnStartClass.setEnabled(false);
            InitAuthSDKHelper.getInstance().initSDK(this, this);
        }


        binding.btnStartClass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickJoin();
            }
        });
    }

    InMeetingNotificationHandle handle = new InMeetingNotificationHandle() {

        @Override
        public boolean handleReturnToConfNotify(Context context, Intent intent) {
            intent = new Intent(context, MyMeetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setAction(InMeetingNotificationHandle.ACTION_RETURN_TO_CONF);
            context.startActivity(intent);
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        MeetingStatus meetingStatus = ZoomSDK.getInstance().getMeetingService().getMeetingStatus();
        if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            /*Initialize Zoom SDK successfully*/
            ZoomSDK.getInstance().getZoomUIService().enableMinimizeMeeting(false);
            setMiniWindows();
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(false);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enableShowMyMeetingElapseTime(true);
            ZoomSDK.getInstance().getMeetingService().addListener(this);
            ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);
            ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedNotificationData(null, handle);
            binding.progressInitializing.setVisibility(View.INVISIBLE);
            // binding.btnStartClass.setEnabled(true);
            onClickJoin();
            if (mZoomSDK.tryAutoLoginZoom() == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
                UserLoginCallback.getInstance().addListener(this);
                showProgressPanel(true);
            } else {
                showProgressPanel(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!mZoomSDK.isInitialized()) {
            /*"Init SDK First"*/
            InitAuthSDKHelper.getInstance().initSDK(this, this);
            return;
        }
    }


    @Override
    public void onZoomSDKLoginResult(long result) {
        if ((int) result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            finish();
        } else {
            showProgressPanel(false);
        }
    }

    @Override
    public void onZoomSDKLogoutResult(long result) {

    }

    @Override
    public void onZoomIdentityExpired() {
        Log.e(TAG, "onZoomIdentityExpired");
        if (mZoomSDK.isLoggedIn()) {
            mZoomSDK.logoutZoom();
        }
    }

    @Override
    public void onZoomAuthIdentityExpired() {
        Log.d(TAG, "onZoomAuthIdentityExpired");
    }

    public void onClickJoin() {
        if (!mZoomSDK.isInitialized()) {
            /*"Init SDK First"*/
            InitAuthSDKHelper.getInstance().initSDK(this, this);
            return;
        }

        ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);

        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            ZoomSDK.getInstance().getSmsService().enableZoomAuthRealNameMeetingUIShown(false);
        } else {
            ZoomSDK.getInstance().getSmsService().enableZoomAuthRealNameMeetingUIShown(true);
        }

        binding.progressBarHome.setVisibility(View.GONE);
        JoinMeetingParams params = new JoinMeetingParams();
        params.meetingNo = Constant.meetingNo;
        params.password = Constant.meetingPassword;
        params.displayName = App.userDataContract.detail.firstName;

       /* params.displayName = DISPLAY_NAME;
        params.meetingNo = meetingNo;
        params.password = meetingPassword;*/


        JoinMeetingOptions options = new JoinMeetingOptions();
        ZoomSDK.getInstance().getMeetingService().joinMeetingWithParams(this, params, ZoomMeetingUISettingHelper.getJoinMeetingOptions());
    }

    private void showProgressPanel(boolean show) {
        if (show) {
            View view = findViewById(R.id.btnSettings);
            if (null != view) {
                view.setVisibility(View.GONE);
            }
        } else {
            View view = findViewById(R.id.btnSettings);
            if (null != view) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        refreshUI();

        setMiniWindows();
    }

    private void setMiniWindows() {
        if (null != mZoomSDK && mZoomSDK.isInitialized() && !mZoomSDK.getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            ZoomSDK.getInstance().getZoomUIService().setZoomUIDelegate(new SimpleZoomUIDelegate() {
                @Override
                public void afterMeetingMinimized(Activity activity) {
                    Intent intent = new Intent(activity, OpenZoomViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    private void refreshUI() {
        if (!ZoomSDK.getInstance().isInitialized()) {
            return;
        }
        MeetingStatus meetingStatus = ZoomSDK.getInstance().getMeetingService().getMeetingStatus();
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING && isResumed) {
                MeetingWindowHelper.getInstance().showMeetingWindow(this);
                showProgressPanel(true);
            } else {
                MeetingWindowHelper.getInstance().hiddenMeetingWindow(true);
                showProgressPanel(false);
            }
        } else {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING && isResumed) {
                showProgressPanel(true);
            } else {
                showProgressPanel(false);
            }
        }
    }


    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode, int internalErrorCode) {
        Log.d(TAG, "onMeetingStatusChanged " + meetingStatus + ":" + errorCode + ":" + internalErrorCode);
        if (!ZoomSDK.getInstance().isInitialized()) {
            showProgressPanel(false);
            return;
        }
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            if (ZoomMeetingUISettingHelper.useExternalVideoSource) {
                ZoomMeetingUISettingHelper.changeVideoSource(true);
            }
        }
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
                showMeetingUi();
            }
        }
        refreshUI();
    }

    private void showMeetingUi() {
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            SharedPreferences sharedPreferences = getSharedPreferences("UI_Setting", Context.MODE_PRIVATE);
            boolean enable = sharedPreferences.getBoolean("enable_rawdata", false);
            Intent intent = null;
            if (!enable) {
                intent = new Intent(this, MyMeetingActivity.class);
                intent.putExtra("from", MyMeetingActivity.JOIN_FROM_UNLOGIN);
            } else {
                intent = new Intent(this, RawDataMeetingActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MeetingWindowHelper.getInstance().onActivityResult(requestCode, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserLoginCallback.getInstance().removeListener(this);

        if (null != ZoomSDK.getInstance().getMeetingService()) {
            ZoomSDK.getInstance().getMeetingService().removeListener(this);
        }
        InitAuthSDKHelper.getInstance().reset();
    }
}
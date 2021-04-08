package com.example.gayaak_10.zoom.customizedmeetingui.bo;

import com.example.gayaak_10.zoom.customizedmeetingui.BaseCallback;
import com.example.gayaak_10.zoom.customizedmeetingui.BaseEvent;

import us.zoom.sdk.IBOAdmin;
import us.zoom.sdk.IBOAdminEvent;
import us.zoom.sdk.ZoomSDK;

public class BOEventCallback extends BaseCallback<BOEventCallback.BOEvent> {
    private static BOEventCallback instance;

    private BOEventCallback() {
        init();
    }

    protected void init() {
        IBOAdmin iboAdmin = ZoomSDK.getInstance().getInMeetingService().getInMeetingBOController().getBOAdminHelper();
        if(iboAdmin != null)
            iboAdmin.setEvent(iboAdminEvent);
    }

    public static BOEventCallback getInstance() {
        if (null == instance) {
            synchronized (BOEventCallback.class) {
                if (null == instance) {
                    instance = new BOEventCallback();
                }
            }
        }
        return instance;
    }

    private IBOAdminEvent iboAdminEvent = new IBOAdminEvent() {
        @Override
        public void onHelpRequestReceived(String strUserID) {
            for (BOEvent event : callbacks) {
                event.onHelpRequestReceived(strUserID);
            }
        }
    };

    public void addEvent(BOEvent event) {
        super.addListener(event);

        IBOAdmin iboAdmin = ZoomSDK.getInstance().getInMeetingService().getInMeetingBOController().getBOAdminHelper();
        if(iboAdmin != null)
            iboAdmin.setEvent(iboAdminEvent);
    }

    public void removeEvent(BOEvent event) {
        super.removeListener(event);
    }

    public interface BOEvent extends BaseEvent {
        void onHelpRequestReceived(String strUserID);
    }
}

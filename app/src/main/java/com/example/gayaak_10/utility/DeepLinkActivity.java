package com.example.gayaak_10.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.login_register.SplashScreenActivity;

class DeepLinkActivity extends AppCompatActivity {

    private Context mContext = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
        mContext = this;

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String action = intent.getAction();
        String data = intent.getDataString();

        try {
            if (Intent.ACTION_VIEW == action && data != null) {
                data = data.replace("room120://", "");
                Log.d("deeplink", " -> $data");

                startActivity(new Intent(DeepLinkActivity.this, SplashScreenActivity.class));
                finish();

               /* if (data.contains("sessionId")) {
                    val splitData = data.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val videoSession = splitData[1]
                    val sessionValue = videoSession.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val sessionId = sessionValue[1]
                    val splittokenId = sessionId.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var id = splittokenId[0]
                    id = id.replace("/", "")
                    invitedSessionId = id

                    UserModel.token = Utils.getStringPreferences(this@DeepLinkActivity, Constant.userToken)
                    if (UserModel.token.isNotEmpty()) {
                        startActivity(Intent(this@DeepLinkActivity, HomeActivity::class.java))
                        finish()
                    } else {

                    }

                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
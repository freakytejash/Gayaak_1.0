package com.example.gayaak_10.services;

import com.example.gayaak_10.utility.SharedPrefsUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInjectInterceptor implements Interceptor {

    public static final String NO_TOKEN_HEADER = "X-No-Token";
    static final String DONT_ADD_AUTH_TOKEN = NO_TOKEN_HEADER + ": true";
    private String AUTHORIZATION = "token";
    private String USERID = "userid";
    private String TOKEN_HEADER = "Token token=";
    private String Token = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPrefsUtil.getStringPreferences(App.appContext, "userToken");
        String userId = SharedPrefsUtil.getStringPreferences(App.appContext, "userId");
        Request original = chain.request();

        boolean noToken = original.header(NO_TOKEN_HEADER) != null;

        Request request;
        if (!noToken) {
            Headers headers = original.headers().newBuilder()
                    .removeAll(NO_TOKEN_HEADER)
                    .set(AUTHORIZATION, token)
                    .set(USERID, userId)
                    .build();

           request =  original.newBuilder().headers(headers).build();
        } else {
            request =  original;
        }

        return chain.proceed(request);
    }
}

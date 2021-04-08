package com.example.gayaak_10.constants;

import com.example.gayaak_10.services.ServiceApi;
import com.example.gayaak_10.services.TokenInjectInterceptor;
import com.example.gayaak_10.tutor.model.TodaySessions;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constant {

    /*-----------------------------------------RETROFIT API-----------------------------------------------------------*/

    public static String baseUrl = "http://103.255.190.131/GaayakAPI/api/";
    public static String token = "";
    public static String userId = "";
    public static String meetingNo = "";
    public static String meetingPassword = "";
    public static Integer liveClassId=0;
    public static String StudentName="";
    public static String StartTimer = "24";
    public static String ShowStartClass = "20";
    public static String EndStartClass = "5";
    public static TodaySessions todaySessionsData = new TodaySessions();

    TokenInjectInterceptor tokenInjectInterceptor = new TokenInjectInterceptor();

    static OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("token", token)
                            .addHeader("x-userid", userId).build();
                    return chain.proceed(request);
                }
            }).build();

    public static ServiceApi retrofitServiceHeader = new Retrofit.Builder().baseUrl(baseUrl)
            .client(defaultHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceApi.class);

    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static ServiceApi retrofitService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceApi.class);

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }


                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }


                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newBuilder().sslSocketFactory(sslSocketFactory);
            okHttpClient.newBuilder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    //Shared preference keys
    public static String USER_DATA = "SavedUserData";
    public static String CART_DATA = "SavedCartData";
    public static final String COUNTRY = "country";
    public static String VIDEO_DATA = "VideoData";

    /*----------------------------------------------------------------------------------------------------------------*/
    public static String HOME = "Home";
    public static String COURSE_CATALOG = "CourseCatalog";
    public static String CALENDAR = "Calendar";
    public static String PROFILE = "UserProfile";
    public static final String OPEN_CART = "open_cart";
    public static String ZOOM_SESSION_FINISHED = "zoomSessionFinished";


    public static String TIMEZONE = "";
}

package com.example.gayaak_10.model.paypalinfo;

import org.json.JSONException;
import org.json.JSONObject;

public class PaypalPaymentInfo {

    public String create_time;
    public String id;
    public JSONObject response;
    public String responseType;
    public JSONObject client;

    public PaypalPaymentInfo(JSONObject jsonObject){
        try {
            responseType = jsonObject.getString("response_type");
            client = jsonObject.getJSONObject("client");
            response = jsonObject.getJSONObject("response");

            id = response.getString("id");
            create_time = response.getString("create_time");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


/* "client": {
            "environment": "mock",
            "paypal_sdk_version": "2.16.0",
            "platform": "Android",
            "product_name": "PayPal-Android-SDK"
        },
        "response": {
            "create_time": "2014-02-12T22:29:49Z",
            "id": "PAY-6RV70583SB702805EKEYSZ6Y",
            "intent": "sale",
            "state": "approved"
        },
        "response_type": "payment"
    }
*/
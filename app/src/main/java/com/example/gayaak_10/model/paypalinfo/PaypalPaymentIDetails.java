package com.example.gayaak_10.model.paypalinfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaypalPaymentIDetails {

    public JSONArray items;
    public JSONObject response;
    public JSONObject client;
    public String quantity;
    public String name;
    public String price;
    public String currency;
    public String sku;

    public PaypalPaymentIDetails(JSONObject jsonObject){
        try {
            response = jsonObject.getJSONObject("item_list");

            items = response.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                quantity = items.getJSONObject(i).getString("quantity");
                name = items.getJSONObject(i).getString("name");

//                price = items.getString("price");
//                currency = items.getString("currency");
//                sku = items.getString("sku");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


/*
2020-08-13 12:27:11.942 27089-27089/com.example.gayaak_10 I/paymentExample: {
        "amount": "100.00",
        "currency_code": "USD",
        "details": {
            "shipping": "0.00",
            "subtotal": "100.00",
            "tax": "0.00"
        },
        "short_description": "sample item",
        "intent": "sale",
        "item_list": {
            "items": [
                {
                    "quantity": "1",
                    "name": "Carnatic Music - Level 1",
                    "price": "100",
                    "currency": "USD",
                    "sku": ""
                }
            ]
        }*/
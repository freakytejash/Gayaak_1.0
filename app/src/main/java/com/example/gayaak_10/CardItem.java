package com.example.gayaak_10;

import com.example.gayaak_10.model.response.CoursesDetail;

public class CardItem extends CoursesDetail {

    //private int mTextResource;
    private int mTitleResource;


    public CardItem(int title) {
        mTitleResource = title;
        //mTextResource = text;

    }

   /* public int getText() {
        return mTextResource;
    }*/

    public int getTitle() {
        return mTitleResource;
    }


}

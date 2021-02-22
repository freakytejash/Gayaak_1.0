package com.example.gayaak_10.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.gayaak_10.R;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity context;
    public Dialog d;
    public Button yes, no;
    public TextView txt_dia;
    private DecisionInterface decisionInterface;
    private String title;

    public CustomDialogClass(Activity context, DecisionInterface decisionInterface, String title) {
        super(context);
        this.context = context;
        this.decisionInterface = decisionInterface;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        txt_dia = findViewById(R.id.txt_dia);
        yes = (Button) findViewById(R.id.btnYes);
        no = (Button) findViewById(R.id.btnNo);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        txt_dia.setText(title);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnYes:
                dismiss();
                decisionInterface.decisionYes();
                break;
            case R.id.btnNo:
                dismiss();
                decisionInterface.decisionNo();
                break;
            default:
                break;
        }
        dismiss();
    }
}


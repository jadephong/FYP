package com.example.fyp;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jadephong on 24/1/2016.
 */
public class DialogCustom
{
    private  Context context;
    public  DialogCustom(Context context)
    {
        this.context=context;
    }
    public void alert(String title,String content){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }
    public SweetAlertDialog confimation(String title,String content) {
        SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmText("Yes!");

        return sweetAlertDialog;
    }

    public void success(String title,String content){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }
}

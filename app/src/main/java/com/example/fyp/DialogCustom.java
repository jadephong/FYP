package com.example.fyp;

import android.content.Context;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

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
    public NiftyDialogBuilder alert(){
        final NiftyDialogBuilder dialogBuilder;
        dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitleColor("#FFFFFFFF") //def
                .withDividerColor("#FFFFFFFF")
                .withMessageColor("#FFFFFF")
                .withDialogColor("#4c4c4c")
                .isCancelableOnTouchOutside(true)
                .withDuration(700)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .withEffect(Effectstype.Shake);

        return dialogBuilder;
    }
}

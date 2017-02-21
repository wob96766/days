package com.mindspree.days.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindspree.days.R;


public class LoadingImageDialog extends Dialog {

    public LoadingImageDialog(Context context, int styleId) {
        super(context, styleId);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress_image);
        setCancelable(false);
    }
}

package com.uk.location.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;

public class SpinnerHelper {
    public SpinnerHelper(){}

    public boolean disableDefaultItem(int position){
        if (position == 0) {
            return false;
        }
        return true;
    }
}

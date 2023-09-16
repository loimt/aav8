package com.bkav.aiotcloud.util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bkav.aiotcloud.R;

public class CustomToast  extends Toast {
    public CustomToast(Context context) {
        super(context);
    }
    public static Toast makeText(Context context, String message, boolean isEr) {
        Toast toast = new Toast(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false);
        TextView l1 = (TextView) layout.findViewById(R.id.toast_text);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.toast_type);
        l1.setText(message);
        if (!isEr) {
            linearLayout.setBackgroundResource(R.drawable.success_shape);
        }  else {
            linearLayout.setBackgroundResource(R.drawable.error_shape);
        }
        toast.setView(layout);
        toast.show();

        return toast;
    }
}

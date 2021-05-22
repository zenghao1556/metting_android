package com.taiyi.metting.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taiyi.metting.R;

public class ToastUtil {

    private static Toast mToast;

    /**
     * 传入文字,在顶部显示
     */
    public static void showToast(Context context, String text, int resourceId,int time) {
        try {
            if (mToast == null) {
                mToast = new Toast(context);
                LinearLayout view = (LinearLayout) View.inflate(context, R.layout.layout_network_tips, null);
                TextView textView = view.findViewById(R.id.tv_message);
                textView.setBackgroundResource(resourceId);
                textView.setText(text);
                mToast.setView(view);
            } else {
                LinearLayout view = (LinearLayout) View.inflate(context, R.layout.layout_network_tips, null);
                TextView textView = view.findViewById(R.id.tv_message);
                textView.setBackgroundResource(resourceId);
                textView.setText(text);
                mToast.setView(view);
            }
            mToast.setDuration(time);

            mToast.setGravity(Gravity.CENTER, 0, 100);
            mToast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

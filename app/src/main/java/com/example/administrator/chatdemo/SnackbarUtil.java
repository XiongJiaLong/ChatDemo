package com.example.administrator.chatdemo;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Administrator on 2017/1/3.
 */

public class SnackbarUtil {
    public static void createSnackBar(final View layout, String s){
        Snackbar.make(layout,s,Snackbar.LENGTH_SHORT)
                .setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(layout,"处理完成",Snackbar.LENGTH_SHORT).show();
            }
        }).show();
    }
}

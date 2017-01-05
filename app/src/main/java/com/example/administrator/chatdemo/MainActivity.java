package com.example.administrator.chatdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

public class MainActivity extends AppCompatActivity {

    EditText et_name;
    Button btn_login;
    String name;
    CoordinatorLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = (EditText) findViewById(R.id.main_et_name);
        btn_login = (Button) findViewById(R.id.btn_login);
        layout = (CoordinatorLayout) findViewById(R.id.container);
        SharedPreferences sharedPreferences = getSharedPreferences("user_name",MODE_PRIVATE);
        String string = sharedPreferences.getString("name","");
        Log.e("===========","===string========="+string);
        if (string!=null){
            if (string.length()>0){
                et_name.setText(string);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_login.setClickable(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                if (name==null|| name.length()<=0){
                    SnackbarUtil.createSnackBar(layout,"名字不可为空");
                }else {
                    SharedPreferences sp = getSharedPreferences("user_name",MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("name",name);
                    edit.commit();
                    Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            }
        });
    }
}

package com.example.administrator.chatdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.chatdemo.adapter.ChatNameListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listView;
    Button btn_add;
    Button btn_clean;
    List<String> list;
    String name;
    ChatNameListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        findViews();
        initData();
    }

    private void findViews(){
        list = new ArrayList<>();
        listView = (ListView) findViewById(R.id.chatNameList);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_clean = (Button) findViewById(R.id.btn_clean);

        btn_add.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initData(){
        adapter = new ChatNameListViewAdapter(list,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                addContact();
                break;
            case R.id.btn_clean:
                if(list != null && list.size()>0){
                    list = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void addContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setTitle("请输入联系人昵称...");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (str == null){
                    Toast.makeText(ChatListActivity.this, "请输入联系人昵称", Toast.LENGTH_SHORT).show();
                }else {
                    if (str.length()<=0 || str.contains(" ")){
                        Toast.makeText(ChatListActivity.this, "你输入的昵称不合格", Toast.LENGTH_SHORT).show();
                    }else {
                        list.add(str);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ChatListActivity.this,ChatActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("toName",list.get(position));
        startActivity(intent);
    }
}

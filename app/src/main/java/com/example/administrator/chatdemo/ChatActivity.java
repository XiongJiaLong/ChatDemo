package com.example.administrator.chatdemo;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.java_websocket.exceptions.InvalidFrameException;
import com.avos.avoscloud.java_websocket.util.Charsetfunctions;
import com.example.administrator.chatdemo.adapter.ChatMessageListViewAdapter;
import com.example.administrator.chatdemo.interfaces.GetOffMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    String name;
    String toName;
    String sendContent;
    String chatName;
    Button btn_send;
    AVIMClient client;
    ListView listView;
    TextView tv_name;
    EditText et_sendContent;
    CoordinatorLayout layout;
    List<AVIMMessage> list;
    ChatMessageListViewAdapter stringArrayAdapter;
    AVIMConversation avim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        toName = intent.getStringExtra("toName");
        if (name == null || toName==null){
            finish();
        }else {
            findViews();
            initAVIMClient();
        }
    }


    private void initAVIMClient(){
        client = AVIMClient.getInstance(name);
        Log.e("============","========sendMessage====");
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null){
                    chatName = name + "与" + toName + "的对话";
                    client.createConversation(Arrays.asList(toName),chatName , null,
                            new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation avimConversation, AVIMException e) {
                                    if (e == null){
                                        receiveMsg();
                                        avim = avimConversation;
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    private void findViews(){
        listView = (ListView) findViewById(R.id.chat_list);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_sendContent = (EditText) findViewById(R.id.et_content);
        layout = (CoordinatorLayout) findViewById(R.id.container);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        tv_name.setText("当前用户:" + name+"  对话方:"+toName);
        list = new ArrayList<>();
        stringArrayAdapter = new ChatMessageListViewAdapter(list, ChatActivity.this,name);
        listView.setAdapter(stringArrayAdapter);
    }

    private void receiveMsg(){
        Log.e("============","========receiveMsg====");
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class,new AVIMMessageHandler(){
            @Override
            public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
                super.onMessage(message, conversation, client);
                Log.e("==============","==message.getFrom()=========="+message.getFrom());
                if (message.getFrom().equals(toName)) {
                    if (message == null) {
                        return;
                    } else {
                        String res = ((AVIMTextMessage) message).getText();
                        Log.e("============","========onMessage===="+res);
                        list.add(message);
                        notifyData();
                    }
                }
            }
        });
//        接受离线消息
        AVIMMessageManager.setConversationEventHandler(new AVIMConversationEventHandler() {
            @Override
            public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
                //有其他成员离开时调用此方法
            }

            @Override
            public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
                //有新成员加入时调用此方法
            }

            @Override
            public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {
                //有成员被踢出对话时调用
            }

            @Override
            public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {
                //当avimClient被邀请到对话时执行
            }

            @Override
            public void onOfflineMessagesUnread(final AVIMClient client, AVIMConversation conversation, int unreadCount) {
                super.onOfflineMessagesUnread(client, conversation, unreadCount);
                if (unreadCount>0){
                    conversation.queryMessages(unreadCount, new AVIMMessagesQueryCallback() {
                        @Override
                        public void done(List<AVIMMessage> listMsg, AVIMException e) {
                            if (client.getClientId().equals(toName)) {
                                if (e == null) {
                                    Log.e("=============","==getOffMsg.getOffMsgs(listMsg);===========");
                                    list.addAll(listMsg);
                                    stringArrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendMessage(){
        AVIMTextMessage msg = new AVIMTextMessage();
        sendContent = et_sendContent.getText().toString();
        if (sendContent == null || sendContent.length()==0){
            SnackbarUtil.createSnackBar(layout,"没有消息");

        }else {
            Log.e("==============","==sendMessage==========");
            msg.setText(sendContent);
            msg.setFrom("name");
            list.add(msg);
            et_sendContent.setText("");
            avim.sendMessage(msg, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    Log.e("==============","==done==========");
                    if (e==null){
                        Log.e("==============","==e==null==========");
                        notifyData();
                    }
                }
            });
        }
    }

    private void notifyData(){
        stringArrayAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(list.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

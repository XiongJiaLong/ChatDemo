package com.example.administrator.chatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.administrator.chatdemo.R;

import java.io.BufferedReader;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */

public class ChatMessageListViewAdapter extends BaseAdapter {

    private final int TYPE1 = 0;
    private final int TYPE2 = 1;
    private List<AVIMMessage> list;
    private Context context;
    private String currentUser;
    private LayoutInflater from;
    private AVIMTextMessage avimMessage;
    private String user;

    public ChatMessageListViewAdapter(List<AVIMMessage> list, Context context,String currentUser) {
        this.list = list;
        this.context = context;
        this.currentUser = currentUser;
         from = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AVIMMessage getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        avimMessage = (AVIMTextMessage) list.get(position);
        user = avimMessage.getFrom();
        if (currentUser.equals(user)){
            return TYPE1;
        }else {
            return TYPE2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgLvViewHolder viewholder = null;
        MsgLvRightViewHolder rightViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE1:
                    convertView = this.from.inflate(R.layout.chat_content_item_left, parent,false);
                    viewholder = new MsgLvViewHolder(convertView);
                    convertView.setTag(viewholder);
                    break;
                case TYPE2:
                    convertView = this.from.inflate(R.layout.chat_content_item_right, parent,false);
                    rightViewHolder = new MsgLvRightViewHolder(convertView);
                    convertView.setTag(rightViewHolder);
                    break;
                default:
                    break;
            }
        }else {
            switch (type){
                case TYPE1:
                    viewholder = (MsgLvViewHolder) convertView.getTag();
                    break;
                case TYPE2:
                    rightViewHolder = (MsgLvRightViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        switch (type){
            case TYPE1:
                viewholder.tv_name.setText(avimMessage.getFrom());
                viewholder.tv_content.setText(avimMessage.getText().toString()+"");
                break;
            case TYPE2:
                rightViewHolder.tv_right_name.setText(avimMessage.getFrom());
                rightViewHolder.tv_right_content.setText(avimMessage.getText().toString() + "");
                break;
            default:
                break;
        }
        return convertView;

    }

     private class MsgLvViewHolder{
        TextView tv_name;
        TextView tv_content;

        public MsgLvViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_tag);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
        }
    }

     private class MsgLvRightViewHolder{
        TextView tv_right_name;
        TextView tv_right_content;

        public MsgLvRightViewHolder(View view) {
            tv_right_name = (TextView) view.findViewById(R.id.tv_right_tag);
            tv_right_content = (TextView) view.findViewById(R.id.tv_right_content);
        }
    }

}

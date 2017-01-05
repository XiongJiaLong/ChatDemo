package com.example.administrator.chatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.chatdemo.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ChatNameListViewAdapter extends BaseAdapter {
    List<String> list;
    Context context;
    LayoutInflater from;

    public ChatNameListViewAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        from = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatViewHolder viewHolder;
        if (convertView == null){
            convertView = from.inflate(R.layout.chat_name_list_item,null);
            viewHolder = new ChatViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ChatViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(list.get(position).charAt(0)+"");
        viewHolder.tv_name.setText(list.get(position));
        return convertView;
    }

    class ChatViewHolder{
        TextView tv_title;
        TextView tv_name;

        public ChatViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}

package com.jp.apps.rating_of_things;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends BaseAdapter {

    private final List<Item> items;
    private final LayoutInflater mInflater;

    public ItemListAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_main_items_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Item item = (Item) getItem(position);

        mViewHolder.tvItem.setText(item.getName());

        return convertView;
    }

    private class MyViewHolder {
        final TextView tvItem;

        MyViewHolder(View item) {
            tvItem = (TextView) item.findViewById(R.id.items_item);
        }
    }
}

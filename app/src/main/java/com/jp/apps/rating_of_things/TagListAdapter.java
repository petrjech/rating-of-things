package com.jp.apps.rating_of_things;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TagListAdapter extends BaseAdapter {

    private final List<Tag> tags;
    private final LayoutInflater mInflater;

    public TagListAdapter(Context context, ArrayList<Tag> tags) {
        this.tags = tags;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_add_tag_tags_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Tag tag = (Tag) getItem(position);

        mViewHolder.tvTag.setText(tag.getName());

        return convertView;
    }

    private class MyViewHolder {
        final TextView tvTag;

        MyViewHolder(View item) {
            tvTag = (TextView) item.findViewById(R.id.tags_item);
        }
    }
}
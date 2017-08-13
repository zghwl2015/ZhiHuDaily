package com.example.zhihudaily.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zhihudaily.R;

import java.util.List;

/**
 * Created by hwl on 2017/7/3.
 * 和滑动菜单ListView有关
 */

public class MenuAdapter extends ArrayAdapter<MyMenu> {

    private int resourceId;

    public MenuAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyMenu> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MyMenu menu = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView menuContent = (TextView) view.findViewById(R.id.menu_content);
        menuContent.setText(menu.getThemeContent());
        return view;

    }
}

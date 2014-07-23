package com.zach.wilson.magic.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.models.Card;

/**
 * Created by Zach on 7/13/2014.
 */
public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private Card[] data;
    private LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, Card[] d, LayoutInflater inflater) {
        activity = a;
        data = d;
        this.inflater = inflater;
        imageLoader = ImageLoader.getInstance();

    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.listitem, null);
        final int pos = position;
        TextView text = (TextView) vi.findViewById(R.id.cardName);
        text.setText(data[position].getName());

        return vi;
    }


}

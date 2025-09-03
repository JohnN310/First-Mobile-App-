// this class is used as a custom arrayadapter for SpotifyApiHelper classes
package com.example.spotifytest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private List<String> items;

    public CustomArrayAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, R.layout.list_item_layout, objects);
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_layout, null);
        }

        // Set the text for the TextView in your custom layout
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(items.get(position));

        return view;
    }
}

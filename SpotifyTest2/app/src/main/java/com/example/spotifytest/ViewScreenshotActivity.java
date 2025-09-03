package com.example.spotifytest;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewScreenshotActivity extends AppCompatActivity {

    private List<Uri> screenshotList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_screenshot);

        // Query the MediaStore for screenshots
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA};
        String selection = MediaStore.Images.ImageColumns.DATA + " like ? ";
        String[] selectionArgs = new String[]{"%Screenshots%"};
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);

        // Add URIs of screenshots to the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                long imageId = cursor.getLong(columnIndex);
                Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
                screenshotList.add(imageUri);
            }
            cursor.close();
        }

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ScreenshotAdapter());
    }

    private class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder> {

        @NonNull
        @Override
        public ScreenshotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_screenshot, parent, false);
            return new ScreenshotViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScreenshotViewHolder holder, int position) {
            holder.imageView.setImageURI(screenshotList.get(position));
        }

        @Override
        public int getItemCount() {
            return screenshotList.size();
        }

        private class ScreenshotViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ScreenshotViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}

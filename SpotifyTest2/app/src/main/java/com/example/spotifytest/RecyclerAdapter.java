package com.example.spotifytest;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotifytest.databinding.EachItemBinding;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private final List<String> itemList;

    public RecyclerAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EachItemBinding binding = EachItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final EachItemBinding binding;

        public ItemViewHolder(EachItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String title) {
            binding.textView.setText(title);
        }
    }
    public String getItemAtPosition(int position) {
        return itemList.get(position);
    }
}

package com.example.crowlingtest.ShowWordActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowlingtest.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ShowWordAdapter extends RecyclerView.Adapter<ShowWordAdapter.ItemViewHolder> {

    private static Context context;
    private ArrayList<ShowWordData> mdata= new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_word_list_item, parent, false);
        this.context = parent.getContext();

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    void addItem(ShowWordData data){
       mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView word, mean;
        private ShowWordData data;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            word = view.findViewById(R.id.Word);
            mean = view.findViewById(R.id.Meaning);
        }

        void onBind(ShowWordData data){
            this.data = data;

            word.setText(data.getWord());
            mean.setText(data.getMeaning());

        }
    }
}

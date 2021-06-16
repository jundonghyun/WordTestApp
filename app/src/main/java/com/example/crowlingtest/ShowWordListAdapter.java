package com.example.crowlingtest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowWordListAdapter extends RecyclerView.Adapter<ShowWordListAdapter.ItemViewHolder> {

    private static Context context;
    private ArrayList<ShowWordListData> mdata = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ocr_list_item, parent, false);
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

    public void addItem(ShowWordListData data){
        mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ShowWordListData data;

        public ItemViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.ocr_word);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        MainActivity.editText.setText((String) textView.getText());
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("Select_word", textView.getText());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        private void onBind(ShowWordListData data){
            this.data = data;
            textView.setText(data.getWord());
        }
    }
}

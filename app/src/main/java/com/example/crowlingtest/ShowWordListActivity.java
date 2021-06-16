package com.example.crowlingtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ShowWordListActivity extends AppCompatActivity {

    private ShowWordListAdapter adapter;
    private ArrayList<ArrayList> word = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word_list);

        init();

        addItem(RecognizingWordActivity.Complete_Recognize_word);

    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.showwordlistrecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ShowWordListAdapter();
        recyclerView.setAdapter(adapter);
    }


    private void addItem(String recognize_word){
        String[] temp = recognize_word.split("\n");
        ArrayList<ShowWordListData> list = new ArrayList<>();


        for(int i = 0; i < temp.length; i++){
            list.add(new ShowWordListData(temp[i]));
            Log.d("TAG", temp[i]);
        }

        for(ShowWordListData d : list){
            adapter.addItem(d);
        }
    }
}
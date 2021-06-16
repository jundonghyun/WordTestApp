package com.example.crowlingtest.ShowWordActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.crowlingtest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ShowWordActivity extends AppCompatActivity {

    private ShowWordAdapter adapter;
    public static ArrayList<HashMap<String, String>> Word_Mean = new ArrayList<>();
    public static String ShowWordFileName;
    public static File ShowWordAdapterFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);

        init();

        addItem();
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.ShowWordRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ShowWordAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void addItem(){
        String line = null;
        String word, mean;

        ArrayList<ShowWordData> list = new ArrayList<>();
        Intent intent = getIntent();
        ShowWordFileName = intent.getExtras().getString("FileList_FileName");
        File dir = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        ShowWordAdapterFile = new File(getExternalFilesDir(null) + "/" + "pic.jpg" ,ShowWordFileName);

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dir+"/"+ShowWordFileName));
            while((line = bufferedReader.readLine()) != null){
                int idx = line.indexOf(" ");
                if(idx == -1){
                    continue;
                }
                word = line.substring(0, idx);
                mean = line.substring(idx+1, line.length());
                list.add(new ShowWordData(word, mean));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ShowWordData d : list){
            HashMap map = new HashMap();
            map.put("word", d.getWord());
            map.put("mean", d.getMeaning());
            Word_Mean.add(map);
            adapter.addItem(d);
        }
    }
}
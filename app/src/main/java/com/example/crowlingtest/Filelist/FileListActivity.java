package com.example.crowlingtest.Filelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.crowlingtest.MainActivity;
import com.example.crowlingtest.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private FileListAdapter adapter;
    public static File deletefile;
    private String File = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        init();

        addItem();

        deletefile = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        adapter.setonItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                setResult(1);
            }
        });
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.FileListRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FileListAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void addItem(){
        FileListData data = new FileListData();

        File dir = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        File[] checkfiles = dir.listFiles();

        if(!dir.exists()){
            return;
        }
        else{
            List<String> FileNameList = new ArrayList<>();
            for(int i = 0; i < checkfiles.length; i++){
                FileNameList.add(checkfiles[i].getName());
                data.setFilename(FileNameList.get(i));

            }
            adapter.addItem(data);
        }
    }
}
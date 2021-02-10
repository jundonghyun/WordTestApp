package com.example.crowlingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crowlingtest.Filelist.FileListActivity;
import com.example.crowlingtest.Filelist.FileListAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private Button AddWord, ShowWordFile, StartTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AddWord = findViewById(R.id.addword);
        ShowWordFile = findViewById(R.id.showfilelist);
        StartTest = findViewById(R.id.start_word_test);

        AddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileListAdapter.choice = 1;
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        });

        ShowWordFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
                if(file.exists() == true){
                    FileListAdapter.choice = 2;
                    startActivity(new Intent(HomeActivity.this, FileListActivity.class));
                }
                else{
                    Toast.makeText(HomeActivity.this, "파일이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        StartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileListAdapter.choice = 3;
                startActivity(new Intent(HomeActivity.this, FileListActivity.class));
            }
        });
    }
}
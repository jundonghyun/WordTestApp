package com.example.crowlingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
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
    private long backKeyPressedTime = 0;


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
                startActivity(new Intent(HomeActivity.this, FileListActivity.class));

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

    @Override
    public void onBackPressed() {
        Toast toast = null;
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true); //1단계 태스크 백그라운드로 이동
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask(); // 2단계 액티비티종료 + 태스크 리스트에서 지우기
            }
            Process.killProcess(Process.myPid()); // 3단계 앱 프로세스 종료
            toast.cancel();
        }
    }
}
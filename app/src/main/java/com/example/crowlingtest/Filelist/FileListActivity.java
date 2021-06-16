package com.example.crowlingtest.Filelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowlingtest.HomeActivity;
import com.example.crowlingtest.MainActivity;
import com.example.crowlingtest.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileListActivity extends AppCompatActivity implements View.OnClickListener{

    private static String FileName = null;
    private FileListAdapter adapter;
    private String File = null;
    private TextView textView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab, fab1;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private LinearLayout Empty, NotEmpty;
    private View customdialog;
    public static File deletefile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        textView = findViewById(R.id.FileListEmpty);
        Empty = findViewById(R.id.FileListEmptyLayout);
        NotEmpty = findViewById(R.id.FileListNotEmptyLayout);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);

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
        recyclerView = findViewById(R.id.FileListRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FileListAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void addItem(){
        FileListData data = new FileListData();

        File dir = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        File[] checkfiles = dir.listFiles();

        if(checkfiles == null){
            dir.mkdir();
            Empty.setVisibility(View.VISIBLE);
            NotEmpty.setVisibility(View.GONE);

        }
        else {
            if (!dir.exists()) {
                return;
            } else {
                Empty.setVisibility(View.GONE);
                NotEmpty.setVisibility(View.VISIBLE);
                List<String> FileNameList = new ArrayList<>();
                List<String> FileAttrList = new ArrayList<>();

                for (int i = 0; i < checkfiles.length; i++) {
                    String size = "";
                    long filesize = checkfiles[i].length();
                    size = Long.toString(filesize) + "bytes";
                    FileNameList.add(checkfiles[i].getName());
                    FileAttrList.add(size);
                    data.setFilename(FileNameList.get(i));
                    data.setFileAttribute(FileAttrList.get(i));

                }
                adapter.addItem(data);
            }
        }
    }

    @Override
    public void onClick(View v) { //플로팅액션버튼 클릭했을시
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fab1:
                anim();
                StoreFile();
                break;
        }
    }
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab1.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab1.setClickable(true);
            isFabOpen = true;
        }
    }

    private void StoreFile(){
        File dir = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        File[] files = dir.listFiles();

        customdialog = (View)View.inflate(FileListActivity.this,R.layout.activity_custom_dialog, null); //커스텀 다이얼로그 선언

        AlertDialog.Builder dial = new AlertDialog.Builder(FileListActivity.this); //빌더에 다이얼로그 연결
        dial.setView(customdialog); //다이얼로그 표시
        dial.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText filename = customdialog.findViewById(R.id.mesgase);

                if(filename.getText().toString().matches("")){
                    Toast.makeText(FileListActivity.this, "파일이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;

                }
                else{
                    FileName = filename.getText().toString();

                    File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg", FileName + ".txt");

                    try {
                        file.createNewFile();
                        finish();
                        startActivity(new Intent(FileListActivity.this, FileListActivity.class));
                        Toast.makeText(FileListActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dial.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FileListActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
            }
        });
        dial.show();
    }

}
package com.example.crowlingtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowlingtest.Filelist.FileListActivity;
import com.example.crowlingtest.Filelist.FileListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {

    public static String FileName = null;
    public static String SelectedFileName;
    private static final int REQ_CODE = 123;
    private View customdialog;
    private EditText editText; //검색할 단어를 입력
    private Button button; //단어 검색을 위한 버튼
    private Button WordSeletButton[] = new Button[4]; //검색된 단어의 뜻을 보여주고 단어와 뜻을 저장하기 위한 버튼

    private String Search; //검색할 단어를 크롤링하기 위한 변수
    private ArrayList<String> Word = new ArrayList<>(); //단어의 뜻을 저장할 배열리스트
    private HashMap<String, String> StoreWord = new HashMap<>(); //단어와 뜻을 저장할 해시맵

    final int PERMISSIONS_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.inputDic);
        button = findViewById(R.id.SearchButton);

        WordSeletButton[0] = findViewById(R.id.WordSelectButton1);
        WordSeletButton[1] = findViewById(R.id.WordSelectButton2);
        WordSeletButton[2] = findViewById(R.id.WordSelectButton3);
        WordSeletButton[3] = findViewById(R.id.WordSelectButton4);

        final Bundle bundle = new Bundle();

        requestPermission();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                Word.clear();
                for (int i = 0; i < 4; i++) {
                    WordSeletButton[i].setVisibility(View.INVISIBLE);
                    WordSeletButton[i].setText("");
                }
                StoreWord.clear();
                Search = String.valueOf(editText.getText());
                FileName = null;
                /*1.단어의 뜻을 저장할 배열 초기화
                 * 2.단어의 뜻을 보여줄 버튼의 초기화
                 * 3.단어와 뜻을 저장할 해시맵 초기화
                 * 4.파일이름 초기화*/


                new Thread() {
                    @Override
                    public void run() {
                        Document doc;
                        try {
                            doc = Jsoup.connect("https://dictionary.cambridge.org/ko/%EC%82%AC%EC%A0%84/%EC%98%81%EC%96%B4-%ED%95%9C%EA%B5%AD%EC%96%B4/" + Search).get();

                            Elements contents = doc.select("div.pr.entry-body__el div.pos-body div.sense-block.pr.dsense.dsense-noh div.sense-body.dsense_b div.def-block.ddef_block span.trans.dtrans.dtrans-se");//회차 id값 가져오기

                            for (Element e : contents) {
                                e.text();
                                Word.add(e.text());
                            }

                            bundle.putStringArrayList("Word", Word);
                            Message msg = handler.obtainMessage();
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();

                    int wordsize = bundle.getStringArrayList("Word").size();

                    if (wordsize == 0) {
                        return;
                    }

                    for (int i = 0; i < wordsize; i++) {
                        if (i >= 4) {
                            break;
                        }
                        WordSeletButton[i].setText(Word.get(i));
                        WordSeletButton[i].setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < Word.size(); i++) {
                        if (i >= 4) {
                            break;
                        }
                        int j = i;
                        WordSeletButton[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StoreWord.put(Search, WordSeletButton[j].getText().toString()); //단어를 해시맵에 단어, 뜻 형태로 저장

                                FindFile();
                                editText.setText("");
                            }
                        });
                    }
                }
            };
        });

    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString( cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            Log.i("debug", "Display Name: " + fileName);
        }
        cursor.close();
        return fileName;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && resultCode == RESULT_OK) {

            Uri selectedfile = data.getData();
            MainActivity.FileName = getFileNameFromUri(selectedfile);

            String line = null;

            HashMap<String, String> overlap = new HashMap<>();

            File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg", FileName);
            /* 저장하려는 중복되는 단어인지 확인하는 곳 */
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String overlap_word, overlap_mean;

                while((line = bufferedReader.readLine()) != null){
                    int idx = line.indexOf(" ");
                    if(idx == -1){
                        continue;
                    }
                    overlap_word = line.substring(0, idx);
                    overlap_mean = line.substring(idx+1, line.length());
                    overlap.put(overlap_word, overlap_mean);
                }

                if(overlap.containsKey(Search)){
                    Toast.makeText(this, "단어장에 이미 저장된 단어입니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    /* 중복되는 단어가 없다면 저장 하는 곳 */
                    try{
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));

                        String word = Search; //해시맵에서 단어를 키로 찾아 단어와 뜻을 파일에 쓰기
                        String mean = StoreWord.get(Search);
                        String WriteFile = word +" "+mean;

                        bufferedWriter.newLine();
                        bufferedWriter.write(WriteFile);
                        bufferedWriter.flush();
                        bufferedWriter.close();

                        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void FindFile(){
        /* 파일 목록 불러오기 */
        File dir = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        File[] files = dir.listFiles();
        if(files.length == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("파일없음")
                    .setMessage("파일을 생성하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            customdialog = (View)View.inflate(MainActivity.this,R.layout.activity_custom_dialog, null); //커스텀 다이얼로그 선언

                            AlertDialog.Builder dial = new AlertDialog.Builder(MainActivity.this); //빌더에 다이얼로그 연결
                            dial.setView(customdialog); //다이얼로그 표시
                            dial.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    EditText filename = customdialog.findViewById(R.id.mesgase);

                                    if(filename.getText().toString().matches("")){
                                        Toast.makeText(MainActivity.this, "파일이름을 입력하세요", Toast.LENGTH_SHORT).show();
                                        return;

                                    }
                                    else{
                                        FileName = filename.getText().toString();

                                        File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg", FileName + ".txt");

                                        try{
                                            FileOutputStream fos = new FileOutputStream(file);

                                            String word = Search; //해시맵에서 단어를 키로 찾아 단어와 뜻을 파일에 쓰기
                                            String mean = StoreWord.get(Search);
                                            String WriteFile = word +" "+mean;

                                            fos.write(WriteFile.getBytes());
                                            fos.flush();
                                            fos.close();

                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                            dial.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dial.show();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
        }
        else{
            /* 파일이 한개이상 존재한다면 파일을 선택하기위해서 파일목록을 보여줌 */

//            Intent intent = new Intent(MainActivity.this, FileListActivity.class);
//            startActivityForResult(intent, 0);

            if(MainActivity.FileName != null){ //파일을 선택한 적이 있다면
                String line = null;
                HashMap<String, String> overlap = new HashMap<>();

                File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg", FileName);
                /* 저장하려는 중복되는 단어인지 확인하는 곳 */
                try{
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String overlap_word, overlap_mean;

                    while((line = bufferedReader.readLine()) != null){
                        int idx = line.indexOf(" ");
                        overlap_word = line.substring(0, idx);
                        overlap_mean = line.substring(idx+1, line.length());
                        overlap.put(overlap_word, overlap_mean);
                    }

                    if(overlap.containsKey(Search)){
                        Toast.makeText(this, "단어장에 이미 저장된 단어입니다", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        /* 중복되는 단어가 없다면 저장 하는 곳 */
                        try{
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));

                            String word = Search; //해시맵에서 단어를 키로 찾아 단어와 뜻을 파일에 쓰기
                            String mean = StoreWord.get(Search);
                            String WriteFile = word +" "+mean;

                            bufferedWriter.newLine();
                            bufferedWriter.write(WriteFile);
                            bufferedWriter.flush();
                            bufferedWriter.close();

                            Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Intent intent = new Intent().setType("*/*") .setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select a file"), REQ_CODE);
            }
        }
    }

    private List<String> FileList(String FolderName){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FolderName;

        File directory = new File(path);
        File[] files = directory.listFiles();

        List<String> filesNameList = new ArrayList<>();

        if(files == null){
            filesNameList.add("null");
            return filesNameList;
        }

        for (int i=0; i< files.length; i++) {
            filesNameList.add(files[i].getName());
        }

        return  filesNameList;
    }

    private void requestPermission() {
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환

        if (shouldProviceRationale) {
            //앱에 필요한 권한이 없어서 권한 요청
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            //권한있을때.
            //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
            makeDir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            makeDir();
        }
        else{
            denialDialog();
        }
    }

    public void denialDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle("알림")
                .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent); //확인버튼누르면 바로 어플리케이션 권한 설정 창으로 이동하도록
                    }
                })
                .create()
                .show();
        
    }

    public File makeDir() {
        File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg");
        if(!file.exists()){
            file.mkdir();
        }

        return file;
    }
}
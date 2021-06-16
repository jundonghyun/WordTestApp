package com.example.crowlingtest.StartTest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowlingtest.Filelist.FileListActivity;
import com.example.crowlingtest.HomeActivity;
import com.example.crowlingtest.MainActivity;
import com.example.crowlingtest.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TestActivity extends AppCompatActivity {

    public static String FilePath;

    private Button[] testbutton = new Button[3];
    private TextView testword;
    private String wordTemp, meanTemp;
    private HashMap<String, String> storeword = new HashMap<>();
    private ArrayList<String> RandomMean = new ArrayList<>();
    private int shufflenum;
    private List<Integer> Rand_Num_List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testbutton[0] = findViewById(R.id.TestButton1);
        testbutton[1] = findViewById(R.id.TestButton2);
        testbutton[2] = findViewById(R.id.TestButton3);
        testword = findViewById(R.id.TestWord);

        String temp = FilePath;
        String line = null;



        File file = new File(getExternalFilesDir(null) + "/" + "pic.jpg", temp);
        /* 단어를 해시맵 형태로 불러옴 */
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String word, mean;

            while((line = bufferedReader.readLine()) != null){
                int idx = line.indexOf(" ");
                if(idx == -1){
                    continue;
                }
                word = line.substring(0, idx);
                mean = line.substring(idx+1, line.length());
                storeword.put(word, mean);
                RandomMean.add(mean);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shufflenum = RandomMean.size();

        if(storeword.size() < 6){
            AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
            int i = 6 - storeword.size();
            builder.setTitle("단어가"+ i + "개 부족합니다");
            builder.setMessage("단어를 더 추가해주세요");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(TestActivity.this, HomeActivity.class));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            continuetest();
        }


        testbutton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = testbutton[0].getText().toString();
                if(temp.equals(meanTemp)){
                    Toast toast = Toast.makeText(TestActivity.this, "정답입니다", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, Gravity.CENTER, 0);
                    toast.show();
                    storeword.remove(wordTemp);
                    if(storeword.isEmpty()){
                        Snackbar.make(v,"테스트를 완료했습니다 \n 확인을 누르면 파일목록으로 돌아갑니다", Snackbar.LENGTH_INDEFINITE)
                                .setAction("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        startActivity(new Intent(TestActivity.this, FileListActivity.class));
                                    }
                                }).show();
                    }
                    else{
                        continuetest();
                    }
                }
                else{
                    Toast.makeText(TestActivity.this, "틀렸습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        testbutton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = testbutton[1].getText().toString();
                if(temp.equals(meanTemp)){
                    Toast toast = Toast.makeText(TestActivity.this, "정답입니다", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, Gravity.CENTER, 0);
                    toast.show();
                    storeword.remove(wordTemp);
                    if(storeword.isEmpty()){
                        Snackbar.make(v,"테스트를 완료했습니다 \n 확인을 누르면 파일목록으로 돌아갑니다", Snackbar.LENGTH_INDEFINITE)
                                .setAction("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        startActivity(new Intent(TestActivity.this, FileListActivity.class));
                                    }
                                }).show();
                    }
                    else{
                        continuetest();
                    }
                }
                else{
                    Toast.makeText(TestActivity.this, "틀렸습니다", Toast.LENGTH_SHORT).show();

                }
            }
        });

        testbutton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = testbutton[2].getText().toString();
                if(temp.equals(meanTemp)){
                    Toast toast = Toast.makeText(TestActivity.this, "정답입니다", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, Gravity.CENTER, 0);
                    toast.show();
                    storeword.remove(wordTemp);
                    if(storeword.isEmpty()){
                        Snackbar.make(v,"테스트를 완료했습니다 \n 확인을 누르면 파일목록으로 돌아갑니다", Snackbar.LENGTH_INDEFINITE)
                                .setAction("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        startActivity(new Intent(TestActivity.this, FileListActivity.class));
                                    }
                                }).show();
                    }
                    else{
                        continuetest();
                    }
                }
                else{
                    Toast.makeText(TestActivity.this, "틀렸습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });




        /* 단어와 뜻을 무작위로 버튼과 텍스트뷰에 매칭 */
    }

    void continuetest(){
        Iterator iterator = storeword.entrySet().iterator();
        Map.Entry entry = (Map.Entry) iterator.next();

        storeword.remove(entry.getKey());
        RandomMean.remove(entry.getKey());

        wordTemp = (String) entry.getKey(); //정답의 단어를 기억할 변수
        meanTemp = (String) entry.getValue(); //정답의 뜻을 기억할 변수

        testword.setText(wordTemp); //맞춰야 하는 단어를 표시

        Random rand = new Random();
        int random = rand.nextInt(2);

        //랜덤한 수를 겹치지 않게 생성
        int meanrandom = 0;
        do{
            meanrandom = rand.nextInt(shufflenum);
            if(!Rand_Num_List.contains(meanrandom)){
                Rand_Num_List.add(meanrandom);
                break;
            }
        }while(true);

        testbutton[random].setText(meanTemp); //3개의 버튼중 정답이 들어갈 버튼이 결정됨

        if(random == 0){ //0이 나왔다면 1,2에 정답이 아닌 뜻이 들어감

            int firstnum = meanrandom;
            int secondnum = 0;
            RandomMean.get(meanrandom);
            testbutton[1].setText(RandomMean.get(meanrandom)); //첫번째 오답버튼에 정답이 아닌 뜻을 넣음
            do{ //정답 버튼과 오답을 가진 버튼의 뜻이 겹치지 않게함
                if(firstnum != secondnum && testbutton[0].getText().toString() != RandomMean.get(secondnum)){ /* 1. 오답버튼1,2의 변수가 겹치지 않고
                                                                                                                 2. 정답 버튼의 뜻과도 겹치지 않아야 마지막 오답버튼에
                                                                                                                    settext함*/
                    testbutton[2].setText(RandomMean.get(secondnum));
                    break;
                }
                secondnum = rand.nextInt(shufflenum);
            }while(true);
        }
        else if(random == 1){ //1이 나왔다면 0,2에 정답이 아닌 뜻이 들어감
            int firstnum = meanrandom;
            int secondnum = 0;
            RandomMean.get(meanrandom);
            testbutton[0].setText(RandomMean.get(meanrandom));
            do{
                if(firstnum != secondnum && testbutton[1].getText().toString() != RandomMean.get(secondnum)){
                    testbutton[2].setText(RandomMean.get(secondnum));
                    break;
                }
                secondnum = rand.nextInt(shufflenum);
            }while(true);
        }
        else if(random == 2){ //2가 나왔다면 0,1에 정답이 아닌 뜻이 들어감
            int firstnum = meanrandom;
            int secondnum = 0;
            RandomMean.get(meanrandom);
            testbutton[0].setText(RandomMean.get(meanrandom));
            do{
                if(firstnum != secondnum && testbutton[2].getText().toString() != RandomMean.get(secondnum)){
                    testbutton[1].setText(RandomMean.get(secondnum));
                    break;
                }
                secondnum = rand.nextInt(shufflenum);
            }while(true);
        }
    }
}
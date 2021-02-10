package com.example.crowlingtest.StartTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowlingtest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TestActivity extends AppCompatActivity {

    public static String FilePath;

    private Button[] testbutton = new Button[3];
    private TextView testword;

    private String wordTemp, meanTemp;
    private int correct = -1;

    private HashMap<String, String> storeword = new HashMap<>();
    private ArrayList<String> RandomMean = new ArrayList<>();
    private int shufflenum;

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

        continuetest();

        testbutton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = testbutton[0].getText().toString();
                if(temp.equals(meanTemp)){
                    Toast.makeText(TestActivity.this, "정답입니다", Toast.LENGTH_SHORT).show();
                    storeword.remove(wordTemp);
                    continuetest();
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
                    Toast.makeText(TestActivity.this, "정답입니다", Toast.LENGTH_SHORT).show();
                    storeword.remove(wordTemp);
                    continuetest();

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
                    Toast.makeText(TestActivity.this, "정답입니다", Toast.LENGTH_SHORT).show();
                    storeword.remove(wordTemp);
                    continuetest();
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

        wordTemp = (String) entry.getKey();
        meanTemp = (String) entry.getValue();

        testword.setText(wordTemp);

        Random rand = new Random();
        int random = rand.nextInt(2 - 0 + 0) + 0;

        int meanrandom = rand.nextInt(shufflenum);
        testbutton[random].setText(meanTemp);

        if(random == 0){
            RandomMean.get(meanrandom);
            testbutton[1].setText(RandomMean.get(meanrandom));
            meanrandom = rand.nextInt(shufflenum);
            testbutton[2].setText(RandomMean.get(meanrandom));
        }
        else if(random == 1){
            RandomMean.get(meanrandom);
            testbutton[0].setText(RandomMean.get(meanrandom));
            meanrandom = rand.nextInt(shufflenum);
            testbutton[2].setText(RandomMean.get(meanrandom));
        }
        else if(random == 2){
            RandomMean.get(meanrandom);
            testbutton[0].setText(RandomMean.get(meanrandom));
            meanrandom = rand.nextInt(shufflenum);
            testbutton[1].setText(RandomMean.get(meanrandom));
        }
    }
}
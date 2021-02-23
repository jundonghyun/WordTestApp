package com.example.crowlingtest.ShowWordActivity;

import java.util.HashMap;

public class ShowWordData {

    String word;
    String meaning;

    public ShowWordData(String word, String mean) {
        this.word = word;
        this.meaning = mean;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

}

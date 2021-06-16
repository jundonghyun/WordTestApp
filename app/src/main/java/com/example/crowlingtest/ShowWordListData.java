package com.example.crowlingtest;

public class ShowWordListData {
    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    private String Word;

    public ShowWordListData(String word){
        this.Word = word;
    }
}

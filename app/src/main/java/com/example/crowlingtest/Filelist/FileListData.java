package com.example.crowlingtest.Filelist;

public class FileListData {
    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    private String Filename;
    private String FileAttribute;

    public String getFileAttribute() {
        return FileAttribute;
    }

    public void setFileAttribute(String fileAttribute) {
        FileAttribute = fileAttribute;
    }
}

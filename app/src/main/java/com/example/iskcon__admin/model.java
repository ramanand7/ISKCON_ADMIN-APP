package com.example.iskcon__admin;

public class model
{
    String filename, fileurl;


    public model() {
    }

    public model(String filename, String fileurl, int nod, int nol, int nov) {
        this.filename = filename;
        this.fileurl = fileurl;

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }


}

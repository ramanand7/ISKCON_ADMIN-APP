package com.example.iskcon__admin;

public class model
{
    String filename, fileurl, imageuri;


    public model() {
    }

    public model(String filename, String fileurl, String imageuri) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.imageuri = imageuri;

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

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}

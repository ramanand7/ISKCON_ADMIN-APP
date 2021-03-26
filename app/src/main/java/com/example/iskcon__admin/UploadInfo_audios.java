package com.example.iskcon__admin;

public class UploadInfo_audios {
    public String name;
    public String url;

    public UploadInfo_audios() {

    }

    public UploadInfo_audios(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

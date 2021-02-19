package com.example.iskcon__admin;

public class UploadMesI {
    private String mName;
    private String date;

    public UploadMesI(String mName, String date) {
        this.mName = mName;
        this.date = date;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

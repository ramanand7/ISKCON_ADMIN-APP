package com.example.iskcon__admin;


public class Member {
    private String name;
    private String Videourl;
    private String search;
    private String Username;


    public Member(){}


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideourl() {
        return Videourl;
    }

    public void setVideourl(String videourl) {
        Videourl = videourl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
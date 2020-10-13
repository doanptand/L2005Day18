package com.t3h.service.model;

/**
 * Created by TEACHER on 03/11/2015.
 */
public class SongItem {
    private static final String TAG = "SongItem";
    private String songName, path,duration,author,fullName;

    public SongItem(String songName, String path, String duration, String author, String fullName) {
        this.songName = songName;
        this.path = path;
        this.duration = duration;
        this.author = author;
        this.fullName = fullName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "\nSong Name: "+songName+"\n"+
               "Path: "+path+"\n"+
               "Duration: "+duration+"\n"+
               "Author: "+author+"\n"+
               "Full Name: "+fullName+"\n";
    }
}

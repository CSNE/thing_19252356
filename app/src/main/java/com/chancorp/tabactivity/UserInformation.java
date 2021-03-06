package com.chancorp.tabactivity;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//계정 정보 조장 클래스

public class UserInformation implements Serializable{



    String password;
    String iD;
    String name;
    String phone;
    String taskName;
    String noteBody;
    String noteTitle;

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }



    String taskDesc;

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    String taskTime;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    String nickname;
    int avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash(){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(this.password.getBytes());
            byte[] digest = sha.digest();

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e("Familink", "NoSuchAlgorithmException on ServerComms>setQueryHash");
            return new String();
        }

    }



}

package com.anand.android.onsitetask1;

public class MessageItemClass {

    private String msg,num;
    private String date,time;


    public MessageItemClass(int id, String msg, String num, String date, String time) {
        this.msg = msg;
        this.num = num;
        this.date = date;
        this.time=time;
    }

    public String getMsg() {
        return msg;
    }

    public String getNum() {
        return num;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

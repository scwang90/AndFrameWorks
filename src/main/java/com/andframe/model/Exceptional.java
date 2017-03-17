package com.andframe.model;

import java.util.Date;
import java.util.UUID;

@SuppressWarnings("unused")
public class Exceptional {

    public enum Statut {
        NEW ,//新的错误
        RECEIVE,//已经查收（正在处理）
        SUBMIT,//处理完毕并提交
        CONFIRM //确认问题已经解决
    }

    public UUID UserID;
    public String Name;
    public String Device;
    public String Version;
    public String Message;
    public String Stack;
    public String Thread;
    public String Remark;
    public String Platform = "Android";
    public String OpeateSystem;
    public Statut Status = Statut.NEW;
    public int ScreneWidth;
    public int ScreneHeight;
    public Date Time;

    public Exceptional() {
        this.Time = new Date();
    }

    public Exceptional(String Name, String Message,
                       String Stack, String Version, String Thread) {
        this.Time = new Date();
        this.Name = Name;
        this.Message = Message;
        this.Stack = Stack;
        this.Version = Version;
        this.Thread = Thread;
    }

}

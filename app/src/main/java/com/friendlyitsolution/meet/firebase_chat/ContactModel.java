package com.friendlyitsolution.meet.firebase_chat;

public class ContactModel {

    String Cname,Cid,path,email,msg,time;

    public ContactModel(String Cid,String Cname,String path,String msg,String time,String email)
    {
        this.time=time;
        this.msg=msg;
        this.email=email;
        this.Cid=Cid;
        this.Cname=Cname;
        this.path=path;

    }


}

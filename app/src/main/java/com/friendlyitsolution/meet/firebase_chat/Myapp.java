package com.friendlyitsolution.meet.firebase_chat;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Myapp extends Application {

 static FirebaseDatabase db;
  static DatabaseReference ref;
static  FirebaseUser firebaseUser;
static String userid,username,userprofile,useremail;


    @Override
    public void onCreate() {
        super.onCreate();

        db=FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(true);

        ref=db.getReference().child("chatroom");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        ref.keepSynced(true);


        if(firebaseUser!=null)
        {
            userid=firebaseUser.getUid();
            userprofile=""+firebaseUser.getPhotoUrl();
            username=firebaseUser.getDisplayName();
            useremail=firebaseUser.getEmail();
        }

    }



}

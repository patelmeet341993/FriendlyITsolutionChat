package com.friendlyitsolution.meet.firebase_chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity {
    Vibrator vibe;
    Button btnlogout;
    TextView name,email;
    CircleImageView img;

    ImageView btnsend;
    EditText etmsg;
    RecyclerView recy;
    static MyAdpter m;
    static List<ContactModel> list;

    RecyclerView recy1;
    static MyAdpter1 m1;
    static List<ContactModel1> list1;
    Map<String,ContactModel1> list1map;


    TextView totmem;

    DatabaseReference myref;

    boolean isLogout=false;
    boolean isVibrate=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vibe= (Vibrator) getSystemService(this.VIBRATOR_SERVICE);


        etmsg=(EditText)findViewById(R.id.etmsg);
        btnsend=(ImageView)findViewById(R.id.btnsend);
        img=(CircleImageView) findViewById(R.id.img);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        btnlogout=(Button)findViewById(R.id.btnlt);
        totmem=(TextView)findViewById(R.id.totmem);
        myref=Myapp.db.getReference("users");

        list=new ArrayList<>();
        m=new MyAdpter(list);

        recy=(RecyclerView)findViewById(R.id.recy);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main2Activity.this);
        recy.setLayoutManager(mLayoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(m);


        list1=new ArrayList<>();
        list1map=new HashMap<>();
        m1=new MyAdpter1(list1);

        recy1=(RecyclerView)findViewById(R.id.recy1);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(false);
        recy1.setLayoutManager(layoutManager);
        recy1.setItemAnimator(new DefaultItemAnimator());
        recy1.setAdapter(m1);



        name.setText(Myapp.username);
        email.setText(Myapp.useremail);
        Glide.with(img.getContext()).load(Myapp.userprofile)
                .override(60, 60)
                .fitCenter()
                .into(img);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });

        getMsg();
        addTextWatcher();
        getUsers();
        setVibrator();
    }


    void setMember()
    {
        totmem.setText(list1.size()+"");
    }

    void getUsers()
    {
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String,String> ss=(Map<String, String>)dataSnapshot.getValue();
                ContactModel1 cm=new ContactModel1(dataSnapshot.getKey(),ss.get("name"),ss.get("url"),ss.get("online"));

                list1.add(cm);
                m1.notifyDataSetChanged();
                list1map.put(dataSnapshot.getKey(),cm);

                setMember();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {




                Map<String,String> ss=(Map<String, String>)dataSnapshot.getValue();
                ContactModel1 cm=list1map.get(dataSnapshot.getKey());
                cm.online=ss.get("online");
                cm.Cname=ss.get("name");

                list1.remove(cm);
                list1.add(0,cm);
                m1.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                ContactModel1 cc=list1map.get(dataSnapshot.getKey());
                list1.remove(cc);
                list1map.remove(cc);
                m1.notifyDataSetChanged();

                setMember();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void setVibrator()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               isVibrate=true;
            }
        }, 5000);
    }

    void getMsg()
    {
        Myapp.ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try {
                    Map<String, String> dd = (Map<String, String>) dataSnapshot.getValue();


                    ContactModel cm = new ContactModel(dd.get("id"), dd.get("name"), dd.get("url"), dd.get("msg"), dd.get("time"), dd.get("email"));
                    list.add(cm);
                    m.notifyItemChanged(list.size() - 1);
                    recy.scrollToPosition(list.size() - 1);

                    if(!dd.get("id").equals(Myapp.userid) && isVibrate) {
                        vibe.vibrate(100);
                    }
                }
                catch(Exception e)
                {

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    void sendMsg()
    {
        String msg=etmsg.getText().toString();
        if(!msg.trim().equals(""))
        {
            Map<String,String> msgdata=new HashMap<>();
            msgdata.put("name",Myapp.username);
            msgdata.put("url",Myapp.userprofile);
            msgdata.put("email",Myapp.useremail);
            msgdata.put("time",getTimeFromStamp());
            msgdata.put("msg",msg);
            msgdata.put("id",Myapp.userid);
            btnsend.setEnabled(false);
            Myapp.ref.push().setValue(msgdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                btnsend.setEnabled(true);
                etmsg.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
               btnsend.setEnabled(true);
               etmsg.setError("Try again later");
                }
            });

        }
    }

    void Logout()
    {

        GoogleSignInOptions gso;
        GoogleSignInClient mGoogleSignInClient;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();

        myref.child(Myapp.userid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                isLogout=true;
                Myapp.firebaseUser=null;
                Intent i=new Intent(Main2Activity.this,Splashscreen.class);
                startActivity(i);
                finish();
            }
        });
    }


    String getTimeFromStamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
       Date d=new Date();
       String dd=sdf.format(d);
       return  dd;


    }


    @Override
    protected void onPause() {
        super.onPause();

if(!isLogout) {
    myref.child(Myapp.userid).child("online").setValue("no");
}
    }

    @Override
    protected void onResume() {
        super.onResume();

if(!isLogout) {
    myref.child(Myapp.userid).child("online").setValue("yes");
}
    }

    void addTextWatcher()
    {
        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(etmsg.getText().toString().isEmpty())
                {

                    myref.child(Myapp.userid).child("name").setValue(Myapp.username);

                }
                else
                {
                    myref.child(Myapp.userid).child("name").setValue("typing..");

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
    }
}

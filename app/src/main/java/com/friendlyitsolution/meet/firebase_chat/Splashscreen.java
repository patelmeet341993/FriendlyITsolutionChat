package com.friendlyitsolution.meet.firebase_chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class Splashscreen extends AppCompatActivity {
    String TAG = "MSG : ";
    SignInButton btn;
    int RC_SIGN_IN = 11;
    Activity ac;
    Vibrator vibe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        ac=this;
        vibe= (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        btn=(SignInButton)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Myapp.firebaseUser!=null)
                {
                    Intent i=new Intent(Splashscreen.this,Main2Activity.class);
                    startActivity(i);
                    finish();
                }
                else
                {

                    btn.setVisibility(View.VISIBLE);
                    btn.setAlpha(0.0f);
                    btn
                            .animate()
                            .setDuration(700)
                            .alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            long pattern[]={0,800,400,800,400};
                            vibe.vibrate(pattern,-1);
                        }
                    });



                }



            }
        }, 2000);


    }

    void signInWithGoogle() {
        GoogleSignInOptions gso;
        GoogleSignInClient mGoogleSignInClient;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            loginWithAccount(data,Splashscreen.this);
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

    }

    void loginWithAccount(Intent i,final Activity con) {

        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final ProgressDialog pd = new ProgressDialog(Splashscreen.this);
        pd.setMessage("please wait");
        pd.setCancelable(false);
        pd.show();
        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(i);
        completedTask.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {

                try {
                    GoogleSignInAccount acct = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);


                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(con, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                                        Log.d(TAG, "signInWithCredential:success");
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                        Myapp.userid=firebaseUser.getUid();
                                        Myapp.userprofile=""+firebaseUser.getPhotoUrl();
                                        Myapp.username=firebaseUser.getDisplayName();
                                        Myapp.useremail=firebaseUser.getEmail();

                                        Map<String,String> dd=new HashMap<>();
                                        dd.put("url",Myapp.userprofile);
                                        dd.put("name",Myapp.username);
                                        dd.put("online","yes");

                                        DatabaseReference rr=Myapp.db.getReference();
                                        rr.child("users").child(Myapp.userid).setValue(dd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Intent i=new Intent(Splashscreen.this,Main2Activity.class);
                                                startActivity(i);
                                                ac.finish();
                                            }
                                        });


                                        //  updateUI(user);
                                    } else {

                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        Toast.makeText(getApplicationContext(),"faild",Toast.LENGTH_LONG).show();

                                    }
                                    pd.dismiss();
                                    // ...
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        completedTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();


            }
        });


    }


}

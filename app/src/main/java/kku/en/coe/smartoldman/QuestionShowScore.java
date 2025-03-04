package kku.en.coe.smartoldman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionShowScore extends AppCompatActivity implements View.OnClickListener {

    private Button read_btn , back_btn , show_answer_btn;
    private String pointer , post_test , title;
    public String[] answer_all;
    private int score = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    MediaPlayer mediaPlayer;
    private TextView score_tv , header_score_tv;
    private ImageView score_img;

    private String[] Hyper = {"0","1","1","1","0","1","1","1","0","0"};
    private String[] Oste  = {"0","1","1","1","1","0","1","1","0","1"};
    private String[] Lipid = {"1","1","0","1","1","1","0","1","0","1"};
    private String[] Diab  = {"0","1","0","1","1","1","0","0","0","1"};
    private String[] Dep   = {"0","1","1","1","1","1","0","1","1","1"};
    ProgressDialog pgd;

    boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_show_score);
        isOnline = isOnline();
        try {
            getSupportActionBar().hide();
        } catch (Exception ignored) {}
        if (isOnline) {
            doInitData();
        } else {
            pgd = ProgressDialog.show(this, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต กรุณาตรวจสอบแล้วลองใหม่อีกครั้ง", "Loading...", true, false);
        }

    }

    private void doInitData() {
        pgd = ProgressDialog.show(this, "กำลังบันทึกคะแนน กรุณารอสักครู่", "Loading...", true, false);
        show_answer_btn = findViewById(R.id.show_answer_btn);
        show_answer_btn.setOnClickListener(this);
        read_btn = findViewById(R.id.read_btn);
        read_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        score_tv = findViewById(R.id.score);
        score_img = findViewById(R.id.img_score);
        header_score_tv = findViewById(R.id.header_score);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");


        doGetIntentData();
        doCheckAnswer(pointer);
        if (post_test == null) {
            post_test = "null";
        }
        try {
            //            Toast.makeText(this,"pointer is : " + pointer,Toast.LENGTH_LONG).show();
            Log.e("posttest" , "pointer is : " + post_test);
            new Timer().schedule(
                    new TimerTask(){
                        @Override
                        public void run(){
                            doCheckPerPost(post_test);
                        }
                    }, 3500);
        } catch (Exception e) {
            pgd.dismiss();
            Toast.makeText(this,"การเชื่อมต่อขัดข้อง ไม่สามารถบันทึกคะแนนได้",Toast.LENGTH_LONG).show();
        }
    }

    private void doCheckAnswer(String pointer) {
        String[] tmp_ans = new String[10];
        String img_name = "";

        switch (pointer) {
            case "Hyper1Activity" :
                tmp_ans = Hyper;
                break;
            case "Oste1Activity" :
                tmp_ans = Oste;
                break;
            case "Lipid1Activity" :
                tmp_ans = Lipid;
                break;
            case "Diab1Activity" :
                tmp_ans = Diab;
                break;
            case "Dep1Activity" :
                tmp_ans = Dep;
                break;
        }
        for(int i = 0; i < tmp_ans.length; i++){
            if (answer_all[i].equals(tmp_ans[i])){
                score += 1;
            }
        }

        if (score < 4 ) {
            img_name = "low";
        } else if ( score > 7){
            img_name = "high";
        } else {
            img_name = "medium";
        }

        int resID = getResources().getIdentifier(img_name , "drawable", getPackageName());
        score_img.setImageResource(resID);
        score_tv.setText(score + " / 10");

        if (post_test == null) {
            header_score_tv.setText("คะแนนก่อนเรียน\n" + title);
            int raw_audio = getResources().getIdentifier("clap" , "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(this,raw_audio);
            mediaPlayer.start();
        } else  {
            header_score_tv.setText("คะแนนหลังเรียน\n" + title);
            int raw_audio = getResources().getIdentifier("clap" , "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(this,raw_audio);
            mediaPlayer.start();
        }
    }

    private void doGetIntentData() {
        Bundle extras = getIntent().getExtras();
        GlobalAnswerQuestion globalAnswerQuestion = GlobalAnswerQuestion.getInstance();
        answer_all = globalAnswerQuestion.getArray_answer();
        pointer = extras.getString("return_point");
        post_test = extras.getString("post_test");
        title = extras.getString("title");
    }

    private void doCheckPerPost(String post_test) {
        String sub_score = String.valueOf(score);
        boolean isPreTest = post_test.length() <= 4;
        switch (pointer) {
            case "Hyper1Activity" :
                if (isPreTest){
                    myRef.child(current_user.getUid()).child("pre_Hyper").setValue(sub_score);
                }else {
                    myRef.child(current_user.getUid()).child("post_Hyper").setValue(sub_score);
                }
                break;
            case "Oste1Activity" :
                if (isPreTest){
                    myRef.child(current_user.getUid()).child("pre_Oste").setValue(sub_score);
                }else {
                    myRef.child(current_user.getUid()).child("post_Oste").setValue(sub_score);
                }
                break;
            case "Lipid1Activity" :
                if (isPreTest){
                    myRef.child(current_user.getUid()).child("pre_Lipid").setValue(sub_score);
                }else {
                    myRef.child(current_user.getUid()).child("post_Lipid").setValue(sub_score);
                }
                break;
            case "Diab1Activity" :
                if (isPreTest){
                    myRef.child(current_user.getUid()).child("pre_Diab").setValue(sub_score);
                }else {
                    myRef.child(current_user.getUid()).child("post_Diab").setValue(sub_score);
                }
                break;
            case "Dep1Activity" :
                if (isPreTest){
                    myRef.child(current_user.getUid()).child("pre_Dep").setValue(sub_score);
                }else {
                    myRef.child(current_user.getUid()).child("post_Dep").setValue(sub_score);
                }
                break;
        }
        pgd.dismiss();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        mediaPlayer.stop();
        if ( v == read_btn ) {
            switch (pointer) {
                case "Hyper1Activity" :
                    intent = new Intent(this,Hyper1Activity.class);
                    break;
                case "Oste1Activity" :
                    intent = new Intent(this,Oste1Activity.class);
                    break;
                case "Lipid1Activity" :
                    intent = new Intent(this,Lipid1Activity.class);
                    break;
                case "Diab1Activity" :
                    intent = new Intent(this,Diab1Activity.class);
                    break;
                case "Dep1Activity" :
                    intent = new Intent(this,Dep1Activity.class);
                    break;
            }
            intent.putExtra("return_point","disease");
            startActivity(intent);
        } else if ( v == back_btn ){
            intent = new Intent(this,DiseaseActivity.class);
            intent.putExtra("return_point","disease");
            startActivity(intent);
        } else if ( v == show_answer_btn ) {
            intent = new Intent(this,AnswerActivity.class);
            intent.putExtra("pointer",pointer);
            startActivity(intent);
        }

    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}

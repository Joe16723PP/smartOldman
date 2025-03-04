package kku.en.coe.smartoldman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
//    FirebaseUser user;
    ProgressDialog pgd;
    Button submitBtn;
    EditText f_name , l_name , ageTv ,weightTv , heightTv;

    String email;
    String password = "123456";
    String TAG = "LogTestLogin";
    private String name , gender , age , weight , height, Cur_Uid;

    RadioGroup rgb;

    int random ;
    double bmi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        rgb = findViewById(R.id.genre);
        rgb.setOnCheckedChangeListener(this);

        f_name = findViewById(R.id.F_Name);
        l_name = findViewById(R.id.L_Name);
//        gend = findViewById(R.id.genre);
        ageTv = findViewById(R.id.age);
        weightTv = findViewById(R.id.weight);
        heightTv = findViewById(R.id.height);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);

//        createAccount();

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("users");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getInfoUser(){
        name = f_name.getText().toString() + " " + l_name.getText().toString();
        age = ageTv.getText().toString();
        weight = weightTv.getText().toString();
        height = heightTv.getText().toString();
        try {
            double tmp_h = Double.parseDouble(height);
            double powHeight = Math.pow((tmp_h / 100),2);
            bmi = (Double.parseDouble(weight) / powHeight) ;
            Log.d("bmi", bmi + " : " + powHeight);

        } catch (Exception e) {
            Toast.makeText(this,"การเข้ารหัสข้อมูลผิดพลาด กรุณาลองใหม่อีกครั้ง",Toast.LENGTH_LONG).show();
            Log.d("bmi", String.valueOf(bmi));
        }
//        Toast.makeText(LoginActivity.this, String.valueOf(bmi), Toast.LENGTH_LONG).show();
    }
    private void createAccount() {
        random = (int)(Math.random() * 100000 + 1);
        email = "dummy" + random + "@testmail.com";
        Log.d(TAG,email);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
//                            user = mAuth.getCurrentUser();
//                            Cur_Uid = user.getUid();
//                            Log.d(TAG, Cur_Uid);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                            mAuth.signOut();
                            createAccount();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if ( v == submitBtn ) {
            try {
                pgd = ProgressDialog.show(this, "กำลังเก็บข้อมูล กรุณารอสักครู่", "Loading...", true, false);
                createAccount();
                getInfoUser();
//                addUser(Cur_Uid,name,gender, weight, age, height, String.valueOf(bmi));
                // set delay time
                new Timer().schedule(
                        new TimerTask(){
                            @Override
                            public void run(){
                                pgd.dismiss();
                                doIntent();
                            }
                        }, 3500);
            } catch (Exception e) {
                Log.d(TAG, "err : wtf" + e);
                Toast.makeText(this,"การเชื่อมต่อกับฐานข้อมูลล้มเหลว \nกรุณาตรวจสอบอินเทอร์เน็ตของท่านแล้วลองใหม่อีกครั้ง",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doIntent() {
        Intent intent = new Intent(this,SubLoginActivity.class);
        intent.putExtra("bmi",String.valueOf(bmi));
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        intent.putExtra("weight", weight);
        intent.putExtra("height", height);
        intent.putExtra("gender", gender);
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if ( checkedId == R.id.female ) {
            gender = "ผู้หญิง";

        } else if ( checkedId == R.id.male ) {
            gender = "ผู้ชาย";
        }
        Log.e("gen",gender);
    }

//    private void updUser(String id, String email) {
//        myRef.child("users").child(id).child("email").setValue(email);
//        Toast.makeText(SecondActivity.this,
//                "ID : " + id + ", Email : " + email,
//                Toast.LENGTH_LONG).show();
//    }
}

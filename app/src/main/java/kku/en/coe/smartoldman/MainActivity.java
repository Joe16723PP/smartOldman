package kku.en.coe.smartoldman;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    CardView cv;
    Button cv, emer_btn;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

//        add audio file
        String audio_name = "title";
        int raw_audio = getResources().getIdentifier(audio_name , "raw", getPackageName());
        mp = MediaPlayer.create(this,raw_audio);
        mp.start();
//        end add audio file

        cv = findViewById(R.id.loginBtn);
        emer_btn = findViewById(R.id.emer_btn);
        emer_btn.setOnClickListener(this);
        cv.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        if ( v == cv ) {
            if ( mUser != null) {
                Intent intent = new Intent(this,BmiActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }
        }
        else if ( v == emer_btn) {
//            Intent intent = new Intent(this,Emergency1Activity.class);
            Intent intent = new Intent(this,Emer2Activity.class);
            startActivity(intent);
        }
    }
}

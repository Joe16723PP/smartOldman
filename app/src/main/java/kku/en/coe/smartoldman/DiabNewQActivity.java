package kku.en.coe.smartoldman;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DiabNewQActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<OsteQListitem> listItems;
    private Button next_btn, back_btn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private TextView tv_questtion;
    private RadioGroup rdg_choice;
    private RadioButton rd_choice_1, rd_choice_2, rd_choice_3, rd_choice_4, rd_choice_5, rd_hidden;
    private ImageButton sound_btn;
    public int[] score = new int[12];
    public int index = 0, total_score = 0, ans = 0, img_btn_state = 0;

    String places[], file_name = "diab_new_quest.json", audio = "";
    JSONArray placesObj;
    JSONObject jsonObject;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oste_newq);
        setTitle("แบบคัดกรองโรคเบาหวานชนิดที่ 2");

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        tv_questtion = findViewById(R.id.tv_questtion);
        rdg_choice = findViewById(R.id.rdg_choice);
        rd_choice_1 = findViewById(R.id.rd_choice_1);
        rd_choice_2 = findViewById(R.id.rd_choice_2);
        rd_choice_3 = findViewById(R.id.rd_choice_3);
        rd_choice_4 = findViewById(R.id.rd_choice_4);
        rd_choice_5 = findViewById(R.id.rd_choice_5);
        rd_choice_5.setVisibility(View.INVISIBLE);
        rd_choice_5.setPadding(1,1,1,1);
        rd_hidden = findViewById(R.id.rd_hidden);
        rd_hidden.setChecked(true);

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);
        sound_btn = findViewById(R.id.sound_btn);
        sound_btn.setOnClickListener(this);

        listItems = new ArrayList<>();
        readLocalJson(file_name, index);
        radioHandle();
    }
    public void doBackMain(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void playMp3(String audio_name) {
        int raw_audio = getResources().getIdentifier(audio_name , "raw", getPackageName());
        mp = MediaPlayer.create(this,raw_audio);
        mp.start();
    }

    private void setPlayMp3() {
        if (img_btn_state == 1) {
            mp.pause();
        }
        img_btn_state = 0;
        sound_btn.setImageResource(R.drawable.play);
    }

    private void setPauseMp3() {
        playMp3(audio);
        img_btn_state = 1;
        sound_btn.setImageResource(R.drawable.pause);
    }

    private void radioHandle() {
        rdg_choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_choice_1) {
                    switch (index) {
                        case 0 : ans = 0; break;
                        case 1 : ans = 0; break;
                        case 2 : ans = 0; break;
                        case 3 : ans = 0; break;
                        case 4 : ans = 0; break;
                        case 5 : ans = 0; break;
                        default: ans = 0;
                    }
                    Log.e("global" ,index + " : ans = " + ans);
                } else if (checkedId == R.id.rd_choice_2) {
                    switch (index) {
                        case 0 : ans = 0; break;
                        case 1 : ans = 2; break;
                        case 2 : ans = 3; break;
                        case 3 : ans = 2; break;
                        case 4 : ans = 2; break;
                        case 5 : ans = 4; break;
                        default: ans = 0;
                    }
                    Log.e("global" ,index + " : ans = " + ans);
                } else if (checkedId == R.id.rd_choice_3) {
                    switch (index) {
                        case 0 : ans = 1; break;
                        case 1 : ans = 0; break;
                        case 2 : ans = 5; break;
                        default: ans = 0;
                    }
                    Log.e("global" ,index + " : ans = " + ans);
                } else if (checkedId == R.id.rd_choice_4) {
                    switch (index) {
                        case 0 : ans = 2; break;
                        default: ans = 0;
                    }
                    Log.e("global" ,index + " : ans = " + ans);
                } else if (checkedId == R.id.rd_choice_5) {
                    ans = 0;
                } else {
                    ans = 0;
                }
                score[index] = ans;
            }
        });
    }

    private void resetRadioButton() {
        rd_hidden.setChecked(true);
    }

    private void checkChoiceVisible(RadioButton radio, String text) {
        if (text.equals("")) {
            radio.setVisibility(View.INVISIBLE);
            rd_choice_5.setPadding(1,1,1,1);
        } else {
            radio.setVisibility(View.VISIBLE);
            rd_choice_5.setPadding(10,10,10,10);
            radio.setText(text);
        }
    }

    private void readLocalJson(String urlVal, int indexR) {
        String json = null;
        try {
            InputStream is = getAssets().open(urlVal);
            int size = is.available();
            Log.d("msg", String.valueOf(size));
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "utf-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONObject object = new JSONObject(json);
            placesObj = (JSONArray) object.get("diab_quest");
            int ObjLng = placesObj.length();
            JSONObject nameObj = (JSONObject) placesObj.get(indexR);
            String id = (String) nameObj.get("id");
            String question = (String) nameObj.get("question");
            String choice_1 = (String) nameObj.get("choice1");
            String choice_2 = (String) nameObj.get("choice2");
            String choice_3 = (String) nameObj.get("choice3");
            String choice_4 = (String) nameObj.get("choice4");
            audio = (String) nameObj.get("audio");

            tv_questtion.setText(question);
            checkChoiceVisible(rd_choice_1,choice_1);
            checkChoiceVisible(rd_choice_2,choice_2);
            checkChoiceVisible(rd_choice_3,choice_3);
            checkChoiceVisible(rd_choice_4,choice_4);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
            setPlayMp3();
            if (rd_hidden.isChecked()){
                Toast.makeText(this, "กรุณาเลือกคำตอบก่อน" , Toast.LENGTH_LONG).show();
            } else {
                if (index < 2) {
//                Toast.makeText(this,index + " / " + score[index],Toast.LENGTH_LONG).show();
                    index = index + 1;
                    readLocalJson(file_name, index);
                    resetRadioButton();
                } else {
                    total_score = 0;
                    Intent intent = new Intent(this,BmiActivity.class);
                    for (int i = 0 ; i < 2 ; i++) {
                        total_score = total_score + score[i];
                    }
//                Toast.makeText(this,"total_score = " + total_score,Toast.LENGTH_LONG).show();
                    myRef.child(current_user.getUid()).child("diab_score").setValue(total_score);
                    intent.putExtra("daib2_score" , total_score);
                    startActivity(intent);
                }
            }
        }

        else if ( v == back_btn ) {
            setPlayMp3();
            if (index > 0) {
                index = index - 1;
                readLocalJson(file_name, index);
                resetRadioButton();
            } else {
                Intent intent = new Intent(this,HyperNewQActivity.class);
                startActivity(intent);
            }
        }

        else if ( v == sound_btn ) {
            if (img_btn_state == 0) {
                setPauseMp3();
            } else {
                setPlayMp3();
            }
        }
    }
}

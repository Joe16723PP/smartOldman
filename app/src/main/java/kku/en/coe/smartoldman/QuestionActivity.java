package kku.en.coe.smartoldman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next_btn , back_btn;
    private RadioGroup rgb;
    private RadioButton noRb , yesRb ,tmpRb;
    private ImageButton play_sound_btn;
    private MediaPlayer mp;
    JSONArray placesObj;
    int index = 0;
    private boolean audio_state = false;
    public String[] answer_first = new String[10];

    String pointer = "";
    String audio_name = "title";

    String file_name, post_test = "", title , ans = "0";
    TextView question_num , question_text , titleText;


    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);
        play_sound_btn = findViewById(R.id.sound_btn);
//        play_sound_btn.setOnClickListener(this);
        titleText = findViewById(R.id.txt_head);
        tmpRb = findViewById(R.id.tmp_rb);
        tmpRb.setVisibility(View.INVISIBLE);
        question_num = findViewById(R.id.question_num_tv);
        question_text = findViewById(R.id.question_text_tv);

        rgb = findViewById(R.id.rgb_question);
        rgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_rb) {
                    ans = "1";
                }else if (checkedId == R.id.no_rb){
                    ans = "0";
                }
            }
        });


        try {
            getIntentData();
        } catch (Exception e) {
            Log.e("filename" , file_name + "::" + pointer + "::" + index + "::" + title + ": " );
        }
        file_name += ".json";
        doReadFirebase();
        readLocalJson(file_name);
    }

    private void doReadFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String str_user = String.valueOf(keyNode.getKey());
                    String uid = current_user.getUid();
                    Log.e("firebase_score" , " :: " + pointer );
                    if (str_user.equals(uid)){
                        User user = keyNode.getValue(User.class);
                        String pre_dep = user.getPre_Dep();
                        String pre_diab = user.getPre_Diab();
                        String pre_hyper = user.getPre_Hyper();
                        String pre_lipid = user.getPre_Lipid();
                        String pre_oste = user.getPre_Oste();

                        Log.e("firebase_score" , "::::" + post_test);

                        if ((post_test == null) && (index == 0)) {
//                            Toast.makeText(QuestionActivity.this,"check pretest",Toast.LENGTH_LONG).show();
                            if (!pre_dep.equals("") && pointer.equals("Dep1Activity")) {
                                Intent intent = new Intent(QuestionActivity.this,Dep1Activity.class);
                                intent.putExtra("return_point","disease");
                                startActivity(intent);
                            }
                            if (!pre_diab.equals("") && pointer.equals("Diab1Activity")) {
                                Intent intent = new Intent(QuestionActivity.this,Diab1Activity.class);
                                intent.putExtra("return_point","disease");
                                startActivity(intent);
                            }
                            if (!pre_hyper.equals("") && pointer.equals("Hyper1Activity")) {
                                Intent intent = new Intent(QuestionActivity.this,Hyper1Activity.class);
                                intent.putExtra("return_point","disease");
                                startActivity(intent);
                            }
                            if (!pre_lipid.equals("") && pointer.equals("Lipid1Activity")) {
                                Intent intent = new Intent(QuestionActivity.this,Lipid1Activity.class);
                                intent.putExtra("return_point","disease");
                                startActivity(intent);
                            }
                            if (!pre_oste.equals("") && pointer.equals("Oste1Activity")) {
                                Intent intent = new Intent(QuestionActivity.this,Oste1Activity.class);
                                intent.putExtra("return_point","disease");
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        post_test = extras.getString("post_test");
        if (post_test == null) {
            setTitle("แบบทดสอบ" + "ก่อนใช้งาน");
        } else {
            setTitle("แบบทดสอบ" + "หลังใช้งาน");
        }
        pointer = extras.getString("next_pointer");

        switch (pointer) {
            case "Hyper1Activity" :
                file_name = "pre_sick1";
                title = getResources().getString(R.string.hypertension_string);
                break;
            case "Oste1Activity" :
                file_name = "pre_sick2";
                title = getResources().getString(R.string.oste_string);
                break;
            case "Lipid1Activity" :
                file_name = "pre_sick3";
                title = getResources().getString(R.string.lipid_string);
                break;
            case "Diab1Activity" :
                file_name = "pre_sick4";
                title = getResources().getString(R.string.diabetes_string);
                break;
            case "Dep1Activity" :
                file_name = "pre_sick5";
                title = getResources().getString(R.string.depression_string);
                break;
        }
    }

    private void readLocalJson(String urlVal) {
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
//            if question
            Log.e("index", "err index" + index);
            if (index > 0) {
                placesObj = (JSONArray) object.get("pre_sick");
                JSONObject nameObj = (JSONObject) placesObj.get(index-1);
                String id = (String) nameObj.get("id");
                String question = (String) nameObj.get("question");
                audio_name = (String) nameObj.get("audio");
                //                set audio file
                int raw_audio = getResources().getIdentifier(audio_name , "raw", getPackageName());
                mp = MediaPlayer.create(this,raw_audio);
                //                set text
                question_num.setText("");
                titleText.setText("คำถามข้อที่ : " + id);
                question_text.setText(question);
                rgb.setVisibility(View.VISIBLE);
            }
//            if guide
            else {
                placesObj = (JSONArray) object.get("guide");
                JSONObject guideObj = (JSONObject) placesObj.get(index);
                String header = (String) guideObj.get("header");
                String text = (String) guideObj.get("text");
                if (post_test == null){
                    audio_name = (String) guideObj.get("audio");
                } else {
                    audio_name = "guide_post_audio";
                }
//                set audio file
                int raw_audio = getResources().getIdentifier(audio_name , "raw", getPackageName());
                mp = MediaPlayer.create(this,raw_audio);
//                set text
                question_num.setText("");
                titleText.setText(header);
                question_text.setText(text);
                rgb.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
            try {
                mp.stop();
            }catch (Exception e) {
            }
            audio_state = false;
            int resID = getResources().getIdentifier("play" , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            play_sound_btn.setImageResource(resID);
            Log.e("index",""+index);
            GlobalAnswerQuestion global = GlobalAnswerQuestion.getInstance();
            if (tmpRb.isChecked()) {
                Toast.makeText(this, "กรุณาเลือกคำตอบก่อน" , Toast.LENGTH_LONG).show();
            } else {
                if ( index >= 10 ) {
//                    Toast.makeText(this,"index : " +index , Toast.LENGTH_LONG).show();
                        answer_first[index -1] = ans;
                        Intent intent = new Intent(this,QuestionShowScore.class);
                        intent.putExtra("return_point",pointer);
                        intent.putExtra("post_test",post_test);
                        intent.putExtra("title",title);
                        startActivity(intent);
                }
                else {
                    if (index != 0) {
                        answer_first[index -1] = ans;
                    }
                }
                index = index+1;
                tmpRb.setChecked(true);
                readLocalJson(file_name);
                global.setArray_answer(answer_first);
            }
        }

        else if ( v == back_btn ) {
            try {
                mp.stop();
            }catch (Exception e) {
                
            }
            Intent intent = null;
            audio_state = false;
            int resID = getResources().getIdentifier("play" , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            play_sound_btn.setImageResource(resID);
            if ( post_test == null){
                if (index == 0) {
                    intent = new Intent(this,DiseaseActivity.class);
                    intent.putExtra("file_name" , file_name);
                    intent.putExtra("next_pointer", pointer);
                    intent.putExtra("post_test",post_test);
                    intent.putExtra("title", title);
                    intent.putExtra("index", index);
                    startActivity(intent);
                }else {
                    tmpRb.setChecked(true);
                    readLocalJson(file_name);
//                intent = new Intent(this,QuestionActivity.class);
                }
            }else {
                if (index == 0) {
//                    intent = new Intent(this,pointer.class);
                    switch (pointer) {
                        case "Hyper1Activity" :
                            intent = new Intent(this,Hyper4Activity.class);
                            break;
                        case "Oste1Activity" :
                            intent = new Intent(this,Oste4Activity.class);
                            break;
                        case "Lipid1Activity" :
                            intent = new Intent(this,Lipid4Activity.class);
                            break;
                        case "Diab1Activity" :
                            intent = new Intent(this,Diab4Activity.class);
                            break;
                        case "Dep1Activity" :
                            intent = new Intent(this,Dep4Activity.class);
                            break;
                    }
                    intent.putExtra("file_name" , file_name);
                    intent.putExtra("next_pointer", pointer);
                    intent.putExtra("return_point","disease");
                    intent.putExtra("post_test",post_test);
                    intent.putExtra("title", title);
//                    intent.putExtra("index", index);
                    startActivity(intent);
                }else {
                    tmpRb.setChecked(true);
                    readLocalJson(file_name);
//                intent = new Intent(this,QuestionActivity.class);
                }

            }
            index = index-1;
        }
        else if (v == play_sound_btn){
            if (!audio_state)
                audio_state = true;
            else
                audio_state = false;
            try {
                if (audio_state) {
//                    Toast.makeText(this,"playing sound",Toast.LENGTH_LONG).show();
                    int resID = getResources().getIdentifier("pause" , "drawable", getPackageName());
                    Log.e("img", String.valueOf(resID));
                    play_sound_btn.setImageResource(resID);
                    mp.start();
                }
                else {
//                    Toast.makeText(this,"pause sound",Toast.LENGTH_LONG).show();
                    int resID = getResources().getIdentifier("play" , "drawable", getPackageName());
                    Log.e("img", String.valueOf(resID));
                    play_sound_btn.setImageResource(resID);
                    mp.pause();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void doBackHome(View view) {
        Intent intent = new Intent(this,DiseaseActivity.class);
        startActivity(intent);
    }
}

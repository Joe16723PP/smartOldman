package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class Lipid4Activity extends AppCompatActivity implements View.OnClickListener {
    private JSONArray sick;
    private JSONObject page, obj;
    private String json, head, text, img, link,rt_point, file_name,sub_img,main_img, img_1, img_2, img_3, img_4;
    private TextView text_title, text_desc, txt_link, img_sub, ref_img;
    private ImageView img_main, img_small1, img_small2, img_small3, img_small4;
    private Button btn_back, btn_next;
    private ImageButton sound_btn;
    private int index , send_index, max_length, img_btn_state = 0;
    String audio = "";
    MediaPlayer mp;

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sick);
        setTitle(R.string.lipid_string);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        file_name = "lipid.json";
        ref_img = findViewById(R.id.ref_img);
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setText("ทำแบบทดสอบ");
        sound_btn = findViewById(R.id.sound_btn);
        text_desc = findViewById(R.id.txt_emer);
        text_title = findViewById(R.id.title);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        sound_btn.setOnClickListener(this);
        img_main = findViewById(R.id.main_img);
        img_sub = findViewById(R.id.sub_img_top);
        txt_link = findViewById(R.id.txt_link);
        img_small1 = findViewById(R.id.img_small1);
        img_small2 = findViewById(R.id.img_small2);
        img_small3 = findViewById(R.id.img_small3);
        img_small4 = findViewById(R.id.img_small4);
        try {
            readJson();
            setIntentData();
            setData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("L4", String.valueOf(e));
        }

        doReadFirebase();
//        Toast.makeText(this,rt_point,Toast.LENGTH_LONG).show();
    }

    private void doReadFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String str_user = String.valueOf(keyNode.getKey());
                    String uid = current_user.getUid();
//                    Log.e("firebase_score" , " :: " + pointer );
                    if (str_user.equals(uid)){
                        User user = keyNode.getValue(User.class);
                        String post_lipid = user.getPost_Lipid();

//                        Log.e("firebase_score" , "::::" + pre_dep + pre_diab + pre_hyper + pre_lipid + pre_oste);

//                        if (!post_lipid.equals("")) {
//                            Intent intent = new Intent(Lipid4Activity.this,DiseaseActivity.class);
//                            intent.putExtra("return_point","disease");
//                            startActivity(intent);
//                        }
                    }
                }
//                Log.e("firebase" , dep_score + ":" + oste_score + ":" + hyper_score + ":" + bmi) ;
                Log.e("firebase_score" , "test test");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void backHome(View view) {
        Intent intent = new Intent(this,DiseaseActivity.class);
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

    private void setIntentData() {
        Bundle extras = getIntent().getExtras();
        try {
            index = Integer.parseInt(extras.getString("index"));
        }catch (Exception e){
            index = max_length-1;
            e.printStackTrace();
        }
        rt_point = extras.getString("return_point");
        Log.e("rtp",rt_point);
//        index_int = Integer.parseInt(index);
//        Log.e("HACK", String.valueOf(index_int));
    }

    private void readJson() {
        try {
            obj = new JSONObject(loadJSONFromAsset(this,file_name));
            sick = (JSONArray) obj.get("lipid");
            max_length = sick.length();
            img_sub.setText(max_length + " / " + max_length);
            page = sick.getJSONObject(max_length-1);
            sub_img = page.getString("sub_img");
            main_img = page.getString("main_img");
            text = page.getString("text");
            head = page.getString("title");
            link = page.getString("link");
            img_1 = page.getString("img_1");
            img_2 = page.getString("img_2");
            img_3 = page.getString("img_3");
            img_4 = page.getString("img_4");
            audio = page.getString("audio");
            ref_img.setText("ที่มา : " + page.getString("ref_img"));

//            if (img != "") {
//                String mDrawableName = img;
//                int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
//                img_main.setImageResource(resID);
//            }
//            txt_head.setText(head);
//            txt_text.setText(text);
//            txt_link.setText(link);
//            Log.e("HACK",head + " " + text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setData() {
//        if (!sub_img.equals("")) {
//            String mDrawableName = sub_img;
//            Log.e("img", mDrawableName);
//            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
//            img_sub.setImageResource(resID);
//        }
        if (!main_img.equals("")) {
            String mDrawableName = main_img;
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            img_main.getLayoutParams().height = 350;
            img_main.setImageResource(resID);
        }
        if (!img_small1.equals("")) {
            String mDrawableName = img_1;
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            img_small1.setImageResource(resID);
        }
        if (!img_small2.equals("")) {
            String mDrawableName = img_2;
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            img_small2.setImageResource(resID);
        }
        if (!img_small3.equals("")) {
            String mDrawableName = img_3;
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            img_small3.setImageResource(resID);
        }
        if (!img_small4.equals("")) {
            String mDrawableName = img_4;
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            img_small4.setImageResource(resID);
        }
        if (!link.equals("")) {
            txt_link.setText(link);
        }
//        else {
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) txt_link.getLayoutParams();
//            lp.setMargins(0,0,0,0);
//            txt_link.setLayoutParams(lp);
//            txt_link.setPadding(0,0,0,0);
//        }
//            txt_head.setText(head);
//            txt_text.setText(text);
//            txt_link.setText(link);
        text_title.setText(head);
        Log.e("HACK",head + " " + text);
    }

    public String loadJSONFromAsset(Context context, String file_name) {
        json = null;
        try {
            InputStream is = context.getAssets().open(file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        if ( v == btn_back) {
            setPlayMp3();
            index -= 1;
            if ( index <= 0 ) {
                Intent intent = new Intent(this,Lipid1Activity.class);
                intent.putExtra("index",String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this,Lipid3Activity.class);
                intent.putExtra("index",String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            }

        } else if ( v == btn_next ) {
            setPlayMp3();
            doReadFirebase();
            index += 1;
            if (rt_point.equals("disease")) {
                Intent intent = new Intent(this,QuestionActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("post_test", "post_test");
                intent.putExtra("next_pointer","Lipid1Activity");
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
                Log.e("HACK", String.valueOf(index));
            } else {
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("index", String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            }
        } else if ( v == sound_btn ) {
//            Intent intent = new Intent(this,Emergency2Activity.class);
//            startActivity(intent);
            if (img_btn_state == 0) {
                setPauseMp3();
            } else {
                setPlayMp3();
            }
        }
    }
}

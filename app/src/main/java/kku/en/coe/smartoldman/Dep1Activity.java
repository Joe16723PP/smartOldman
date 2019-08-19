package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Dep1Activity extends AppCompatActivity implements View.OnClickListener {
    private JSONArray sick;
    private JSONObject page, obj;
    private String json, head, text, main_img, sub_img, img, link, file_name,rt_point, img_1, img_2, img_3, img_4;
    private ImageView img_main , img_small1, img_small2, img_small3, img_small4;
    private Button btn_back, btn_next;
    private ImageButton sound_btn;
    private TextView text_title,text_desc,txt_link , img_sub, ref_img;
    private int index = 0, send_index, max_length;
    private MediaPlayer mp;
    String audio_name = "title";
    private boolean audio_state = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sick);
        setTitle(R.string.depression_string);
        file_name = "depression.json";
        btn_back = findViewById(R.id.btn_back);
        btn_back.setText("เนื้อหาทั้งหมด");
        ref_img = findViewById(R.id.ref_img);
        btn_next = findViewById(R.id.btn_next);
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
        readJson();
        setData();
        getIntentData();
    }
    public void backHome(View view) {
        Intent intent = new Intent(this,DiseaseActivity.class);
        startActivity(intent);
    }

    private void getIntentData() {
        try {
            Bundle extras = getIntent().getExtras();
            rt_point = extras.getString("return_point");
        } catch (Exception e) {
            rt_point = "";
        }
    }

    private void setData() {
//        if (!sub_img.equals("")) {
//            String mDrawableName = sub_img;
//            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
//            Log.e("img", String.valueOf(resID));
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
        text_desc.setText(text);
        text_title.setText(head);
        Log.e("HACK",head + " " + text);
    }

    private void readJson() {
        try {
            obj = new JSONObject(loadJSONFromAsset(this,file_name));
            sick = (JSONArray) obj.get("depression");
            max_length = sick.length();
            page = sick.getJSONObject(0);
            img_sub.setText( (index+1) + " / " + max_length);
//            head = page.getString("head");
            sub_img = page.getString("sub_img");
            main_img = page.getString("main_img");
            text = page.getString("text");
            head = page.getString("title");
            link = page.getString("link");
            img_1 = page.getString("img_1");
            img_2 = page.getString("img_2");
            img_3 = page.getString("img_3");
            img_4 = page.getString("img_4");
            audio_name = page.getString("audio");
            ref_img.setText("ที่มา : " + page.getString("ref_img"));
            //                set audio file
            int raw_audio = getResources().getIdentifier(audio_name , "raw", getPackageName());
            mp = MediaPlayer.create(this,raw_audio);
//            img = page.getString("img");
//            link = page.getString("link");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public String loadJSONFromAsset(Context context,String file_name) {
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
            mp.stop();
            if ( rt_point.equals("disease") ) {
                Intent intent = new Intent(this,DiseaseActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        } else if ( v == btn_next ) {
            mp.stop();
            index += 1;
            Intent intent = new Intent(this,Dep2Activity.class);
            intent.putExtra("index",String.valueOf(index));
            intent.putExtra("return_point",rt_point);
            startActivity(intent);
        } else if ( v == sound_btn ) {
            if (!audio_state)
                audio_state = true;
            else
                audio_state = false;
            try {
                if (audio_state) {
//                    Toast.makeText(this,"playing sound",Toast.LENGTH_LONG).show();
                    int resID = getResources().getIdentifier("pause" , "drawable", getPackageName());
                    Log.e("img", String.valueOf(resID));
                    sound_btn.setImageResource(resID);
                    mp.start();
                }
                else {
//                    Toast.makeText(this,"pause sound",Toast.LENGTH_LONG).show();
                    int resID = getResources().getIdentifier("play" , "drawable", getPackageName());
                    Log.e("img", String.valueOf(resID));
                    sound_btn.setImageResource(resID);
                    mp.pause();
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

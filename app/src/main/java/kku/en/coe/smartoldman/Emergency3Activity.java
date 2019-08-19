package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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

public class Emergency3Activity extends AppCompatActivity implements View.OnClickListener {
    private JSONArray sick;
    private JSONObject page, obj;
    private String json, head, text, img, link, file_name,sub_img,main_img,rt_point,img_1, img_2, img_3, img_4 , phone_call = "tel:";
    private TextView text_title, text_desc, txt_link;
    private ImageView img_main, img_small1, img_small2, img_small3, img_small4;
    private Button btn_back, btn_next;
    private ImageButton sound_btn, img_sub;
    private int index , send_index, max_length;
    private MediaPlayer mp;
    String audio_name = "title";
    private boolean audio_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency1);
        setTitle(R.string.emerStr);
        file_name = "emergency.json";
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        sound_btn = findViewById(R.id.sound_btn);
        sound_btn.setImageResource(R.drawable.play);
        text_desc = findViewById(R.id.txt_emer);
        text_title = findViewById(R.id.title);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        sound_btn.setOnClickListener(this);
        img_main = findViewById(R.id.main_img);
        img_sub = findViewById(R.id.sub_img_top);
        img_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(phone_call));
                    if (ActivityCompat.checkSelfPermission(Emergency3Activity.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Emergency3Activity.this,
                                android.Manifest.permission.CALL_PHONE)) {
                        } else {
                            ActivityCompat.requestPermissions(Emergency3Activity.this,
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    101);
                        }
                    }
                    startActivity(callIntent);
                } catch (Exception e){
                    Log.e("call" , String.valueOf(e));
                }

            }
        });
        txt_link = findViewById(R.id.txt_link);
        img_small1 = findViewById(R.id.img_small1);
        img_small2 = findViewById(R.id.img_small2);
        img_small3 = findViewById(R.id.img_small3);
        img_small4 = findViewById(R.id.img_small4);
        setIntentData();
        readJson();
        setData();
    }
    private void setData() {
        if (!sub_img.equals("")) {
            String mDrawableName = sub_img;
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            Log.e("img", String.valueOf(resID));
            img_sub.setImageResource(resID);
        }
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

        text_desc.setText(text);
        text_title.setText(head);
        Log.e("HACK",head + " " + text);
    }

    public void doBackMain(View view) {
        if (rt_point.equals("disease")) {
            Intent intent = new Intent(this,DiseaseActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);        }
    }
    private void setIntentData() {
        Bundle extras = getIntent().getExtras();
        index = Integer.parseInt(extras.getString("index"));
        rt_point = extras.getString("return_point");
//        index_int = Integer.parseInt(index);
//        Log.e("HACK", String.valueOf(index_int));
    }
    private void readJson() {
        try {
            obj = new JSONObject(loadJSONFromAsset(this,file_name));
            sick = (JSONArray) obj.get("emergency");
            max_length = sick.length();
            page = sick.getJSONObject(index);
            sub_img = page.getString("sub_img");
            main_img = page.getString("main_img");
            text = page.getString("text");
            head = page.getString("title");
            link = page.getString("link");
            img_1 = page.getString("img_1");
            img_2 = page.getString("img_2");
            img_3 = page.getString("img_3");
            img_4 = page.getString("img_4");
            phone_call += page.getString("phone_call");
            audio_name = page.getString("audio");
            //                set audio file
            int raw_audio = getResources().getIdentifier(audio_name , "raw", getPackageName());
            mp = MediaPlayer.create(this,raw_audio);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            mp.stop();
            index -= 1;
            if ( index <= 0 ) {
                Intent intent = new Intent(this,Emergency1Activity.class);
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this,Emergency2Activity.class);
                intent.putExtra("index",String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            }

        } else if ( v == btn_next ) {
            mp.stop();
            index += 1;
            if (index >= (max_length - 1)) {
                Intent intent = new Intent(this,Emergency4Activity.class);
                intent.putExtra("index", String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
                Log.e("HACK", String.valueOf(index));
            } else {
                Intent intent = new Intent(this,Emergency2Activity.class);
                intent.putExtra("index", String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            }
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

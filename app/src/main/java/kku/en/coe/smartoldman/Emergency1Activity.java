package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Emergency1Activity extends AppCompatActivity implements View.OnClickListener {
    private JSONArray sick;
    private JSONObject page, obj;
    private String json, head, text, main_img, sub_img, img, link, file_name,rt_point;
    private ImageView img_main , img_sub;
    private Button btn_back, btn_next , sound_btn;
    private TextView text_title,text_desc;
    private int index = 0, send_index, max_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency1);
        setTitle(R.string.emerStr);
        file_name = "emergency.json";
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        sound_btn = findViewById(R.id.sound_btn);
        text_desc = findViewById(R.id.txt_emer);
        text_title = findViewById(R.id.title);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        sound_btn.setOnClickListener(this);
        img_main = findViewById(R.id.main_img);
        img_sub = findViewById(R.id.sub_img_top);
        readJson();
        setData();
        getIntentData();
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
            sick = (JSONArray) obj.get("emergency");
            max_length = sick.length();
            page = sick.getJSONObject(0);
//            head = page.getString("head");
            sub_img = page.getString("sub_img");
            main_img = page.getString("main_img");
            text = page.getString("text");
            head = page.getString("title");
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
            if ( rt_point.equals("disease") ) {
                Intent intent = new Intent(this,DiseaseActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        } else if ( v == btn_next ) {
            index += 1;
            Intent intent = new Intent(this,Emergency2Activity.class);
            intent.putExtra("index",String.valueOf(index));
            intent.putExtra("return_point",rt_point);
            startActivity(intent);
        } else if ( v == sound_btn ) {
//            Intent intent = new Intent(this,Emergency2Activity.class);
//            startActivity(intent);
            Toast.makeText(this,"play sound" ,Toast.LENGTH_LONG).show();
        }
    }
}

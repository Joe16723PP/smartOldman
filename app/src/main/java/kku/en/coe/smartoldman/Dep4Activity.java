package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Dep4Activity extends AppCompatActivity implements View.OnClickListener {
    private JSONArray sick;
    private JSONObject page, obj;
    private String json, head, text, img, link,rt_point, file_name,sub_img,main_img, img_1, img_2, img_3, img_4;
    private TextView text_title, text_desc, txt_link;
    private ImageView img_main,img_sub, img_small1, img_small2, img_small3, img_small4;
    private Button btn_back, btn_next , sound_btn;
    private int index , send_index, max_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency1);
        setTitle(R.string.depression_string);
        file_name = "depression.json";
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
        txt_link = findViewById(R.id.txt_link);
        img_small1 = findViewById(R.id.img_small1);
        img_small2 = findViewById(R.id.img_small2);
        img_small3 = findViewById(R.id.img_small3);
        img_small4 = findViewById(R.id.img_small4);
        setIntentData();
        readJson();
        setData();
        Toast.makeText(this,rt_point,Toast.LENGTH_LONG).show();
    }


    private void setIntentData() {
        Bundle extras = getIntent().getExtras();
        index = Integer.parseInt(extras.getString("index"));
        rt_point = extras.getString("return_point");
        Log.e("rtp",rt_point);
//        index_int = Integer.parseInt(index);
//        Log.e("HACK", String.valueOf(index_int));
    }

    private void readJson() {
        try {
            obj = new JSONObject(loadJSONFromAsset(this,file_name));
            sick = (JSONArray) obj.get("depression");
            max_length = sick.length();
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
        if (!sub_img.equals("")) {
            String mDrawableName = sub_img;
            Log.e("img", mDrawableName);
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
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
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) txt_link.getLayoutParams();
            lp.setMargins(0,0,0,0);
            txt_link.setLayoutParams(lp);
            txt_link.setPadding(0,0,0,0);
        }
//            txt_head.setText(head);
//            txt_text.setText(text);
//            txt_link.setText(link);
        text_desc.setText(text);
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
            index -= 1;
            if ( index <= 0 ) {
                Intent intent = new Intent(this,Dep1Activity.class);
                intent.putExtra("index",String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this,Dep3Activity.class);
                intent.putExtra("index",String.valueOf(index));
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            }

        } else if ( v == btn_next ) {
            index += 1;
            if (rt_point.equals("disease")) {
                Intent intent = new Intent(this,DiseaseActivity.class);
                intent.putExtra("index", String.valueOf(index));
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
            Toast.makeText(this,"play sound" ,Toast.LENGTH_LONG).show();
        }
    }
}

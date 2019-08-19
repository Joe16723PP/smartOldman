package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class AnswerActivity extends AppCompatActivity implements View.OnClickListener {

    private JSONArray array_obj;
    private JSONObject obj_data, obj;
    private String[] tmp_answer_array;

    private String pointer, json;
    private String[] Hyper = {"1","1","1","1","1","1","0","0","1","0"};
    private String[] Oste  = {"0","1","0","1","1","0","1","0","1","0"};
    private String[] Lipid = {"1","1","0","1","1","1","0","1","0","1"};
    private String[] Diab  = {"1","0","1","1","0","1","0","0","0","1"};
    private String[] Dep   = {"1","1","1","1","1","0","1","0","1","1"};

    private TextView answer_tv, answer_tv_header;
    private String file_name;
    private Button read_detail_btn,back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        answer_tv = findViewById(R.id.answer_detail);
        answer_tv_header = findViewById(R.id.header_answer);
        read_detail_btn = findViewById(R.id.read_detail_btn);
        read_detail_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        getIntentData();
        readJson();
    }

    private void getIntentData() {
        try {
            Bundle extras = getIntent().getExtras();
            pointer = extras.getString("pointer");
        } catch (Exception e) {
            pointer = "";
        }
        switch (pointer) {
            case "Hyper1Activity" :
                file_name = "pre_sick1.json";
                tmp_answer_array = Hyper;
                answer_tv_header.setText("เฉลยคำถามโรคความดันโลหิตสูง");
                break;
            case "Oste1Activity" :
                file_name = "pre_sick2.json";
                tmp_answer_array = Oste;
                answer_tv_header.setText("เฉลยคำถามโรคข้อเข่าเสื่อม");
                break;
            case "Lipid1Activity" :
                file_name = "pre_sick3.json";
                tmp_answer_array = Lipid;
                answer_tv_header.setText("เฉลยคำถามโรคไขมันในเลือดสูง");
                break;
            case "Diab1Activity" :
                file_name = "pre_sick4.json";
                tmp_answer_array = Diab;
                answer_tv_header.setText("เฉลยคำถามโรคเบาหวาน");
                break;
            case "Dep1Activity" :
                file_name = "pre_sick5.json";
                tmp_answer_array = Dep;
                answer_tv_header.setText("เฉลยคำถามโรคซึมเศร้า");
                break;
        }
    }

    private void readJson() {
        try {
            obj = new JSONObject(loadJSONFromAsset(this,file_name));
            array_obj = (JSONArray) obj.get("pre_sick");
            for (int i = 0; i < array_obj.length(); i++) {
                String txt_ans;
                if (tmp_answer_array[i].equals("0")) {
                    txt_ans = "ไม่ใช่";
                } else {
                    txt_ans = "ใช่";
                }
                obj_data = array_obj.getJSONObject(i);
                answer_tv.append((i+1) + " : " + obj_data.getString("question") + "\nคำตอบ : " + txt_ans + "\n");
            }

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
        if (v == read_detail_btn) {
            Intent intent = null;
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
            intent.putExtra("file_name" , file_name);
            intent.putExtra("next_pointer", pointer);
            intent.putExtra("return_point","disease");
//                    intent.putExtra("index", index);
            startActivity(intent);

        } else if (v == back_btn) {
            Intent intent = new Intent(this,DiseaseActivity.class);
            startActivity(intent);
        }
    }
}

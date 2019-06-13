package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private RadioButton noRb , yesRb;
    JSONArray placesObj;
    int index = 0;
    public String[] answer_first = new String[10];

    String pointer = "";

    String file_name, post_test, title , ans = "0";
    TextView question_num , question_text , titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        noRb = findViewById(R.id.no_rb);
        yesRb = findViewById(R.id.yes_rb);
        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);
        titleText = findViewById(R.id.txt_head);

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
                else {
                    ans = "0";
                }
            }
        });

        getIntentData();
        Log.e("filename",file_name);
        readLocalJson(file_name);

    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        try {
            title = extras.getString("title");
            post_test = extras.getString("post_test");
            String index_str = extras.getString("index");
            index = Integer.parseInt(index_str);
            file_name = extras.getString("file_name");
            try {
                setTitle("แบบทดสอบ" + title);
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            post_test = null;
            Log.e("joe", e.getMessage());
            e.printStackTrace();
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
        setTitle("แบบทดสอบ" + title);
        file_name += ".json";
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
            if (index != 0) {
                placesObj = (JSONArray) object.get("pre_sick");
                JSONObject nameObj = (JSONObject) placesObj.get(index-1);
                String id = (String) nameObj.get("id");
                String question = (String) nameObj.get("question");
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
            Log.e("index",""+index);
            GlobalAnswerQuestion global = GlobalAnswerQuestion.getInstance();

            if ( index >= 10 ) {
                try {
                    answer_first[index -1] = ans;
                    Intent intent = new Intent(this,QuestionShowScore.class);
                    intent.putExtra("return_point",pointer);
                    intent.putExtra("post_test",post_test);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (index == 0) {
                Toast.makeText(this,"" + index , Toast.LENGTH_LONG).show();
                getIntentData();
            }
            else {
                answer_first[index -1] = ans;
            }
            index = index+1;
            readLocalJson(file_name);
            global.setArray_answer(answer_first);
//            String[] tmp_ans = global.getArray_answer();
            noRb.setChecked(false);
            yesRb.setChecked(false);
//            intent.putExtra("file_name" , file_name);
//            intent.putExtra("next_pointer", pointer);
//            intent.putExtra("post_test",post_test);
//            intent.putExtra("title", title);
//            intent.putExtra("index", index);
//            startActivity(intent);

//            Toast.makeText(this,"do next" , Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this,Question2Activity.class);
//            ArrayAnswer global = ArrayAnswer.getInstance();
//            try {
//                int[] tmp_ans = global.getArray_answer();
//                ArrayList<String> tmp_list = new ArrayList<>();
//                String tmp = "";
//                for (int i = 0; i < tmp_ans.length ;i ++) {
//                    tmp += tmp_ans[i];
//                    tmp_list.add(String.valueOf(tmp_ans[i]));
//                }
//                intent.putStringArrayListExtra("answer_1",tmp_list);
//                intent.putExtra("file_name" , file_name);
//                intent.putExtra("next_pointer", pointer);
//                intent.putExtra("post_test",post_test);
//                intent.putExtra("title", title);
//
//                answer_first[index] = ans;
//                Log.e("answer", String.valueOf(answer_first[index]));
//
//                ArrayAnswer answer = ArrayAnswer.getInstance();
//                answer.setArray_answer(answer_first);
//
//                startActivity(intent);
//                Log.e("global" ,": " + tmp );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        else if ( v == back_btn ) {
            Intent intent = null;
            if (index == 0) {
                intent = new Intent(this,DiseaseActivity.class);
                intent.putExtra("file_name" , file_name);
                intent.putExtra("next_pointer", pointer);
                intent.putExtra("post_test",post_test);
                intent.putExtra("title", title);
                intent.putExtra("index", index);
                startActivity(intent);
            }else {
                index = index-1;
                readLocalJson(file_name);
                noRb.setChecked(false);
                yesRb.setChecked(false);
//                intent = new Intent(this,QuestionActivity.class);
            }


        }
    }
}

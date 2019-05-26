package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private RecyclerView recyclerView;
    private Button next_btn , back_btn;

    private List<QuestionListItem> listItems;
    JSONArray placesObj;

    String pointer = "";
    String file_name, post_test, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getIntentData();

        listItems = new ArrayList<>();
        readLocalJson(file_name);

    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        try {
            post_test = extras.getString("post_test");
        } catch (Exception e) {
            post_test = null;
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
            placesObj = (JSONArray) object.get("pre_sick");
            int ObjLng = placesObj.length();
            for (int i = 0 ; i < ObjLng - 10 ; i++) {
                JSONObject nameObj = (JSONObject) placesObj.get(i);
                String id = (String) nameObj.get("id");
                String question = (String) nameObj.get("question");

                QuestionListItem listItem = new QuestionListItem(
                        question, id
                );
                listItems.add(listItem);
            }

            RecyclerView.Adapter adapter = new QuestionAdapter(listItems, this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
            Toast.makeText(this,"do next" , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,Question2Activity.class);
            ArrayAnswer global = ArrayAnswer.getInstance();
            try {
                int[] tmp_ans = global.getArray_answer();
                ArrayList<String> tmp_list = new ArrayList<>();
                String tmp = "";
                for (int i = 0; i < tmp_ans.length ;i ++) {
                    tmp += tmp_ans[i];
                    tmp_list.add(String.valueOf(tmp_ans[i]));
                }
                intent.putStringArrayListExtra("answer_1",tmp_list);
                intent.putExtra("file_name" , file_name);
                intent.putExtra("next_pointer", pointer);
                intent.putExtra("post_test",post_test);
                intent.putExtra("title", title);
                startActivity(intent);
                Log.e("global" ,": " + tmp );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if ( v == back_btn ) {
            Intent intent = new Intent(this,DiseaseActivity.class);
            startActivity(intent);
        }
    }
}

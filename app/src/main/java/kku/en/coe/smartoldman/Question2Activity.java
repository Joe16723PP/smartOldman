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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Question2Activity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    public ArrayList<String> answer_all = new ArrayList<>();
    private Button next_btn, back_btn;

    private List<QuestionListItem> listItems;
    JSONArray placesObj;

    String tmp1 = "";
    private String[] tmp_ans_1 = new String[10];

    String file_name , pointer, post_test, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this,"Question 2" , Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        doGetAnswer();

        readLocalJson(file_name);
    }

    public void doBackHome(View view) {
        Intent intent = new Intent(this,DiseaseActivity.class);
        startActivity(intent);
    }


    private void doGetAnswer() {
        ArrayList<String> tmp_list;
        Bundle extras = getIntent().getExtras();
        try {
            post_test = extras.getString("post_test");
            title = extras.getString("title");
            setTitle("แบบทดสอบ" + title);
        } catch (Exception e) {
            post_test = null;
        }
        tmp_list = extras.getStringArrayList("answer_1");
        file_name = extras.getString("file_name");
        pointer = extras.getString("next_pointer");
        for (int i = 0; i< tmp_list.size()-10 ;i++) {
            tmp_ans_1[i] = tmp_list.get(i);
            tmp1 += tmp_ans_1[i];
            answer_all.add(tmp_list.get(i));
        }
       Log.e("loop", String.valueOf(answer_all));
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
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONObject object = (JSONObject) new JSONObject(json);
            placesObj = (JSONArray) object.get("pre_sick");
            int ObjLng = placesObj.length();
            for (int i = 10 ; i < ObjLng; i++) {
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
            Intent intent = new Intent(this,QuestionShowScore.class);
//            Toast.makeText(this,"do next" , Toast.LENGTH_LONG).show();
            ArrayAnswer global = ArrayAnswer.getInstance();

            try {
                int[] tmp_ans = global.getArray_answer();
                for (int i = 10; i < tmp_ans.length ;i ++) {
                    tmp1 += tmp_ans[i];
                    answer_all.add(String.valueOf(tmp_ans[i]));
                }
                intent.putStringArrayListExtra("answer_all",answer_all);
                intent.putExtra("return_point",pointer);
                intent.putExtra("post_test",post_test);
                intent.putExtra("title",title);
                startActivity(intent);
                Log.e("finalll" ,": " + answer_all );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if ( v == back_btn ) {
            Intent intent = new Intent(this,QuestionActivity.class);
            intent.putExtra("next_pointer", pointer);
            intent.putExtra("post_test",post_test);
            startActivity(intent);
        }
    }
}

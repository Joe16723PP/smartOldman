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
    private RecyclerView.Adapter adapter;
//    public int answer_pre_sick1[] = new int[10];
    private Button next_btn , back_btn;

//    private List<ListItem> listItems;
    private List<QuestionListItem> listItems;
    JSONArray placesObj;
    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setTitle("Question");

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        String urlFIle = "pre_sick1.json";
        readLocalJson(urlFIle);

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
            placesObj = (JSONArray) object.get("pre_sick1");
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

            adapter = new QuestionAdapter(listItems,this);
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

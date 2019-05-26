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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HyperQActivity extends AppCompatActivity implements View.OnClickListener {
// 3
    private RecyclerView recyclerView;
    private Button next_btn , back_btn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private List<QuestionListItem> listItems;
    JSONArray placesObj;

    String pointer = "";
    String file_name, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setTitle("แบบคัดกรองโรคความดันโลหิตสูง");
        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);


        file_name = "hyper_question.json";
        listItems = new ArrayList<>();
        doReadJson();
    }

    private void doReadJson() {
        String json = null;
        try {
            InputStream is = getAssets().open(file_name);
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
            placesObj = (JSONArray) object.get("hyper_queue");
            int ObjLng = placesObj.length();
            for (int i = 0 ; i < ObjLng ; i++) {
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
            ArrayAnswer global = ArrayAnswer.getInstance();
            int[] tmp_ans = global.getArray_answer();
            int score = 0;
            for (int i = 0 ; i < tmp_ans.length; i++) {
                if (tmp_ans[i] == 1) {
                    score += 1;
                }
            }
            myRef.child(current_user.getUid()).child("hyper_score").setValue(score);
            Intent intent = new Intent(this,BmiActivity.class);
            startActivity(intent);
//            Toast.makeText(this,""+score,Toast.LENGTH_LONG).show();

        } else if ( v == back_btn ) {
            Intent intent = new Intent(this,DepQ3Activity.class);
            startActivity(intent);
        }
    }
}

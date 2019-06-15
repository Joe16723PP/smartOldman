package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HyperNewQActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<OsteQListitem> listItems;
    private Button next_btn, back_btn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private TextView txt_head, question_num_tv, question_text_tv;
    private RadioGroup rgb_question;
    private RadioButton no_rb, yes_rb, tmp_rb;
    public int[] score = new int[12];
    public int index = 0, total_score = 0, ans = 0;

    String places[], file_name = "hyper_question.json";
    JSONArray placesObj;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        setTitle("แบบคัดกรองโรคความดันโลหิตสูง");

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        txt_head = findViewById(R.id.txt_head);
        question_num_tv = findViewById(R.id.question_num_tv);
        question_text_tv = findViewById(R.id.question_text_tv);
        rgb_question = findViewById(R.id.rgb_question);
        no_rb = findViewById(R.id.no_rb);
        yes_rb = findViewById(R.id.yes_rb);
        tmp_rb = findViewById(R.id.tmp_rb);
        tmp_rb.setVisibility(View.INVISIBLE);

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        listItems = new ArrayList<>();
        readLocalJson(file_name, index);
        radioHandle();
    }

    private void radioHandle() {
        rgb_question.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_rb) {
                    ans = 1;
                } else if (checkedId == R.id.no_rb) {
                    ans = 0;
                }  else {
                    ans = 0;
                }
                score[index] = ans;
            }
        });
    }

    private void resetRadioButton() {
        tmp_rb.setChecked(true);
    }

    private void readLocalJson(String urlVal, int indexR) {
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
            placesObj = (JSONArray) object.get("hyper_queue");
            int ObjLng = placesObj.length();
            JSONObject nameObj = (JSONObject) placesObj.get(indexR);
            String id = (String) nameObj.get("id");
            String question = (String) nameObj.get("question");

            txt_head.setText(id);
            question_text_tv.setText(question);
            question_num_tv.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
            if (index < 6) {
                Toast.makeText(this,index + " / " + score[index],Toast.LENGTH_LONG).show();
                index = index + 1;
                readLocalJson(file_name, index);
                resetRadioButton();
            } else {
                total_score = 0;
                Intent intent = new Intent(this,DiabNewQActivity.class);
                try {
                    for (int i = 0 ; i < 6 ; i++) {
                        total_score = total_score + score[i];
                    }
                    Toast.makeText(this,"total_score = " + total_score,Toast.LENGTH_LONG).show();
                    myRef.child(current_user.getUid()).child("hyper_score").setValue(total_score);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                startActivity(intent);
            }
        }

        else if ( v == back_btn ) {
            if (index > 0) {
                index = index - 1;
                readLocalJson(file_name, index);
                resetRadioButton();
            } else {
                Intent intent = new Intent(this,DepNewQActivity.class);
                startActivity(intent);
            }
        }
    }
}

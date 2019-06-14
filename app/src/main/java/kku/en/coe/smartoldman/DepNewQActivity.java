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

public class DepNewQActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<OsteQListitem> listItems;
    private Button next_btn, back_btn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private TextView tv_questtion;
    private RadioGroup rdg_choice;
    private RadioButton rd_choice_1, rd_choice_2, rd_choice_3, rd_choice_4, rd_choice_5, rd_hidden;
    public int[] score = new int[12];
    public int index = 0, total_score = 0, ans = 0;

    String places[], file_name = "dep_new_quest.json";
    JSONArray placesObj;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oste_newq);
        setTitle("แบบคัดกรองภาวะซึมเศร้า");

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        tv_questtion = findViewById(R.id.tv_questtion);
        rdg_choice = findViewById(R.id.rdg_choice);
        rd_choice_1 = findViewById(R.id.rd_choice_1);
        rd_choice_2 = findViewById(R.id.rd_choice_2);
        rd_choice_3 = findViewById(R.id.rd_choice_3);
        rd_choice_4 = findViewById(R.id.rd_choice_4);
        rd_choice_5 = findViewById(R.id.rd_choice_5);
        rd_hidden = findViewById(R.id.rd_hidden);

        rd_choice_3.setVisibility(View.INVISIBLE);
        rd_choice_3.setPadding(1,1,1,1);
        rd_choice_4.setVisibility(View.INVISIBLE);
        rd_choice_4.setPadding(1,1,1,1);
        rd_choice_5.setVisibility(View.INVISIBLE);
        rd_choice_5.setPadding(1,1,1,1);
        rd_hidden.setChecked(true);

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        listItems = new ArrayList<>();
        readLocalJson(file_name, index);
        radioHandle();
    }

    private void radioHandle() {
        rdg_choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_choice_1) {
                    ans = 1;
                } else if (checkedId == R.id.rd_choice_2) {
                    ans = 0;
                }  else {
                    ans = 0;
                }
                score[index] = ans;
            }
        });
    }

    private void resetRadioButton() {
        rd_hidden.setChecked(true);
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
            placesObj = (JSONArray) object.get("dep_quest");
            int ObjLng = placesObj.length();
            JSONObject nameObj = (JSONObject) placesObj.get(indexR);
            String id = (String) nameObj.get("id");
            String question = (String) nameObj.get("question");
            String choice_1 = (String) nameObj.get("choice1");
            String choice_2 = (String) nameObj.get("choice2");

            tv_questtion.setText(question);
            rd_choice_1.setText(choice_1);
            rd_choice_2.setText(choice_2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
            if (index < 8) {
                Toast.makeText(this,index + " / " + score[index],Toast.LENGTH_LONG).show();
                index = index + 1;
                readLocalJson(file_name, index);
                resetRadioButton();
            } else {
                total_score = 0;
                Intent intent = new Intent(this,DepQActivity.class);
                for (int i = 0 ; i < 8 ; i++) {
                    total_score = total_score + score[i];
                }
                Toast.makeText(this,"total_score = " + total_score,Toast.LENGTH_LONG).show();
                myRef.child(current_user.getUid()).child("dep_score").setValue(total_score);
                intent.putExtra("dep_score" , total_score);
//                startActivity(intent);
            }
        }

        else if ( v == back_btn ) {
            if (index > 0) {
                index = index - 1;
                readLocalJson(file_name, index);
                resetRadioButton();
            } else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
}

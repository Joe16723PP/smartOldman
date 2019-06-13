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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OsteNewQActivity extends AppCompatActivity implements View.OnClickListener {
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
    private RadioButton rd_choice_1, rd_choice_2, rd_choice_3, rd_choice_4, rd_choice_5;

    String places[], file_name = "oste_quest.json";
    JSONArray placesObj;
    JSONObject jsonObject;
    int index = 0, score[] = new int[11], total_score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oste_newq);
        setTitle("แบบคัดกรองโรคเข่าเสื่อม");

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

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        listItems = new ArrayList<>();
        readLocalJson(file_name, index);
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
            placesObj = (JSONArray) object.get("oste_quest");
            int ObjLng = placesObj.length();
            JSONObject nameObj = (JSONObject) placesObj.get(indexR);
            String id = (String) nameObj.get("id");
            String question = (String) nameObj.get("question");
            String choice_1 = (String) nameObj.get("choice1");
            String choice_2 = (String) nameObj.get("choice2");
            String choice_3 = (String) nameObj.get("choice3");
            String choice_4 = (String) nameObj.get("choice4");
            String choice_5 = (String) nameObj.get("choice5");

            tv_questtion.setText(question);
            rd_choice_1.setText(choice_1);
            rd_choice_2.setText(choice_2);
            rd_choice_3.setText(choice_3);
            rd_choice_4.setText(choice_4);
            rd_choice_5.setText(choice_5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
            rdg_choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rd_choice_1) {
                        score[index] = 4;
                    } else if (checkedId == R.id.rd_choice_2) {
                        score[index] = 3;
                    } else if (checkedId == R.id.rd_choice_2) {
                        score[index] = 2;
                    } else if (checkedId == R.id.rd_choice_2) {
                        score[index] = 1;
                    } else {
                        score[index] = 0;
                    }
                }
            });
            if (index < 11) {
                index = index + 1;
                readLocalJson(file_name, index);
            } else {
                Intent intent = new Intent(this,DepQActivity.class);
                for (int i = 0 ; i < 12 ; i++) {
                    total_score = score[i];
                }
                Toast.makeText(this," " + total_score,Toast.LENGTH_LONG).show();
//                startActivity(intent);
            }
        }

        else if ( v == back_btn ) {
            if (index > 0) {
                index = index - 1;
                readLocalJson(file_name, index);
            } else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
}

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

public class OsteQActivity extends AppCompatActivity implements View.OnClickListener {
// 1
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<OsteQListitem> listItems;
    private Button next_btn, back_btn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;

    String places[], file_name = "oste_quest.json";
    JSONArray placesObj;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oste_q);
        setTitle("แบบคัดกรองโรคเข่าเสื่อม");
        Toast.makeText(this,"เนื่องจากเป็นการเข้าสู่ระบบครั้งแรก กรุณาทำแบบคัดกรองให้ครบถ้วน",Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        listItems = new ArrayList<>();
        readLocalJson(file_name);
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
            placesObj = (JSONArray) object.get("oste_quest");
            int ObjLng = placesObj.length();
            for (int i = 0 ; i < ObjLng ; i++) {
                JSONObject nameObj = (JSONObject) placesObj.get(i);
                String id = (String) nameObj.get("id");
                String question = (String) nameObj.get("question");
                String choice_1 = (String) nameObj.get("choice1");
                String choice_2 = (String) nameObj.get("choice2");
                String choice_3 = (String) nameObj.get("choice3");
                String choice_4 = (String) nameObj.get("choice4");
                String choice_5 = (String) nameObj.get("choice5");

                OsteQListitem qListitem = new OsteQListitem(
                        id, question, choice_1, choice_2, choice_3,choice_4,choice_5
                );
                listItems.add(qListitem);
            }

            adapter = new OsteQAdapter(listItems,this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
//            Toast.makeText(this,"do next" , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,DepQActivity.class);
            OsteAAnswer global = OsteAAnswer.getOsteInst();
            try {
                int oste_score = 0;
                int[] tmp_ans = global.getOste_answer();
                for (int i = 0 ; i < tmp_ans.length ; i++) {
                    oste_score = oste_score + tmp_ans[i];
                }
                myRef.child(current_user.getUid()).child("oste_score").setValue(oste_score);
//                intent.putStringArrayListExtra("answer_1",tmp_list);
//                intent.putExtra("oste_score" , oste_score);
//                intent.putExtra("next_pointer", pointer);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if ( v == back_btn ) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}

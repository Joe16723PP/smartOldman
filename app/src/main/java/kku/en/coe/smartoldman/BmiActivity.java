package kku.en.coe.smartoldman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BmiActivity extends AppCompatActivity implements View.OnClickListener {

    double bmi;
    int dep_score, oste_score, hyper_score;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    ProgressDialog pgd;

    private TextView bmi_tv , risk_tv , detail_risk_tv;
    private Button see_all_btn;

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().hide();

        risk_tv = findViewById(R.id.risk);
        detail_risk_tv = findViewById(R.id.detail_risk);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        see_all_btn = findViewById(R.id.see_all);
        see_all_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pgd = ProgressDialog.show(this, "กำลังโหลดข้อมูล กรุณารอสักครู่", "Loading...", true, false);
        doReadFirebase();
        

    }

    private void doReadFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String str_user = String.valueOf(keyNode.getKey());
                    String uid = current_user.getUid();
                    if (str_user.equals(uid)){
                        User user = keyNode.getValue(User.class);
                        dep_score = user.getDep_score();
                        oste_score = user.getOste_score();
                        hyper_score = user.getHyper_score();
                        bmi = Double.parseDouble(user.getBmi());
                        String urlFIle = "disease.json";
                        readLocalJson(urlFIle);
                    }
                }
                Log.e("firebase" , dep_score + ":" + oste_score + ":" + hyper_score + ":" + bmi) ;
                pgd.dismiss();
                String res_bmi = "";
                if (bmi < 23){
                    res_bmi = "ผอม";
                } else if (bmi > 27.5) {
                    res_bmi = "อ้วน";
                } else {
                    res_bmi = "สมส่วน";
                }
                detail_risk_tv.setText("ดัชนีมวลกายของคุณคือ : " + (float)bmi + "\nคุณมีรูปร่าง" + res_bmi);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readLocalJson(String urlFIle) {
//        read json with buffer to string
        String json = null;
        try {
            InputStream is = getAssets().open(urlFIle);
            int size = is.available();
            Log.d("msg", String.valueOf(size));
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        create and use json with str "json"
        try {
            listItems = new ArrayList<>();
            JSONObject object = (JSONObject) new JSONObject(json);
            JSONArray placesObj = (JSONArray) object.get("disease");

            int status = 0;
            if (oste_score <= 19) {
                JSONObject nameObj = (JSONObject) placesObj.get(1);
                String name = (String) nameObj.get("title");
                String desc = (String) nameObj.get("description");
                String color = (String) nameObj.get("color");
                String pointer = (String) nameObj.get("pointer");

                ListItem listItem = new ListItem(
                        name, desc, desc , color , pointer
                );
                listItems.add(listItem);
                status = 1;
            }
            if (dep_score > 3 ) {
                JSONObject nameObj = (JSONObject) placesObj.get(4);
                String name = (String) nameObj.get("title");
                String desc = (String) nameObj.get("description");
                String color = (String) nameObj.get("color");
                String pointer = (String) nameObj.get("pointer");

                ListItem listItem = new ListItem(
                        name, desc, desc , color , pointer
                );
                listItems.add(listItem);
                status = 1;

            }
            if (hyper_score > 4) {
                JSONObject nameObj = (JSONObject) placesObj.get(0);
                String name = (String) nameObj.get("title");
                String desc = (String) nameObj.get("description");
                String color = (String) nameObj.get("color");
                String pointer = (String) nameObj.get("pointer");

                ListItem listItem = new ListItem(
                        name, desc, desc , color , pointer
                );
                listItems.add(listItem);
                status = 1;

            }
            if (bmi >= 25 ) {
                JSONObject nameObj = (JSONObject) placesObj.get(3);
                String name = (String) nameObj.get("title");
                String desc = (String) nameObj.get("description");
                String color = (String) nameObj.get("color");
                String pointer = (String) nameObj.get("pointer");

                ListItem listItem = new ListItem(
                        name, desc, desc , color , pointer
                );
                listItems.add(listItem);
                status = 1;

            }
            if (status == 1){
                risk_tv.setText("คุณมีความเสี่ยงโรคดังต่อไปนี้");
            } else {
                risk_tv.setText("ไมมีความเสี่ยงเลย ! \nสุขภาพของคุณดีมาก");
            }
            adapter = new MyAdapter(listItems,this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if ( v == see_all_btn ) {
            Intent intent = new Intent(this,DiseaseActivity.class);
            startActivity(intent);
        }
    }
}

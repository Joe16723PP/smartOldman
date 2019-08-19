package kku.en.coe.smartoldman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private TextView bmi_tv , risk_tv , detail_risk_tv , guide_line1, guide_line2, guide_line3, guide_line4, guide_line5;
    private Button see_all_btn;
    private ImageView guide_line1_img, guide_line2_img, guide_line3_img, guide_line4_img, guide_line5_img , risk_img;

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().hide();

        guide_line1 = findViewById(R.id.guide_line1);
        guide_line2 = findViewById(R.id.guide_line2);
        guide_line3 = findViewById(R.id.guide_line3);
        guide_line4 = findViewById(R.id.guide_line4);

        guide_line1_img = findViewById(R.id.guide_line1_img);
        guide_line2_img = findViewById(R.id.guide_line2_img);
        guide_line3_img = findViewById(R.id.guide_line3_img);
        guide_line4_img = findViewById(R.id.guide_line4_img);

        risk_img = findViewById(R.id.risk_img);
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


        if (isOnline()) {
            pgd = ProgressDialog.show(this, "กำลังโหลดข้อมูล กรุณารอสักครู่", "Loading...", true, false);
            doReadFirebase();
            int raw_audio = getResources().getIdentifier("bmi" , "raw", getPackageName());
            MediaPlayer mp = MediaPlayer.create(this,raw_audio);
            mp.start();
        }
        else {
            pgd = ProgressDialog.show(this, "ไม่มีการเชื่อมต่อกับอินเตอร์เน็ต กรุณาตรวจสอบ", "Loading...", true, false);
        }
        

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
                String res_bmi;
                if (bmi < 18.5){
                    res_bmi = "ผอม";
                } else if (bmi >= 18.5 && bmi <= 22.9) {
                    res_bmi = "สมส่วน";
                } else if (bmi >= 23 && bmi <= 24.9) {
                    res_bmi = "ค่อนข้างอ้วน";
                    guide_line4.setText("\n โรคเบาหวาน" + "\n- ออกกำลังกายสม่ำเสมอ"
                            + "\n- ควบคุมน้ำหนังตัวให้อยู่ในเกณฑ์ที่เหมาะสม");
                    guide_line4_img.setImageResource(R.drawable.bowan);
                } else if (bmi >= 25 && bmi <= 29.9) {
                    guide_line4.setText("\n โรคเบาหวาน" + "\n- ออกกำลังกายสม่ำเสมอ"
                            + "\n- ควบคุมน้ำหนังตัวให้อยู่ในเกณฑ์ที่เหมาะสม");
                    guide_line4_img.setImageResource(R.drawable.bowan);
                    res_bmi = "อ้วน";
                } else {
                    res_bmi = "อ้วนมาก";
                    guide_line4.setText("\n โรคเบาหวาน" + "\n- ออกกำลังกายสม่ำเสมอ"
                            + "\n- ควบคุมน้ำหนังตัวให้อยู่ในเกณฑ์ที่เหมาะสม");
                    guide_line4_img.setImageResource(R.drawable.bowan);
                }
                detail_risk_tv.setText("ดัชนีมวลกายของคุณคือ\n" + (float)bmi + "\nคุณมีรูปร่าง" + res_bmi);
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
                guide_line1_img.setImageResource(R.drawable.knee_risk);
                guide_line1.setText("\n" + name
                        + "\n- ควรปรึกษาศัลยแพทย์ผู้เชี่ยวชาญกระดูกและข้อเพื่อรับการตรวจรักษา"
                        + "\n- เอกซเรย์ข้อเข่าและประเมินอาการของโรค");
//                guide_line.append("\n" + name
//                        + "\n- ควรปรึกษาศัลยแพทย์ผู้เชี่ยวชาญกระดูกและข้อเพื่อรับการตรวจรักษา"
//                        + "\n- เอกซเรย์ข้อเข่าและประเมินอาการของโรค"
//                );
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
                guide_line2_img.setImageResource(R.drawable.sad_risk);
                guide_line2.setText("\n" + name
                        + "\n- มีภาวะซึมเศร้า ควรได้รับบริการปรึกษาหรือพบแพทย์เพื่อการบำบัดรักษา");
//                guide_line.append("\n" + name
//                        + "\n- มีภาวะซึมเศร้า ควรได้รับบริการปรึกษาหรือพบแพทย์เพื่อการบำบัดรักษา"
//                );

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
//                gui
//                guide_line.append("คำแนะนำการปฎิบัติตัวโรค " + name);

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
                guide_line3_img.setImageResource(R.drawable.hyper_risk);
                guide_line3.setText("\n" + name
                        + "\n- ออกกำลังกายสม่ำเสมอ"
                        + "\n- ควบคุมน้ำหนังตัวให้อยู่ในเกณฑ์ที่เหมาะสม"
                        + "\n- ตรวจวัดความดันโลหิต");
//                guide_line.append(
//                        "\n" + name
//                        + "\n- ออกกำลังกายสม่ำเสมอ"
//                        + "\n- ควบคุมน้ำหนังตัวให้อยู่ในเกณฑ์ที่เหมาะสม"
//                        + "\n- ตรวจวัดความดันโลหิต"
//                );

            }
            if (status == 1){
                risk_tv.setText("คำแนะนำการปฎิบัติตัว");
            } else {
                risk_tv.setText("ไมมีความเสี่ยงเลย ! \nสุขภาพของคุณดีมาก");
                risk_img.setImageResource(R.drawable.no_risk);
            }
//            adapter = new MyAdapter(listItems,this);
//            recyclerView.setAdapter(adapter);

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

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}

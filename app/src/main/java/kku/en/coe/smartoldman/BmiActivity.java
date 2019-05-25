package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BmiActivity extends AppCompatActivity implements View.OnClickListener {

    float bmi;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    private TextView bmi_tv , risk_tv;
    private Button see_all_btn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().hide();
        getValueIntent();

        bmi_tv = findViewById(R.id.bmi);
        risk_tv = findViewById(R.id.risk);

        see_all_btn = findViewById(R.id.see_all);
        see_all_btn.setOnClickListener(this);

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

            JSONObject nameObj = (JSONObject) placesObj.get(3);
            String name = (String) nameObj.get("title");
            String desc = (String) nameObj.get("description");
            String color = (String) nameObj.get("color");
            String pointer = (String) nameObj.get("pointer");

            ListItem listItem = new ListItem(
                    name, desc, desc , color , pointer
            );

            listItems.add(listItem);

            adapter = new MyAdapter(listItems,this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        check if bmi is risk
        bmi_tv.setText("BMI : " + String.valueOf(bmi));
        if (bmi >= 25 ){
            recyclerView = findViewById(R.id.rcv);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            risk_tv.setText("คุณมีความเสี่ยงเกี่ยวกับโรคต่อไปนี้ !");
            String urlFIle = "disease.json";
            readLocalJson(urlFIle);
        } else {
            risk_tv.setText("สุขภาพดีมาก !");
        }
    }

    private void getValueIntent() {
        Bundle extras = getIntent().getExtras();
        String str_bmi = extras.getString("bmi");
        bmi = Float.parseFloat(str_bmi);
//        TextView view = (TextView) findViewById(R.id.textView);
//        view.setText(inputString);
    }

    @Override
    public void onClick(View v) {
        if ( v == see_all_btn ) {
            Intent intent = new Intent(this,DiseaseActivity.class);
            startActivity(intent);
        }
    }
}

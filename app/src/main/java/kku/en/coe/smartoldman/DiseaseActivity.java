package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DiseaseActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    String places[] ;
    JSONArray placesObj;
    JSONObject jsonObject;

    private Button edit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        String urlFIle = "disease.json";
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
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            jsonObject = (JSONObject) new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject object = (JSONObject) new JSONObject(json);
            placesObj = (JSONArray) object.get("disease");
            int ObjLng = placesObj.length();
            places = new String[ObjLng];
            for (int i = 0 ; i < ObjLng ; i++) {
                JSONObject nameObj = (JSONObject) placesObj.get(i);
                String name = (String) nameObj.get("title");
                String desc = (String) nameObj.get("description");
                String color = (String) nameObj.get("color");
                String pointer = (String) nameObj.get("pointer");
                places[i] = name;

                ListItem listItem = new ListItem(
                        name, desc, desc , color , pointer
                );

                listItems.add(listItem);
            }

            adapter = new MyAdapter(listItems,this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == edit_btn ) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
}

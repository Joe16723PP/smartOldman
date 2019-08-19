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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DepQActivity extends AppCompatActivity implements View.OnClickListener {
// 2
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<DepQListitem> listItems;
    private Button next_btn , back_btn;

    String file_name = "dep_quest.json";
    JSONArray placesObj;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        setTitle("แบบคัดกรองโรคซึมเศร้า");
        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            placesObj = (JSONArray) object.get("dep_quest");
            int ObjLng = placesObj.length();
            for (int i = 0 ; i < ObjLng - 20 ; i++) {
                JSONObject nameObj = (JSONObject) placesObj.get(i);
                String id = (String) nameObj.get("id");
                String question = (String) nameObj.get("question");

                DepQListitem listItem = new DepQListitem(
                        id, question
                );
                listItems.add(listItem);
            }

            adapter = new DepQAdapter(listItems,this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ( v == next_btn ) {
//            Toast.makeText(this,"do next" , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,DepQ2Activity.class);
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
                intent.putExtra("file_name" , file_name);
//                intent.putExtra("next_pointer", pointer);
                startActivity(intent);
                Log.e("global" ,": tmp " + tmp );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if ( v == back_btn ) {
            Intent intent = new Intent(this,OsteQActivity.class);
            startActivity(intent);
        }
    }
}

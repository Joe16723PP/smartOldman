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

public class DepQ2Activity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<DepQListitem> listItems;
    private Button next_btn , back_btn;
    public ArrayList<String> answer_all = new ArrayList<>();
    public ArrayList<String> tmp_list = new ArrayList<>();

    String tmp1 = "", file_name, pointer;
    JSONArray placesObj;
    JSONObject jsonObject;

    private String[] tmp_ans_1 = new String[10];
    private String[] tmp_ans_2 = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        setTitle("แบบคัดกรองโรคซึมเศร้า(ต่อ)");

        recyclerView = findViewById(R.id.rcv_question);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        next_btn = findViewById(R.id.next_question);
        next_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_question);
        back_btn.setOnClickListener(this);

        doGetAnswer();
        listItems = new ArrayList<>();
        readLocalJson(file_name);
    }

    private void doGetAnswer() {
        ArrayList<String> tmp_list = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        tmp_list = extras.getStringArrayList("answer_1");
        file_name = extras.getString("file_name");
        pointer = extras.getString("next_pointer");
//        Toast.makeText(this,"" + tmp_list + file_name,Toast.LENGTH_SHORT).show();
//        Log.e("loop" , String.valueOf(tmp_list));
        String tmp = "";
        for (int i = 0; i< tmp_list.size()-20 ;i++) {
            tmp_ans_1[i] = tmp_list.get(i);
            tmp1 += tmp_ans_1[i];
            answer_all.add(tmp_list.get(i));
//            try {
//                answer_all.set(i,tmp_list.get(i));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
        Log.e("loop", String.valueOf(answer_all));
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
            for (int i = 10 ; i < ObjLng - 10 ; i++) {
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
            Toast.makeText(this,"do next 2" , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,DepQ3Activity.class);
            ArrayAnswer global = ArrayAnswer.getInstance();
            try {
                int[] tmp_ans = global.getArray_answer();

                for (int i = 10; i < tmp_ans.length ;i ++) {
                    tmp1 += tmp_ans[i];
                    tmp_list.add(String.valueOf(tmp_ans[i]));
                    tmp_ans_2[i-10] = String.valueOf(tmp_ans[i]);
                    answer_all.add(String.valueOf(tmp_ans[i]));
                }
                intent.putStringArrayListExtra("answer_1",answer_all);
                intent.putExtra("file_name" , file_name);
                intent.putExtra("next_pointer", pointer);
                startActivity(intent);
//                Log.e("answer_all" ,": answer_all " + answer_all );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if ( v == back_btn ) {
            Intent intent = new Intent(this,QuestionActivity.class);
            intent.putExtra("next_pointer", pointer);
            startActivity(intent);
        }
    }
}

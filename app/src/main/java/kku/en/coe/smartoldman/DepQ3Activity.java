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

public class DepQ3Activity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<DepQListitem> listItems;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private Button next_btn , back_btn;
    public ArrayList<String> answer_all = new ArrayList<>();

    String tmp1 = "", file_name , pointer;
    int[] no_choice_array = {1,5,7,9,15,19,21,27,29,30};
    JSONArray placesObj;
    JSONObject jsonObject;

    private String[] tmp_ans_1 = new String[20];
    private String[] tmp_ans_2 = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        setTitle("แบบคัดกรองโรคซึมเศร้า(ต่อ)");

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

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
        Log.e("tmp_list", String.valueOf(tmp_list));
        String tmp = "";
        for (int i = 0; i< tmp_list.size() - 10 ;i++) {
            tmp_ans_1[i] = tmp_list.get(i);
            tmp1 += tmp_ans_1[i];
            answer_all.add(tmp_list.get(i));
        }
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
            for (int i = 20 ; i < ObjLng ; i++) {
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
            Intent intent = new Intent(this,HyperQActivity.class);
            ArrayAnswer global = ArrayAnswer.getInstance();
            try {
                int[] tmp_ans = global.getArray_answer();
                ArrayList<String> tmp_list = new ArrayList<>();
                for (int i = 20; i < tmp_ans.length ;i ++) {
                    tmp1 += tmp_ans[i];
                    tmp_list.add(String.valueOf(tmp_ans[i]));
                    tmp_ans_2[i-20] = String.valueOf(tmp_ans[i]);
                    answer_all.add(String.valueOf(tmp_ans[i]));
                }
                Log.e("all" , String.valueOf(answer_all));

                int dep_score = 0;
                int status = 1;
                for (int i = 0; i < 30 ;i ++) {
                    for (int ref : no_choice_array){
                        if ( i == (ref-1) ) {
                            if (String.valueOf(answer_all.get(i)).equals("0")){
                                dep_score += 1;
                            }
                            status = 0;
                        }
                    }
                    if (status == 1) {
                        if (String.valueOf(answer_all.get(i)).equals("1")){
                            dep_score += 1;
                            Log.e("answer" , answer_all.get(i));
                        }
                    }
                    status = 1;

                }
                myRef.child(current_user.getUid()).child("dep_score").setValue(dep_score);
//                intent.putStringArrayListExtra("answer_1",tmp_list);
//                intent.putExtra("file_name" , file_name);
//                intent.putExtra("next_pointer", pointer);
                startActivity(intent);
                Log.e("total" , String.valueOf(dep_score));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if ( v == back_btn ) {
            Intent intent = new Intent(this,DepQ2Activity.class);
            intent.putExtra("next_pointer", pointer);
            startActivity(intent);
        }
    }
}

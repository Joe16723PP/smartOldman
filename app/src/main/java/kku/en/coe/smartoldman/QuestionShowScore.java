package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionShowScore extends AppCompatActivity implements View.OnClickListener {

    private Button read_btn , back_btn;
    private String pointer;
    public ArrayList<String> answer_all = new ArrayList<>();
    private int score = 0;

    private TextView score_tv;
    private ImageView score_img;

    private String[] Hyper = {"1","1","1","1","1","1","0","0","1","0","1","1","1","1","0","0","1","0","1","0"};
    private String[] Oste = {"0","1","1","1","0","1","1","0","1","1","1","0","0","0","1","1","1","1","0","0"};
    private String[] Lipid = {"1","1","0","1","0","1","1","0","1","1","0","1","1","1","1","1","0","0","1","1"};
    private String[] Diab = {"0","1","1","0","1","1","1","0","0","1","1","0","0","0","1","0","1","1","1","1"};
    private String[] Dep = {"0","1","1","1","1","0","1","0","1","1","0","1","1","0","1","1","1","0","1","1"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_show_score);

        read_btn = findViewById(R.id.read_btn);
        read_btn.setOnClickListener(this);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        score_tv = findViewById(R.id.score);
        score_img = findViewById(R.id.img_score);

        doGetIntentData();
        doCheckAnswer(pointer);
    }

    private void doCheckAnswer(String pointer) {
        String[] tmp_ans = new String[20];
        String img_name = "";

        switch (pointer) {
            case "Hyper1Activity" :
                tmp_ans = Hyper;
                break;
            case "Oste1Activity" :
                tmp_ans = Oste;
                break;
            case "Lipid1Activity" :
                tmp_ans = Lipid;
                break;
            case "Diab1Activity" :
                tmp_ans = Diab;
                break;
            case "Dep1Activity" :
                tmp_ans = Dep;
                break;
        }
        for(int i = 0; i < tmp_ans.length; i++){
            if (answer_all.get(i).equals(tmp_ans[i])){
                score += 1;
            }
        }

        if (score < 10 ) {
            img_name = "unhappy";
        } else if ( score > 15){
            img_name = "in_love";
        } else {
            img_name = "thinking";
        }

        int resID = getResources().getIdentifier(img_name , "drawable", getPackageName());
        score_img.setImageResource(resID);
        score_tv.setText(score + " / 20");
    }

    private void doGetIntentData() {
        Bundle extras = getIntent().getExtras();
        answer_all = extras.getStringArrayList("answer_all");
        pointer = extras.getString("return_point");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if ( v == read_btn ) {
            switch (pointer) {
                case "Hyper1Activity" :
                    intent = new Intent(this,Hyper1Activity.class);
                    break;
                case "Oste1Activity" :
                    intent = new Intent(this,Oste1Activity.class);
                    break;
                case "Lipid1Activity" :
                    intent = new Intent(this,Lipid1Activity.class);
                    break;
                case "Diab1Activity" :
                    intent = new Intent(this,Diab1Activity.class);
                    break;
                case "Dep1Activity" :
                    intent = new Intent(this,Dep1Activity.class);
                    break;
            }
        } else if ( v == back_btn ){
            intent = new Intent(this,DiseaseActivity.class);
        }

        intent.putExtra("return_point","disease");
        startActivity(intent);
    }
}

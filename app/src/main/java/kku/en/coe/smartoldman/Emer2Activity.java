package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Emer2Activity extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, next_btn;
    CardView emer_1669, emer_191, emer_199;
    private String rt_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emer2);
        back_btn = findViewById(R.id.btn_back);
//        next_btn = findViewById(R.id.btn_next);
        emer_1669 = findViewById(R.id.emer_1669);
        emer_191 = findViewById(R.id.emer_191);
        emer_199 = findViewById(R.id.emer_199);

        emer_199.setOnClickListener(this);
        emer_191.setOnClickListener(this);
        emer_1669.setOnClickListener(this);
        back_btn.setOnClickListener(this);
//        next_btn.setOnClickListener(this);

        try {
            Bundle extras = getIntent().getExtras();
            rt_point = extras.getString("return_point");
        } catch (Exception e) {
            rt_point = "";
        }
        Log.e("rtp", "rtp : "+rt_point);
    }

    @Override
    public void onClick(View v) {
        if ( v == back_btn ) {
            if (rt_point.equals("disease")) {
                Intent intent = new Intent(this,DiseaseActivity.class);
                intent.putExtra("return_point",rt_point);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        } else if (v == next_btn ) {
            Intent intent = new Intent(this, Emergency1Activity.class);
            intent.putExtra("return_point",rt_point);
            startActivity(intent);
        } else if ( v == emer_1669 ) {
            Intent intent = new Intent(this, Emergency2Activity.class);
            intent.putExtra("index","1");
            intent.putExtra("return_point",rt_point);
            startActivity(intent);
        } else if ( v == emer_191 ) {
            Intent intent = new Intent(this, Emergency2Activity.class);
            intent.putExtra("index","2");
            intent.putExtra("return_point",rt_point);
            startActivity(intent);
        } else if ( v == emer_199 ) {
            Intent intent = new Intent(this, Emergency2Activity.class);
            intent.putExtra("index","3");
            intent.putExtra("return_point",rt_point);
            startActivity(intent);
        }

    }
}

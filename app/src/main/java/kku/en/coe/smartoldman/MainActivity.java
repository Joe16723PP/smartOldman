package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    CardView cv;
    Button cv, emer_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        cv = findViewById(R.id.loginBtn);
        emer_btn = findViewById(R.id.emer_btn);
        emer_btn.setOnClickListener(this);
        cv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if ( v == cv ) {
            Intent intent = new Intent(this,DiseaseActivity.class);
            startActivity(intent);
        }
        else if ( v == emer_btn) {
                Intent intent = new Intent(this,Emergency1Activity.class);
            startActivity(intent);
        }
    }
}

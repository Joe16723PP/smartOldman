package kku.en.coe.smartoldman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BmiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().hide();
    }
}

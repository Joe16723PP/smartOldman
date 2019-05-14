package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    CardView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if ( v == submitBtn ) {
            Intent intent = new Intent(LoginActivity.this,BmiActivity.class);
            startActivity(intent);
        }
    }
}

package kku.en.coe.smartoldman;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button submitBtn;
    EditText f_name , l_name , gend , ageTv ,weightTv , heightTv;

    String email;
    String password = "123456";
    String TAG = "LogTestLogin";
    private String name , gender , age , weight , height, Cur_Uid;

    int random ;
    float bmi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        f_name = findViewById(R.id.F_Name);
        l_name = findViewById(R.id.L_Name);
        gend = findViewById(R.id.genre);
        ageTv = findViewById(R.id.age);
        weightTv = findViewById(R.id.weight);
        heightTv = findViewById(R.id.height);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Cur_Uid = currentUser.getUid();
//        createAccount();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getInfoUser(){
        name = f_name.getText().toString() + " " + l_name.getText().toString();
        gender = gend.getText().toString();
        age = ageTv.getText().toString();
        weight = weightTv.getText().toString();
        height = heightTv.getText().toString();
        float tmp_h = Float.parseFloat(height);
        float powHeight = (float) Math.pow((tmp_h / 100),2);

        bmi = (Integer.parseInt(weight) / powHeight) ;
        Log.d("bmi", bmi + " : " + powHeight);
//        Toast.makeText(LoginActivity.this,name + gender + age + weight + height , Toast.LENGTH_LONG).show();
    }
    private void createAccount() {
        random = (int)(Math.random() * 100000 + 1);
        email = "dummy" + random + "@testmail.com";
        Log.d(TAG,email);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            createAccount();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if ( v == submitBtn ) {
            createAccount();
            getInfoUser();
            addUser(Cur_Uid,name,gender, weight, age, height, String.valueOf(bmi));
            Intent intent = new Intent(LoginActivity.this,BmiActivity.class);
            intent.putExtra("bmi",String.valueOf(bmi));
            startActivity(intent);
        }
    }

    private void addUser(String uid, String name, String gender , String weight , String age , String height, String bmi) {
        User user = new User(name, age, gender, weight, height, bmi,"","","","","","","","","","");
        Map<String, Object> UserValues = user.toMap();

        Map<String, Object> childUpdates= new HashMap<>();
        childUpdates.put(uid, UserValues);
        myRef.updateChildren(childUpdates);
//        Toast.makeText(SecondActivity.this,
//                "Write new user : " + id ,
//                Toast.LENGTH_LONG).show();
    }

//    private void updUser(String id, String email) {
//        myRef.child("users").child(id).child("email").setValue(email);
//        Toast.makeText(SecondActivity.this,
//                "ID : " + id + ", Email : " + email,
//                Toast.LENGTH_LONG).show();
//    }
}

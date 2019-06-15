package kku.en.coe.smartoldman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SubLoginActivity extends AppCompatActivity {


    private static final String TAG = "Logtest";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    String name , gender , age , weight , height, Cur_Uid;

    double bmi;
    private ProgressDialog pgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this,"sub login ",Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_sub_login);
//        checkConnection();
        if(isOnline()){
//            Toast.makeText(this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");
            mAuth = FirebaseAuth.getInstance();
            try {
                Cur_Uid = mAuth.getCurrentUser().getUid();
            } catch (Exception e){
                Log.e("uidErr" , ""+e);
            }

            getValueIntent();
            Toast.makeText(this,Cur_Uid,Toast.LENGTH_SHORT).show();
            addUser(Cur_Uid,name,gender, weight, age, height, String.valueOf(bmi));

        }else{
            pgd = ProgressDialog.show(this, "ไม่ได้เชื่อมต่ออินเทอร์เน็ต", "กลับลังกลับสู่หน้าแรก...", true, false);
            new Timer().schedule(
                    new TimerTask(){
                        @Override
                        public void run(){
                            pgd.dismiss();
                            Intent intent = new Intent(SubLoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }, 3000);
        }

    }

    private void addUser(String uid, String name, String gender , String weight , String age , String height, String bmi) {
        User user = new User(name, age, gender, weight, height, bmi,"","","","","","","","","","",0,0,0);
        Map<String, Object> UserValues = user.toMap();

        Map<String, Object> childUpdates= new HashMap<>();
        childUpdates.put(uid, UserValues);
        myRef.updateChildren(childUpdates);
        Log.d(TAG,uid);
        Toast.makeText(this,
                "Write new user : " + Cur_Uid,
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,OsteNewQActivity.class);
        int diap_score = 0;
//check diap age
        if (Integer.parseInt(age) >= 50 ){
            diap_score += 2;
        }else if ( (Integer.parseInt(age) >= 45) && (Integer.parseInt(age) <= 49)){
            diap_score += 1;
        }
//check diap bmi
        if (Double.parseDouble(bmi) >= 27.5 ){
            diap_score += 5;
        }else if ( (Double.parseDouble(bmi) >= 23.0) && (Double.parseDouble(bmi) <= 27.4)){
            diap_score += 3;
        }
//check diap gender
        if  (gender.equals("ผู้หญิง")) {
            diap_score += 2;
        }

        myRef.child(Cur_Uid).child("diab_score").setValue(diap_score);
        intent.putExtra("bmi",bmi);
        startActivity(intent);
    }

    private void getValueIntent() {
        Bundle extras = getIntent().getExtras();
        String str_bmi = extras.getString("bmi");
        bmi = Double.parseDouble(str_bmi);
        name = extras.getString("name");
        weight = extras.getString("weight");
        age = extras.getString("age");
        height = extras.getString("height");
        gender = extras.getString("gender");
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}

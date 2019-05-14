package kku.en.coe.smartoldman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Hyper3Activity extends AppCompatActivity {
    private JSONArray sick;
    private JSONObject page, obj;
    private String json, head, text, img, link, index;
    private TextView txt_head, txt_text, txt_link;
    private ImageView img_main;
    private Button btn_back, btn_next;
    private int index_int, send_index, max_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyper1);

        setTitle(R.string.hyper_string);

        Bundle extras = getIntent().getExtras();
        index = extras.getString("index");
        index_int = Integer.parseInt(index);
        Log.e("HACK", String.valueOf(index_int));

        img_main = findViewById(R.id.img_main);
        txt_head = findViewById(R.id.txt_head);
        txt_text = findViewById(R.id.txt_text);
        txt_link = findViewById(R.id.txt_link);
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);

        try {
            obj = new JSONObject(loadJSONFromAsset(Hyper3Activity.this));
            sick = (JSONArray) obj.get("sick");
            max_length = sick.length();
            page = sick.getJSONObject(index_int);
            head = page.getString("head");
            text = page.getString("text");
            img = page.getString("img");
            link = page.getString("link");

            if (img != "") {
                String mDrawableName = img;
                int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                img_main.setImageResource(resID);
            }
            txt_head.setText(head);
            txt_text.setText(text);
            txt_link.setText(link);
            Log.e("HACK",head + " " + text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_int -= 1;
                send_index = index_int;
                if (index_int <= 0) {
                    Intent intent = new Intent(Hyper3Activity.this,Hyper1Activity.class);
                    intent.putExtra("index", String.valueOf(send_index));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Hyper3Activity.this,Hyper2Activity.class);
                    intent.putExtra("index", String.valueOf(send_index));
                    startActivity(intent);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_int += 1;
                send_index = index_int;
                if (index_int >= (max_length - 1)) {
                    Intent intent = new Intent(Hyper3Activity.this,Hyper4Activity.class);
                    intent.putExtra("index", String.valueOf(send_index));
                    Log.e("HACK", String.valueOf(max_length));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Hyper3Activity.this,Hyper2Activity.class);
                    intent.putExtra("index", String.valueOf(send_index));
                    startActivity(intent);
                }
            }
        });
    }

    public String loadJSONFromAsset(Context context) {
        json = null;
        try {
            InputStream is = context.getAssets().open("sick1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

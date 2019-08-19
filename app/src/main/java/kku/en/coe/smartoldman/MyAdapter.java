package kku.en.coe.smartoldman;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;


    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_recyclerview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ListItem listItem = listItems.get(position);
        viewHolder.tvHead.setText(listItem.getHeader());
        viewHolder.tvDesc.setText(listItem.getDesc());
        viewHolder.color.setBackgroundColor(Color.parseColor(listItem.getColor()));

        try {
            String resIcon = listItem.getImgUrl();
            viewHolder.icon.setImageResource(Integer.parseInt(resIcon));
        }catch (Exception e){
            e.printStackTrace();
        }



//        String https_url = listItem.getImgUrl().replace("http","https");

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"clicked : " + listItem.getHeader(),Toast.LENGTH_LONG).show();
                String pointer = listItem.getPointer();
                checkIntent(pointer,v);

            }
        });

    }

    private void checkIntent(String pointer, View v) {
        if (pointer.equals("Hyper1Activity")) {
            Intent intent = new Intent(context,QuestionActivity.class);
            intent.putExtra("index",0);
            intent.putExtra("next_pointer",pointer);
            v.getContext().startActivity(intent);
        }else if (pointer.equals("Oste1Activity")) {
            Intent intent = new Intent(context,QuestionActivity.class);
            intent.putExtra("index",0);
            intent.putExtra("next_pointer",pointer);
            v.getContext().startActivity(intent);
        }else if (pointer.equals("Lipid1Activity")) {
            Intent intent = new Intent(context,QuestionActivity.class);
            intent.putExtra("index",0);
            intent.putExtra("next_pointer",pointer);
            v.getContext().startActivity(intent);
        }else if (pointer.equals("Diab1Activity")) {
            Intent intent = new Intent(context,QuestionActivity.class);
            intent.putExtra("index",0);
            intent.putExtra("next_pointer",pointer);
            v.getContext().startActivity(intent);
        }else if (pointer.equals("Dep1Activity")) {
            Intent intent = new Intent(context,QuestionActivity.class);
            intent.putExtra("index",0);
            intent.putExtra("next_pointer",pointer);
            v.getContext().startActivity(intent);
        }else if (pointer.equals("Emergency1Activity")) {
            Intent intent = new Intent(context,Emer2Activity.class);
            intent.putExtra("return_point","disease");
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHead, tvDesc , color;
        public LinearLayout linearLayout;
        public ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon_sick);
            tvHead = itemView.findViewById(R.id.header);
            tvDesc = itemView.findViewById(R.id.desc);
            color = itemView.findViewById(R.id.front_color);
            linearLayout = itemView.findViewById(R.id.lnLayout);
        }
    }
}

package kku.en.coe.smartoldman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<QuestionListItem> listItems;
    private Context context;
    public int[] answer_first = new int[20];

    public QuestionAdapter(List<QuestionListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pre_post_listitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final QuestionListItem listItem = listItems.get(position);
        viewHolder.question_number.setText(listItem.getQuestion_number());
        viewHolder.question_text.setText(listItem.getQuestion());

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Toast.makeText(context,checkedId + " : " + listItem.getQuestion_number(),Toast.LENGTH_LONG).show();
                int ans;
                int index = Integer.parseInt(listItem.getQuestion_number()) - 1;
                if (checkedId == R.id.yes_rb) {
                    ans = 1;
                }else {
                    ans = 0;
                }
                answer_first[index] = ans;
                Log.e("answer", String.valueOf(answer_first[index]));

                ArrayAnswer answer = ArrayAnswer.getInstance();
                answer.setArray_answer(answer_first);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView question_number, question_text;
        public ImageView img;
        public RadioGroup radioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question_number = itemView.findViewById(R.id.question_num_tv);
            question_text = itemView.findViewById(R.id.question_text_tv);
            radioGroup = itemView.findViewById(R.id.rbg_question);
        }
    }
}

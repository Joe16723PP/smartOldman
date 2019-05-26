package kku.en.coe.smartoldman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DepQAdapter extends RecyclerView.Adapter<DepQAdapter.ViewHolder> {

    private List<DepQListitem> listItems;
    private Context context;
    public int[] answer_first = new int[30];

    public DepQAdapter(List<DepQListitem> listitems, Context context) {
        this.listItems = listitems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pre_post_listitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DepQAdapter.ViewHolder viewHolder, int position) {
        final DepQListitem listItem = listItems.get(position);
        viewHolder.question_number.setText(listItem.getDepId());
        viewHolder.question_text.setText(listItem.getDepQuestion());

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(context,checkedId + " : " + listItem.getDepId(),Toast.LENGTH_LONG).show();
                int ans;
                int index = Integer.parseInt(listItem.getDepId()) - 1;
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

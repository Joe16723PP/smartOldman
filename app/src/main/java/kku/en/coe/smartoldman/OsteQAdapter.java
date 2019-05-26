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

import java.util.List;

import static kku.en.coe.smartoldman.R.id.rdg_choice;

public class OsteQAdapter extends RecyclerView.Adapter<OsteQAdapter.ViewHolder> {

    private List<OsteQListitem> listItems;
    private Context context;
    public int[] answer_first = new int[12];

    public OsteQAdapter(List<OsteQListitem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_oste_choice, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final OsteQListitem listItem = listItems.get(position);
        viewHolder.tv_question.setText(listItem.getQuestion());
        viewHolder.rd_choice_1.setText(listItem.getChoice1());
        viewHolder.rd_choice_2.setText(listItem.getChoice2());
        viewHolder.rd_choice_3.setText(listItem.getChoice3());
        viewHolder.rd_choice_4.setText(listItem.getChoice4());
        viewHolder.rd_choice_5.setText(listItem.getChoice5());

        viewHolder.rdg_choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(context,checkedId + " : " + listItem.getId(),Toast.LENGTH_LONG).show();
                int ans;
                int index = Integer.parseInt(listItem.getId());
                if (checkedId == R.id.rd_choice_1) {
                    ans = 4;
                } else if (checkedId == R.id.rd_choice_2) {
                    ans = 3;
                } else if (checkedId == R.id.rd_choice_3) {
                    ans = 2;
                } else if (checkedId == R.id.rd_choice_4) {
                    ans = 1;
                } else {
                    ans = 0;
                }
                answer_first[index - 1] = ans;
                OsteAAnswer answer = OsteAAnswer.getOsteInst();
                answer.setOste_answer(answer_first);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_question;
        public RadioGroup rdg_choice;
        public RadioButton rd_choice_1, rd_choice_2, rd_choice_3, rd_choice_4, rd_choice_5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_question = itemView.findViewById(R.id.tv_questtion);
            rdg_choice = itemView.findViewById(R.id.rdg_choice);
            rd_choice_1 = itemView.findViewById(R.id.rd_choice_1);
            rd_choice_2 = itemView.findViewById(R.id.rd_choice_2);
            rd_choice_3 = itemView.findViewById(R.id.rd_choice_3);
            rd_choice_4 = itemView.findViewById(R.id.rd_choice_4);
            rd_choice_5 = itemView.findViewById(R.id.rd_choice_5);
        }
    }
}

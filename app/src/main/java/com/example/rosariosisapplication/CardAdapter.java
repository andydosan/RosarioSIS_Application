package com.example.rosariosisapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ClassGradesCardHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    private Context context2;
    private ArrayList<CardHolder> cards;

    public CardAdapter(Context context2, ArrayList<CardHolder> cards,
                       RecyclerViewInterface recyclerViewInterface) {
        this.context2 = context2;
        this.cards = cards;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ClassGradesCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.card, parent, false);
        return new ClassGradesCardHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassGradesCardHolder holder, int position) {
        CardHolder card = cards.get(position);
        holder.setDetails((card));
    }

    @Override
    public int getItemCount() {
        //return null!=cards?cards.size():0;
        return cards.size();
    }

    public static class ClassGradesCardHolder extends RecyclerView.ViewHolder{
        private TextView teacher, percentage, classname, lettergrade;

        ClassGradesCardHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            teacher = itemView.findViewById(R.id.teacher);
            percentage = itemView.findViewById(R.id.percentage);
            classname = itemView.findViewById(R.id.classname);
            lettergrade = itemView.findViewById(R.id.lettergrade);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick((pos));
                        }
                    }
                }
            });
        }

        void setDetails(CardHolder card){
            teacher.setText(card.getTeacher2());
            percentage.setText(card.getPercentage2());
            classname.setText(card.getClassname2());
            lettergrade.setText(card.getLettergrade2());
        }
    }
}

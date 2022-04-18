package com.example.rosariosisapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder2> {


    private Context context2;
    private ArrayList<CardHolder> cards;


    public CardAdapter(Context context2, ArrayList<CardHolder> cards) {
        this.context2 = context2;
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.card, parent, false);
        return new CardHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder2 holder, int position) {
        CardHolder card = cards.get(position);
        holder.setDetails((card));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class CardHolder2 extends RecyclerView.ViewHolder{
        private TextView teacher, percentage, classname, lettergrade;

        CardHolder2(View itemView){
            super(itemView);
            teacher = itemView.findViewById(R.id.teacher);
            percentage = itemView.findViewById(R.id.percentage);
            classname = itemView.findViewById(R.id.classname);
            lettergrade = itemView.findViewById(R.id.lettergrade);
        }
        void setDetails(CardHolder card){
            teacher.setText(card.getTeacher2());
            percentage.setText(card.getPercentage2());
            classname.setText(card.getClassname2());
            lettergrade.setText(card.getLettergrade2());
        }
    }
}

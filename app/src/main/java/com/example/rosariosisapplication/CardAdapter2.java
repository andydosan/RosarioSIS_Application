package com.example.rosariosisapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter2 extends RecyclerView.Adapter<CardAdapter2.AssignmentGradesCardHolder> {


    private Context context2;
    private ArrayList<CardHolder2> cards;

    public CardAdapter2(Context context2, ArrayList<CardHolder2> cards) {
        this.context2 = context2;
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardAdapter2.AssignmentGradesCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.card2, parent, false);
        return new CardAdapter2.AssignmentGradesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentGradesCardHolder holder, int position) {
        CardHolder2 card = cards.get(position);
        holder.setDetails((card));
    }

    @Override
    public int getItemCount() {
        //return null!=cards?cards.size():0;
        return cards.size();
    }

    class AssignmentGradesCardHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{
        private TextView assignmentname, points, assignmentcategory, assignmentpercentage;

        AssignmentGradesCardHolder(View itemView){
            super(itemView);
            assignmentname = itemView.findViewById(R.id.assignmentname);
            points = itemView.findViewById(R.id.points);
            assignmentcategory = itemView.findViewById(R.id.assignmentcategory);
            assignmentpercentage = itemView.findViewById(R.id.assignmentpercentage);
        }
        void setDetails(CardHolder2 card){
            assignmentname.setText(card.getAssignmentname2());
            points.setText(card.getPoints2());
            assignmentcategory.setText(card.getAssignmentcategory2());
            assignmentpercentage.setText(card.getAssignmentpercentage2());
        }
    }
}

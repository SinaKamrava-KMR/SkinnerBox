package com.mainway.skinnerbox.Fragment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class StatisticsDialog extends DialogFragment {

    private Context context;
    private int groupId;
    private ModelManager modelManager;

    private TextView okBtn;

    private TextView tvDate1;
    private TextView tvDate2;
    private TextView tvDate3;
    private TextView tvDate4;

    private TextView tvReviewSize1;
    private TextView tvReviewSize2;
    private TextView tvReviewSize3;
    private TextView tvReviewSize4;

    private String date1;
    private String date2;
    private String date3;
    private String date4;


    public StatisticsDialog(Context context,int groupId){
        this.context = context;
        this.groupId = groupId;
        modelManager=new ModelManager(context);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_statistics,null,false);
        builder.setView(view);
        findById(view);
        init();
        setReviewDates();
        return builder.create();
    }

    public void findById(View v){
            okBtn=v.findViewById(R.id.tv_statistics_Ok);
            tvDate1=v.findViewById(R.id.tv_date1);
            tvDate2=v.findViewById(R.id.tv_date2);
            tvDate3=v.findViewById(R.id.tv_date3);
            tvDate4=v.findViewById(R.id.tv_date4);
            tvReviewSize1=v.findViewById(R.id.tv_group_page_numCardsToday1);
            tvReviewSize2=v.findViewById(R.id.tv_group_page_numCardsDay2);
            tvReviewSize3=v.findViewById(R.id.tv_group_page_numCardsDay3);
            tvReviewSize4=v.findViewById(R.id.tv_group_page_numCardsDay4);

    }

    public void init(){
        date1= GetDate.date(context);
        date2=GetDate.getNextDay(1);
        date3=GetDate.getNextDay(2);
        date4=GetDate.getNextDay(3);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void setReviewDates(){
        tvDate1.setText(date1);
        tvDate2.setText(date2);
        tvDate3.setText(date3);
        tvDate4.setText(date4);
        tvReviewSize1.setText(String.valueOf(modelManager.reviewCards(groupId,date1).size()));
        tvReviewSize2.setText(String.valueOf(modelManager.reviewCards(groupId,date2).size()));
        tvReviewSize3.setText(String.valueOf(modelManager.reviewCards(groupId,date3).size()));
        tvReviewSize4.setText(String.valueOf(modelManager.reviewCards(groupId,date4).size()));
    }
}

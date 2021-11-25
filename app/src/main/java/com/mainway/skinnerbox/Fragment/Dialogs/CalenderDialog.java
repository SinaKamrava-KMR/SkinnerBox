package com.mainway.skinnerbox.Fragment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class CalenderDialog extends DialogFragment {
    private Context context;
    private CalendarView calendar;
    private TextView dateView;
    private TextView setDate;
    private ResultCalender resultCalender;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;
        super.onAttach(context);
        this.resultCalender= (ResultCalender) context;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_calender,null,false);
        builder.setView(view);
        calendar=view.findViewById(R.id.Calender);
        dateView=view.findViewById(R.id.tv_CalenderDialog_ShowDate);
        dateView.setText(GetDate.date(context));
        setDate=view.findViewById(R.id.tv_DialogCalender_setDate);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date
                        = year + "-"
                        + (month + 1) + "-" +dayOfMonth ;

                // set this date in TextView for Display
                dateView.setText(Date);
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultCalender.onGetDate(dateView.getText().toString());
                dismiss();
            }
        });
        return builder.create();

    }

    public interface ResultCalender{
        void onGetDate(String date);
    }
}

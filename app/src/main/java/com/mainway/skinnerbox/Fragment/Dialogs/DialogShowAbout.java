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

import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class DialogShowAbout extends DialogFragment {
    private Context context;
    private TextView showText;
    private TextView okBtn;
    private String text;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public DialogShowAbout(String txt){
        this.text=txt;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.show_about_layout,null,false);
        init(view);

        builder.setView(view);
        return builder.create();
    }

    public void init(View view){
        showText=view.findViewById(R.id.textView_about);
        okBtn=view.findViewById(R.id.textView_ok);
        showText.setText(text);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

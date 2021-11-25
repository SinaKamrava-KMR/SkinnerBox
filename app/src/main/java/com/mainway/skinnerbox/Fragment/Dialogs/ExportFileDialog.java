package com.mainway.skinnerbox.Fragment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mainway.skinnerbox.Access.Export.ExportFile;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class ExportFileDialog extends DialogFragment {

    private Context context;
    private TextView exportFileBtn;
    private int groupId;
    private ExportFile exportFile;



    public ExportFileDialog(int groupId,Context context){

        this.context = context;
        this.groupId = groupId;
        exportFile=new ExportFile(context,groupId);
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_export_all_groups,null,false);
        builder.setView(view);
        init(view);
        return builder.create();
    }

    public void init(View v){
        exportFileBtn=v.findViewById(R.id.tv_Dialog_ImportFile_ImportBtnDone);
        exportFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if ( exportFile.export()){
                   Toast.makeText(context, "export was successful", Toast.LENGTH_SHORT).show();
                   dismiss();
               }else {
                    dismiss();
               }
            }
        });
    }
}

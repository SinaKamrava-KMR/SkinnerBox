package com.mainway.skinnerbox.Fragment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mainway.skinnerbox.Access.Import.ImportFile;
import com.mainway.skinnerbox.Fragment.BottomSheets.ShowFilesBottomSheet;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ImportDialog extends DialogFragment implements View.OnClickListener {

    private Context context;
    private ImportFile importFile;
    //===================
    private TextView chooseFile;
    private int groupId;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;

        super.onAttach(context);

    }

    public ImportDialog(Context context1,int groupId){
        this.groupId = groupId;
        importFile=new ImportFile(context1,groupId);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_import_file,null,false);
        builder.setView(view);
       // setCancelable(false);
        finById(view);
        setListener();
        return builder.create();
    }
    //==========================
    public void finById(View v){
        chooseFile =v.findViewById(R.id.tv_Dialog_ImportFile_ImportBtnDone);
    }

    public void setListener(){

        chooseFile.setOnClickListener(this);
    }

    
    public void clickImport(){
        ShowFilesBottomSheet bottomSheet=new ShowFilesBottomSheet();
        bottomSheet.show(getParentFragmentManager(),null);
        dismiss();
    }

    //==========================
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.tv_Dialog_ImportFile_ImportBtnDone:
                clickImport();
                break;
        }
    }

}

package com.mainway.skinnerbox.Fragment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class AddGroupDialog extends DialogFragment {
    private Context context;
    private TextInputLayout textInputLayout;
    private TextInputEditText getGroupNameEt;
    private TextView doneBtn;
    private CallBackDialogResult dialogResult;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;
        this.dialogResult= (CallBackDialogResult) context;
        super.onAttach(context);

    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_add_group,null,false);
        builder.setView(view);
        textInputLayout=view.findViewById(R.id.etl_dialog_group);
        getGroupNameEt=view.findViewById(R.id.et_dialog_group_EnterGroupName);
        doneBtn=view.findViewById(R.id.tv_dialog_add_group_Done);
        onClickDone();
        return builder.create();
    }


    public void onClickDone(){
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getGroupNameEt.getText().length()>0){
                    if (!getGroupNameEt.getText().toString().contains("'")){
                        GroupItem group=new GroupItem();
                        group.setCreateDate(GetDate.date(context));
                        group.setGroupName(getGroupNameEt.getText().toString().trim().replace(" ","_"));
                        group.setCardsNum(0);
                        group.setReviewDate("Not Review");
                        dialogResult.onAddGroupName(group);
                        dismiss();

                    }else {
                        textInputLayout.setError(getResources().getString(R.string.errorForSingleQuotationEt));
                    }

                }else{
                    textInputLayout.setError(getResources().getString(R.string.errorForEmptyEt));
                }
            }
        });
    }


    public interface CallBackDialogResult{
        void onAddGroupName(GroupItem groupItem);
    }

}

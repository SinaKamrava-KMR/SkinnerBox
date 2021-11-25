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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mainway.skinnerbox.Models.Constants;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class EditGroupDialog extends DialogFragment {
    private Context context;
    private TextInputLayout textInputLayout;
    private TextInputEditText getGroupNameEt;
    private TextView doneBtn;
    private CallBackEditDialogResult dialogResult;
    private GroupItem getGroup;
    private TextView title;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;
        this.dialogResult= (CallBackEditDialogResult) context;
        getGroup =getArguments().getParcelable(Constants.EDIT_GROUP_NAME_KEY);
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
        title=view.findViewById(R.id.tv_EditGroupName);
        title.setText(getResources().getString(R.string.EditGroupName));
        getGroupNameEt.setText(getGroup.getGroupName().replace("_"," "));
        onClickDone();
        return builder.create();
    }

    public void onClickDone(){
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getGroupNameEt.getText().length()>0){
                    if (!getGroupNameEt.getText().toString().contains("'")){
                        GroupItem group= getGroup;
                        group.setGroupName(getGroupNameEt.getText().toString().trim().replace(" ","_"));
                        dialogResult.onEditGroupName(group);
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
    public interface CallBackEditDialogResult{
        void onEditGroupName(GroupItem groupItem);
    }
}

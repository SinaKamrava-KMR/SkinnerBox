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

import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeleteCardDialog  extends DialogFragment {

    private Context context;
    private ModelManager manager;
    private int groupId;
    private DeleteResult deleteResult;
    private List<Card> cardList;
    //=============================
    private TextView deleteBtn;
    private TextView cancelBtn;

    public DeleteCardDialog(Context context,int groupId){
        this.context = context;
        this.deleteResult= (DeleteResult) context;
        manager=new ModelManager(context);
        this.groupId = groupId;
        this.cardList=manager.getCards(groupId);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_warnning_delete_cards,null,false);
        builder.setView(view);
        findById(view);
        init();
        return builder.create();
    }

    public void findById(View v){
        deleteBtn=v.findViewById(R.id.tv_dialogWarningDelete_DeleteBtn);
        cancelBtn=v.findViewById(R.id.tv_dialogWarningDelete_CancelBtn);
    }

    public void init(){
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (cardList.size()>0){
                  manager.deleteCardsInGroup(groupId);
                  Toast.makeText(context, "Remove All Cards", Toast.LENGTH_SHORT).show();
                  deleteResult.onClickDelete();
                  dismiss();
              }else {
                  Toast.makeText(context, getResources().getString(R.string.YouMustAddCardForDelete), Toast.LENGTH_SHORT).show();
              }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public interface DeleteResult{
        void onClickDelete();
    }




}

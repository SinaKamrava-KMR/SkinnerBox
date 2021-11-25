package com.mainway.skinnerbox.Pages.AddCard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.mainway.skinnerbox.Fragment.Dialogs.RecordVoiceDialog;
import com.mainway.skinnerbox.Fragment.Pages.EditCardToggleFragment;
import com.mainway.skinnerbox.Fragment.Pages.ViewCardsFragment;
import com.mainway.skinnerbox.Manager.FileManager;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.R;
import com.mainway.skinnerbox.SharedPreference.AppSharedPreference;

public class EditCard extends AppCompatActivity implements View.OnClickListener, EditCardToggleFragment.ToggleCallBack,
        RecordVoiceDialog.ResultRecording {

    private ImageView backBtn;
    private ImageView favoriteBtn;
    private ImageView saveBtn;
    private TextView boxNumber;
    private TextView trueNum;
    private TextView reviewNum;
    private TextView falseNum;
    private MaterialButtonToggleGroup toggleGroup;
    private Card card;
    private ModelManager manager;
    private String oldTextF;
    private String oldTextB;
    private String oldPicF;
    private String oldPicB;
    private String oldAudioF;
    private String oldAudioB;
    private AppSharedPreference sharedPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_card_page);
        sharedPreference=new AppSharedPreference(this);
        findById();
        clickToggleBtn();
        init();
        backBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


    }

    public void findById(){
        backBtn=findViewById(R.id.iv_EditCard_Back);
        favoriteBtn=findViewById(R.id.iv_EditCard_Favorite);
        saveBtn=findViewById(R.id.iv_EditCard_SaveEdit_AppBar);
        boxNumber=findViewById(R.id.tv_EditCard_BoxNumber);
        trueNum=findViewById(R.id.tv_EditCard_trueNum);
        reviewNum=findViewById(R.id.tv_EditCard_reviewNum);
        falseNum=findViewById(R.id.tv_EditCard_falseNum);
        toggleGroup=findViewById(R.id.toggleGroup_EditCard);
    }
    public void init(){
        manager=new ModelManager(this);
        Bundle arg=getIntent().getExtras().getParcelable("GetBundle");
        card=arg.getParcelable("GetArg");

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_EditCard_frameCard,new EditCardToggleFragment(EditCard.this,card,"F"));
        transaction.commit();

        if (card.getBox()==0){
            boxNumber.setText("New Card");
        }else if (card.getBox()==6){
            boxNumber.setText("Memorized");
        }else {
            boxNumber.setText("box "+card.getBox());
        }
        trueNum.setText(String.valueOf(card.getTrueNum()));
        reviewNum.setText(String.valueOf(card.getReviewNum()));
        falseNum.setText(String.valueOf(card.getFalseNum()));
        if (card.getIsFavorite()==0){
            favoriteBtn.setImageDrawable(getDrawable(R.drawable.heart));
        }else {
            favoriteBtn.setImageDrawable(getDrawable(R.drawable.favorite));
        }

        oldTextF=card.getFrontText();
        oldTextB=card.getBackText();
        oldPicF=card.getPicFront();
        oldPicB=card.getPicBack();
        oldAudioF=card.getAudioFront();
        oldAudioB=card.getAudioBack();


    }

    //===========================================

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iv_EditCard_Favorite:
                if (card.getIsFavorite()==0){
                    card.setIsFavorite(1);
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.favorite));
                }else {
                    card.setIsFavorite(0);
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.heart));
                }
                break;
            case R.id.iv_EditCard_Back:
                clickBackBtn();
                break;
            case R.id.iv_EditCard_SaveEdit_AppBar:
                updateCard();
                break;

        }
    }
    //===========================================
    public void clickToggleBtn(){

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId==R.id.btn_EditCard_Front && isChecked){
                    Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame_EditCard_frameCard);
                    if (fragment instanceof EditCardToggleFragment){
                        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_EditCard_frameCard,new EditCardToggleFragment(EditCard.this,card,"F"));
                        transaction.commit();
                    }

                }else  if (checkedId==R.id.btn_EditCard_Back && isChecked){
                    Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame_EditCard_frameCard);
                    if (fragment instanceof EditCardToggleFragment){
                        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_EditCard_frameCard,new EditCardToggleFragment(EditCard.this,card,"B"));
                        transaction.commit();
                    }
                }
            }
        });
    }
    public void updateCard(){
        if (manager.update(card)){
            if (!oldPicF.equals(card.getPicFront())){
                FileManager.deleteFile(oldPicF);
            }else if (!oldPicB.equals(card.getPicBack())){
                FileManager.deleteFile(oldPicB);
            }else if(!oldAudioF.equals(card.getAudioFront())){
                FileManager.deleteFile(oldAudioF);
            }else if(!oldAudioB.equals(card.getAudioBack())){
                FileManager.deleteFile(oldAudioB);
            }
            sharedPreference.setAudioFPath("");
            sharedPreference.setAudioBPath("");
            sharedPreference.setPicFront("");
            sharedPreference.setPicBack("");
            onBackPressed();
            Toast.makeText(this, getResources().getString(R.string.updateCard), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getResources().getString(R.string.ErrorUpdateCard), Toast.LENGTH_SHORT).show();

        }
    }
    public void clickBackBtn(){
        onBackPressed();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!oldPicF.equals(card.getPicFront())){
                          FileManager.deleteFile(card.getPicFront());
                }else if (!oldPicB.equals(card.getPicFront())){
                          FileManager.deleteFile(card.getPicBack());
                }else if(!oldAudioF.equals(card.getAudioFront())){
                          FileManager.deleteFile(card.getAudioFront());
                }else if(!oldAudioB.equals(card.getAudioBack())){
                          FileManager.deleteFile(card.getAudioBack());
                }
                sharedPreference.setAudioFPath("");
                sharedPreference.setAudioBPath("");

            }
        });

    }
    //======================================Toggle Cal Back ========================
    @Override
    public void onEditPic(String path, String status) {
        if (status.equals("F")){
            card.setPicFront(path);
            sharedPreference.setPicFront(path);
        }else{
            card.setPicBack(path);
            sharedPreference.setPicBack(path);
        }
        setCardsInFragment();

    }

    @Override
    public void onEditVoice(String path, String status) {
        if (status.equals("F")){
            card.setAudioFront(path);
            sharedPreference.setAudioFPath(path);
        }else{
            card.setAudioBack(path);
            sharedPreference.setAudioBPath(path);
        }
        setCardsInFragment();
    }

    @Override
    public void onDeleteVoice(String audio, String status) {
        if (status.equals("F")){
            card.setAudioFront("");
            sharedPreference.setAudioFPath("");
        }else{
            card.setAudioBack("");
            sharedPreference.setAudioBPath("");
        }
        setCardsInFragment();
    }

    @Override
    public void onDeletePic(String audio, String status) {
        if (status.equals("F")){
            card.setPicFront("");
            sharedPreference.setPicFront("");
        }else{
            card.setPicBack("");
            sharedPreference.setPicBack("");
        }
        setCardsInFragment();
    }

    @Override
    public void onEditText(String text, String status) {
        if (status.equals("F")){
            card.setFrontText(text);
        }else{
            card.setBackText(text);
        }
        setCardsInFragment();

    }

    @Override
    public void onSetVoice(String path, String status) {
        if (status.equals("F")){
            card.setAudioFront(path);
            sharedPreference.setAudioFPath(path);
        }else{
            card.setAudioBack(path);
            sharedPreference.setAudioBPath(path);
        }
        setCardsInFragment();
    }
    //==============================================================================

    public void setCardsInFragment(){
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame_EditCard_frameCard);
        if (fragment instanceof EditCardToggleFragment){
            ((EditCardToggleFragment) fragment).setCards(this.card);
        }
    }

}
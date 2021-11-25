package com.mainway.skinnerbox.Pages.AddCard;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.Access.PopUp.CustomPopUp;
import com.mainway.skinnerbox.Fragment.Dialogs.CalenderDialog;
import com.mainway.skinnerbox.Fragment.Dialogs.RecordVoiceDialog;
import com.mainway.skinnerbox.Manager.AppPermissions;
import com.mainway.skinnerbox.Manager.FileManager;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.Constants;
import com.mainway.skinnerbox.R;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class AddCard extends AppCompatActivity implements View.OnClickListener, CalenderDialog.ResultCalender
,CallBackFromPopUp, RecordVoiceDialog.ResultRecording {


    private TextView showAddedDate;
    private TextView saveCard;
    private ImageView calender;
    private ImageView backToGroupPage;
    //=================Front of Card====================
    private ImageView picFront;
    private EditText frontET;
    private RelativeLayout containerFrontPlayer;
    private ImageView playIconFront;
    private LottieAnimationView animationFrontPlayer;
    private TextView frontPlayerDuration;
    private ImageView moreFrontPlayer;
    private ImageView addPhotoFront;
    private FrameLayout containerFrontRecord;
    //=================Back of Card====================
    private ImageView picBack;
    private EditText backET;
    private RelativeLayout containerBackPlayer;
    private ImageView playIconBack;
    private LottieAnimationView animationBackPlayer;
    private TextView backPlayerDuration;
    private ImageView moreBackPlayer;
    private ImageView addPhotoBack;
    private FrameLayout containerBackRecord;

    //========================================================================================
    private String audioPathFront;
    private String audioPathBack;
    private String picFrontPath;
    private String picBackPath;
    private String groupName;
    private String lastId;
    enum MusicState{
        PLAYING,STOPPED,PAUSED
    }
    private MediaPlayer mediaPlayerF;
    private MediaPlayer mediaPlayerB;
    private MusicState musicStateF=MusicState.STOPPED;
    private MusicState musicStateB=MusicState.STOPPED;
    private Timer timerF;
    private Timer timerB;
    private ActivityResultLauncher<Intent> startActivityForResult;
    private  ActivityResultLauncher<Intent> startActivityForResultB;
    private FileManager fileManager;
    private ModelManager manager;

    //========================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_page);
        findById();
        init();
        setListener();

    }

    public void findById(){

        calender=findViewById(R.id.iv_AddCard_Calender);
        showAddedDate=findViewById(R.id.tv_AddCard_AddedDateText);
        backToGroupPage=findViewById(R.id.iv_AddCard_Back_togroupPage);
        saveCard=findViewById(R.id.iv_AddCard_SaveBtn_AppBar);
        //======================Front of Card================================
        picFront=findViewById(R.id.iv_AddCard_picFront);
        frontET=findViewById(R.id.editText_AddCard_Question);
        containerFrontPlayer=findViewById(R.id.Container_AddCard_PlayerFront);
        playIconFront=findViewById(R.id.iv_AddCard_PlayQuestion);
        animationFrontPlayer=findViewById(R.id.animationView);
        frontPlayerDuration=findViewById(R.id.tv_AddCard_duration);
        moreFrontPlayer=findViewById(R.id.iv_AddEDitCardPage_MoreAudio);
        addPhotoFront=findViewById(R.id.iv_EditCard_ImageUploadQuestion);
        containerFrontRecord=findViewById(R.id.Frame_AddCard_ForRecordAudioFront);
        //=====================Back of Card==================================
        picBack=findViewById(R.id.iv_AddCard_picBack);
        backET=findViewById(R.id.editText_AddCard_Answer);
        containerBackPlayer=findViewById(R.id.Container_AddCard_PlayerBack);
        playIconBack=findViewById(R.id.iv_AddCard_PlayAnswer);
        animationBackPlayer=findViewById(R.id.animationView_Back);
        backPlayerDuration=findViewById(R.id.tv_AddCard_duration_Back);
        moreBackPlayer=findViewById(R.id.iv_AddEDitCardPage_MoreAudio_Back);
        addPhotoBack=findViewById(R.id.iv_EditCard_ImageUploadAnswer);
        containerBackRecord=findViewById(R.id.Frame_AddCard_ForRecordAudioBack);


    }
    public void init(){
        groupName=getIntent().getExtras().getString("GroupName");
        manager=new ModelManager(AddCard.this);
        lastId=String.valueOf(manager.lastId());
        frontPlayerDuration.setText("00:00");
        backPlayerDuration.setText("00:00");
        containerFrontPlayer.setBackground(getResources().getDrawable(R.drawable.background_for_empty_group));
        containerBackPlayer.setBackground(getResources().getDrawable(R.drawable.background_for_empty_group));
        fileManager=new FileManager(AddCard.this);
        lunchFrontResult();
        lunchBackResult();
          audioPathFront="";
          audioPathBack="";
          picFrontPath="";
          picBackPath="";
    }
    public void setListener(){
        showAddedDate.setText(GetDate.date(this));
        calender.setOnClickListener(this);
        backToGroupPage.setOnClickListener(this);
        saveCard.setOnClickListener(this);
        containerFrontPlayer.setOnClickListener(this);
        containerBackPlayer.setOnClickListener(this);
        moreFrontPlayer.setOnClickListener(this);
        moreBackPlayer.setOnClickListener(this);
        playIconFront.setOnClickListener(this);
        playIconBack.setOnClickListener(this);
        addPhotoFront.setOnClickListener(this);
        addPhotoBack.setOnClickListener(this);
        containerFrontRecord.setOnClickListener(this);
        containerBackRecord.setOnClickListener(this);
    }


    //=======================Override OnClickListener For Click Items========================
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iv_AddCard_Back_togroupPage:
                onBackPressed();
                break;
            case R.id.iv_AddCard_SaveBtn_AppBar:
                saveCard();
                break;
            case R.id.iv_AddCard_Calender:
                CalenderDialog dialog=new CalenderDialog();
                dialog.show(getSupportFragmentManager(),null);
                break;
            case R.id.iv_EditCard_ImageUploadQuestion:
                if (AppPermissions.checkImagePermissions(this)){
                    goToGallery("F");

                }else {
                    AppPermissions.requestImagePermissions(this);
                }

                break;
            case R.id.iv_EditCard_ImageUploadAnswer:
                if (AppPermissions.checkImagePermissions(this)){
                    goToGallery("B");

                }else {
                    AppPermissions.requestImagePermissions(this);
                }

                break;
            case R.id.Frame_AddCard_ForRecordAudioFront:
                if (AppPermissions.checkAudioPermissions(this)){
                    startFrontRecord();

                }else {
                    AppPermissions.requestAudioPermissions(this);
                }
                startFrontRecord();
                break;
            case R.id.Frame_AddCard_ForRecordAudioBack:
                if (AppPermissions.checkAudioPermissions(this)){
                    startBackRecord();

                }else{
                    AppPermissions.requestAudioPermissions(this);
                }

                break;
            case R.id.iv_AddCard_PlayQuestion:
                if (containerFrontPlayer.getForeground() == null){
                    clickPlayAudioFront();
                }
                break;
            case R.id.iv_AddCard_PlayAnswer:
                if (containerBackPlayer.getForeground() == null){
                    clickPlayAudioBack();
                }
                break;
            case R.id.iv_AddEDitCardPage_MoreAudio:
            case R.id.Container_AddCard_PlayerFront:
                if (containerFrontPlayer.getForeground() == null){
                    CustomPopUp.popUpVP(AddCard.this,v,AddCard.this,"F");
                }
                break;
            case R.id.iv_AddEDitCardPage_MoreAudio_Back:
            case R.id.Container_AddCard_PlayerBack:
                if (containerBackPlayer.getForeground() == null){
                    CustomPopUp.popUpVP(AddCard.this,v,AddCard.this,"B");
                }
                break;
        }
    }

    //================================Method For Do Tacks====================================
    public void saveCard(){
        if (!isCardEmptyForAdd()){
            manager=new ModelManager(AddCard.this);
            Card card=new Card();
            card.setGroupId(manager.getGroup(groupName).getId());
            card.setIsFavorite(0);
            card.setFalseNum(0);
            card.setTrueNum(0);
            card.setReviewNum(0);
            card.setAddedDate(showAddedDate.getText().toString());
            try {
                card.setReviewDate(GetDate.nextDay(showAddedDate.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            card.setBox(0);

            //=====================
            if (frontET.length()>0){

                card.setFrontText(frontET.getText().toString());
            }else {
                card.setFrontText("");
            }
            if (backET.length()>0){
                card.setBackText(backET.getText().toString());
            }else{
                card.setBackText("");
            }
            card.setPicFront(picFrontPath);
            card.setPicBack(picBackPath);
            card.setAudioFront(audioPathFront);
            card.setAudioBack(audioPathBack);
            if (manager.addCard(card)>0){
                backToFirstState();
                Toast.makeText(AddCard.this, getResources().getString(R.string.CardAdded), Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(AddCard.this, "Can not Add Empty Card !", Toast.LENGTH_SHORT).show();
        }
    }
    public void backToFirstState(){
        frontET.setText("");
        backET.setText("");
        audioPathFront="";
        audioPathBack="";
        picFrontPath="";
        picBackPath="";
        lastId=String.valueOf(manager.lastId());
        frontPlayerDuration.setText("00:00");
        backPlayerDuration.setText("00:00");
        containerBackPlayer.setBackground(getResources().getDrawable(R.drawable.background_for_empty_group));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            containerBackPlayer.setForeground(getDrawable(R.drawable.foreground_voice_cont));
        }
        containerFrontPlayer.setBackground(getResources().getDrawable(R.drawable.background_for_empty_group));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            containerFrontPlayer.setForeground(getDrawable(R.drawable.foreground_voice_cont));
        }
        picFront.setImageDrawable(null);
        picBack.setImageDrawable(null);




    }

    public void lunchFrontResult(){
        startActivityForResult=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()==RESULT_OK){

                            Intent intent = result.getData();
                            Uri selectedImage = intent.getData();
                            String fileName=groupName+"_F_"+lastId+".jpg";
                            Log.i("fileUri", "onActivityResult: "+selectedImage.toString());

                            picFront.setImageURI(selectedImage);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setPicPathFront( fileManager.jpgFilePath(selectedImage,fileName));
                                }
                            });

//                            AsyncTask<Void,Void,Void> task1=new AsyncTask<Void, Void, Void>() {
//                                @Override
//                                protected Void doInBackground(Void... voids) {
//                                    return null;
//                                }
//
//
//                            };
//                            task1.execute();


                        }
                    }
                });

    }

    public void lunchBackResult(){
        startActivityForResultB=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()==RESULT_OK){

                            Intent intent = result.getData();
                            Uri selectedImage = intent.getData();
                            String fileName=groupName+"_B_"+lastId+".jpg";
                            picBack.setImageURI(selectedImage);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setPicBackPath(fileManager.jpgFilePath(selectedImage,fileName));
                                }
                            });
//                            AsyncTask<Void,Void,Void> task1=new AsyncTask<Void, Void, Void>() {
//                                @Override
//                                protected Void doInBackground(Void... voids) {
//                                }
//
//
//                            };
//                            task1.execute();


                        }
                    }
                });
    }

    public void goToGallery(String status){
        if(!AppPermissions.checkImagePermissions(AddCard.this)){
          AppPermissions.requestImagePermissions(AddCard.this);
        }
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (status.equals("F")){
            startActivityForResult.launch(intent);
        }else{
            startActivityForResultB.launch(intent);
        }
    }

    public void startFrontRecord(){
        RecordVoiceDialog dialog=new RecordVoiceDialog("F",groupName,lastId);
        dialog.show(getSupportFragmentManager(),null);
    }

    public void setPicPathFront(String path){
        picFrontPath=path;
    }
    public void setPicBackPath(String path){
        picBackPath=path;
    }
    public void startBackRecord(){
        RecordVoiceDialog dialog=new RecordVoiceDialog("B",groupName,lastId);
        dialog.show(getSupportFragmentManager(),null);
    }

    public void clickPlayAudioFront(){
        if (mediaPlayerF==null){
            musicStateF = MusicState.STOPPED;
            mediaPlayerF = MediaPlayer.create(AddCard.this, Uri.parse(audioPathFront));
        }

        if(musicStateF== MusicState.PLAYING){

            musicStateF = MusicState.PAUSED;
            animationFrontPlayer.pauseAnimation();
            playIconFront.setImageResource(R.drawable.ic_play_standard);
            mediaPlayerF.pause();


        }else {

            playIconFront.setImageResource(R.drawable.ic_paus_standard);
            animationFrontPlayer.playAnimation();
            musicStateF = MusicState.PLAYING;
            mediaPlayerF.start();
            timerF=new Timer();
            timerF.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayerF!=null){
                                frontPlayerDuration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                            }
                        }
                    });

                }
            },100,100);
            mediaPlayerF.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayerF != null) {
                        playIconFront.setImageResource(R.drawable.ic_play_standard);
                        animationFrontPlayer.pauseAnimation();
                        musicStateF = MusicState.STOPPED;
                        frontPlayerDuration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                        mediaPlayerF.release();
                        mediaPlayerF= null;

                    }
                    timerF.cancel();
                }
            });

        }







    }

    public void clickPlayAudioBack(){
        if (mediaPlayerB==null){
            musicStateB = MusicState.STOPPED;
            mediaPlayerB = MediaPlayer.create(AddCard.this, Uri.parse(audioPathBack));
        }

        if(musicStateB== MusicState.PLAYING){

            musicStateB = MusicState.PAUSED;
            animationBackPlayer.pauseAnimation();
            playIconBack.setImageResource(R.drawable.ic_play_standard);
            mediaPlayerB.pause();


        }else {

            playIconBack.setImageResource(R.drawable.ic_paus_standard);
            animationBackPlayer.playAnimation();
            musicStateB = MusicState.PLAYING;
            mediaPlayerB.start();
            timerB=new Timer();
            timerB.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayerB!=null){
                                backPlayerDuration.setText(Constants.convertFormat(mediaPlayerB.getCurrentPosition()));
                            }
                        }
                    });

                }
            },100,100);
            mediaPlayerB.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayerB != null) {
                        playIconBack.setImageResource(R.drawable.ic_play_standard);
                        animationBackPlayer.pauseAnimation();
                        musicStateB = MusicState.STOPPED;
                        backPlayerDuration.setText(Constants.convertFormat(mediaPlayerB.getCurrentPosition()));
                        mediaPlayerB.release();
                        mediaPlayerB= null;

                    }
                    timerB.cancel();
                }
            });

        }







    }

    public void deleteVoiceFile(String status){
        if (status.equals("F")){
            if (FileManager.deleteFile(audioPathFront)){
                containerFrontPlayer.setBackground(getResources().getDrawable(R.drawable.background_for_empty_group));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    containerFrontPlayer.setForeground(getDrawable(R.drawable.foreground_voice_cont));
                }
                audioPathFront="";
                frontPlayerDuration.setText("00:00");
            }

        }else{
            if (FileManager.deleteFile(audioPathBack)){
                backPlayerDuration.setText("00:00");
                audioPathBack="";
                containerBackPlayer.setBackground(getResources().getDrawable(R.drawable.background_for_empty_group));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    containerBackPlayer.setForeground(getDrawable(R.drawable.foreground_voice_cont));
                }

            }
        }
    }

    public boolean isCardEmptyForAdd(){
        int etFSize=frontET.length();
        int etBSize=backET.length();
        int imageFSize=picFrontPath.length();
        int imageBSize=picBackPath.length();
        int audioFSize=audioPathFront.length();
        int audioBSize=audioPathBack.length();
        if (etBSize==0 && etFSize==0 && imageFSize==0 && imageBSize==0 && audioFSize==0 && audioBSize==0){
            return  true;
        }
        return false;
    }

    //=============================== Get Date From Calender  ==========================================
    @Override
    public void onGetDate(String date) {
        showAddedDate.setText(date);
    }

    //=================== Result Methods From PopUp Interface When Click More Options Audio Player==============
    @Override
    public void onClickDelete(String status) {
        deleteVoiceFile(status);
    }

    @Override
    public void onClickReplace(String status) {

        if (status.equals("F")){
            startFrontRecord();
        }else{
            startBackRecord();
        }
    }
    //============================Result From Recording Voice ==============================
    @Override
    public void onSetVoice(String path,String status) {

        if (status.equals("F")){
            this.audioPathFront=path;
            containerFrontPlayer.setBackground(getResources().getDrawable(R.drawable.background_play_audio));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                containerFrontPlayer.setForeground(null);
            }
        }else{
            this.audioPathBack=path;
            containerBackPlayer.setBackground(getResources().getDrawable(R.drawable.background_play_audio));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                containerBackPlayer.setForeground(null);
            }
        }
    }
    //=====================================================================


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerF!=null){
            timerF.cancel();
        }
        if (timerB!=null){
            timerB.cancel();
        }
        if (mediaPlayerF!=null){
            mediaPlayerF.release();
            mediaPlayerF=null;
        }
        if (mediaPlayerB!=null){
            mediaPlayerB.release();
            mediaPlayerB=null;
        }
    }
}
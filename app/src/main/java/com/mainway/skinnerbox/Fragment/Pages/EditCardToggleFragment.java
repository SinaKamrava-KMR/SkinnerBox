package com.mainway.skinnerbox.Fragment.Pages;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.mainway.skinnerbox.Access.PopUp.CustomPopUp;
import com.mainway.skinnerbox.Fragment.Dialogs.RecordVoiceDialog;
import com.mainway.skinnerbox.Manager.AppPermissions;
import com.mainway.skinnerbox.Manager.FileManager;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.Constants;
import com.mainway.skinnerbox.Pages.AddCard.AddCard;
import com.mainway.skinnerbox.Pages.AddCard.CallBackFromPopUp;
import com.mainway.skinnerbox.R;
import com.mainway.skinnerbox.SharedPreference.AppSharedPreference;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

public class EditCardToggleFragment extends Fragment implements View.OnClickListener,CallBackFromPopUp {
    private Context context;
    private Card card;
    private String status;
    private ToggleCallBack callBack;
    //=======================
    private ImageView photo;
    private ImageView playVoice;
    private ImageView replaceVoice;
    private ImageView deleteVoice;
    private FrameLayout moreOptionPhoto;
    private LottieAnimationView playerAnimation;
    private TextView duration;
    private EditText etCardText;
    private  String audioPathFront;
    private ModelManager manager;
    private FileManager fileManager;
    private String groupName;
    private String lastId;
    enum MusicState{
        PLAYING,STOPPED,PAUSED
    }
    private MediaPlayer mediaPlayerF;
    private MusicState musicStateF= MusicState.STOPPED;
    private Timer timerF;
    private ActivityResultLauncher<Intent> startActivityForResult;
    private String picFrontPath;
    private AppSharedPreference sharedPreference;


    public EditCardToggleFragment(Context context,Card card,String status){
        this.context = context;
        this.card = card;
        this.status = status;
        this.callBack= (ToggleCallBack) context;
        sharedPreference=new AppSharedPreference(context);
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items_edit_card,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager=new ModelManager(context);
        fileManager=new FileManager(context);
        groupName=manager.getGroup(card.getGroupId()).getGroupName();
        this.lastId=String.valueOf(card.getId());
        findItemById(view);
        init();
        lunchFrontResult();
        setListener();
    }

    public void findItemById(View v){
        photo=v.findViewById(R.id.iv_EditCard_picFront);
        moreOptionPhoto=v.findViewById(R.id.frame_EditCard_MoreOptions_FrontPic);
        playVoice=v.findViewById(R.id.iv_editCard_playBtnFrontCard);
        playerAnimation=v.findViewById(R.id.animation_EditCard_Front);
        duration=v.findViewById(R.id.tv_EditCard_durationPlayerFront);
        replaceVoice=v.findViewById(R.id.iv_EditCard_ReplaceFrontVoice);
        deleteVoice=v.findViewById(R.id.iv_EditCard_DeleteFrontVoice);
        etCardText=v.findViewById(R.id.et_EditCard_TextFront);

    }
    public void init(){

        if (status.equals("F")){
           if (card.getPicFront().length()>0){
               photo.setImageURI(Uri.parse(card.getPicFront()));
           }else{
               photo.setImageDrawable(null);
           }
            audioPathFront=card.getAudioFront();
            etCardText.setText(card.getFrontText());
            picFrontPath=card.getPicFront();

        }else {
            if (card.getPicBack().length()>0){
                photo.setImageURI(Uri.parse(card.getPicBack()));
            }else {
                photo.setImageDrawable(null);
            }
            audioPathFront=card.getAudioBack();
            etCardText.setText(card.getBackText());
            picFrontPath=card.getPicBack();

        }
        onEditText();
    }
    public void startRecord(){
        RecordVoiceDialog dialog=new RecordVoiceDialog(status,groupName,lastId,"up");
        dialog.show(getChildFragmentManager(),null);
    }
    public void playAudio(){

        if (status.equals("F")){
            if (!sharedPreference.getAudioFPath().equals("")){
                audioPathFront=sharedPreference.getAudioFPath();
            }
        }else {
            if (!sharedPreference.getAudioBPath().equals("")){
                audioPathFront=sharedPreference.getAudioBPath();
            }
        }

        if (mediaPlayerF==null){
            musicStateF = MusicState.STOPPED;

            mediaPlayerF = MediaPlayer.create(context, Uri.parse(audioPathFront));
        }

        if(musicStateF== MusicState.PLAYING){

            musicStateF = MusicState.PAUSED;
            playerAnimation.pauseAnimation();
            playVoice.setImageResource(R.drawable.ic_play_standard);
            mediaPlayerF.pause();


        }else {

            if (mediaPlayerF!=null){
                playVoice.setImageResource(R.drawable.ic_paus_standard);
                playerAnimation.playAnimation();
                musicStateF = MusicState.PLAYING;
                mediaPlayerF.start();
                timerF=new Timer();
                timerF.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayerF!=null){
                                        duration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                                    }
                                }
                            });
                        }

                    }
                },100,100);
                mediaPlayerF.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mediaPlayerF != null) {
                            playVoice.setImageResource(R.drawable.ic_play_standard);
                            playerAnimation.pauseAnimation();
                            musicStateF = MusicState.STOPPED;
                            duration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                            mediaPlayerF.release();
                            mediaPlayerF= null;

                        }
                        timerF.cancel();
                    }
                });

            }
        }







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
                            String fileName=groupName+"_"+status+"_"+"_up_"+lastId+".jpg";

                            photo.setImageURI(selectedImage);

                            if (getActivity()!=null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        picFrontPath=fileManager.jpgFilePath(selectedImage,fileName);
                                        callBack.onEditPic(picFrontPath,status);
                                    }
                                });
                            }
//                            AsyncTask<Void,Void,Void> task1=new AsyncTask<Void, Void, Void>() {
//                                @Override
//                                protected Void doInBackground(Void... voids) {
//
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
    public void onEditText(){
        etCardText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    callBack.onEditText(s.toString(),status);
                }else{
                    Toast.makeText(context, "Can not Set Empty Text in Card", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void goToGallery(String status){

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult.launch(intent);

    }
    public void setListener(){
        moreOptionPhoto.setOnClickListener(this);
        playVoice.setOnClickListener(this);
        replaceVoice.setOnClickListener(this);
        deleteVoice.setOnClickListener(this);


    }

    //==========================================
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.frame_EditCard_MoreOptions_FrontPic:
                CustomPopUp.popUpVP(context,v,EditCardToggleFragment.this,"F");
                break;
            case R.id.iv_editCard_playBtnFrontCard:
                if (!audioPathFront.equals("")){
                   playAudio();
                }else{
                    Toast.makeText(context, getResources().getString(R.string.EmptyVoice), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_EditCard_ReplaceFrontVoice:
                startRecord();
                break;
            case R.id.iv_EditCard_DeleteFrontVoice:
                if (FileManager.deleteFile(audioPathFront)){
                    callBack.onDeleteVoice("",status);
                    audioPathFront="";
                }else {
                    Toast.makeText(context, "Can Not Remove this Voice", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    //==========================================
    @Override
    public void onClickDelete(String status) {
        if (picFrontPath.length()>0){
            if (FileManager.deleteFile(picFrontPath)){
                callBack.onDeletePic("",status);
                picFrontPath="";
                photo.setImageDrawable(null);
                Toast.makeText(context,  getResources().getString(R.string.DeleteImageContents), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "You must add a photo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickReplace(String status) {
        goToGallery(status);
    }
    //==========================================
    public interface ToggleCallBack{
        void onEditPic(String path,String status);
        void onEditVoice(String path,String status);
        void onDeleteVoice(String audio,String status);
        void onDeletePic(String audio,String status);
        void onEditText(String text,String status);
    }

    public void setCards(Card card){
        this.card=card;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerF!=null){
            timerF.cancel();
        }
        if (mediaPlayerF!=null){
            mediaPlayerF.release();
            mediaPlayerF=null;
        }
    }
}

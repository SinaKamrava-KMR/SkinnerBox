package com.mainway.skinnerbox.Fragment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.mainway.skinnerbox.Access.PlayerManager.Record;
import com.mainway.skinnerbox.Manager.AppPermissions;
import com.mainway.skinnerbox.Manager.FileManager;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

public class RecordVoiceDialog extends DialogFragment implements View.OnClickListener {


    private Context context;
    private String status;
    private boolean isRecording=false;
    enum MusicState{
        PLAYING,STOPPED,PAUSED
    }
    private MediaPlayer mediaPlayer;
    private MusicState musicState= MusicState.STOPPED;
    //=====================================
    private LottieAnimationView recordingVoice;
    private LottieAnimationView animationForPlayer;
    private Chronometer chronometer;
    private ImageView playVoiceBtn;
    private TextView removeVoice;
    private TextView cancel;
    private TextView setVoice;
    private  Record record;
    private String fileName;
    private String filePath="";

    private FileManager fileManager;
    private ResultRecording resultRecording;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;
        this.resultRecording= (ResultRecording) context;
        super.onAttach(context);
    }
    public RecordVoiceDialog(String status,String groupName,String lastCardId){
        this.status=status;
        fileName=groupName+"_"+status+"_"+lastCardId+".mp3";
    }
    public RecordVoiceDialog(String status,String groupName,String lastCardId,Context context1){
        this.status=status;
        fileName=groupName+"_"+status+"_"+lastCardId+".mp3";
        this.resultRecording= (ResultRecording) context1;
    }
    public RecordVoiceDialog(String status,String groupName,String lastCardId,String up){
        this.status=status;
        fileName=groupName+"_"+status+"_"+up+lastCardId+".mp3";
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_record_audio,null,false);
        builder.setView(view);
        setCancelable(false);
        init(view);
        setListener();
        return builder.create();
    }
    public void init(View v){
        record=new Record(context);
        fileManager=new FileManager(context);
        recordingVoice=v.findViewById(R.id.lottieAnimationView_recordingVoice);
        chronometer=v.findViewById(R.id.chronometer);
        playVoiceBtn=v.findViewById(R.id.iv_RecordVoice_PlayBrn);
        animationForPlayer=v.findViewById(R.id.animationView1);
        removeVoice=v.findViewById(R.id.tv_recordingVoice_removeVoice);
        cancel=v.findViewById(R.id.tv_RecordVoice_CancelBtn);
        setVoice=v.findViewById(R.id.tv_RecordVoice_Save);
    }
    public void setListener(){

        recordingVoice.setOnClickListener(this);
        playVoiceBtn.setOnClickListener(this);
        removeVoice.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setVoice.setOnClickListener(this);
    }
    //=======================override onClick Method From Listener=========================

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.lottieAnimationView_recordingVoice:
                if (!AppPermissions.checkAudioPermissions(context)) {
                    AppPermissions.requestAudioPermissions(getActivity());
                }
                startRecord();

                break;
            case R.id.iv_RecordVoice_PlayBrn:
               if (!filePath.equals("")){
                   playVoice();
               }else{
                   Toast.makeText(context, "you must Record Voice", Toast.LENGTH_SHORT).show();
               }
                break;
            case R.id.tv_recordingVoice_removeVoice:
                if (!filePath.equals("")){
                    removeVoice();
                    Toast.makeText(context, getResources().getString(R.string.DeleteVoiceContents), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "you must Record Voice", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_RecordVoice_CancelBtn:
                cancel();
                break;
            case R.id.tv_RecordVoice_Save:
                setVoice();
                dismiss();
                break;
        }
    }

    //==============================Methods For Record Voice  Tasks=====================
    public void startRecord(){

        if (isRecording){
            record.stopRecord();
            recordingVoice.clearAnimation();
            recordingVoice.setProgress(0);
            recordingVoice.pauseAnimation();
            chronometer.stop();
            isRecording=false;
            filePath=fileManager.mp3FilePath(fileName);
        }else{
            recordingVoice.playAnimation();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            record.startRecord(fileName);
            isRecording=true;
        }

    }
    public void playVoice(){

        if (mediaPlayer==null){
            musicState = MusicState.STOPPED;
            mediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
        }

        if(musicState== MusicState.PLAYING){

            musicState = MusicState.PAUSED;
            animationForPlayer.pauseAnimation();
            playVoiceBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();


        }else {

            playVoiceBtn.setImageResource(R.drawable.pause);
            animationForPlayer.playAnimation();
            musicState =MusicState.PLAYING;
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        playVoiceBtn.setImageResource(R.drawable.ic_play);
                        animationForPlayer.pauseAnimation();
                        musicState =MusicState.STOPPED;
                        mediaPlayer.release();
                        mediaPlayer = null;

                    }
                }
            });

        }





    }
    public void removeVoice(){
            if(FileManager.deleteFile(filePath)){
                filePath="";
            }
    }
    public void cancel(){
        removeVoice();
        dismiss();
    }
    public void setVoice(){
       if (!filePath.equals("")){
           resultRecording.onSetVoice(filePath,status);
           dismiss();
       }else{
           Toast.makeText(context, getResources().getString(R.string.errorEmptyVoice), Toast.LENGTH_SHORT).show();
            dismiss();
       }

       dismiss();

    }

    public interface ResultRecording{
        void onSetVoice(String path,String status);
    }

}

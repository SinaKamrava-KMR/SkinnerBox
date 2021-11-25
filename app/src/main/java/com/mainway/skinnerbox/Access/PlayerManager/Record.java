package com.mainway.skinnerbox.Access.PlayerManager;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.mainway.skinnerbox.Manager.FileManager;
import com.mainway.skinnerbox.Models.Constants;

import java.io.File;
import java.io.IOException;

public class Record {
    private   MediaRecorder mediaRecorder;
    private Context context;
    private FileManager fileManager;

    public Record(Context context){
        this.context = context;
        fileManager=new FileManager(context);


    }
    public  void recordVoice(String cardId,String groupName,String status){
//        mediaRecorder=new MediaRecorder();
//        String recordFileName=groupName+"_"+status+"_"+cardId+".mp3";
//        String filePath=context.getApplicationContext().getFilesDir().getPath();
//        Log.i("recordVoice", "recordVoice: "+filePath);
//        File file=new File(filePath+"/"+"voices");
//        if (!file.exists()){
//            file.mkdir();
//        }
//
//
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setOutputFile(file.getPath()+"/"+recordFileName);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaRecorder.start();
    }
    public void startRecord(String fileName){
        mediaRecorder=new MediaRecorder();
        Log.i("mediaRecorder", "startRecord: " +( mediaRecorder==null));
        File file=fileManager.getFolder(Constants.VOICE_FOLDER);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(file.getPath()+File.separator+fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        Log.i("mediaRecorder", "startRecord: mediaRecorder start");
    }
    public  void stopRecord(){

            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder=null;

    }


}

package com.mainway.skinnerbox.Manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    private Context context;
    private String dirPicPath;
    private String dirVoicePath;
    private String picFolderPath="/.skinner/images";
    private String voiceFolderPath="/.skinner/voices";

    public FileManager(Context context){
        this.context = context;
        dirPicPath=context.getExternalFilesDir(picFolderPath).getPath();
        dirVoicePath=context.getExternalFilesDir(voiceFolderPath).getPath();

    }

    public File getFolder(String status){

        if (status.equals(Constants.IMAGE_FOLDER) && StorageHelper.isExternalStorageWritable()){

            File picFile=new File(dirPicPath);

                if (!picFile.exists()){
                    picFile.mkdir();
                }

            Log.i("newPicFile", "CreateFile: ");
            return picFile;

        }else if (status.equals(Constants.VOICE_FOLDER) && StorageHelper.isExternalStorageWritable()){

            File voiceFile=new File(dirVoicePath);
            if (!voiceFile.exists()){
                voiceFile.mkdir();
            }
            return voiceFile;

        }

        return null;
    }
    public String mp3FilePath(String fileName){
        File voiceFile=new File(dirVoicePath+File.separator+fileName);
        if (voiceFile.exists()){
            return voiceFile.getPath();
        }else {
            return "";
        }
    }
    //===========================================================================
    public String jpgFilePath(Uri uri,String fileName){

        Bitmap imageBitmap=decodeUri(uri);
        FileOutputStream outputStream = null;
        File dir=getFolder(Constants.IMAGE_FOLDER);
        File picFile=new File(dir+File.separator+fileName);
        try {
            outputStream=new FileOutputStream(picFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return picFile.getPath();

    }

    private Bitmap decodeUri(Uri selectedImage) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final int REQUIRED_SIZE = 140;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    //===========================================================================
    public static boolean deleteFile(String filePath){
        File file=new File(filePath);
        Log.i("deleteF", "deleteFile: "+file.getName());
        if (file.exists()){
            return file.delete();
        }
        return false;
    }
    public static void deleteFilesInGroup(Card card){
        if (card.getAudioBack().length()>0){
            deleteFile(card.getAudioBack());
        }
        if (card.getAudioFront().length()>0){
            deleteFile(card.getAudioFront());
        }
        if (card.getPicFront().length()>0){
            deleteFile(card.getPicFront());
        }
        if (card.getPicBack().length()>0){
            deleteFile(card.getPicBack());
        }
    }
    public static boolean voiceExist(String cardId, String groupName, String status, Context context){
        String audioName=groupName+"_"+status+"_"+cardId+".mp3";
        String voicePath=context.getApplicationContext().getFilesDir().getPath()+"/voices/"+audioName;
        File file=new File(voicePath);
        return file.exists();
    }
    public static String voicePath(String cardId,String groupName,String status,Context context){
        String audioName=groupName+"_"+status+"_"+cardId+".mp3";
        String voicePath=context.getApplicationContext().getFilesDir().getPath()+"/voices/"+audioName;
        return voicePath;
    }


}

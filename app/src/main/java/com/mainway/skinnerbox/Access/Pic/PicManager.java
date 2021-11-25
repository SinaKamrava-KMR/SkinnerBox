package com.mainway.skinnerbox.Access.Pic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicManager {
    private Context context;

    public PicManager(Context context){

        this.context = context;
    }



    public File getOutputMediaFile(String groupName,String cardId,String status){
        String imageFileName=groupName+"_"+status+"_"+cardId+".jpg";
        String filePath=context.getApplicationContext().getFilesDir().getPath();
        File file=new File(filePath+"/images");
        if (!file.exists()){
            file.mkdir();
        }
        File myFile=new File(file.getPath()+File.separator+imageFileName);
        return myFile;

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public String storeImage(Uri pictureUri, File file) throws IOException {
        File pictureFile = file;
        ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), pictureUri);
        Bitmap image = ImageDecoder.decodeBitmap(source);
        if (pictureFile == null) {
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            return pictureFile.getPath();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
            return "";
        } catch (IOException e) {

            Log.d("TAG", "Error accessing file: " + e.getMessage());
            return "";
        }
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);
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
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }




}

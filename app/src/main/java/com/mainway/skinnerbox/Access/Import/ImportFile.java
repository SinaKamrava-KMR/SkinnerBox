package com.mainway.skinnerbox.Access.Import;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Pages.AddCard.AddCard;
import com.mainway.skinnerbox.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportFile {

    private Context context;
    private int groupId;
    private ModelManager manager;
    private boolean isAdded=false;


    public ImportFile(Context context , int groupId){
        manager=new ModelManager(context);
        this.context = context;
        this.groupId = groupId;

    }

    public boolean addWord(String key,String value){
        Card card=new Card();
        card.setGroupId(groupId);
        card.setIsFavorite(0);
        card.setFalseNum(0);
        card.setTrueNum(0);
        card.setReviewNum(0);
        card.setAddedDate(GetDate.date(context));
        card.setReviewDate(GetDate.getTomorrow());
        card.setBox(0);
        card.setFrontText(key);
        card.setBackText(value);
        //=====================
        card.setPicFront("");
        card.setPicBack("");
        card.setAudioFront("");
        card.setAudioBack("");
        if (manager.addCard(card)>0){
           return true;
        }else {
            return false;
        }

    }

    public boolean importFile(String fileLocation){


        File textFile=new File(fileLocation);
        if (!textFile.getName().toLowerCase().contains(".txt")){
            return false;
        }

        if (!textFile.exists() && !textFile.getName().contains(".txt")){
            Log.i("importFileDD", "textFile.exists : "+(!textFile.exists()));
            Log.i("importFileDD", "textFile.getName().contains: "+(textFile.getName().contains(".txt")));
            return false;
        }
        FileInputStream fIn = null;
        BufferedReader myReader = null;
        try {
            //=============================
             fIn = new FileInputStream(textFile);
             myReader = new BufferedReader(new InputStreamReader(fIn));
            String line = "";
            while ((line= myReader.readLine())!=null){
                Log.i("importFileDD", "read line : "+(line));
                String[] strings = TextUtils.split(line, ":");
                if (strings.length != 2){
                    continue;
                }
                String key=strings[0].replace("'"," ").trim();
                String value=strings[1].replace("'"," ").trim();
                if (addWord(key,value)){
                    isAdded=true;
                }

            }

            //=============================
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("importFileDD", "FileNotFoundException : "+(fIn==null));
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("importFileDD", "IOException : "+(myReader==null));
            return false;
        }finally {
            if (myReader!=null){
                try {
                    myReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isAdded){
            Toast.makeText(context, "Import File Completed", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return false;
        }
        //=============================================
    }

}

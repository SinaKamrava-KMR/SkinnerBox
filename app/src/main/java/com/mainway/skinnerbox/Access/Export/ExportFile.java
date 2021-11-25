package com.mainway.skinnerbox.Access.Export;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Manager.StorageHelper;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportFile {
    private Context context;
    private int groupId;
    private ModelManager manager;
    private List<Card> cardList;
    private String groupName;
    private String folderPath="/SkinnerBox";
    public ExportFile(Context context , int groupId){
        manager=new ModelManager(context);
        this.context = context;
        this.groupId = groupId;
        cardList=manager.getCards(groupId);
        this.groupName=manager.getGroup(groupId).getGroupName();
    }

    public List<String> getText(List<Card> cards){
        List<String> texts=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.get(i).getFrontText().equals("")){
                String txt=cards.get(i).getFrontText()+":"+cards.get(i).getBackText();
                texts.add(txt);
            }
        }
        return texts;
    }

    public String getFolder(){
        if (StorageHelper.isExternalStorageWritable()){
            String path =context.getExternalFilesDir(folderPath).getPath();
            File folder=new File(path);

            if (!folder.exists()){
                folder.mkdir();
            }

            Log.i("newPicFile", "CreateFile: ");
            return folder.getPath();

        }else {

            return  "";
        }
    }


    public boolean export() {

        if (cardList.isEmpty()){
            Toast.makeText(context, "not found cards in group", Toast.LENGTH_SHORT).show();
            return false;
        }
        File file = new File(getFolder(), groupName + ".txt");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                for (int i = 0; i < cardList.size(); i++) {
                    stream.write(getText(cardList).get(i).getBytes());
                    stream.write("\n".getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                stream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("folderPath", "export: " + file.getPath());

            return false;

        }
    }


}

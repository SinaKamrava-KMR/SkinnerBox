package com.mainway.skinnerbox.Models;

import java.util.concurrent.TimeUnit;

public class Constants {
    public static final String DATABASE_NAME="leitner.db";
    public static final String QUESTION="question";
    public static final String ANSWER="answer";
    public static final String REVIEW_DATE="review_date";
    public static final String BOX_NUMBER="box_number";
    public static final String TRUE_NUM="true_num";
    public static final String FALSE_NUM="false_num";
    public static final String REVIEW_NUM="review_num";
    public static final String LAST_ACCESS="last_access";
    public static final String CREATED_GROUP="created_group";
    public static final String ID="id";
    public static final String EDIT_GROUP_NAME_KEY="edit_group_name";
    public static final String SEND_GROUP_ACTIVITY="send_group_activity";
    public static final int NEW_CARD=0;
    public static final int BOX1=1;
    public static final int BOX2=2;
    public static final int BOX3=3;
    public static final int BOX4=4;
    public static final int BOX5=5;
    public static final int LEARNED=6;
    public static final int GET_ALL=7;
    public static final int REQUEST_CODE_FRONT=1001;
    public static final int REQUEST_CODE_BACK=1002;
    public static final String SEND_CARDS_ ="send_cards_to";
    public static String convertFormat(long duration){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration)
                ,TimeUnit.MILLISECONDS.toSeconds(duration)
                ,TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    public static String IMAGE_FOLDER="images";
    public static String VOICE_FOLDER="voices";


}

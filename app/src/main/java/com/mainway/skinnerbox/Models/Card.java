package com.mainway.skinnerbox.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="cards")
public class Card implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int groupId;
    private String frontText;
    private String backText;
    private String audioFront;
    private String audioBack;
    private String picFront;
    private String picBack;
    private String reviewDate;
    private String addedDate;
    private int box;
    private int trueNum;
    private int falseNum;
    private int reviewNum;
    private int isFavorite;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFrontText() {
        return frontText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public String getAudioFront() {
        return audioFront;
    }

    public void setAudioFront(String audioFront) {
        this.audioFront = audioFront;
    }

    public String getAudioBack() {
        return audioBack;
    }

    public void setAudioBack(String audioBack) {
        this.audioBack = audioBack;
    }

    public String getPicFront() {
        return picFront;
    }

    public void setPicFront(String picFront) {
        this.picFront = picFront;
    }

    public String getPicBack() {
        return picBack;
    }

    public void setPicBack(String picBack) {
        this.picBack = picBack;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getTrueNum() {
        return trueNum;
    }

    public void setTrueNum(int trueNum) {
        this.trueNum = trueNum;
    }

    public int getFalseNum() {
        return falseNum;
    }

    public void setFalseNum(int falseNum) {
        this.falseNum = falseNum;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.groupId);
        dest.writeString(this.frontText);
        dest.writeString(this.backText);
        dest.writeString(this.audioFront);
        dest.writeString(this.audioBack);
        dest.writeString(this.picFront);
        dest.writeString(this.picBack);
        dest.writeString(this.reviewDate);
        dest.writeString(this.addedDate);
        dest.writeInt(this.box);
        dest.writeInt(this.trueNum);
        dest.writeInt(this.falseNum);
        dest.writeInt(this.reviewNum);
        dest.writeInt(this.isFavorite);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.groupId = source.readInt();
        this.frontText = source.readString();
        this.backText = source.readString();
        this.audioFront = source.readString();
        this.audioBack = source.readString();
        this.picFront = source.readString();
        this.picBack = source.readString();
        this.reviewDate = source.readString();
        this.addedDate = source.readString();
        this.box = source.readInt();
        this.trueNum = source.readInt();
        this.falseNum = source.readInt();
        this.reviewNum = source.readInt();
        this.isFavorite = source.readInt();
    }

    public Card() {
    }

    protected Card(Parcel in) {
        this.id = in.readInt();
        this.groupId = in.readInt();
        this.frontText = in.readString();
        this.backText = in.readString();
        this.audioFront = in.readString();
        this.audioBack = in.readString();
        this.picFront = in.readString();
        this.picBack = in.readString();
        this.reviewDate = in.readString();
        this.addedDate = in.readString();
        this.box = in.readInt();
        this.trueNum = in.readInt();
        this.falseNum = in.readInt();
        this.reviewNum = in.readInt();
        this.isFavorite = in.readInt();
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}

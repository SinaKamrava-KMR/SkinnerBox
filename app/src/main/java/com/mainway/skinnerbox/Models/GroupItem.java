package com.mainway.skinnerbox.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Groups")
public class GroupItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "group_name")
    private String groupName;
    @ColumnInfo(name = "cards_num")
    private long cardsNum;
    @ColumnInfo(name = "review_date")
    private String reviewDate;
    @ColumnInfo(name = "create_date")
    private String createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getCardsNum() {
        return cardsNum;
    }

    public void setCardsNum(long cardsNum) {
        this.cardsNum = cardsNum;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.groupName);
        dest.writeLong(this.cardsNum);
        dest.writeString(this.reviewDate);
        dest.writeString(this.createDate);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.groupName = source.readString();
        this.cardsNum = source.readLong();
        this.reviewDate = source.readString();
        this.createDate = source.readString();
    }

    public GroupItem() {

    }

    protected GroupItem(Parcel in) {
        this.id = in.readInt();
        this.groupName = in.readString();
        this.cardsNum = in.readLong();
        this.reviewDate = in.readString();
        this.createDate = in.readString();
    }

    public static final Parcelable.Creator<GroupItem> CREATOR = new Parcelable.Creator<GroupItem>() {
        @Override
        public GroupItem createFromParcel(Parcel source) {
            return new GroupItem(source);
        }

        @Override
        public GroupItem[] newArray(int size) {
            return new GroupItem[size];
        }
    };
}

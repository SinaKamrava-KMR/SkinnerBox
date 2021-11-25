package com.mainway.skinnerbox.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mainway.skinnerbox.Models.GroupItem;

import java.util.List;

@Dao
public interface GroupItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long addGroup(GroupItem groupItem);

    @Delete
    int deleteGroup(GroupItem groupItem);

    @Update
    int updateGroup(GroupItem groupItem);

    @Query("DELETE  FROM groups")
     void deleteAllGroups();

    @Query("SELECT * FROM GROUPS")
    List<GroupItem> getGroups();

    @Query("SELECT * FROM GROUPS WHERE group_name LIKE '%'|| :query ||'%'")
    List<GroupItem> searchGroup(String query);

    @Query("SELECT id FROM GROUPS WHERE group_name=:groupName")
    int getGroupId(String groupName);

    @Query("SELECT * FROM GROUPS WHERE id =:groupId")
    GroupItem getGroupNameFromId(int groupId);





}

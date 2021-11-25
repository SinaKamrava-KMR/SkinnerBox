package com.mainway.skinnerbox.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mainway.skinnerbox.Models.Card;

import java.util.List;

@Dao
public interface CardsTackDao {
    @Insert
    long addCard(Card card);

    @Delete
    int delete(Card card);

    @Update
    int update(Card card);

    @Query("SELECT * FROM cards WHERE groupId=:groupId")
    List<Card> getGroupCards(int groupId);

    @Query("DELETE FROM cards WHERE groupId=:groupId ")
    void deleteGroupCards(int groupId);

    @Query("DELETE FROM CARDS")
    void deleteAllCards();

    @Query("SELECT * FROM CARDS WHERE frontText LIKE '%'||:query||'%' ")
    List<Card> search(String query);

    @Query("SELECT * FROM cards WHERE id=:cardId")
    Card getCard(int cardId);

    @Query("SELECT * FROM CARDS WHERE groupId=:groupId AND reviewDate=:reviewDate")
    List<Card> getReviewCards(int groupId,String reviewDate);

    @Query("SELECT * FROM CARDS WHERE groupId=:groupId AND box=:box")
    List<Card> getBoxCards(int groupId,int box);

    @Query("SELECT COUNT(id) FROM CARDS WHERE groupId=:groupId AND box=:box")
    long sizeBox(int groupId,int box);

    @Query("SELECT MAX(id) FROM CARDS")
    int lastId();

    @Query("SELECT * FROM CARDS WHERE groupId=:groupId AND isFavorite=1")
    List<Card> favoriteCards(int groupId);







}

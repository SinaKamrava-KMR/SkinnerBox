package com.mainway.skinnerbox.Manager;

import android.content.Context;
import android.util.Log;

import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.DataBase.AppDataBase;
import com.mainway.skinnerbox.DataBase.CardsTackDao;
import com.mainway.skinnerbox.DataBase.GroupItemDao;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.SharedPreference.AppSharedPreference;

import java.util.ArrayList;
import java.util.List;

import static com.mainway.skinnerbox.Models.Constants.LEARNED;
import static com.mainway.skinnerbox.Models.Constants.NEW_CARD;

public class ModelManager {


    private GroupItemDao groupItemDao;
    private CardsTackDao cardsTackDao;
    private AppDataBase appDataBase;
    private Context context;
    private AppSharedPreference sharedPreference;

    public  ModelManager(Context context){
        appDataBase=AppDataBase.getInstance(context);
        this.context = context;
        cardsTackDao=appDataBase.getCardsTackDao();
        groupItemDao=appDataBase.getGroupItemDao();
        sharedPreference=new AppSharedPreference(context);

    }

    public int lastId(){
        int result=(sharedPreference.getLastId()+1);
        Log.i("lastIdShare", "last Id : "+result);
        return result;
    }

    public boolean updateGroup(GroupItem groupItem){
        int result=groupItemDao.updateGroup(groupItem);
        if (result>0){
            return true;
        }else{
            return false;
        }
    }

    public List<Card> getCards(int groupId){
        return cardsTackDao.getGroupCards(groupId);
    }

    public List<Card> getBoxCards(int groupId,int box){
        if (box==0){
            List<Card> filterCards=new ArrayList<>();
            List<Card> cards=cardsTackDao.getBoxCards(groupId,box);
            for (int i = 0; i < cards.size(); i++) {

                if (cards.get(i).getAddedDate().equals(GetDate.date(context))){
                    filterCards.add(cards.get(i));
                }else {
                    if (cards.get(i).getBox()==0){
                        Card card=cards.get(i);
                        card.setBox(1);
                        cardsTackDao.update(card);
                    }
                }

            }

            return filterCards;
        }else {
            return cardsTackDao.getBoxCards(groupId,box);
        }
    }

    public int boxSize(int groupId,int box){
        return (int) cardsTackDao.sizeBox(groupId,box);
    }

    public List<Card> reviewCards(int groupId,String date){
        return cardsTackDao.getReviewCards(groupId,date);
    }

    public List<Card> reviewCardsToday(int groupId){
        List<Card> cardsForReview=new ArrayList<>();
        List<Card> allCards=cardsTackDao.getGroupCards(groupId);
        String[] todayDate=GetDate.date(context).split("-");
        for (int i = 0; i < allCards.size(); i++) {
            String date=allCards.get(i).getReviewDate();
            String[] dateList=date.split("-");

            if (Integer.parseInt(dateList[0])<=Integer.parseInt(todayDate[0]) ){

                if(Integer.parseInt(dateList[1])<Integer.parseInt(todayDate[1])){
                    if ((allCards.get(i).getBox()!=NEW_CARD)&&(allCards.get(i).getBox()!=LEARNED)){
                        cardsForReview.add(allCards.get(i));
                    }

                }else if(Integer.parseInt(dateList[1])==Integer.parseInt(todayDate[1])){
                    if(Integer.parseInt(dateList[2])<=Integer.parseInt(todayDate[2])){
                        if ((allCards.get(i).getBox()!=NEW_CARD)&&(allCards.get(i).getBox()!=LEARNED)){
                            cardsForReview.add(allCards.get(i));
                        }
                    }
                }
            }

        }
        return cardsForReview;
    }

    public boolean deleteCard(Card card){
        int result=cardsTackDao.delete(card);
        if (result>0){
            return true;
        }else{
            return false;
        }
    }

    public GroupItem getGroup(int groupId){
        return groupItemDao.getGroupNameFromId(groupId);
    }

    public GroupItem getGroup(String groupName){
        return getGroup(groupItemDao.getGroupId(groupName));
    }

    public int addCard(Card card){
        int result=(int) cardsTackDao.addCard(card);
        sharedPreference.setLastId(result);
        Log.i("lastIdShare", "addCard: "+result);
        return result;
    }
    public boolean update(Card card){
        if (cardsTackDao.update(card)>0){
            return true;
        }else{
            return false;
        }
    }
    public Card getCard(int cardId){
        return cardsTackDao.getCard(cardId);
    }

    public String firstDateForReview(List<Card> cards){
        String today=GetDate.date(context);
        List<String> dates=GetDate.dateList();
        String reviewDate="";
        for (int i = 0; i < dates.size(); i++) {
            for (int j = 0; j < cards.size(); j++) {
                if (cards.get(j).getReviewDate().equals(today)){
                    reviewDate=today;
                    Log.i("dateForReview", "firstDateForReview: "+reviewDate);
                    return reviewDate;

                }else if (cards.get(j).getReviewDate().equals(dates.get(i))){
                    reviewDate=dates.get(i);
                    Log.i("dateForReview", "firstDateForReview: "+reviewDate);
                    return reviewDate;
                }
            }
        }
        return reviewDate;
    }

    public List<Card> searchCard(String s){
        return  cardsTackDao.search(s);
    }

    public List<Card> favoriteCards(int groupId){
        return cardsTackDao.favoriteCards(groupId);
    }

   public void deleteCardsInGroup(int groupId){
        List<Card> cardList=getCards(groupId);
       for (int i = 0; i < cardList.size(); i++) {
           FileManager.deleteFilesInGroup(cardList.get(i));
           deleteCard(cardList.get(i));
       }
   }






}

package com.mainway.skinnerbox.Pages.Review;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.Fragment.Pages.EditCardToggleFragment;
import com.mainway.skinnerbox.Fragment.Pages.ShowAnswerFragment;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.Constants;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.mainway.skinnerbox.Models.Constants.BOX1;
import static com.mainway.skinnerbox.Models.Constants.BOX2;
import static com.mainway.skinnerbox.Models.Constants.BOX3;
import static com.mainway.skinnerbox.Models.Constants.BOX4;
import static com.mainway.skinnerbox.Models.Constants.BOX5;
import static com.mainway.skinnerbox.Models.Constants.LEARNED;

public class ReviewCards extends AppCompatActivity implements View.OnClickListener, ShowAnswerFragment.ResultAnswer {

    //======================================
    private ImageView backBtn;
    private ImageView favoriteBtn;
    private TextView  boxNumber;
    private TextView  falseNum;
    private TextView  reviewNum;
    private TextView  trueNum;
    private List<Card> reviewCards;
    private ImageView photo;
    private FrameLayout photoContainer;
    private RelativeLayout playerContainer;
    private ImageView playBtn;
    private LottieAnimationView animationPlayer;
    private TextView playerDuration;
    private TextView textFront;
    private TextView showAnswerBtn;
    private TextView skipBtn;
    private View divider;
    private int isFavorite;
    //======================================
    private GroupItem groupItem;
    private ModelManager manager;



    //======================================
    enum MusicState{
        PLAYING,STOPPED,PAUSED
    }
    private MediaPlayer mediaPlayerF;
    private MusicState musicStateF= MusicState.STOPPED;
    private Timer timerF;
    private String audioPath="";
    //======================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);
        Bundle bundle=getIntent().getExtras().getBundle("putArg");
        groupItem=bundle.getParcelable("putGroupItem");
        manager=new ModelManager(this);
        reviewCards=manager.reviewCardsToday(groupItem.getId());
        findById();
        init(0);
        setListener();

    }


    public void findById(){
        backBtn=findViewById(R.id.iv_Review_AnswerBack);
        favoriteBtn=findViewById(R.id.iv_Review_Answer_Favorite);
        boxNumber=findViewById(R.id.tv_ReviewPage_Answer_boxNumber);
        falseNum=findViewById(R.id.tv_Review_A_FalseNum);
        reviewNum=findViewById(R.id.tv_Review_A_ReviewNum);
        trueNum=findViewById(R.id.tv_Review_A_trueNum);
        photo=findViewById(R.id.iv_Review_Question_ImageQuestion);
        photoContainer=findViewById(R.id.frameLayout_Answer);
        playerContainer=findViewById(R.id.relativeLayout4);
        playBtn=findViewById(R.id.iv_editCard_playBtnFrontCard);
        animationPlayer=findViewById(R.id.animation_EditCard_Front);
        playerDuration=findViewById(R.id.tv_EditCard_durationPlayerFront);
        textFront=findViewById(R.id.tv_Review_A_TextAnswer);
        showAnswerBtn=findViewById(R.id.tv_Review_A_False);
        skipBtn=findViewById(R.id.tv_Review_Answer_TrueBtn);
        divider=findViewById(R.id.divider_Review_Q);
    }

    public void init(int index) {
        photoContainer.setVisibility(View.VISIBLE);
        playerContainer.setVisibility(View.VISIBLE);
        textFront.setVisibility(View.VISIBLE);
        if (!reviewCards.isEmpty()) {
            if (timerF != null) {
                timerF.cancel();
            }
            if (mediaPlayerF != null) {
                mediaPlayerF.release();
                mediaPlayerF = null;
            }
            //======================================
            isFavorite = reviewCards.get(index).getIsFavorite();
            if (isFavorite == 1) {
                favoriteBtn.setImageDrawable(getDrawable(R.drawable.favorite));
            } else {
                favoriteBtn.setImageDrawable(getDrawable(R.drawable.heart));
            }
            //======================================
            boxNumber.setText("box " + reviewCards.get(index).getBox());
            falseNum.setText(String.valueOf(reviewCards.get(index).getFalseNum()));
            trueNum.setText(String.valueOf(reviewCards.get(index).getTrueNum()));
            reviewNum.setText(String.valueOf(reviewCards.get(index).getReviewNum()));
            if (reviewCards.get(index).getPicFront().length() > 0) {
                photo.setImageURI(Uri.parse(reviewCards.get(0).getPicFront()));
            } else {
                photoContainer.setVisibility(View.GONE);
            }
            Log.i("playerCont", "init: " + (reviewCards.get(index).getAudioFront().length() > 0));
            if (reviewCards.get(index).getAudioFront().length() > 0) {

                audioPath = reviewCards.get(index).getAudioFront();
            } else {
                playerContainer.setVisibility(View.GONE);
            }
            textFront.setText(reviewCards.get(index).getFrontText());
        }else{
            onBackPressed();
        }

    }
    public void setListener(){
        backBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        showAnswerBtn.setOnClickListener(this);
        skipBtn.setOnClickListener(this);
    }
    public void playAudio(String path){


        if (mediaPlayerF==null){
            musicStateF = MusicState.STOPPED;

            mediaPlayerF = MediaPlayer.create(this, Uri.parse(path));
        }

        if(musicStateF== MusicState.PLAYING){

            musicStateF = MusicState.PAUSED;
            animationPlayer.pauseAnimation();
            playBtn.setImageResource(R.drawable.ic_play_standard);
            mediaPlayerF.pause();


        }else {

            if (mediaPlayerF!=null){
                playBtn.setImageResource(R.drawable.ic_paus_standard);
                animationPlayer.playAnimation();
                musicStateF = MusicState.PLAYING;
                mediaPlayerF.start();
                timerF=new Timer();
                timerF.schedule(new TimerTask() {
                    @Override
                    public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayerF!=null){
                                        playerDuration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                                    }
                                }
                            });


                    }
                },100,100);
                mediaPlayerF.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mediaPlayerF != null) {
                            playBtn.setImageResource(R.drawable.ic_play_standard);
                            animationPlayer.pauseAnimation();
                            musicStateF = MusicState.STOPPED;
                            playerDuration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                            mediaPlayerF.release();
                            mediaPlayerF= null;

                        }
                        timerF.cancel();
                    }
                });

            }
        }







    }
    public void onClickShowAnswer(){
        playerContainer.setVisibility(View.GONE);
        photoContainer.setVisibility(View.GONE);
        textFront.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.FrameForReviewPage,new ShowAnswerFragment(reviewCards.get(0)));
        fragmentTransaction.commit();
    }
    public void onClickSkip(Card newCard){
        newCard.setIsFavorite(isFavorite);
        newCard.setReviewNum(newCard.getReviewNum()+1);
        newCard.setReviewDate(GetDate.getTomorrow());
        manager.update(newCard);
        removeItemReview(newCard);
    }

    //==========================
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iv_Review_AnswerBack:
                onBackPressed();
                break;
            case R.id.iv_Review_Answer_Favorite:
                if (isFavorite==1){
                    isFavorite=0;
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.heart));
                }else{
                    isFavorite=1;
                    favoriteBtn.setImageDrawable(getDrawable(R.drawable.favorite));
                }
                break;
            case R.id.tv_Review_A_False:
                if (!reviewCards.isEmpty()){
                    onClickShowAnswer();
                }
                break;
            case R.id.tv_Review_Answer_TrueBtn:
                if (!reviewCards.isEmpty()){
                    onClickSkip(reviewCards.get(0));
                }else{
                    onBackPressed();
                }
                break;
            case R.id.iv_editCard_playBtnFrontCard:
                if (audioPath.length()>0){
                    playAudio(audioPath);
                }
                break;
        }
    }

    //==========================
    @Override
    public void onClickTrue(Card newCard) {

        newCard.setIsFavorite(isFavorite);
        newCard.setReviewNum(newCard.getReviewNum()+1);
        newCard.setTrueNum(newCard.getTrueNum()+1);
        int box=newCard.getBox();
        switch (box){
            case BOX1:
                newCard.setBox(BOX2);
                newCard.setReviewDate(GetDate.getNextDay(2));
                break;
            case BOX2:
                newCard.setBox(BOX3);
                newCard.setReviewDate(GetDate.getNextDay(4));
                break;
            case BOX3:
                newCard.setBox(BOX4);
                newCard.setReviewDate(GetDate.getNextDay(9));
                break;
            case BOX4:
                newCard.setBox(BOX5);
                newCard.setReviewDate(GetDate.getNextDay(14));
                break;
            case BOX5:
                newCard.setBox(LEARNED);
                break;

        }
        manager.update(newCard);
        //======================
        removeItemReview(newCard);
        //=====================

    }

    @Override
    public void onClickFalse(Card newCard) {
        newCard.setIsFavorite(isFavorite);
        newCard.setReviewNum(newCard.getReviewNum()+1);
        newCard.setFalseNum(newCard.getFalseNum()+1);
        newCard.setBox(BOX1);
        newCard.setReviewDate(GetDate.getTomorrow());
        manager.update(newCard);
        //======================
        removeItemReview(newCard);
        //=====================

    }
    //==========================

    public  void  removeItemReview(Card card){
        for (int i = 0; i < reviewCards.size(); i++) {
            if (reviewCards.get(i).getId()==card.getId()){
                reviewCards.remove(i);
            }
        }
        init(0);

    }
}
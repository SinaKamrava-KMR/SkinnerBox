package com.mainway.skinnerbox.Fragment.Pages;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.Constants;
import com.mainway.skinnerbox.Pages.Review.ReviewCards;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class ShowAnswerFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private Card card;
    private ResultAnswer resultAnswer;


    //=============================
    enum MusicState{
        PLAYING,STOPPED,PAUSED
    }
    private MediaPlayer mediaPlayerF;
    private MusicState musicStateF= MusicState.STOPPED;
    private Timer timerF;
    private String audioPath="";
    //=============================
    private ImageView photo;
    private MaterialCardView photoContainer;
    private RelativeLayout playerContainer;
    private ImageView playBtn;
    private LottieAnimationView animationPlayer;
    private TextView playerDuration;
    private TextView textBack;
    private TextView trueBtn;
    private TextView falseBtn;
    //=============================

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;
        super.onAttach(context);
        this.resultAnswer= (ResultAnswer) context;
    }

    public ShowAnswerFragment(Card card){
        this.card = card;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_answer_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findById(view);
        init();
        setListener();
    }

    //================================
    public void findById(View v){
        photo=v.findViewById(R.id.iv_Review_Question_ImageAnswer_Back);
        photoContainer=v.findViewById(R.id.frameLayout_Answer_Back);
        playerContainer=v.findViewById(R.id.relativeLayout4_Answer);
        playBtn=v.findViewById(R.id.iv_editCard_playBtnBackCard);
        animationPlayer=v.findViewById(R.id.animation_EditCard_Back);
        playerDuration=v.findViewById(R.id.tv_EditCard_durationPlayerBack);
        textBack=v.findViewById(R.id.tv_Review_A_TextBack);
        trueBtn=v.findViewById(R.id.tv_Review_Answer_TrueBtn_Back);
        falseBtn=v.findViewById(R.id.tv_Review_A_False_Back);
    }
    public void init(){


        if (card.getPicBack().length()>0){
            photo.setImageURI(Uri.parse(card.getPicBack()));
        }else{
            photoContainer.setVisibility(View.GONE);
        }
        if (card.getAudioBack().length()>0){
            audioPath=card.getAudioBack();
        }else{
            playerContainer.setVisibility(View.GONE);
        }
        textBack.setText(card.getBackText());

    }
    public void setListener(){
        playBtn.setOnClickListener(this);
        trueBtn.setOnClickListener(this);
        falseBtn.setOnClickListener(this);
    }
    public void playAudio(String path){


        if (mediaPlayerF==null){
            musicStateF = MusicState.STOPPED;

            mediaPlayerF = MediaPlayer.create(context, Uri.parse(path));
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
                    if (getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayerF!=null){
                                    playerDuration.setText(Constants.convertFormat(mediaPlayerF.getCurrentPosition()));
                                }
                            }
                        });
                    }


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
    public void goToNextPage(){
        Fragment fragment=getParentFragmentManager().findFragmentById(R.id.FrameForReviewPage);
        Log.i("fragmentTran", "onClickTrue: "+(fragment instanceof ShowAnswerFragment));
        if (fragment instanceof ShowAnswerFragment){
            FragmentTransaction transaction=getParentFragmentManager().beginTransaction();
            transaction.remove(fragment);
            transaction.commit();

        }
    }
    //================================
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iv_editCard_playBtnBackCard:
                if (audioPath.length()>0){
                    playAudio(audioPath);
                }
                break;
            case R.id.tv_Review_Answer_TrueBtn_Back:
                resultAnswer.onClickTrue(card);
                goToNextPage();
                break;
            case R.id.tv_Review_A_False_Back:
                resultAnswer.onClickFalse(card);
                goToNextPage();
                break;
        }
    }
    //===============================
    public interface ResultAnswer{
        void onClickTrue(Card card);
        void onClickFalse(Card card);
    }
    //================================
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerF!=null){
            timerF.cancel();
        }
        if (mediaPlayerF!=null){
            mediaPlayerF.release();
            mediaPlayerF=null;
        }
    }
}


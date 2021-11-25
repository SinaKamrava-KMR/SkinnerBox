package com.mainway.skinnerbox.Pages.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.mainway.skinnerbox.Access.Date.GetDate;
import com.mainway.skinnerbox.Access.Export.ExportFile;
import com.mainway.skinnerbox.Access.Import.ImportFile;
import com.mainway.skinnerbox.Access.PopUp.CustomPopUp;
import com.mainway.skinnerbox.Fragment.BottomSheets.ShowFilesBottomSheet;
import com.mainway.skinnerbox.Fragment.Dialogs.DeleteCardDialog;
import com.mainway.skinnerbox.Fragment.Dialogs.ExportFileDialog;
import com.mainway.skinnerbox.Fragment.Dialogs.ImportDialog;
import com.mainway.skinnerbox.Fragment.Dialogs.StatisticsDialog;
import com.mainway.skinnerbox.Fragment.Pages.ViewCardsFragment;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.Pages.AddCard.AddCard;
import com.mainway.skinnerbox.Pages.Main.MainActivity;
import com.mainway.skinnerbox.Pages.Review.ReviewCards;
import com.mainway.skinnerbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.mainway.skinnerbox.Models.Constants.SEND_GROUP_ACTIVITY;

public class GroupPage extends AppCompatActivity implements MoreResult, ShowFilesBottomSheet.CompletedImport,
        DeleteCardDialog.DeleteResult {
    //-----------------------Field in Group Page-----------------
    private TextView progressValue;
    private LinearProgressIndicator progressIndicator;
    private TextView cardToReview;
    private TextView startReviewBtn;
    private TextView todayDate;
    private TextView createGroupDate;
    private TextView newCardsNum;
    private TextView memorizedCards;
    private TextView cardsNum;
    private TextView statisticsBTN;
    private TextView viewCardsBTN;
    private TextView box1;
    private TextView box2;
    private TextView box3;
    private TextView box4;
    private TextView box5;
    private TextView groupName;
    private ImageView back;
    private ImageView moreOptions;
    private ExtendedFloatingActionButton fabAddNewCard;
    //-------------------------------------------------------------

    private GroupItem groupItem;
    private ModelManager modelManager;
    private List<Card> cards=new ArrayList<>();
    private String today;
    private List<Card> cardsForReview=new ArrayList<>();
    private List<Card> cardsForNewAdded=new ArrayList<>();
    private List<Card> cardsForMemorized=new ArrayList<>();
    private ImportFile importFile;


    //-------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page_layout);
        modelManager=new ModelManager(this);

        groupItem=getIntent().getExtras().getParcelable(SEND_GROUP_ACTIVITY);
        findIds();
        init();
        onClickBtn();


    }

    public void findIds(){

        back=findViewById(R.id.iv_backIc_GroupPage);
        moreOptions=findViewById(R.id.iv_MoreOptions_GroupPage);
        groupName=findViewById(R.id.tv_GroupMainPage_GroupName);
        progressValue=findViewById(R.id.tv_GroupPage_ProgressPercent);
        progressIndicator=findViewById(R.id.progress_show_percent);
        cardToReview=findViewById(R.id.tv_GroupPage_CardsToReviewNum);
        startReviewBtn=findViewById(R.id.TV_ReviewStartBTN);
        todayDate=findViewById(R.id.tv_groupMainPage_todayDate);
        createGroupDate=findViewById(R.id.tv_GroupMainPage_CreateGroupDate);
        newCardsNum=findViewById(R.id.tv_GroupMainPage_NewCards);
        memorizedCards=findViewById(R.id.tv_GroupMainPage_MemorizedCardsNum);
        cardsNum=findViewById(R.id.tv_GroupMainPage_CardsNum);
        statisticsBTN=findViewById(R.id.tv_GroupPage_statistics);
        viewCardsBTN=findViewById(R.id.tv_GroupPage_ViewAllCards);
        box1=findViewById(R.id.tv_group_page_box1);
        box2=findViewById(R.id.tv_group_page_box2);
        box3=findViewById(R.id.tv_group_page_box3);
        box4=findViewById(R.id.tv_group_page_box4);
        box5=findViewById(R.id.tv_group_page_box5);

        fabAddNewCard=findViewById(R.id.fab_GroupPageLayout_addNewCard);

    }

    public void init(){


        //==============================

        cards=modelManager.getCards(groupItem.getId());
        cardsForNewAdded=modelManager.getBoxCards(groupItem.getId(),0);
        cardsForMemorized=modelManager.getBoxCards(groupItem.getId(),6);
        cardsForReview=modelManager.reviewCardsToday(groupItem.getId());
        Log.i("initCall", "init: "+cardsForReview.size());
        //==============================

        if (groupItem.getCardsNum()!=cards.size()){
            GroupItem updateGroup=groupItem;
            updateGroup.setCardsNum(cards.size());
            updateGroup.setReviewDate(modelManager.firstDateForReview(cards));
            modelManager.updateGroup(updateGroup);
        }

        if (cardsForReview.size()!=0){
            GroupItem updateGroup=groupItem;
            updateGroup.setCardsNum(cards.size());
            updateGroup.setReviewDate(GetDate.date(this));
            modelManager.updateGroup(updateGroup);
        }else {
            GroupItem updateGroup=groupItem;
            updateGroup.setCardsNum(cards.size());
            updateGroup.setReviewDate(modelManager.firstDateForReview(cards));
            modelManager.updateGroup(updateGroup);
        }
        //==============================
        today=GetDate.date(this);
        todayDate.setText(today);
        progressValue.setText(progressResult()+"%");
        progressIndicator.setProgressCompat(progressResult(),true);
        groupName.setText(groupItem.getGroupName().replace("_"," "));
        createGroupDate.setText(groupItem.getCreateDate());
        cardsNum.setText(String.valueOf(cards.size()));
        newCardsNum.setText(String.valueOf(cardsForNewAdded.size()));
        memorizedCards.setText(String.valueOf(cardsForMemorized.size()));
        cardToReview.setText(String.valueOf(cardsForReview.size()));
        box1.setText(String.valueOf(modelManager.boxSize(groupItem.getId(),1)));
        box2.setText(String.valueOf(modelManager.boxSize(groupItem.getId(),2)));
        box3.setText(String.valueOf(modelManager.boxSize(groupItem.getId(),3)));
        box4.setText(String.valueOf(modelManager.boxSize(groupItem.getId(),4)));
        box5.setText(String.valueOf(modelManager.boxSize(groupItem.getId(),5)));

    }

    //======================
    public void onClickBtn(){
        startReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!cardsForReview.isEmpty()){
                    Bundle args=new Bundle();
                    args.putParcelable("putGroupItem",groupItem);
                    Intent intent =new Intent(GroupPage.this, ReviewCards.class);
                    intent.putExtra("putArg",args);
                    startActivity(intent);
                }else{
                  Toast.makeText(GroupPage.this, getResources().getString(R.string.youNotReview), Toast.LENGTH_SHORT).show();

              }
            }
        });

        statisticsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsDialog dialog=new StatisticsDialog(GroupPage.this,groupItem.getId());
                dialog.show(getSupportFragmentManager(),null);

            }
        });

        viewCardsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cards.size()!=0){
                  Bundle arg=new Bundle();
                  arg.putParcelable("GetGroupItem",groupItem);
                  Intent intent=new Intent(GroupPage.this,ViewCardsFragment.class);
                  intent.putExtra("getGroupBundle",arg);
                  startActivity(intent);
                }else{
                    Toast.makeText(GroupPage.this, getResources().getString(R.string.YouMustAddCard), Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopUp.popUpMoreGroup(v,GroupPage.this,getSupportFragmentManager());
            }
        });

        fabAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GroupPage.this, AddCard.class);
                intent.putExtra("CardId",String.valueOf(modelManager.lastId()));
                intent.putExtra("GroupName",groupItem.getGroupName());
                startActivity(intent);
            }
        });
    }
    //======================
    public int progressResult(){
        float sum=cardsForMemorized.size();
        float all=cards.size();
        float result= sum==0?0:(sum/all)*100;
        Log.i("result", "result : "+(int)result);
        return (int)result;
    }
    //======================

    @Override
    public void onClickDeleteAll() {
        DeleteCardDialog dialog=new DeleteCardDialog(this,groupItem.getId());
        dialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onClickImportFile() {
        ImportDialog dialog=new ImportDialog(this,groupItem.getId());
        dialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onClickExportFile() {
        ExportFileDialog dialog=new ExportFileDialog(groupItem.getId(),this);
        dialog.show(getSupportFragmentManager(),null);
    }
    //======================
    @Override
    protected void onRestart() {
        init();
        super.onRestart();


    }

    @Override
    public void onCompleted(File file) {
        importFile=new ImportFile(this,groupItem.getId());
        if (importFile.importFile(file.getPath())){
            init();
        }else {
            Toast.makeText(this, "can not import this file ", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClickDelete() {
        init();
    }
}
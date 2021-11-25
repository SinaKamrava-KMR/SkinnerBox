package com.mainway.skinnerbox.Fragment.Pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mainway.skinnerbox.Access.PopUp.CustomPopUp;
import com.mainway.skinnerbox.Manager.FileManager;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.Pages.AddCard.EditCard;
import com.mainway.skinnerbox.Pages.AddCard.ResultCategoryPopUp;
import com.mainway.skinnerbox.Pages.Group.SearchAdapter;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewCardsFragment extends AppCompatActivity implements SearchAdapter.CallBackFromAdapter , View.OnClickListener
  , ResultCategoryPopUp {
    //===========================================================
    private ImageView backBTN;
    private EditText etSearch;
    private TextView filterCategory;
    private ImageView icFilter;
    //=========================================================
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private GroupItem groupItem;
    private ModelManager manager;
    private List<Card> allCards;
    //=========================================================

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_cards);
        Bundle bundle=getIntent().getExtras().getParcelable("getGroupBundle");
        this.groupItem=bundle.getParcelable("GetGroupItem");
        manager=new ModelManager(this);
        adapter=new SearchAdapter(this,ViewCardsFragment.this);
        findById();
        init();
        setListener();
    }

    public void findById(){

        backBTN=findViewById(R.id.iv_ViewCards_BackBtn);
        etSearch=findViewById(R.id.et_ViewCards_search);
        filterCategory=findViewById(R.id.tv_filterCategoryText);
        icFilter=findViewById(R.id.iv_more_ViewCards_filterMore);
        recyclerView=findViewById(R.id.RecyclerView_ViewCards);
        LinearLayoutManager manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
    }

    public void init(){
        filterCategory.setText(getResources().getString(R.string.viewAll));
        allCards=manager.getCards(groupItem.getId());
        adapter.setCards(allCards);
        recyclerView.setAdapter(adapter);
    }

    public void setListener(){
        backBTN.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    adapter.setCards(manager.searchCard(s.toString()));
                    filterCategory.setText(getResources().getString(R.string.viewAll));
                }else {
                    adapter.setCards(manager.getCards(groupItem.getId()));
                    filterCategory.setText(getResources().getString(R.string.viewAll));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        icFilter.setOnClickListener(this);
        filterCategory.setOnClickListener(this);
    }

    @Override
    public void onClickItem(Card card) {
        Bundle arg=new Bundle();
        arg.putParcelable("GetArg",card);
        Intent intent=new Intent(this, EditCard.class);
        intent.putExtra("GetBundle",arg);
        startActivity(intent);

    }

    @Override
    public void onClickDelete(Card card) {
        if (manager.deleteCard(card)){
            adapter.deleteCard(card);
            FileManager.deleteFilesInGroup(card);

        }else {
            Toast.makeText(this,getResources().getString(R.string.canNotDeleteCard), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iv_ViewCards_BackBtn:
                onBackPressed();
                break;
            case R.id.iv_more_ViewCards_filterMore:
            case R.id.tv_filterCategoryText:
                CustomPopUp.popUpShowCategory(this,v,this);
                break;
        }
    }

    @Override
    public void onFavorite() {
        adapter.setCards(manager.favoriteCards(groupItem.getId()));
        filterCategory.setText(getResources().getString(R.string.favorite));

    }

    @Override
    public void onNewCards() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),0));
        filterCategory.setText(getResources().getString(R.string.newCardsAdded));
    }

    @Override
    public void onMemorized() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),6));
        filterCategory.setText(getResources().getString(R.string.memorized));
    }

    @Override
    public void onViewAll() {
        adapter.setCards(manager.getCards(groupItem.getId()));
        filterCategory.setText(getResources().getString(R.string.viewAll));
    }

    @Override
    public void onBox1() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),1));
        filterCategory.setText("box 1");
    }

    @Override
    public void onBox2() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),2));
        filterCategory.setText("box 2");
    }

    @Override
    public void onBox3() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),3));
        filterCategory.setText("box 3");
    }

    @Override
    public void onBox4() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),4));
        filterCategory.setText("box 4");
    }

    @Override
    public void onBox5() {
        adapter.setCards(manager.getBoxCards(groupItem.getId(),5));
        filterCategory.setText("box 5");
    }

    //============================================
    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }
    //============================================
}

package com.mainway.skinnerbox.Pages.Main;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.UriCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mainway.skinnerbox.Access.Export.ExportFile;
import com.mainway.skinnerbox.Access.PopUp.CustomPopUp;
import com.mainway.skinnerbox.Access.Restart.RestartActivity;
import com.mainway.skinnerbox.DataBase.AppDataBase;
import com.mainway.skinnerbox.DataBase.GroupItemDao;
import com.mainway.skinnerbox.Fragment.Dialogs.AddGroupDialog;
import com.mainway.skinnerbox.Fragment.Dialogs.EditGroupDialog;
import com.mainway.skinnerbox.Manager.ModelManager;
import com.mainway.skinnerbox.Models.Constants;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.Pages.Group.GroupPage;
import com.mainway.skinnerbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GroupAdapter.AdapterCallBack,
        AddGroupDialog.CallBackDialogResult, EditGroupDialog.CallBackEditDialogResult {


        private ImageView searchGroup;
        private ImageView moreOptions;
        private ExtendedFloatingActionButton addGroupBtn;
        private RecyclerView groupRecyclerView;
        private GroupAdapter adapter;
        private LinearLayoutManager layoutManager;
        private TextView titleApp;
        private EditText searchEt;
        private ImageView searchBack;
        private GroupItemDao groupItemDao;
        private ModelManager manager;
        private ExportFile exportFile;
        private List<GroupItem> itemList=new ArrayList<>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            groupItemDao=AppDataBase.getInstance(this).getGroupItemDao();
            itemList=groupItemDao.getGroups();
            manager=new ModelManager(this);
            init();
            onClickAddGroup();
            searchGroup();
            clickPageBtn();


        }


   //--------------------------Methods for this page------------------------------------------------


        public void init(){
            titleApp=findViewById(R.id.tv_titleApp);
            searchBack=findViewById(R.id.iv_search_et_back_main);
            searchGroup=findViewById(R.id.iv_main_search_group);
            searchEt=findViewById(R.id.editText_searchEt_Main);
            moreOptions=findViewById(R.id.iv_main_more);
            addGroupBtn=findViewById(R.id.fab_main_add_new_Group);
            groupRecyclerView=findViewById(R.id.rv_main);
            layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
            groupRecyclerView.setLayoutManager(layoutManager);
            adapter=new GroupAdapter(this);
            groupRecyclerView.setAdapter(adapter);
            adapter.setGroups(groupItemDao.getGroups());
            if (itemList.isEmpty()){
                searchGroup.setVisibility(View.INVISIBLE);
            }else {
                searchGroup.setVisibility(View.VISIBLE);
            }
        }
        public void onClickAddGroup(){
            addGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddGroupDialog dialog=new AddGroupDialog();
                    dialog.show(getSupportFragmentManager(),null);
                }
            });
        }
        public void searchGroup(){
            searchEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()>0){

                        List<GroupItem> list=groupItemDao.searchGroup(s.toString().replace(" ","_"));

                        if(list.size()==0){
                            GroupItem groupItem=new GroupItem();
                            groupItem.setCreateDate("404");
                            list.add(groupItem);

                        }
                        adapter.setGroups(list);


                    }else {
                        adapter.setGroups(groupItemDao.getGroups());
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        public void clickPageBtn(){
            searchGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (titleApp.getVisibility()==View.VISIBLE) {
                        titleApp.setVisibility(View.GONE);
                        searchEt.setVisibility(View.VISIBLE);
                        searchGroup.setVisibility(View.GONE);
                        moreOptions.setVisibility(View.GONE);
                        searchBack.setVisibility(View.VISIBLE);
                        addGroupBtn.setVisibility(View.GONE);

                    }
                }
            });
            searchBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,MainActivity.class));

                }
            });
            moreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomPopUp.popUpMoreMain(v,MainActivity.this,getSupportFragmentManager());

                }
            });


        }

 //-------------------------------------Adapter Call Back Methods-----------------------------------
    @Override
    public void onClickDelete(GroupItem groupItem) {
        if (groupItemDao.deleteGroup(groupItem)>0){
            adapter.deleteGroup(groupItem);
            manager.deleteCardsInGroup(groupItem.getId());
            //---------------
            if (groupItemDao.getGroups().isEmpty()) searchGroup.setVisibility(View.INVISIBLE);
            //--------------
        }else {
            Toast.makeText(this, "this group can not to remove", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickEdit(GroupItem groupItem) {
            Bundle bundle=new Bundle();
            bundle.putParcelable(Constants.EDIT_GROUP_NAME_KEY,groupItem);
            EditGroupDialog dialog=new EditGroupDialog();
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(),null);

    }

    @Override
    public void onClickItemView(GroupItem groupItem) {
        if(groupItem!=null){
            Bundle args=new Bundle();
            args.putParcelable(Constants.SEND_GROUP_ACTIVITY,groupItem);
            Intent intent=new Intent(MainActivity.this, GroupPage.class);
            intent.putExtras(args);
            startActivity(intent);

        }
    }

    @Override
    public void onClickShare(GroupItem groupItem) {
        if(groupItem!=null){
            Bundle args=new Bundle();
            args.putParcelable(Constants.SEND_GROUP_ACTIVITY,groupItem);
            Intent intent=new Intent(MainActivity.this, GroupPage.class);
            intent.putExtras(args);
            startActivity(intent);

        }
    }


    //------------------------------call Back Result From Add Group Dialog And Edit That--------------------------

    @Override
    public void onAddGroupName(GroupItem groupItem) {
            if (groupItemDao.getGroupId(groupItem.getGroupName())==0){

                if (groupItemDao.addGroup(groupItem)>0){
                    groupItem.setId(groupItemDao.getGroupId(groupItem.getGroupName()));
                    adapter.addGroup(groupItem);
                    groupRecyclerView.smoothScrollToPosition(0);
                    //---------------
                    if (searchGroup.getVisibility()==View.INVISIBLE) searchGroup.setVisibility(View.VISIBLE);
                    //--------------
                    Toast.makeText(this, getResources().getString(R.string.ToastForAddGroup), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.ErrorToastForAddGroup), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, getResources().getString(R.string.preBuild), Toast.LENGTH_SHORT).show();
            }





    }

    @Override
    public void onEditGroupName(GroupItem groupItem) {
        if (groupItemDao.updateGroup(groupItem)>0){
            adapter.updateGroup(groupItem);
        }
    }

    //----------------------------------------------------------------------------------------------





    @Override
    protected void onRestart() {
        if (titleApp.getVisibility()!=View.VISIBLE) {
            titleApp.setVisibility(View.VISIBLE);
            searchEt.setVisibility(View.GONE);
            searchGroup.setVisibility(View.VISIBLE);
            moreOptions.setVisibility(View.VISIBLE);
            searchBack.setVisibility(View.GONE);
            addGroupBtn.setVisibility(View.VISIBLE);
        }
            init();
        super.onRestart();
    }
}



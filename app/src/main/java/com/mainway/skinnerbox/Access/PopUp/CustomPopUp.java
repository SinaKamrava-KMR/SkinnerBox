package com.mainway.skinnerbox.Access.PopUp;

import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mainway.skinnerbox.Fragment.BottomSheets.GroupSettings;
import com.mainway.skinnerbox.Fragment.BottomSheets.MainSettings;
import com.mainway.skinnerbox.Fragment.Dialogs.DialogShowAbout;
import com.mainway.skinnerbox.Fragment.Dialogs.ImportDialog;
import com.mainway.skinnerbox.Fragment.Pages.GuideFragment;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.Pages.AddCard.CallBackFromPopUp;
import com.mainway.skinnerbox.Pages.AddCard.CallBackPic;
import com.mainway.skinnerbox.Pages.AddCard.ResultCategoryPopUp;
import com.mainway.skinnerbox.Pages.Group.MoreResult;
import com.mainway.skinnerbox.Pages.Main.GroupAdapter;
import com.mainway.skinnerbox.R;

import java.io.File;

public class CustomPopUp {
    public static void popUpMoreMain(View v, Context context, FragmentManager manager){

        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_main, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.menu_item_help_main:
                        FragmentTransaction ft=manager.beginTransaction();
                        ft.replace(R.id.frame_fragment_Show,new GuideFragment());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case R.id.menu_item_about_main:
                        DialogShowAbout dialogShowAbout=new DialogShowAbout(context.getString(R.string.my_info));
                        dialogShowAbout.show(manager,null);
                        break;
                }
                return false;
            }
        });
    }

    public static void popUpMoreGroup(View v, Context context, FragmentManager manager){

        MoreResult moreResult= (MoreResult) context;
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_group, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.menu_item_Import_Group:
                        moreResult.onClickImportFile();
                        break;
                    case R.id.menu_item_Export_Group:
                        moreResult.onClickExportFile();
                        break;
                    case R.id.menu_item_DeleteAllCards_Group:
                        moreResult.onClickDeleteAll();
                        break;
                }
                return false;
            }
        });
    }

    public static void showPopupGroupItem(View v, GroupItem groupItem, Context context, GroupAdapter.AdapterCallBack adapterCallBack) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_group_item, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.menu_item_Edit_GroupItem:
                        adapterCallBack.onClickEdit(groupItem);
                        break;
                    case R.id.menu_item_delete_GroupItem:
                        adapterCallBack.onClickDelete(groupItem);
                        break;
                }
                return false;
            }
        });

    }

    public static void popUpVP(Context context, View v, CallBackFromPopUp callBack, String status){
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.voice_more, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.menu_item_delete_Voice:
                        callBack.onClickDelete(status);

                        break;
                    case R.id.menu_item_replace_Voice:
                        callBack.onClickReplace(status);
                        break;

                }
                return false;
            }
        });

    }
    public static void popUpShowCategory(Context context, View v, ResultCategoryPopUp callBack){
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.category_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.menu_item_ViewAll:
                        callBack.onViewAll();
                        break;
                    case R.id.menu_item_Favorite:
                        callBack.onFavorite();
                        break;
                    case R.id.menu_item_newCards:
                        callBack.onNewCards();
                        break;
                    case R.id.menu_item_Memorized:
                        callBack.onMemorized();
                        break;
                    case R.id.menu_item_Box1:
                        callBack.onBox1();
                        break;
                    case R.id.menu_item_box2:
                        callBack.onBox2();
                        break;
                    case R.id.menu_item_box3:
                        callBack.onBox3();
                        break;
                    case R.id.menu_item_box4:
                        callBack.onBox4();
                        break;
                    case R.id.menu_item_box5:
                        callBack.onBox5();
                        break;

                }
                return false;
            }
        });

    }

}

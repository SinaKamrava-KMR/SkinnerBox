package com.mainway.skinnerbox.Pages.Main;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mainway.skinnerbox.Access.PopUp.CustomPopUp;
import com.mainway.skinnerbox.Models.GroupItem;
import com.mainway.skinnerbox.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;


public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GroupItem> groupItems=new ArrayList<>();
    private Context context;
    private static final String TAG = "adp";
    private AdapterCallBack adapterCallBack;


    public GroupAdapter(Context context){
        this.context = context;
        this.adapterCallBack= (AdapterCallBack) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view ;
        switch (viewType){
            case 0:

                view = LayoutInflater.from(context).inflate(R.layout.activity_main_add_group_when_empty,parent,false);
                return new EmptyViewHolder(view);
            case 1:
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.item_show_group,parent,false);
                return new ShowGroupViewHolder(view);

            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (groupItems.isEmpty()){
            return 0;
        }else if (groupItems.size()-1==position){
            return 2;
        }else {
            return 1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 1:
            case 2:
                ShowGroupViewHolder viewHolder= (ShowGroupViewHolder) holder;
                viewHolder.bindHolder(groupItems.get(position),position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return groupItems.isEmpty()?1: groupItems.size();
    }

    public void addGroup(GroupItem group){
        this.groupItems.add(0,group);
        Log.i(TAG, "addGroup: "+group.getGroupName());
        notifyDataSetChanged();
    }
    public void setGroups(List<GroupItem> groups){
        this.groupItems=groups;
        Log.i(TAG, "setGroups: ");
        notifyDataSetChanged();
    }
    public void deleteGroup(GroupItem group){
        for (int i = 0; i < groupItems.size(); i++) {
            Log.i(TAG, groupItems.get(i).getGroupName()+" id : "+groupItems.get(i).getId() );
            if (groupItems.get(i).getId()==group.getId()){
                groupItems.remove(i);
                Log.i(TAG, "deleteGroup: "+group.getGroupName());
                notifyItemRemoved(i);
                break;
            }
        }

    }
    public void updateGroup(GroupItem group){
        for (int i = 0; i < groupItems.size(); i++) {
            if (groupItems.get(i).getId()==group.getId()){
                groupItems.set(i,group);
                notifyItemChanged(i);
                break;
            }
        }
    }
    public  int getGroupId(){
        int id=groupItems.size();
        return id;
    }


    public class ShowGroupViewHolder extends RecyclerView.ViewHolder{
        private TextView showGroupName;
        private TextView reviewDate;
        private TextView cardsNum;
        private TextView createDate;
        private ImageView more;
        private ImageView share;

        public ShowGroupViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            showGroupName=itemView.findViewById(R.id.tv_itemGroup_GroupName);
            cardsNum=itemView.findViewById(R.id.tv_ItemGroup_CardNumber);
            createDate=itemView.findViewById(R.id.tv_itemGroup_CreateDate);
            reviewDate=itemView.findViewById(R.id.tv_ItemGroup_ReviewData);
            more=itemView.findViewById(R.id.iv_item_show_group_more_group);
            share=itemView.findViewById(R.id.iv_ItemsGroup_share);

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void bindHolder(GroupItem groupItem, int position){
        if (!groupItem.getCreateDate().equals("404")){
            showGroupName.setText(groupItem.getGroupName().replace("_"," "));
            cardsNum.setText(String.valueOf(groupItem.getCardsNum()));
            reviewDate.setText(groupItem.getReviewDate());
            createDate.setText(groupItem.getCreateDate());
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   CustomPopUp.showPopupGroupItem(v,groupItem,context,adapterCallBack);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallBack.onClickShare(groupItem);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallBack.onClickItemView(groupItem);
                }
            });
            if (getItemViewType()==2){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,50,0,400);
                itemView.setLayoutParams(params);
            }
        }else {
            itemView.setForeground(context.getResources().getDrawable(R.drawable.foreground));
        }

        }
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

        }

    }

    public interface AdapterCallBack{
        void onClickDelete(GroupItem groupItem);
        void onClickEdit(GroupItem groupItem);
        void onClickItemView(GroupItem groupItem);
        void onClickShare(GroupItem groupItem);
    }

}

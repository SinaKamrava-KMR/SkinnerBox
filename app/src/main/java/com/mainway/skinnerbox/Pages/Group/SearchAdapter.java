package com.mainway.skinnerbox.Pages.Group;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private Context context;
    private List<Card> filterCards=new ArrayList<>();
    private CallBackFromAdapter callBackFromAdapter;

    public SearchAdapter(Context context,CallBackFromAdapter callBackFromAdapter1){
        this.context = context;
        this.callBackFromAdapter= callBackFromAdapter1;


    }
    @NonNull
    @NotNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.item_show_card,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchAdapter.SearchViewHolder holder, int position) {
            holder.bindView(filterCards.get(position));
    }

    @Override
    public int getItemCount() {
        return filterCards.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageCard;
        private TextView frontText;
        private TextView backText;
        private TextView boxNumber;
        private TextView trueNum;
        private TextView falseNum;
        private TextView reviewNum;
        private TextView deleteCard;
        private ImageView favoriteIC;

        public SearchViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageCard=itemView.findViewById(R.id.iv_ItemShowCard_ImageView_into);
            frontText=itemView.findViewById(R.id.tv_ItemShowCard_Question);
            backText=itemView.findViewById(R.id.tv_ItemShowCard_Answer);
            boxNumber=itemView.findViewById(R.id.tv_ItemShowCard_BoxNumber);
            trueNum=itemView.findViewById(R.id.tv_TrueNumCard);
            reviewNum=itemView.findViewById(R.id.tv_ReviewNumCard);
            falseNum=itemView.findViewById(R.id.tv_FalseNumCard);
            deleteCard=itemView.findViewById(R.id.tv_ItemShowCard_BtnDelete);
            favoriteIC=itemView.findViewById(R.id.iv_ViewCard_isFavoriteCard);

        }

        public void bindView(Card card){
            if (card.getIsFavorite()==1){
                favoriteIC.setImageDrawable(context.getDrawable(R.drawable.favorite));
            }else {
                favoriteIC.setImageDrawable(context.getDrawable(R.drawable.heart));

            }

            if (card.getPicFront().length()>0){
                Log.i("getPicId", "bindView: "+card.getPicFront() +"card Id :"+card.getId());
                File fil=new File(card.getPicFront());
               if (fil.exists()){
                   Log.i("filepath", "bindView: "+fil.getPath());
                  //imageCard.setImageURI(Uri.fromFile(fil));
                   Picasso.with(context).load(fil).into(imageCard);
               }

            }else {
                imageCard.setImageDrawable(null);
            }

            if (card.getFrontText().length()>0){
                frontText.setText(card.getFrontText());
            }else{
                frontText.setText("No Text");
            }

            if (card.getBackText().length()>0){
                backText.setText(card.getBackText());
            }else{
                backText.setText("No Text For Back of Card !!!");
            }

            if (card.getBox()==0){
                boxNumber.setText("New Card");
            }
            else if(card.getBox()==6){
                boxNumber.setText("Memorized");
            }else {
                boxNumber.setText("box "+card.getBox());
            }


            trueNum.setText(String.valueOf(card.getTrueNum()));
            reviewNum.setText(String.valueOf(card.getReviewNum()));
            falseNum.setText(String.valueOf(card.getFalseNum()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackFromAdapter.onClickItem(card);
                }
            });

            deleteCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackFromAdapter.onClickDelete(card);
                }
            });

        }
    }

    public void setCards(List<Card> cards){
        this.filterCards=cards;
        notifyDataSetChanged();
    }

    public void deleteCard(Card card){
        for (int i = 0; i < filterCards.size(); i++) {
            if (card.getId()==filterCards.get(i).getId()){
                filterCards.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public interface CallBackFromAdapter{
        void onClickItem(Card card);
        void onClickDelete(Card card);
    }

}

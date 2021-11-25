package com.mainway.skinnerbox.Fragment.BottomSheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.mainway.skinnerbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private Context context;
    private List<File> files;
    private List<File> filteredFiles;
    private FileItemEventListener fileItemEventListener;

    public  FileAdapter(Context context, List<File> files,FileItemEventListener fileItemEventListener){

        this.context = context;
        this.files = new ArrayList<>(files);
        this.filteredFiles=this.files;
        this.fileItemEventListener = fileItemEventListener;
    }


    @NonNull

    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_file_grid,parent,false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  FileAdapter.FileViewHolder holder, int position) {
        holder.bind(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public  class FileViewHolder extends RecyclerView.ViewHolder{
        private TextView fileName;
        private ImageView fileIcon;


        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName=itemView.findViewById(R.id.tv_fileName);
            fileIcon=itemView.findViewById(R.id.iv_file);
        }

        public void bind(File file){
            if (file.isDirectory()){
                fileIcon.setImageResource(R.drawable.ic_folder_black_32dp);
            }else{
                if (file.getName().toLowerCase().contains(".txt")){
                    fileIcon.setImageResource(R.drawable.ic_text);
                }else if (file.getName().toLowerCase().contains(".jpg")){
                    fileIcon.setImageResource(R.drawable.ic_image);
                }else if (file.getName().toLowerCase().contains(".mp3")){
                    fileIcon.setImageResource(R.drawable.ic_mp3);
                }else{
                    fileIcon.setImageResource(R.drawable.ic_file_black_32dp);
                }

            }

            fileName.setText(file.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileItemEventListener.onFileItemClick(file);
                }
            });
            if (getItemViewType()==2){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,50);
                itemView.setLayoutParams(params);
            }else if (getItemViewType()==3){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,50);
                itemView.setLayoutParams(params);
            }

        }
    }


    public void setFile(List<File> files1){
       this.filteredFiles=files1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (filteredFiles.isEmpty()){
            return 0;
        }else if (filteredFiles.size()-1==position){
            return 2;
        }else if (filteredFiles.size()-2==position){
            return 3;
        }else {
            return 1;
        }
    }
    public interface FileItemEventListener{
        void onFileItemClick(File file);
    }



    public void search(String query){
        if (query.length()>0){
            List<File> result=new ArrayList<>();
            for (File file : this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())){
                    result.add(file);
                }
            }

            this.filteredFiles=result;
            notifyDataSetChanged();

        }else{
            this.filteredFiles=this.files;
            notifyDataSetChanged();
        }
    }

}


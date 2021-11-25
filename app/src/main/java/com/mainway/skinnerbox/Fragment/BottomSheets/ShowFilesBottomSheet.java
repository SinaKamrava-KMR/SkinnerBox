package com.mainway.skinnerbox.Fragment.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mainway.skinnerbox.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ShowFilesBottomSheet extends BottomSheetDialogFragment implements FileAdapter.FileItemEventListener {

    private Context context;
    private RecyclerView recyclerView;
    private ImageView back;
    private CompletedImport completedImport;
    private FileAdapter adapter;
    private List<File> files;
    private String root ="/storage/emulated/0/";
    private File textFile;
    private String oldPath=root;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        this.context = context;
        super.onAttach(context);
        this.textFile=new File(root);
        this.files= Arrays.asList(textFile.listFiles());
        this.completedImport= (CompletedImport) context;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheetfragment_files,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new FileAdapter(context,files,ShowFilesBottomSheet.this);
        recyclerView=view.findViewById(R.id.recyclerView_ChooseFile);
        back=view.findViewById(R.id.iv_BackToOldPath);
        GridLayoutManager layoutManager=new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

       //=================================

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!oldPath.equals(root)){
                String[] strings=oldPath.split("/");
                String root2="";
                for (int i = 0; i < strings.length-1; i++) {
                    root2=root2+strings[i]+"/";
                }
                Log.i("root2", "root2: "+root2);
                Log.i("root2", "oldPath: "+oldPath);

                    oldPath=root2;
                    File newFile=new File(root2);
                    adapter.setFile( Arrays.asList(newFile.listFiles()));


            }else {
                dismiss();
            }

            }
        });

        //================================
    }

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()){
            oldPath=file.getPath();
            File newFile=new File(file.getPath());
            adapter.setFile( Arrays.asList(newFile.listFiles()));
        }else{

            if (!file.getName().toLowerCase().contains(".txt")){
                Toast.makeText(context, "you must choose text file", Toast.LENGTH_SHORT).show();
            }else {
                completedImport.onCompleted(file);
                dismiss();
            }
        }
    }

    //==========================
    public interface CompletedImport{
        void onCompleted(File file);
    }
}

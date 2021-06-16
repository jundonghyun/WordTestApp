package com.example.crowlingtest.Filelist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowlingtest.MainActivity;
import com.example.crowlingtest.R;
import com.example.crowlingtest.ShowWordActivity.ShowWordActivity;
import com.example.crowlingtest.ShowWordActivity.ShowWordAdapter;
import com.example.crowlingtest.StartTest.TestActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ItemViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(View v, int pos);
    }


    public static int choice; // 1이면 그냥 바로 단어 저장, 2이면 다이얼로그 띄움

    private static Context context;
    private ArrayList<FileListData> mdata = new ArrayList<>();
    private OnItemClickListener mListener = null;

    public void setonItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist_item, parent, false);
        this.context = parent.getContext();

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(mdata.get(position));
    }



    @Override
    public int getItemCount() {
        return mdata.size();
    }

    void addItem(FileListData data){
        mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView fileattr;
        private FileListData data;

        public ItemViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.FileName);
            fileattr = view.findViewById(R.id.FileAttr);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mListener.onItemClick(v, pos);
                        /* 파일 목록을 선택하면 파일의 이름을 MainActivity로 보냄 */
                        if(choice == 1){
                            MainActivity.SelectedFileName = textView.getText().toString();
                            v.getContext().startActivity(new Intent(FileListAdapter.context, MainActivity.class));
                        }
                        else if(choice == 2){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("단어장 삭제 및 보기");
                            builder.setItems(R.array.ChooseFunction, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] items = v.getResources().getStringArray(R.array.ChooseFunction);

                                    if(items[which].equals( "저장된 단어 보기")){
                                        String Filename = textView.getText().toString();

                                        Intent intent = new Intent(context, ShowWordActivity.class);
                                        intent.putExtra("FileList_FileName", Filename);
                                        context.startActivity(intent);

                                    }
                                    else if(items[which].equals("단어장파일 삭제")){
                                        /* 실제 파일삭제 */
                                        File file = new File(FileListActivity.deletefile, textView.getText().toString());
                                        file.delete(); //실제 데이터 삭제

                                        Toast.makeText(context, "단어장 삭제됨", Toast.LENGTH_SHORT).show();
                                        mdata.remove(pos); //리스트에서만 삭제됨 실제 데이터는 삭제되지 않음
                                        notifyItemRemoved(pos); //삭제후 리사이클뷰 다시 초기화
                                        notifyDataSetChanged();


                                    }
                                    else if(items[which].equals("단어 추가")){
                                        MainActivity.SelectedFileName = textView.getText().toString();
                                        v.getContext().startActivity(new Intent(FileListAdapter.context, MainActivity.class));
                                    }
                                    else if(items[which].equals("단어 삭제 및 수정")){
                                        String Filename = textView.getText().toString();

                                        Intent intent = new Intent(context, ShowWordActivity.class);
                                        intent.putExtra("FileList_FileName", Filename);
                                        context.startActivity(intent);
                                    }

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else if(choice == 3){
                            TestActivity.FilePath = textView.getText().toString();
                            v.getContext().startActivity(new Intent(FileListAdapter.context, TestActivity.class));
                        }
                    }
                }
            });
        }


        void onBind(FileListData data){
            this.data = data;
            textView.setText(data.getFilename());
            fileattr.setText(data.getFileAttribute());
        }
    }
}

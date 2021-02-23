package com.example.crowlingtest.ShowWordActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowlingtest.MainActivity;
import com.example.crowlingtest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ShowWordAdapter extends RecyclerView.Adapter<ShowWordAdapter.ItemViewHolder> {

    private static Context context;
    private ArrayList<ShowWordData> mdata= new ArrayList<>();
    private View customdialog;


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_word_list_item, parent, false);
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

    void addItem(ShowWordData data){
       mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView word, mean;
        private ShowWordData data;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            word = view.findViewById(R.id.Word);
            mean = view.findViewById(R.id.Meaning);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("작업을 선택하세요");
                        builder.setItems(R.array.WordFunction, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] items = v.getResources().getStringArray(R.array.WordFunction);

                                if (items[which].equals("단어 수정")) {
                                    customdialog = (View) View.inflate(context, R.layout.activity_showwordadapter_custom_dialog, null);
                                    AlertDialog.Builder dial = new AlertDialog.Builder(context);
                                    dial.setView(customdialog);
                                    dial.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EditText mean = customdialog.findViewById(R.id.mean);
                                            if (mean.getText().toString().matches("")) {
                                                Toast.makeText(context, "뜻을 입력하세요", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                String line = null;

                                                HashMap<String, String> modifyword = new HashMap<>();

                                                try {
                                                    BufferedReader bufferedReader = new BufferedReader(new FileReader(ShowWordActivity.ShowWordAdapterFile));

                                                    String remove_word, remove_mean;

                                                    while ((line = bufferedReader.readLine()) != null) {
                                                        int idx = line.indexOf(" ");
                                                        remove_word = line.substring(0, idx);
                                                        remove_mean = line.substring(idx + 1, line.length());
                                                        modifyword.put(remove_word, remove_mean);
                                                    }

                                                    modifyword.remove(word.getText());
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                    dial.create();
                                    dial.show();

                                } else if (items[which].equals("단어 삭제")) {
                                    String line = null;

                                    HashMap<String, String> removeword = new HashMap<>();

                                    try {
                                        BufferedReader bufferedReader = new BufferedReader(new FileReader(ShowWordActivity.ShowWordAdapterFile));
                                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ShowWordActivity.ShowWordAdapterFile, true));
                                        File f = ShowWordActivity.ShowWordAdapterFile;


                                        String remove_word, remove_mean;

                                        while ((line = bufferedReader.readLine()) != null) {
                                            int idx = line.indexOf(" ");
                                            remove_word = line.substring(0, idx);
                                            remove_mean = line.substring(idx + 1, line.length());
                                            removeword.put(remove_word, remove_mean);
                                        }

                                        removeword.remove(word.getText());

                                        Iterator<String> keys = removeword.keySet().iterator();
                                        String WriteFile = "";
                                        while(keys.hasNext()){
                                            String key = keys.next();
                                            WriteFile += key +" "+removeword.get(key) +"\n";
                                        }
                                        FileWriter fw = new FileWriter(f);
                                        fw.write(WriteFile);

                                        fw.close();
                                        bufferedReader.close();

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mdata.remove(pos);
                                    notifyItemRemoved(pos);
                                }
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }

        void onBind(ShowWordData data){
            this.data = data;

            word.setText(data.getWord());
            mean.setText(data.getMeaning());

        }
    }
}

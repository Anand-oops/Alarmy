package com.anand.android.onsitetask1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecycleViewHolder> {

    private static final String TAG = "MessageAdapter";
    private ArrayList<MessageItemClass> messageEntries;
    Context context;

    public MessageAdapter(ArrayList<MessageItemClass> arrayList, Context context){
        this.messageEntries=arrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_message_item, parent, false);
        return new MessageAdapter.RecycleViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, final int position) {
        MessageItemClass messageItem = messageEntries.get(position);

        if(messageItem.getMsg().length()>25)
            holder.body.setText(messageItem.getMsg().substring(0,25)+"..");
        else
            holder.body.setText(messageItem.getMsg());


        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date mDate= null;
        try {
            mDate = dateFormat.parse(messageItem.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onBindViewHolder: "+dateFormat.format(mDate));
        holder.date.setText(dateFormat.format(mDate));
        holder.number.setText(messageItem.getNum());
        holder.time.setText(messageItem.getTime());

        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("Delete Message... ");
                dialog.setTitle("");
                dialog.setIcon(R.drawable.ic_delete);
                dialog.setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                dialog.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MessageHelper messageHelper= new MessageHelper(context);
                                messageHelper.deleteData(i+1);
                                messageEntries.remove(i);

                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                return true;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return messageEntries.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder {

        TextView body;
        TextView number;
        TextView date;
        TextView time;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.id_msg);
            number = itemView.findViewById(R.id.id_num);
            date = itemView.findViewById(R.id.id_date);
            time = itemView.findViewById(R.id.id_time);
        }
    }
}

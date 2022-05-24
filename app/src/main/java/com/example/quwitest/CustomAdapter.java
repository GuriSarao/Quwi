package com.example.quwitest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    String[] personNames;
    Context context;
    private MyViewHolder holder;
    private int position;

    public CustomAdapter(Context context, String[] personNames) {
        this.context = context;
        this.personNames = personNames;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONObject obj1 = new JSONObject(personNames[position]);
            holder.name.setText(obj1.getString("type"));
            holder.time.setText(changeDate(obj1.getString("dta_create")));
            if (obj1.getString("message_last").equals("null"))
                holder.message.setText("no message");
            else {
                holder.message.setText(obj1.getString("message_last"));
            }

            if (!obj1.getBoolean("pin_to_top"))
                holder.pinned.setVisibility(View.GONE);
            if (!obj1.getBoolean("is_unread_manual"))
                holder.isRead.setVisibility(View.GONE);

            // implement setOnClickListener event on item view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // display a toast with person name on item click
                    Toast.makeText(context, obj1.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // set the data in items

    }

    private String changeDate(String date) {
        String time = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat requiredYearFormat = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat requiredDateFormat = new SimpleDateFormat("dd MMM");
            SimpleDateFormat requiredTimeFormat = new SimpleDateFormat("HH:mm");
            Date past = format.parse(date);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//            if(seconds<60)
//            {
//                time = "now";
//                System.out.println(seconds+" seconds ago");
//            }
//            else if(minutes<60)
//            {
//                time = minutes+"m";
//                System.out.println(minutes+" minutes ago");
//            }
            if (hours < 24) {
                time = requiredTimeFormat.format(format.parse(date));
                System.out.println(hours + " hours ago");
            } else if (days < 365) {
                time = requiredDateFormat.format(format.parse(date));
                System.out.println(hours + " hours ago");
            } else {
                time = requiredYearFormat.format(format.parse(date));
                System.out.println(time + " days ago");
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return time;
    }

    @Override
    public int getItemCount() {
        return personNames.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, message;// init the item view's
        ImageView pinned,isRead;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            message = (TextView) itemView.findViewById(R.id.message);

            pinned = (ImageView) itemView.findViewById(R.id.pinned);
            isRead = (ImageView) itemView.findViewById(R.id.isRead);
        }
    }

}

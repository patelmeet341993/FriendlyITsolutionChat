package com.friendlyitsolution.meet.firebase_chat;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdpter1 extends RecyclerView.Adapter<MyAdpter1.MyViewHolder> {

    private List<ContactModel1> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        CircleImageView img;


        public MyViewHolder(View view) {
            super(view);

            name=(TextView)view.findViewById(R.id.name);
            img=(CircleImageView)view.findViewById(R.id.img);

        }
    }


    public MyAdpter1(List<ContactModel1> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlayout1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ContactModel1 model = moviesList.get(position);


        holder.name.setText(model.Cname);

            Glide.with(holder.img.getContext()).load(model.path)
                    .override(40, 40)
                    .fitCenter()
                    .into(holder.img);

            if(model.online.equals("yes"))
            {
                holder.img.setBorderColor(Color.parseColor("#1b8eda"));
                holder.name.setTextColor(Color.parseColor("#1b8eda"));
            }
            else
            {

                holder.img.setBorderColor(Color.parseColor("#ff90a4ae"));
                holder.name.setTextColor(Color.parseColor("#ff90a4ae"));


            }




    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

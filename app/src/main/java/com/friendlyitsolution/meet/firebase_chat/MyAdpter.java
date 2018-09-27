package com.friendlyitsolution.meet.firebase_chat;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyViewHolder> {

    private List<ContactModel> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name,msg,time,email;
        CircleImageView img;
        RelativeLayout lay;

        public MyViewHolder(View view) {
            super(view);

            lay=(RelativeLayout)view.findViewById(R.id.lay);
            time=(TextView)view.findViewById(R.id.time);
            email=(TextView)view.findViewById(R.id.email);
            name=(TextView)view.findViewById(R.id.name);
            msg=(TextView) view.findViewById(R.id.msg);
            img=(CircleImageView)view.findViewById(R.id.img);

        }
    }


    public MyAdpter(List<ContactModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ContactModel model = moviesList.get(position);


        if(model.Cid.equals(Myapp.userid))
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.lay.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.lay.setLayoutParams(params);

        }
        else
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.lay.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.lay.setLayoutParams(params);

        }

        holder.name.setText(model.Cname);
        holder.msg.setText(model.msg);
        holder.time.setText(model.time);
            Glide.with(holder.img.getContext()).load(model.path)
                    .override(40, 40)
                    .fitCenter()
                    .into(holder.img);

        holder.email.setText(model.email);






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

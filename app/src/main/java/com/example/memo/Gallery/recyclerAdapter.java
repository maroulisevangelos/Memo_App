package com.example.memo.Gallery;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.R;

import java.util.ArrayList;

public class recyclerAdapter extends  RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{
    private ArrayList<photo> photolist;
    private MainPhoto mainPhotoActivity;

    public recyclerAdapter(ArrayList<photo> photolist, MainPhoto mainPhotoActivity){
        this.photolist = photolist;
        this.mainPhotoActivity = mainPhotoActivity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView photoImg;
        private TextView nameImg;
        public MyViewHolder (final View view){
            super(view);
            photoImg = view.findViewById(R.id.Image);
            nameImg = view.findViewById(R.id.ImageName);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = photolist.get(position).getName();
        String info = photolist.get(position).getInfo();

        holder.nameImg.setText(name +"     "+ info);


        int imageResId = holder.itemView.getContext().getResources().getIdentifier(name, "drawable", holder.itemView.getContext().getPackageName());

        // Check if the resource exists before setting it
        if (imageResId != 0) {
            holder.photoImg.setImageResource(imageResId);
        } else {
            // Handle the case where the drawable is not found, possibly set a default image or handle the error
            holder.photoImg.setImageResource(R.drawable.default_image);
        }

        // Set OnClickListener to open the image in full screen
        holder.photoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FullScreenImageActivity.class);
                intent.putExtra("imageResId", imageResId);
                intent.putExtra("imageName", name);
                intent.putExtra("imageInfo", info);
                mainPhotoActivity.findElementWithValue(name);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photolist.size();
    }
}

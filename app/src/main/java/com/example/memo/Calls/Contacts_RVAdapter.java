package com.example.memo.Calls;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.R;

import java.util.ArrayList;
import java.util.List;

public class Contacts_RVAdapter extends RecyclerView.Adapter<Contacts_RVAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ContactsModel> Contacts;

    public Contacts_RVAdapter(Context context, ArrayList<ContactsModel> Contacts) {
        this.context = context;
        this.Contacts = Contacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContactsModel contact = Contacts.get(position);

        // Check if contact or name is null
        if (contact == null || contact.getName() == null) {
            // Handle the null case, maybe log an error or provide a default value
            holder.name.setText("Unknown");
        } else {
            holder.name.setText(contact.getName());
        }

        // Check if contact or relation is null
        if (contact == null || contact.getRelation() == null) {
            // Handle the null case, maybe log an error or provide a default value
            holder.relation.setText("Unknown");
        } else {
            holder.relation.setText(contact.getRelation());
        }

        // Check if contact or num1 is null
        if (contact == null || contact.getNum1() == null) {
            // Handle the null case, maybe log an error or provide a default value
            holder.num1.setText("Unknown");
        } else {
            holder.num1.setText(contact.getNum1());
        }

        // Check if contact or num2 is null
        if (contact == null || contact.getNum2() == null) {
            // Handle the null case, maybe log an error or provide a default value
            holder.num2.setText("Unknown");
        } else {
            holder.num2.setText(contact.getNum2());
        }

        // Set image from drawable if available
        if (contact != null && contact.getPhoto() != null) {
            int imageResource = context.getResources().getIdentifier(contact.getPhoto(), "drawable", context.getPackageName());
            if (imageResource != 0) {
                holder.imageView.setImageResource(imageResource);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Example of setting a default image
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Set default image if contact or photo is null
        }

        // Set click listener on CardView
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactMain.class);
            intent.putExtra("name", contact != null ? contact.getName() : "Unknown");
            intent.putExtra("relation", contact != null ? contact.getRelation() : "Unknown");
            intent.putExtra("num1", contact != null ? contact.getNum1() : "Unknown");
            intent.putExtra("num2", contact != null ? contact.getNum2() : "Unknown");
            intent.putExtra("image", contact != null && contact.getPhoto() != null ? contact.getPhoto() : "");
            context.startActivity(intent);
        });
    }




    @Override
    public int getItemCount() {
        return Contacts.size();
    }

    public void filterList(List<ContactsModel> filteredList) {
        Contacts = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, relation, num1, num2;
        ImageView imageView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
            relation = itemView.findViewById(R.id.textView2);
            num1 = itemView.findViewById(R.id.textView3);
            num2 = itemView.findViewById(R.id.textView4);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}








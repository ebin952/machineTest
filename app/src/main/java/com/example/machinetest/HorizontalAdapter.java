package com.example.machinetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    Context context;
    List<HorizontalModel> horizontalModelList =  new ArrayList<>();
    CategoryInterface categoryInterface;


    public HorizontalAdapter(Context context, List<HorizontalModel> horizontalModelList, CategoryInterface categoryInterface) {
        this.context = context;
        this.horizontalModelList = horizontalModelList;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_grid,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalAdapter.ViewHolder holder, int position) {



        holder.name.setText(horizontalModelList.get(position).getName());
        byte[] c_image = horizontalModelList.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(c_image, 0, c_image.length);
        holder.imageView.setImageBitmap(bitmap);




    }

    @Override
    public int getItemCount() {
        return horizontalModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_img);
            name = itemView.findViewById(R.id.category_text);
            linearLayout = itemView.findViewById(R.id.horizontal_grid);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categoryInterface.onItemClick(getAdapterPosition());
                }
            });



        }
    }
}

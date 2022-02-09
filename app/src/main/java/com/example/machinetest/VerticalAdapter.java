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

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

    Context context;
    List<VerticaLModel>  verticaLModelList= new ArrayList();
    ProductInterface productInterface;

    public VerticalAdapter(Context context, List<VerticaLModel> verticaLModelList) {
        this.context = context;
        this.verticaLModelList = verticaLModelList;
    }

    public VerticalAdapter(Context context, List<VerticaLModel> verticaLModelList, ProductInterface productInterface) {
        this.context = context;
        this.verticaLModelList = verticaLModelList;
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.v_grid,parent,false);
        return new ViewHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.p_name.setText(verticaLModelList.get(position).getProduct_name());
        holder.c_name.setText(verticaLModelList.get(position).getCategory_name());
        byte[] p_image = verticaLModelList.get(position).getProduct_image();
        Bitmap bitmap = BitmapFactory.decodeByteArray(p_image, 0, p_image.length);
        holder.p_imageView.setImageBitmap(bitmap);
        holder.p_price.setText(verticaLModelList.get(position).getPrice());


    }

    @Override
    public int getItemCount() {
        return verticaLModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView p_imageView;
        TextView p_name;
        TextView c_name;
        TextView p_price;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            p_imageView = itemView.findViewById(R.id.product_img);
            c_name = itemView.findViewById(R.id.product_category);
            p_name = itemView.findViewById(R.id.product_name);
            p_price = itemView.findViewById(R.id.product_price);
            linearLayout = itemView.findViewById(R.id.vertical);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productInterface.onProductClick(getAdapterPosition());
                }
            });







        }
    }
}

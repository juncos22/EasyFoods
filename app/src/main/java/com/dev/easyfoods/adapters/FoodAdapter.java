package com.dev.easyfoods.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.easyfoods.R;
import com.dev.easyfoods.clases.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.DataHolder> {
    private List<Food> foodList;
    private static ClickListener listener;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        byte[] foodImg = foodList.get(position).getPhoto();
        Bitmap foto = BitmapFactory.decodeByteArray(foodImg, 0, foodImg.length);
        holder.photoFood.setImageBitmap(foto);
        holder.nameFood.setText(foodList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setOnItemClickListener(ClickListener listener) {
        FoodAdapter.listener = listener;
    }

    public interface ClickListener {
        void onItemClick(int pos, View v);
    }

    static class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView photoFood;
        TextView nameFood;

        DataHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            photoFood = itemView.findViewById(R.id.photoFood);
            nameFood = itemView.findViewById(R.id.nameFood);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition(), v);
        }
    }
}

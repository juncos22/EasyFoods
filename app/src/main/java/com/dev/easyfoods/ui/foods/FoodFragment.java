package com.dev.easyfoods.ui.foods;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.easyfoods.R;
import com.dev.easyfoods.activities.DetallesActivity;
import com.dev.easyfoods.adapters.FoodAdapter;
import com.dev.easyfoods.clases.Food;
import com.dev.easyfoods.dao.FoodDaoImpl;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {


    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        FoodDaoImpl impl = new FoodDaoImpl(getContext());
        List<Food> foodList = impl.showFoods();
        if (foodList.size() == 0) {
            TextView tvRecetas = view.findViewById(R.id.tvRecetas);
            tvRecetas.setText("No hay recetas disponibles");
        }else {
            RecyclerView rvFoods = view.findViewById(R.id.rvFoods);
            FoodAdapter adapter = new FoodAdapter(foodList);
            adapter.setOnItemClickListener((pos, v) -> {
                Food f = foodList.get(pos);
                Intent itemIntent = new Intent(getContext(), DetallesActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                itemIntent.putExtra("food", f);
                startActivity(itemIntent);
                try {
                    getActivity().finish();
                }catch (NullPointerException e) {
                    Log.i("ERROR", Objects.requireNonNull(e.getMessage()));
                }
            });
            rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));
            rvFoods.setAdapter(adapter);
        }

        return view;
    }

}

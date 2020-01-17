package com.dev.easyfoods.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.easyfoods.R;
import com.dev.easyfoods.clases.Food;
import com.dev.easyfoods.dao.FoodDaoImpl;

public class DetallesActivity extends AppCompatActivity {
    private Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        ImageView photo = findViewById(R.id.photo);
        TextView name = findViewById(R.id.name);
        TextView description = findViewById(R.id.description);

        food = (Food) getIntent().getSerializableExtra("food");
        if (food != null) {
            byte[] imagen = food.getPhoto();
            Bitmap foto = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);

            photo.setImageBitmap(foto);
            name.setText(food.getName());
            description.setText(food.getDescription());
        }
    }

    public void eliminarReceta(View view) {
        int foodId = food.getId();
        FoodDaoImpl impl = new FoodDaoImpl(getApplicationContext());
        impl.deleteFood(foodId);
        Toast.makeText(this, "Receta Eliminada", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MenuActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    public void modificarReceta(View view) {
        Intent itemIntent = new Intent(this, ModificarActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        itemIntent.putExtra("food", food);
        startActivity(itemIntent);
        finish();
    }
}

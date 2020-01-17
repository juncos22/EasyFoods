package com.dev.easyfoods.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.easyfoods.R;
import com.dev.easyfoods.clases.Food;
import com.dev.easyfoods.dao.FoodDaoImpl;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ModificarActivity extends AppCompatActivity {
    private static final int TOMAR_FOTO = 1;
    private static final int SELECCIONAR_FOTO = 0;
    private Food food;
    private EditText foodName, foodDesc;
    private ImageView foodPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        foodName = findViewById(R.id.foodName);
        foodDesc = findViewById(R.id.foodDesc);
        foodPhoto = findViewById(R.id.foodPhoto);

        food = (Food) getIntent().getSerializableExtra("food");
        if (food != null) {
            foodName.setText(food.getName());
            foodDesc.setText(food.getDescription());
            byte[] img = food.getPhoto();
            Bitmap foto = BitmapFactory.decodeByteArray(img, 0, img.length);
            foodPhoto.setImageBitmap(foto);
        }
    }

    public void seleccionarFoto(View view) {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Eliga una opcion");
        dialog.setItems(opciones, (dialog1, which) -> {
            if (opciones[which].equals("Tomar Foto")) {
                tomarFoto();
            }else if (opciones[which].equals("Elegir de galeria")) {
                buscarFoto();
            }else {
                dialog1.dismiss();
            }
        });
        dialog.show();
    }

    private void tomarFoto() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoIntent, TOMAR_FOTO);
        }
    }

    private void buscarFoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/");
        startActivityForResult(Intent.createChooser(galleryIntent, "Seleccione la aplicacion"), SELECCIONAR_FOTO);
    }

    public void actualizarReceta(View view) {
        if (foodName.getText().toString().isEmpty() || foodDesc.getText().toString().isEmpty()) {
            Toast.makeText(this, "Los 2 primeros campos son obligatorios", Toast.LENGTH_SHORT).show();
            foodName.requestFocus();
        }else {
            Bitmap bitmap = ((BitmapDrawable) foodPhoto.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] foto = stream.toByteArray();

            food.setName(foodName.getText().toString());
            food.setDescription(foodDesc.getText().toString());
            food.setPhoto(foto);

            FoodDaoImpl impl = new FoodDaoImpl(this);
            impl.updateFood(food);
            limpiarCampos();
            Toast.makeText(this, "Receta Actualizada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MenuActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
    }

    private void limpiarCampos() {
        foodName.getText().clear();
        foodDesc.getText().clear();
        foodPhoto.setImageResource(R.drawable.img_default);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TOMAR_FOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap fotoCargada = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    foodPhoto.setImageBitmap(fotoCargada);
                }
                break;
            case SELECCIONAR_FOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Uri path = data.getData();
                    Picasso.get().load(path)
                            .resize(100, 100).centerCrop()
                            .into(foodPhoto);
                }
                break;
        }
    }
}

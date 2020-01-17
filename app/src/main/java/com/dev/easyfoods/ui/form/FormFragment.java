package com.dev.easyfoods.ui.form;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dev.easyfoods.R;
import com.dev.easyfoods.clases.Food;
import com.dev.easyfoods.dao.Dao;
import com.dev.easyfoods.dao.FoodDaoImpl;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {

    private static final int COD_SELECCION = 10;
    private static final int COD_PHOTO = 20;
    private EditText foodName, foodDesc;
    private ImageView foodPhoto;

    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        foodName = view.findViewById(R.id.foodName);
        foodDesc = view.findViewById(R.id.foodDesc);
        foodPhoto = view.findViewById(R.id.foodPhoto);
        Button btnSeleccionar = view.findViewById(R.id.seleccionFoto);
        Button btnGuardarReceta = view.findViewById(R.id.guardarReceta);

        btnSeleccionar.setOnClickListener(l -> cargarFoto());
        btnGuardarReceta.setOnClickListener(l -> guardarReceta());

        return view;
    }

    private void limpiarCampos() {
        foodName.getText().clear();
        foodDesc.getText().clear();
        foodPhoto.setImageResource(R.drawable.img_default);

        foodName.requestFocus();
    }

    private void guardarReceta() {
        if (foodName.getText().toString().isEmpty() || foodDesc.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Los 2 primeros campos son obligatorios", Toast.LENGTH_SHORT).show();
            foodName.requestFocus();
        }else {
            try {
                Bitmap foto = ((BitmapDrawable) foodPhoto.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytePhoto = stream.toByteArray();

                Food food = new Food(foodName.getText().toString(), foodDesc.getText().toString(),
                        bytePhoto);
                Dao dao = new FoodDaoImpl(getContext());
                dao.addFood(food);
                Toast.makeText(getContext(), "Receta Guardada", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tomarFoto() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(Objects.requireNonNull(getContext()).getPackageManager()) != null) {
            startActivityForResult(photoIntent, COD_PHOTO);
        }
    }

    private void buscarFoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/");
        startActivityForResult(Intent.createChooser(galleryIntent, "Seleccione la aplicacion"),
                COD_SELECCION);
    }

    private void cargarFoto() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de la galeria", "Cancelar"};
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Eliga una opcion");
        dialog.setItems(opciones, (dialog1, which) -> {
            if (opciones[which].equals("Tomar Foto")) {
                tomarFoto();
            } else if (opciones[which].equals("Elegir de la galeria")) {
                buscarFoto();
            } else {
                dialog1.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case COD_SELECCION:
                if (resultCode == RESULT_OK && data != null) {
                    Uri path = data.getData();
                    Picasso.get().load(path).resize(100, 100)
                            .centerInside().into(foodPhoto);
                }
                break;
            case COD_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap fotoCargada = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    foodPhoto.setImageBitmap(fotoCargada);
                }
                break;
        }
    }
}

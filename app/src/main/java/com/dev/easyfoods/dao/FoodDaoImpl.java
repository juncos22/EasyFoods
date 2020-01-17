package com.dev.easyfoods.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.dev.easyfoods.clases.Food;
import com.dev.easyfoods.db.Conexion;

import java.util.ArrayList;
import java.util.List;

public class FoodDaoImpl implements Dao {
    private Conexion c;
    private String query;

    public FoodDaoImpl(Context context) {
        c = new Conexion(context);
    }

    @Override
    public void addFood(Food f) {
        query = "INSERT INTO food (name, description, photo) VALUES (?, ?, ?)";
        SQLiteDatabase db = c.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.clearBindings();
        stmt.bindString(1, f.getName());
        stmt.bindString(2, f.getDescription());
        stmt.bindBlob(3, f.getPhoto());
        stmt.executeInsert();
        db.close();
    }

    @Override
    public void updateFood(Food f) {
        query = "UPDATE food SET name = ?, description = ?, photo = ? " +
                "WHERE id = ?";
        SQLiteDatabase db = c.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.clearBindings();
        stmt.bindString(1, f.getName());
        stmt.bindString(2, f.getDescription());
        stmt.bindBlob(3, f.getPhoto());
        stmt.bindLong(4, f.getId());
        stmt.executeUpdateDelete();
        db.close();
    }

    @Override
    public void deleteFood(int id) {
        query = "DELETE FROM food WHERE id = ?";
        SQLiteDatabase db = c.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.clearBindings();
        stmt.bindLong(1, id);
        stmt.executeUpdateDelete();
        db.close();
    }

    @Override
    public List<Food> showFoods() {
        query = "SELECT * FROM food";
        List<Food> foodList = new ArrayList<>();

        SQLiteDatabase db = c.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food", null);
        Food f;
        while (cursor.moveToNext()) {
            f = new Food(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3));

            foodList.add(f);
        }
        cursor.close();
        db.close();

        return foodList;
    }
}

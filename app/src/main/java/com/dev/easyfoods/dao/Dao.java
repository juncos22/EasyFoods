package com.dev.easyfoods.dao;

import com.dev.easyfoods.clases.Food;

import java.util.List;

public interface Dao {
    void addFood(Food f);
    void updateFood(Food f);
    void deleteFood(int id);
    List<Food> showFoods();
}

package com.dev.easyfoods.clases;

import java.io.Serializable;
import java.util.Arrays;

public class Food implements Serializable {
    private int id;
    private String name;
    private String description;
    private byte[] photo;

    public Food(String name, String description, byte[] photo) {
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Food(int id, String name, String description, byte[] photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}

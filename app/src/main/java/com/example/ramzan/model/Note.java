package com.example.ramzan.model;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int quantity;
    private double price;
    private String image;
    //private byte[] image;


    public Note( String image, String title, String description, double price, int quantity) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }


    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

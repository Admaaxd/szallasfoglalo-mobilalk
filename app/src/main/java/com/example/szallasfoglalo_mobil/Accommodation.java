package com.example.szallasfoglalo_mobil;

public class Accommodation {
    private String name;
    private int imageResource;
    private String location;
    private double price;
    private String description;

    private String startDate;
    private String endDate;

    public Accommodation(String name, int imageResource, String location, double price, String description) {
        this.name = name;
        this.imageResource = imageResource;
        this.location = location;
        this.price = price;
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}

package com.example.skygo.models;

public class Seat {
    private String seatNumber;
    private boolean isOccupied;
    private boolean isSelected;

    public Seat() {
        // Default constructor required for Firebase
    }

    public Seat(String seatNumber, boolean isOccupied, boolean isSelected) {
        this.seatNumber = seatNumber;
        this.isOccupied = isOccupied;
        this.isSelected = isSelected;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
package com.example.skygo.models;

public class Reservation {
    private String ticketId;
    private String flightId;
    private String passengerName;
    private String passengerEmail;
    private String seatNumber;
    private double totalPrice;

    public Reservation() {
        // Required for Firebase
    }

    public Reservation(String ticketId, String flightId, String passengerName,
                       String passengerEmail, String seatNumber, double totalPrice) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.seatNumber = seatNumber;
        this.totalPrice = totalPrice;
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}

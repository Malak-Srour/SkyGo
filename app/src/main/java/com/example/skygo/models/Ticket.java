package com.example.skygo.models;

public class Ticket {
    public String ticketId, flightId, passengerName, passengerEmail, seatNumber;
    public double totalPrice;

    public Ticket() {} // Required

    public Ticket(String ticketId, String flightId, String passengerName,
                  String passengerEmail, String seatNumber, double totalPrice) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.seatNumber = seatNumber;
        this.totalPrice = totalPrice;
    }
}

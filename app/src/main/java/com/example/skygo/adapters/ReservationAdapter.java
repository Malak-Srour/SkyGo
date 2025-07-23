package com.example.skygo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.skygo.R;
import com.example.skygo.models.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation r = reservationList.get(position);

        holder.textTicketId.setText("Ticket: " + r.getTicketId());
        holder.textPassenger.setText("Name: " + r.getPassengerName());
        holder.textEmail.setText("Email: " + r.getPassengerEmail());
        holder.textFlight.setText("Flight ID: " + r.getFlightId());
        holder.textSeat.setText("Seats: " + r.getSeatNumber());
        holder.textPrice.setText("Total: $" + String.format("%.2f", r.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textTicketId, textPassenger, textEmail, textFlight, textSeat, textPrice;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textTicketId = itemView.findViewById(R.id.text_ticket_id);
            textPassenger = itemView.findViewById(R.id.text_passenger_name);
            textEmail = itemView.findViewById(R.id.text_passenger_email);
            textFlight = itemView.findViewById(R.id.text_flight_id);
            textSeat = itemView.findViewById(R.id.text_seat_number);
            textPrice = itemView.findViewById(R.id.text_total_price);
        }
    }
}

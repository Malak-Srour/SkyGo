package com.example.skygo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skygo.R;
import com.example.skygo.models.Flight;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<Flight> flightList;
    private OnFlightClickListener listener;
    private boolean isAdmin;

    public interface OnFlightClickListener {
        void onFlightClick(Flight flight);
        void onEditClick(Flight flight);
        void onDeleteClick(Flight flight);
    }

    public FlightAdapter(List<Flight> flightList, OnFlightClickListener listener, boolean isAdmin) {
        this.flightList = flightList;
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        // Fill all UI fields with flight data
        holder.textViewAirline.setText(flight.getAirline());
        holder.textViewFlightId.setText(flight.getFlightId());
        holder.textViewClass.setText(flight.getFlightClass());
        holder.textViewRoute.setText(flight.getOrigin() + " → " + flight.getDestination());
        holder.textViewTime.setText(flight.getDepartureTime() + " - " + flight.getArrivalTime());
        holder.textViewDate.setText(flight.getDate());
        holder.textViewSeats.setText(flight.getAvailableSeats() + " seats available");
        holder.textViewPrice.setText("$" + String.format("%.2f", flight.getPrice()));

        if (isAdmin) {
            holder.buttonEdit.setVisibility(View.VISIBLE);
            holder.buttonDelete.setVisibility(View.VISIBLE);

            holder.buttonEdit.setOnClickListener(v -> listener.onEditClick(flight));
            holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClick(flight));
        } else {
            holder.buttonEdit.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onFlightClick(flight));
    }


    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAirline, textViewFlightId, textViewClass;
        TextView textViewRoute, textViewTime, textViewDate, textViewSeats, textViewPrice;
        Button buttonEdit, buttonDelete;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAirline = itemView.findViewById(R.id.text_view_airline);
            textViewFlightId = itemView.findViewById(R.id.text_view_flight_id);
            textViewClass = itemView.findViewById(R.id.text_view_class);
            textViewRoute = itemView.findViewById(R.id.text_view_route);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewSeats = itemView.findViewById(R.id.text_view_seats);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            buttonEdit = itemView.findViewById(R.id.buttonEditFlight);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteFlight);
        }
    }

}

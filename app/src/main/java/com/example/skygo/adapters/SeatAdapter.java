package com.example.skygo.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skygo.R;
import com.example.skygo.models.Seat;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Seat> seatList;
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }

    public SeatAdapter(List<Seat> seatList, OnSeatClickListener listener) {
        this.seatList = seatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);

        holder.textViewSeatNumber.setText(seat.getSeatNumber());

        // Set seat color based on status
        if (seat.isOccupied()) {
            holder.textViewSeatNumber.setBackgroundColor(Color.RED);
            holder.textViewSeatNumber.setTextColor(Color.WHITE);
        } else if (seat.isSelected()) {
            holder.textViewSeatNumber.setBackgroundColor(Color.GREEN);
            holder.textViewSeatNumber.setTextColor(Color.WHITE);
        } else {
            holder.textViewSeatNumber.setBackgroundColor(Color.LTGRAY);
            holder.textViewSeatNumber.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSeatClick(seat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSeatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSeatNumber = itemView.findViewById(R.id.text_view_seat_number);
        }
    }
}
package com.example.expensetracker;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {
    ArrayList<History> arrayListHistory;

    public HistoryRecyclerViewAdapter(ArrayList<History> arrayListHistory){
        this.arrayListHistory = new ArrayList<>(arrayListHistory);
    }

    private String formatBalance(int balance){
        return String.format("IDR %,d", balance);
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_histori, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.HistoryViewHolder holder, int position) {
        final History history = arrayListHistory.get(position);

        // Set tiap elemen
        holder.txtJudulTransaksi.setText(history.getTransaksiJudul());
        holder.txtJumlahTransaksi.setText(String.valueOf(history.getJumlahTransaksi()));

        // Hitung durasi
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime pastTime = LocalDateTime.of(history.getDateTime().getYear(), history.getDateTime().getMonth(), history.getDateTime().getDayOfMonth(), history.getDateTime().getHour(), history.getDateTime().getMinute());
        Duration duration = Duration.between(pastTime, currentTime);
        long minutes = duration.toMinutes();
        String durationStr;
        if(minutes == 0){
            durationStr = "Baru saja";
        }
        if (minutes != 0 && minutes < 60) {
            durationStr = minutes + " menit lalu";
        } else {
            long hours = duration.toHours();
            if (hours < 24) {
                durationStr = hours + " jam lalu";
            } else {
                long days = duration.toDays();
                durationStr = days + " hari lalu";
            }
        }
        holder.txtDurasi.setText(durationStr);

        // Set icon
        // 0 = income ; 1 = outcome
        if(history.getTipeTransaksi() == 0){
            holder.iconTransaksi.setImageResource(R.drawable.transaction_income);
            holder.txtJumlahTransaksi.setTextColor(Color.parseColor("#5CDFB2"));
        }
        else {
            holder.iconTransaksi.setImageResource(R.drawable.transaction_outcome);
            holder.txtJumlahTransaksi.setTextColor(Color.parseColor("#EB5757"));
        }
        holder.txtJumlahTransaksi.setText(formatBalance(Integer.parseInt(holder.txtJumlahTransaksi.getText().toString())));
    }

    @Override
    public int getItemCount() {
        return arrayListHistory.size();
    }

    public void updateArray(ArrayList<History> tempArrHistory){
        arrayListHistory = tempArrHistory;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        ImageView iconTransaksi;
        TextView txtJudulTransaksi, txtDurasi, txtJumlahTransaksi;
        public HistoryViewHolder(@NonNull View itemView){
            super(itemView);
            iconTransaksi = itemView.findViewById(R.id.iconTransaksi);
            txtJudulTransaksi = itemView.findViewById(R.id.txtJudulTransaksi);
            txtDurasi = itemView.findViewById(R.id.txtDurasi);
            txtJumlahTransaksi = itemView.findViewById(R.id.txtJumlahTransaksi);
        }
    }
}

package com.example.expensetracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class History implements Parcelable {
    private int id;
    private int userId;
    private String transaksiJudul;
    private LocalDateTime dateTime;
    private int jumlahTransaksi;
    private int tipeTransaksi;

    public History(){}

    public History(int id, int userId, String transaksiJudul, LocalDateTime dateTime, int jumlahTransaksi, int tipeTransaksi) {
        this.id = id;
        this.userId = userId;
        this.transaksiJudul = transaksiJudul;
        this.dateTime = dateTime;
        this.jumlahTransaksi = jumlahTransaksi;
        this.tipeTransaksi = tipeTransaksi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTransaksiJudul() {
        return transaksiJudul;
    }

    public void setTransaksiJudul(String transaksiJudul) {
        this.transaksiJudul = transaksiJudul;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(int jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public int getTipeTransaksi() {
        return tipeTransaksi;
    }

    public void setTipeTransaksi(int tipeTransaksi) {
        this.tipeTransaksi = tipeTransaksi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.transaksiJudul);
        dest.writeSerializable(this.dateTime);
        dest.writeInt(this.jumlahTransaksi);
        dest.writeInt(this.tipeTransaksi);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.userId = source.readInt();
        this.transaksiJudul = source.readString();
        this.dateTime = (LocalDateTime) source.readSerializable();
        this.jumlahTransaksi = source.readInt();
        this.tipeTransaksi = source.readInt();
    }

    protected History(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.transaksiJudul = in.readString();
        this.dateTime = (LocalDateTime) in.readSerializable();
        this.jumlahTransaksi = in.readInt();
        this.tipeTransaksi = in.readInt();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}

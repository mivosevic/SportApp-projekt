package com.example.rivios_.sportapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 26.5.2016..
 */
public class Game implements Parcelable{
    private long id;
    private String team1;
    private String team2;
    private int result1;
    private int result2;
    private Date datum;
    private String winner;

    public Game() {
        this.id = 0;
        this.team1 = null;
        this.team2 = null;
        this.result1 = 0;
        this.result2 = 0;
        this.datum = null;
        this.winner = null;
    }

    public Game(long id, String team1, String team2, int result1, int result2, Date datum, String winner) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.result1 = result1;
        this.result2 = result2;
        this.datum = datum;
        this.winner = winner;
    }

    protected Game(Parcel in) {
        id = in.readLong();
        team1 = in.readString();
        team2 = in.readString();
        result1 = in.readInt();
        result2 = in.readInt();
        winner = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public int getResult1() {
        return result1;
    }

    public int getResult2() {
        return result2;
    }

    public Date getDatum() {
        return datum;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setResult1(int result1) {
        this.result1 = result1;
    }

    public void setResult2(int result2) {
        this.result2 = result2;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(team1);
        parcel.writeString(team2);
        parcel.writeInt(result1);
        parcel.writeInt(result2);
        parcel.writeString(winner);
    }
}

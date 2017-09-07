package com.example.rivios_.sportapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by admin on 26.5.2016..
 */
public class GameStatsAdapter extends BaseAdapter{
    ArrayList<Game> games;

    public GameStatsAdapter(ArrayList<Game> games) {
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return games.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.list_element, null);

            holder = new GameViewHolder();
            holder.tvTeam1 = (TextView) convertView.findViewById(R.id.tvTeam1);
            holder.tvTeam2 = (TextView) convertView.findViewById(R.id.tvTeam2);
            holder.tvRezultat = (TextView) convertView.findViewById(R.id.tvRezultat);
            holder.tvDatum = (TextView) convertView.findViewById(R.id.tvDatum);

            convertView.setTag(holder);
        }
        else {
            holder = (GameViewHolder) convertView.getTag();
        }

        Game current = games.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

        holder.tvTeam1.setText(current.getTeam1());
        holder.tvTeam2.setText(current.getTeam2());
        holder.tvRezultat.setText(current.getResult1() + " : " + current.getResult2());
        holder.tvDatum.setText(sdf.format(current.getDatum()));

        return convertView;
    }

    static class GameViewHolder {
        private TextView tvTeam1;
        private TextView tvTeam2;
        private TextView tvRezultat;
        private TextView tvDatum;
    }
}
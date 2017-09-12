package com.example.rivios_.sportapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rivios_.sportapp.data.BasketballPlayerStats;

import java.util.ArrayList;

/**
 * Created by admin on 6.6.2016..
 */
public class PlayerStatsAdapter extends BaseAdapter {
    ArrayList<BasketballPlayerStats> playerStats;

    public PlayerStatsAdapter(ArrayList<BasketballPlayerStats> playerStats) {
        this.playerStats = playerStats;
    }

    @Override
    public int getCount() {
        return playerStats.size();
    }

    @Override
    public Object getItem(int position) {
        return playerStats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return playerStats.get(position).getAthlete().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StatsViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.list_player_element, null);

            holder = new StatsViewHolder();
            holder.tvNamePos = (TextView) convertView.findViewById(R.id.tvNamePos);
            holder.tvStats = (TextView) convertView.findViewById(R.id.tvStats);

            convertView.setTag(holder);
        } else {
            holder = (StatsViewHolder) convertView.getTag();
        }

        BasketballPlayerStats current = playerStats.get(position);

        String str;
        if (current.getGameCount() == 0)
        {
            str = current.getStats().getTeam();
        }
        else
        {
            str = Integer.toString(current.getGameCount());
        }

        holder.tvNamePos.setText(current.getAthlete().getName() + ",  " + current.getAthlete().getNickname() + ", " + str);
        holder.tvStats.setText("PTS:  " + current.getStats().getPoints() +
                "   AST: " + current.getStats().getAssists() +
                "   RBD: " + current.getStats().getJumps());

        return convertView;
    }

    static class StatsViewHolder {
        private TextView tvNamePos;
        private TextView tvStats;
    }
}

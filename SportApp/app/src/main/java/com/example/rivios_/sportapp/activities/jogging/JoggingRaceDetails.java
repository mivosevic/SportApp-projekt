package com.example.rivios_.sportapp.activities.jogging;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DeleteDialog;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.Athlete;
import com.example.rivios_.sportapp.data.JoggingRace;
import com.example.rivios_.sportapp.data.JoggingRunnerStats;
import com.example.rivios_.sportapp.data.JoggingStats;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class JoggingRaceDetails extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, AdapterView.OnItemLongClickListener, DeleteDialog.DeleteDialogListener {

    private GoogleMap mMap;
    private ArrayList<JoggingRunnerStats> rs = new ArrayList<JoggingRunnerStats>();

    ListView lvRunners;
    ArrayAdapter<JoggingRunnerStats> adapter;

    JoggingRace race;
    GameDBHelper dbHelper = GameDBHelper.getInstance(this);

    int deletePos = -1;
    long deleteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogging_race_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        race = i.getParcelableExtra("RACE");

        // dohvati listu statistika koje su vezane uz trenutnu utrku pomoću id-a utrke
        ArrayList<JoggingStats> stats = dbHelper.getJoggingRunnerStats(race.getRaceId(), false);
        for (JoggingStats st : stats)
        {
            // za svaku statistiku, iskombiniraj sportaša s runnerId-em te statistike i njegovu statistiku u novi RunnerStats
            rs.add(new JoggingRunnerStats(dbHelper.getAthlete(st.getRunnerId()), st));  // dodaj RunnerStats na listu za listview
        }

        lvRunners = (ListView) findViewById(R.id.lvRunnerStats);    //nađi listview
        adapter = new ArrayAdapter<JoggingRunnerStats>(this, android.R.layout.simple_list_item_1, rs);  // popuni adapter
        lvRunners.setAdapter(adapter); // poveži adapter i listu
        lvRunners.setOnItemLongClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this); // slušaj kada će mapa biti učitana
    }

    private com.google.android.gms.maps.model.LatLng convert(LatLng ll)
    {
        return new com.google.android.gms.maps.model.LatLng(ll.lat, ll.lng);
    }

    @Override
    public void onMapLoaded() { // kad je mapa učitana
        if (race != null)
        {
            // Dohvati komprimirani zapis rute utrke
            EncodedPolyline encp = new EncodedPolyline(race.getEncodedRoute());

            ArrayList<com.google.android.gms.maps.model.LatLng> points = new ArrayList();
            for (LatLng ll : encp.decodePath()) // naredbom decodePath iz komprimiranog zapisa napravi listu LatLng
            {
                points.add(convert(ll));
            }

            // nacrtaj PolyLine na mapi iz dekodirane liste
            mMap.addPolyline(new PolylineOptions().addAll(points));

            // dodaj marker na početak rute
            Marker m1 = mMap.addMarker(new MarkerOptions().position(points.get(0))
                    .title("Start")
                    .snippet(race.getStart()));
            // dodaj marker na kraj liste
            Marker m2 = mMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1))
                    .title("Cilj")
                    .snippet(race.getFinish()));
            // napravi Bounds objekt (granice koje uokviruju dodanu rutu)
            LatLngBounds b =  new LatLngBounds.Builder().include(m1.getPosition())
                    .include(m2.getPosition())
                    .build();
            // zumiraj mapu na bounds
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(b, 10));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
        DeleteDialog delDialog =  new DeleteDialog();
        delDialog.setListener(this);
        deletePos = pos;
        deleteId = id;
        delDialog.show(getFragmentManager(), Constants.DELETE_DIALOG_TAG);

        return true;
    }

    @Override
    public void onDialogClick(boolean yes) {
        if (yes) {
            if (dbHelper.deleteJoggingRaceRunnerStats(rs.get(deletePos).getStats().getRaceId(), rs.get(deletePos).getStats().getRunnerId())) {
                rs.remove(deletePos);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
            Intent i = new Intent();
            i.setClass(this, JoggingRunners.class);
            i.putExtra(Constants.ATHLETE_TAG, rs.get(deletePos).getRunner());
            i.putExtra(Constants.STATS_TAG, rs.get(deletePos).getStats());
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        rs.clear();
        ArrayList<JoggingStats> stats = dbHelper.getJoggingRunnerStats(race.getRaceId(), false);

        for (JoggingStats st : stats)
        {
            rs.add(new JoggingRunnerStats(dbHelper.getAthlete(st.getRunnerId()), st));
        }
        adapter.notifyDataSetChanged();
    }
}

package com.example.android.jst20;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//Ubidots imports

import android.os.Handler;

import com.github.mikephil.charting.data.Entry;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

//GoogleMap object
GoogleMap m_map;
boolean mapReady = false;
//For polylines
LatLng collegeHall2 = new LatLng(27.525138, -97.882431);
LatLng walMart2 = new LatLng(27.489250, -97.853612);





//MarkerOptions Class defines marker
MarkerOptions turnerBishop;
MarkerOptions mesquiteVillage;
MarkerOptions businessBuilding;
MarkerOptions javelinaStation;
MarkerOptions jerniganLibrary;
//Ubidots variables
private String API_KEY = "HKM7SOBdxgPaW9hVNgquUU0IWYP7sv";
private String coordinatesID = "56d5d69d7625424120f93ca2";
private String coords;
private String lat1;
private String lng1;

@Override
public Resources getResources() {
    return super.getResources();
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //MarkerOptions created
    turnerBishop = new MarkerOptions()
            .position(new LatLng(27.523646, -97.884196))
            .title("Bishop Hall")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop));//Custom icon
    mesquiteVillage = new MarkerOptions()
            .position(new LatLng(27.526586, -97.884106))
            .title("Mesquite Village")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop));//Custom icon
    businessBuilding = new MarkerOptions()
            .position(new LatLng(27.527186, -97.882553))
            .title("Business Building")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop));//Custom icon
    javelinaStation = new MarkerOptions()
            .position(new LatLng(27.530239, -97.883928))
            .title("Javelina Station")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop));//Custom icon
    jerniganLibrary= new MarkerOptions()
            .position(new LatLng(27.525628, -97.882072))
            .title("Jernigan Library")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop));//Custom icon

    //Wiring button for flyTo
    Button btnWalmart = (Button) findViewById(R.id.btnWalmart);
    btnWalmart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mapReady)
                getCoords();
        }
    });

    //Setting Normal Button
    Button btnMap = (Button) findViewById(R.id.btnMap);
    btnMap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mapReady)
                m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    });
    //Setting Satellite Button
    Button btnSatellite = (Button) findViewById(R.id.btnSatellite);
    btnSatellite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mapReady)
                m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    });
    //Setting Hybrid Button
    Button btnHybrid = (Button) findViewById(R.id.btnHybrid);
    btnHybrid.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mapReady)
                m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    });

    MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
}
public void getCoords() {
    (new UbidotsClient()).handleUbidots(coordinatesID, API_KEY, new UbidotsClient.UbiListener() {
        @Override
        public void onDataReady(List<UbidotsClient.Value> result) {
            Log.d("Coords", "======== On data Ready ===========");

            List<Entry> entries = new ArrayList();


            Entry be = new Entry(result.get(0).value, 0);
            entries.add(be);
            Log.d("Data", be.toString());

            final String con = result.get(0).context;


            Handler handler = new Handler(MainActivity.this.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    coords = con;
                    //"{"Lat": "  27.495609", "Long": " -97.870003"}"
                    StringTokenizer tokens = new StringTokenizer(coords, "\"");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();
                    String third = tokens.nextToken();

                    lng1 = tokens.nextToken();//-97

                    String fifth = tokens.nextToken();
                    String sixth = tokens.nextToken();
                    String seventh = tokens.nextToken();

                    lat1 = tokens.nextToken();//27


                    //"{"Lat": "  27.495609", "Long": " -97.870003"}"

                    double lng = Double.parseDouble(lng1);
                    double lat = Double.parseDouble(lat1);
                    //creating a CameraPosition to flyTo
                    CameraPosition BUS = CameraPosition.builder()
                            .target(new LatLng(lat, lng))
                            .zoom(17)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    flyTo(BUS);
                }
            });

        }
    });


}

private void flyTo(CameraPosition target) {
    m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 1000, null);


}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu_main; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }

    return super.onOptionsItemSelected(item);
}

//@params Passing GoogleMap map to m_map
@Override
public void onMapReady(GoogleMap map) {
    mapReady = true;
    m_map = map;
    //Add markers here
    m_map.addMarker(turnerBishop);
    m_map.addMarker(jerniganLibrary);
    m_map.addMarker(javelinaStation);
    m_map.addMarker(mesquiteVillage);
    m_map.addMarker(businessBuilding);

    //Setting and view collegeHall in three lines and can add more functionality
    LatLng collegeHall = new LatLng(27.525138, -97.882431);
    CameraPosition target = CameraPosition.builder().target(collegeHall).zoom(18).build();
    m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

    //Creating lines
    map.addPolyline(new PolylineOptions().geodesic(true)
            .add(collegeHall2)
            .add(walMart2));

    //map.addPolygon(new PolygonOptions().add(collegeHall2, walMart2).fillColor(Color.GREEN));

    //Making a circle
    map.addCircle(new CircleOptions()
            .center(collegeHall2)
            .radius(5000)
            .strokeColor(Color.GREEN)
            .fillColor(Color.argb(64, 0, 255, 0)));//partialy transparent

//    Creating initial flyTo for NEWYORK
//    m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//    flyTo(NEWYORK);
}
}
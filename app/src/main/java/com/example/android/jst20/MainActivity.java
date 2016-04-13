package com.example.android.jst20;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
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


import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//creating a CameraPosition to flyTo
static final CameraPosition WALMART = CameraPosition.builder()
        .target(new LatLng(27.489250, -97.853612))
        .zoom(17)
        .bearing(0)
        .tilt(45)
        .build();
//GoogleMap object
GoogleMap m_map;
boolean mapReady = false;
//MarkerOptions Class defines marker
MarkerOptions bishopHall;
//For polylines
LatLng collegeHall2 = new LatLng(27.525138, -97.882431);
LatLng walMart2 = new LatLng(27.489250, -97.853612);

@Override
public Resources getResources() {
    return super.getResources();
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //MarkerOptions created
    bishopHall = new MarkerOptions()
            .position(new LatLng(27.525357, -97.884611))
            .title("Bishop Hall")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));//Custom icon


    //Wiring button for flyTo
    Button btnWalmart = (Button) findViewById(R.id.btnWalmart);
    btnWalmart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mapReady)
                flyTo(WALMART);
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

private void flyTo(CameraPosition target) {
    m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 10000, null);


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
    m_map.addMarker(bishopHall);
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
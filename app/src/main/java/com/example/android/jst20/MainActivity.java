package com.example.android.jst20;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//Ubidots imports

import android.os.Handler;

import com.github.mikephil.charting.data.Entry;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//For "About" fragment
boolean aboutOn = false;
//GoogleMap object
GoogleMap m_map;
boolean mapReady = false;
//For polylines
LatLng collegeHall2 = new LatLng(27.525138, -97.882431);
LatLng walMart2 = new LatLng(27.489250, -97.853612);


//MarkerOptions Class defines Blue route marker
MarkerOptions turnerBishop;
MarkerOptions mesquiteVillage;
MarkerOptions businessBuilding;
MarkerOptions javelinaStation;
MarkerOptions jerniganLibrary;
//MarkerOptions for Gold route
MarkerOptions wAveCParkingLot;
MarkerOptions steinkeGymParkingLot;
MarkerOptions klebergHall;
MarkerOptions manningHallParkingLot;
MarkerOptions englishMusicParkingLot;
MarkerOptions studentUnionBuilding;

LatLng Student_Union_Building = new LatLng(27.523337, -97.882248);


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


    //MarkerOptions for Blue route created
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

    //MarkerOptions for Gold route created
    wAveCParkingLot = new MarkerOptions()
            .position(new LatLng(27.527511, -97.878625))
            .title("West Avenue C Parking Lot")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop));//Custom icon
    steinkeGymParkingLot = new MarkerOptions()
            .position(new LatLng(27.526617, -97.878936))
            .title("Steinke Gym Parking Lot")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop));//Custom icon
    klebergHall = new MarkerOptions()
            .position(new LatLng(27.526379, -97.881028))
            .title("Kleberg Hall")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop));//Custom icon
    englishMusicParkingLot = new MarkerOptions()
            .position(new LatLng(27.525152, -97.879977))
            .title("English & Music Parking Lot")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop));//Custom icon
    manningHallParkingLot = new MarkerOptions()
            .position(new LatLng(27.523675, -97.878681))
            .title("Manning Hall")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop));//Custom icon
    studentUnionBuilding= new MarkerOptions()
            .position(new LatLng(27.523337, -97.882248))
            .title("Student Union Building")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop));//Custom icon
    //Wiring button for flyTo
    Button btnBus = (Button) findViewById(R.id.btnBus);
    btnBus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mapReady)
                getCoords();
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
                    //Tokenize the string of the JSON object
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
                    m_map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("BUS").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker)));
                    //creating a CameraPosition to flyTo
                    CameraPosition BUS = CameraPosition.builder()
                            .target(new LatLng(lat, lng))
                            .zoom(18)
                            .bearing(0)
                            .tilt(15)
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
    switch (item.getItemId()) {
        case R.id.action_about:
            Fragment newFragment = new about();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(aboutOn==false){
            // User chose the "Settings" item, show the app settings UI...
            //return true;


            fragmentTransaction.replace(R.id.map, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            aboutOn=true;
            }
            else{

            fragmentTransaction.replace(R.id.map, newFragment);
                fragmentTransaction.remove(newFragment);
                fragmentTransaction.commit();
                aboutOn=false;
            }

//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fT = fragmentManager.beginTransaction();
//            about fA = new about();
//            fT.replace(android.R.id.content, fA);
//            fT.commit();
            return true;
        case R.id.action_map:
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            //return true;
            m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        case R.id.action_hybrid:
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            //return true;
            m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        case R.id.action_satellite:
            // User chose the "Favorite" action, mark the current item
            // as a favorite...

            m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        default:
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);

    }
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
    m_map.addMarker(wAveCParkingLot);
    m_map.addMarker(steinkeGymParkingLot);
    m_map.addMarker(klebergHall);
    m_map.addMarker(englishMusicParkingLot);
    m_map.addMarker(manningHallParkingLot);
    m_map.addMarker(studentUnionBuilding);
    //Newman Hall
    LatLng Newman_Hall = new LatLng(27.529989, -97.884042);
    m_map.addMarker(new MarkerOptions().position(Newman_Hall).title("Newman Hall").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop)));
    //Javelina Dining Hall
    LatLng Javelina_Dining_Hall = new LatLng(27.525364, -97.884648);
    m_map.addMarker(new MarkerOptions().position(Javelina_Dining_Hall).title("Javelina Dining Hall").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop)));
    //Kingsville Legends
    LatLng Kingsville_Legends = new LatLng(27.522352, -97.886422);
    m_map.addMarker(new MarkerOptions().position(Kingsville_Legends).title("Kingsville Legends").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_bus_stop)));
    //Gold Bus route start(University Square)
    LatLng University_Square = new LatLng(27.530368, -97.881767);
    m_map.addMarker(new MarkerOptions().position(University_Square).title("University Square Apartments").icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_bus_stop)));
    //Setting and view collegeHall in three lines and can add more functionality
    LatLng collegeHall = new LatLng(27.525138, -97.882431);
    m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    CameraPosition target = CameraPosition.builder().target(collegeHall).zoom(16).build();
    m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));


    //markers for bus stops outside of TAMUK campus
//Shell Store
    LatLng Shell_Store = new LatLng(27.53047, -97.87773);
    m_map.addMarker(new MarkerOptions().position(Shell_Store).title("Shell Store").icon(BitmapDescriptorFactory.fromResource(R.drawable.conveniencestore)));

//Post Office and Kleberg Bank
    LatLng Kleberg_Bank_and_Post_Office = new LatLng(27.516722, -97.867930);
    m_map.addMarker(new MarkerOptions().position(Kleberg_Bank_and_Post_Office).title("Kleberg Bank and Post Office").icon(BitmapDescriptorFactory.fromResource(R.drawable.postal)));

//HEB
    LatLng HEB = new LatLng(27.516033, -97.863034);
    m_map.addMarker(new MarkerOptions().position(HEB).title("HEB").icon(BitmapDescriptorFactory.fromResource(R.drawable.supermarket)));

//Wells Fargo
    LatLng Wells_Fargo = new LatLng(27.511336, -97.856504);
    m_map.addMarker(new MarkerOptions().position(Wells_Fargo).title("Wells Fargo").icon(BitmapDescriptorFactory.fromResource(R.drawable.bank)));

//Walgreens
    LatLng Walgreens = new LatLng(27.516001, -97.856611);
    m_map.addMarker(new MarkerOptions().position(Walgreens).title("Walgreens").icon(BitmapDescriptorFactory.fromResource(R.drawable.conveniencestore)));

//Sundial Plaza
    LatLng Sundial_Plaza = new LatLng(27.506978, -97.854911);
    m_map.addMarker(new MarkerOptions().position(Sundial_Plaza).title("Sundial Plaza").icon(BitmapDescriptorFactory.fromResource(R.drawable.sandwich)));

//Southgate Mall
    LatLng Southgate_Mall = new LatLng(27.494645, -97.857428);
    m_map.addMarker(new MarkerOptions().position(Southgate_Mall).title("Southgate Mall").icon(BitmapDescriptorFactory.fromResource(R.drawable.mall)));

//WalMart
    LatLng Walmart = new LatLng(27.489848, -97.853845);
    m_map.addMarker(new MarkerOptions().position(Walmart).title("Walmart").icon(BitmapDescriptorFactory.fromResource(R.drawable.supermarket)));

//Whataburger&Chilis
    LatLng Whataburger_Chilis = new LatLng(27.490495, -97.848534);
    m_map.addMarker(new MarkerOptions().position(Whataburger_Chilis).title("Whataburger & Chilis").icon(BitmapDescriptorFactory.fromResource(R.drawable.burger)));

//Wildhorse mall
    LatLng Wildhorse = new LatLng(27.500853, -97.842932);
    m_map.addMarker(new MarkerOptions().position(Wildhorse).title("Wildhorse Mall").icon(BitmapDescriptorFactory.fromResource(R.drawable.cinema)));
//f1f73e
    //Creating lines
//    map.addPolyline(new PolylineOptions().geodesic(true).width(15).color(getResources().getColor(R.color.colorPrimary))
//            .add(collegeHall2)
//            .add(walMart2));
//bus routes
    //blue bus route (left side of campus)
    m_map.addPolyline(new PolylineOptions()
            .add(new LatLng(27.530239, -97.883928)).add(new LatLng(27.530249, -97.882496))
            .add(new LatLng(27.526500, -97.882464)).add(new LatLng(27.526462, -97.885661))
            .add(new LatLng(27.523646, -97.885634)).add(new LatLng(27.523646, -97.884196))
            .add(new LatLng(27.525121, -97.884175)) .add(new LatLng(27.525121, -97.883102))
                    //start of loop around College Hall
            .add(new LatLng(27.524948, -97.883026)).add(new LatLng(27.524759, -97.882877))
            .add(new LatLng(27.524657, -97.882662)).add(new LatLng(27.524578, -97.882405))
            .add(new LatLng(27.524709, -97.882083)).add(new LatLng(27.524854, -97.881911))
            .add(new LatLng(27.525032, -97.881831)).add(new LatLng(27.525187, -97.881782))
            .add(new LatLng(27.525408, -97.881831)).add(new LatLng(27.525598, -97.881997))
            .add(new LatLng(27.525684, -97.882195)).add(new LatLng(27.525723, -97.882436))
            .add(new LatLng(27.526491, -97.882436))
            .width(15)
            .color(getResources().getColor(R.color.colorPrimary)));
    //gold bus route (right side of campus)
    m_map.addPolyline(new PolylineOptions()
            .add(new LatLng(27.530365, -97.881690)).add(new LatLng(27.530253, -97.882495))
            .add(new LatLng(27.527342, -97.882441)).add(new LatLng(27.527528, -97.877908))
            .add(new LatLng(27.526543, -97.877881)).add(new LatLng(27.526500, -97.881202))
            .add(new LatLng(27.525168, -97.881181)).add(new LatLng(27.525206, -97.877844))
            .add(new LatLng(27.525206, -97.877844)).add(new LatLng(27.523465, -97.877820))
            .add(new LatLng(27.523518, -97.878992)).add(new LatLng(27.522571, -97.878997))
            .add(new LatLng(27.522557, -97.880842)).add(new LatLng(27.522614, -97.881159))
            .add(new LatLng(27.522590, -97.882237))
                    //start of loop around College Hall
            .add(new LatLng(27.524598, -97.882309)).add(new LatLng(27.524709, -97.882081))
            .add(new LatLng(27.524854, -97.881909)).add(new LatLng(27.525032, -97.881828))
            .add(new LatLng(27.525187, -97.881779)).add(new LatLng(27.525408, -97.881828))
            .add(new LatLng(27.525598, -97.881994)).add(new LatLng(27.525684, -97.882194))
            .add(new LatLng(27.525723, -97.882439)).add(new LatLng(27.527342, -97.882441))
            .width(15)
            .color(Color.YELLOW));


}
}
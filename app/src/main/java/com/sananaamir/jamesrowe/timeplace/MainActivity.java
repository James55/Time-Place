package com.sananaamir.jamesrowe.timeplace;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    Location currentLocation;
    String writtenLocation;
    Address currentAddress;

    ArrayList<String> times;
    ArrayAdapter<String> timesAdapter;
    ListView listTime;

    ArrayList<String> locations;
    ArrayAdapter<String> locationsAdapter;
    ListView listLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the written location
        writtenLocation = "UNDEFINED";

        //Initialize the list arrays and their respective adapters
        //Initialize Time components
        listTime = (ListView) findViewById(R.id.listTime);
        times = new ArrayList<String>();
        timesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, times);
        listTime.setAdapter(timesAdapter);
        //Initialize Location components
        listLocation = (ListView) findViewById(R.id.listLocation);
        locations = new ArrayList<String>();
        locationsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, locations);
        listLocation.setAdapter(locationsAdapter);

        //Location Manager
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                currentLocation = location;

                String address = null;
                String cityName = null;
                String country = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    cityName = addresses.get(0).getLocality();
                    address = addresses.get(0).getAddressLine(0);
                    country = addresses.get(0).getAddressLine(2);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                writtenLocation = address;
                if(address != null)
                    writtenLocation += "\n";
                writtenLocation += (cityName + ", " + country);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void markLocation(View view) {
        //Determine current location and time

        //Add loc/time to the listview

        Calendar calendar = Calendar.getInstance();
        int clockHour = calendar.get(Calendar.HOUR);
        String actualHour = "";

        //Hour formatting
        if(clockHour == 0)
            actualHour = "12";
        else if(clockHour > 12)
            actualHour = (clockHour - 12) + "";
        else
            actualHour = clockHour + "";

        int minute = calendar.get(Calendar.MINUTE);
        String actualMinute = "" + minute;
        if(minute < 10)
            actualMinute = "0" + minute;

        String clockTime = actualHour + ":" + actualMinute;

        //AM or PM Formatting
        if(clockHour > 11)
            clockTime += " p.m.\n";
        else
            clockTime += " a.m.\n";

        //Add the calendar date
        String day = calendar.get(Calendar.DATE) + "";
        int month = calendar.get(Calendar.MONTH);
        String actualMonth = (month + 1) + "";
        String year = calendar.get(Calendar.YEAR) + "";
        year = year.substring(2);
        //Add a 0 to the front if a single digit month
        //if(month < 9)
        //	month = "0" + month;

        clockTime += (actualMonth + '-' + day + '-' + year);

        timesAdapter.add(clockTime);

        //Write the location to the locations list
        locationsAdapter.add(writtenLocation + "\n");
    }
}

package com.example.root.geolocation3d;

import android.graphics.Color;
import android.location.GpsSatellite;
import android.content.Context;
import android.location.Location;
import android.location.LocationProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.GpsStatus;

import java.util.Iterator;

public class MainActivity extends ActionBarActivity {
    TextView mTextView;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView4;
    TextView mTextView5;
    TextView mTextView6;
    TableLayout mTableLayout;
    LocationManager locationManager;
    GpsStatus gpsStatus = null;

    MainActivity tthis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tthis = this;

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView3 = (TextView) findViewById(R.id.textView3);
        mTextView4 = (TextView) findViewById(R.id.textView4);
        mTextView5 = (TextView) findViewById(R.id.textView5);
        mTextView6 = (TextView) findViewById(R.id.textView6);
        mTableLayout = (TableLayout) findViewById(R.id.tableLayout);

        mTextView.setText("Info about system:");
        mTextView5.setText("Waiting for satelites ...");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String info = String.format("Latitude: %f", latitude);
                mTextView2.setText(info);

                info = String.format("Longitude: %f", longitude);
                mTextView3.setText(info);

                double altitude = location.getAltitude();
                info = String.format("Altitude: %f", altitude);
                mTextView4.setText(info);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch(status) {
                    case LocationProvider.AVAILABLE: {}
                    break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE: {}
                    break;
                    case LocationProvider.OUT_OF_SERVICE: {}break;
                }
            }

            public void onProviderEnabled(String provider) {
                String info = String.format("Info about system: %s is enadbled", provider);
                mTextView.setText(info);
            }

            public void onProviderDisabled(String provider) {
                String info = String.format("Info about system: %s is disabled", provider);
                mTextView.setText(info);
            }
        };

        /*GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
            public void onNmeaReceived(long timestamp, String nmea) {
                //String info = String.format("timestamp = %l", timestamp);
                mTextView6.setText(nmea);
            }
        };*/

        GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
            public void onGpsStatusChanged(int event) {
                gpsStatus = locationManager.getGpsStatus(gpsStatus);

                switch(event) {
                    case GpsStatus.GPS_EVENT_FIRST_FIX: {
                        //String.format("GPS_EVENT_FIRST_FIX %d", gpsStatus.getTimeToFirstFix());
                    }; break;
                    case GpsStatus.GPS_EVENT_STARTED: {}; break;
                    case GpsStatus.GPS_EVENT_STOPPED: {}; break;
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {
                        int satListCount = 0;

                        mTableLayout.removeAllViews();
                        mTableLayout.setBackgroundColor(Color.BLACK);

                        TableRow row = new TableRow(tthis);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);
                        row.setBackgroundColor(Color.WHITE);

                        TextView text1 = new TextView(tthis);
                        TextView text2 = new TextView(tthis);
                        TextView text3 = new TextView(tthis);
                        TextView text4 = new TextView(tthis);
                        TextView text5 = new TextView(tthis);
                        TextView text6 = new TextView(tthis);

                        text1.setText("id");
                        text2.setText("Prn");
                        text3.setText("InFix");
                        text4.setText("Snr");
                        text5.setText("Azi");
                        text6.setText("Ele");

                        row.addView(text1);
                        row.addView(text2);
                        row.addView(text3);
                        row.addView(text4);
                        row.addView(text5);
                        row.addView(text6);
                        mTableLayout.addView(row, satListCount);

                        for(GpsSatellite satellite : gpsStatus.getSatellites()) {
                            satListCount++;
                            row = new TableRow(tthis);
                            lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            row.setBackgroundColor(Color.WHITE);

                            text1 = new TextView(tthis);
                            text2 = new TextView(tthis);
                            text3 = new TextView(tthis);
                            text4 = new TextView(tthis);
                            text5 = new TextView(tthis);
                            text6 = new TextView(tthis);

                            text1.setText( String.format("%d", satListCount) );
                            text2.setText( String.format("%d", satellite.getPrn()) );
                            text3.setText( satellite.usedInFix() ? "true" : "false" );
                            text4.setText( String.format("%.2f", satellite.getSnr()) );
                            text5.setText( String.format("%.2f", satellite.getAzimuth()) );
                            text6.setText( String.format("%.2f", satellite.getElevation()) );

                            row.addView(text1);
                            row.addView(text2);
                            row.addView(text3);
                            row.addView(text4);
                            row.addView(text5);
                            row.addView(text6);

                            mTableLayout.addView(row, satListCount);
                        }

                        String info = String.format("Satelites: %d = %d", satListCount, gpsStatus.getTimeToFirstFix());
                        mTextView5.setText(info);
                    }; break;
                }
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //locationManager.addNmeaListener(nmeaListener);
        locationManager.addGpsStatusListener(gpsStatusListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

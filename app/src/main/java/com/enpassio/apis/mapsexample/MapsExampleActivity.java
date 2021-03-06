package com.enpassio.apis.mapsexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enpassio.apis.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* References/resources
 * https://stackoverflow.com/a/33023788   --->> Location listeners
 * https://developer.android.com/guide/topics/location/strategies
 * https://stackoverflow.com/a/27504607  --->> Draggable maps
 * https://github.com/Sishin/MapLocation  --->> Uber like map
 * https://github.com/amanjeetsingh150/UberCarAnimation   --->> Uber car animation
 * https://gist.github.com/rajtheinnovator/d3ae76273f7a2acbe161868df684c6be   --->> Turn on Gps programmatically
 * https://developer.android.com/training/location/receive-location-updates
 * https://www.journaldev.com/13325/android-location-api-tracking-gps   --->> LocationListener with lifecycle
 * https://stackoverflow.com/a/37048987   --->> Rotate map and car animation
 * https://www.ipragmatech.com/add-custom-image-in-google-maps-marker-android/   --->>Include bounds/markers for proper zooming
 * https://stackoverflow.com/a/36746136   --->> Google maps intent
 * https://stackoverflow.com/a/13912034   --->> Car animation for single set of source & destination
 * */
public class MapsExampleActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location mLocation;
    public static final int LOCATION_PERMISSION_CODE = 2;
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 1133;
    //handle gps
    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    private LocationListener mLocationListener;
    private CameraPosition cameraPosition;
    private ImageView markerIconView;
    private TextView markerLocationNameTextView;
    private Button parseJsonButton;
    private TextView parsedJsonData;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private GoogleMap.OnCameraMoveStartedListener onCameraMoveStartedListener;
    //dummy data for route
    ArrayList<LatLng> listOfPoints;
    private Marker mMarker;
    boolean isMarkerRotating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_example);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        markerIconView = findViewById(R.id.marker_icon_view);
        markerLocationNameTextView = findViewById(R.id.location_name_text_view);
        parseJsonButton = findViewById(R.id.parse_json);
        parsedJsonData = findViewById(R.id.parsed_json_data);

        //new location details
        listOfPoints = new ArrayList<>();
        listOfPoints.add(new LatLng(12.9172279, 77.61009200000001));
        listOfPoints.add(new LatLng(12.9215591, 77.61116079999999));
        listOfPoints.add(new LatLng(12.9346852, 77.60970669999999));
        listOfPoints.add(new LatLng(12.936785, 77.6036768));
        listOfPoints.add(new LatLng(12.9369538, 77.60213));
        listOfPoints.add(new LatLng(12.9362289, 77.6016369));

        parseJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCarMoveAndHandlemarkerRotation();
            }
        });
        markerIconView.setVisibility(View.GONE);
        markerIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("my_tag", "custom view clicked");
                Toast.makeText(MapsExampleActivity.this, "custom view clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                //Log.d("my_tag", "location fetched inside LocationListener is: " + location.getLatitude() + ", " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        //handle location callback
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        permissionHandle();
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.d("my_tagggsss", "onCameraIdle called");
                handleCameraMove();
                mMap.setOnCameraIdleListener(null);
                mMap.setOnCameraMoveStartedListener(onCameraMoveStartedListener);
            }
        };
        onCameraMoveStartedListener = new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Log.d("my_tagggsss", "onCameraMoveStarted called");
                if (mMap != null) {
                    mMap.clear();
                }
                markerIconView.setVisibility(View.VISIBLE);
                mMap.setOnCameraIdleListener(onCameraIdleListener);
                mMap.setOnCameraMoveStartedListener(null);
            }
        };
    }

    private void animateCarMoveAndHandlemarkerRotation() {
        mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(listOfPoints.get(1).latitude, listOfPoints.get(1).longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
        final Location sourceLocation = new Location(LocationManager.GPS_PROVIDER);
        sourceLocation.setLatitude(listOfPoints.get(2).latitude);
        sourceLocation.setLongitude(listOfPoints.get(2).longitude);
        final Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
        targetLocation.setLatitude(listOfPoints.get(3).latitude);
        targetLocation.setLongitude(listOfPoints.get(3).longitude);
        animateMarker(mMarker, new LatLng(sourceLocation.getLatitude(), sourceLocation.getLongitude()), new LatLng(targetLocation.getLatitude(), targetLocation.getLongitude()), false);
    }

    public void animateMarker(final Marker marker, final LatLng fromPosition, final LatLng toPosition,
                              final boolean hideMarker) {
        if (mMap != null) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final LatLng startLatLng = fromPosition;
            final long duration = 5000;
            final Interpolator interpolator = new LinearInterpolator();
            isMarkerRotating = false;
            rotateMarker(mMarker, bearingBetweenLocations(fromPosition, toPosition));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setVisible(true);
                        }
                    }
                }
            });
        }
    }

    private void rotateMarker(final Marker marker, final double toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = (float) (t * toRotation + (1 - t) * startRotation);

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void permissionHandle() {
        int DeviceSdkVersion = Build.VERSION.SDK_INT;
        if (DeviceSdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkIfLocationPermissionGranted()) {
                if (!gpsIsEnabled()) {
                    enableGps();
                }
                getCurrentLocationOfUser();
            } else {
                getUsersLocationPermission();
            }
        }
    }

    private void getUsersLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_CODE);

    }

    private void enableGps() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(MapsExampleActivity.this, LOCATION_SETTINGS_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //open setting and switch on GPS manually
                            break;
                    }
                }
            }
        });
    }

    private boolean gpsIsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private boolean checkIfLocationPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    if (!gpsIsEnabled()) {
                        enableGps();
                    }
                    getCurrentLocationOfUser();
                } else {
                    //not granted, so don't do anything location related

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocationOfUser() {
        if (checkIfLocationPermissionGranted()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                mLocation = location;
                                List<Address> currentAddress = null;
                                Log.d("my_tag", "location fetched is: " + location.getAltitude());
                                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                                try {
                                    currentAddress = new Geocoder(MapsExampleActivity.this, Locale.getDefault()).getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mMap.addMarker(new MarkerOptions().position(sydney).title(currentAddress.get(0).getFeatureName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                            }
                        }
                    });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        mMap.setOnCameraMoveStartedListener(onCameraMoveStartedListener);
    }

    private void handleCameraMove() {
        Log.d("my_taggg", "handleCameraMove called");
        // Cleaning all the markers.
        if (mMap != null) {
            mMap.clear();
        }
        markerIconView.setVisibility(View.GONE);
        cameraPosition = new CameraPosition.Builder().target(mMap.getCameraPosition().target).zoom(mMap.getCameraPosition().zoom).build();
        mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin)).anchor(0.5f, 0.5f));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        List<Address> currentAddress = null;
        try {
            currentAddress = new Geocoder(MapsExampleActivity.this, Locale.getDefault()).getFromLocation(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        markerLocationNameTextView.setText(currentAddress != null ? currentAddress.get(0).getAddressLine(0) : "");

        //start navigation on click on location text
        markerLocationNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = "com.google.android.apps.maps";
                String query = "google.navigation:q=" + mMap.getCameraPosition().target.latitude + "," + mMap.getCameraPosition().target.longitude;
                Intent intent = MapsExampleActivity.this.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(query));
                startActivity(intent);
            }
        });
    }
}
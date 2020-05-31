package com.example.mymoviememoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.entity.Cinema;
import com.example.mymoviememoir.entity.Person;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    NetworkConnection networkConnection;
    List<LatLng> locations;
    Person person;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        mView = inflater.inflate(R.layout.map_fragment, container, false);
        ((MainActivity)getActivity()).setActionBarTitle("Map");
        mMapView = mView.findViewById(R.id.mapView);
        locations = new ArrayList<>();
        mMapView.onCreate(savedInstanceState);
        networkConnection = new NetworkConnection();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int personID = sharedPref.getInt("personID", 0);
        GetPerson getPerson = new GetPerson();
        getPerson.execute(personID);
        //mMapView = mView.findViewById(R.id.mapView);
        //mMapView.getMapAsync(this);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private class GetPerson extends AsyncTask<Integer, Void, Person> {

        @Override
        protected Person doInBackground(Integer... params) {
            return networkConnection.getPersonByID(params[0]);
        }

        @Override
        protected void onPostExecute(Person details) {
            person = details;
            LatLng loc = getLocationFromAddress(details.getAddress());
            if (loc != null)
                locations.add(loc);
            GetCinemas getCinemas = new GetCinemas();
            getCinemas.execute();
        }
    }

    private class GetCinemas extends AsyncTask<Void, Void, List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(Void... voids) {
            return networkConnection.getAllCinemas();
        }

        @Override
        protected void onPostExecute(final List<Cinema> details) {
            for (Cinema cin : details) {
                LatLng loc = getLocationFromAddress(cin.getPostcode());
                if (loc != null) {
                    locations.add(loc);
                }
                else
                {
                    details.remove(loc);
                }
            }
            mMapView.onResume();
            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    int i = 0;
                    for (LatLng currentlocation : locations) {
                        if (currentlocation == locations.get(0)) {
                            googleMap.addMarker(new MarkerOptions().position(currentlocation).title(person.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));
                            float zoomLevel = (float) 10.0;
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, zoomLevel));
                        } else {
                            googleMap.addMarker(new MarkerOptions().position(currentlocation).title(details.get(i).getName() + ", " + details.get(i).getPostcode()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        }
                        i++;
                    }


                }
            });
        }
    }
}

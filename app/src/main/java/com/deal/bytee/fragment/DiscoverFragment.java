package com.deal.bytee.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.deal.bytee.Utils.MyUtils.getAddress;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deal.bytee.Network.GPSTracker;
import com.deal.bytee.R;
import com.deal.bytee.adapter.HomeRestaurantAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class DiscoverFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    View root;
    int mapType = GoogleMap.MAP_TYPE_NORMAL;
    SupportMapFragment mapFragment;
    OnMapReadyCallback mapReadyCallback;
    private GoogleApiClient googleApiClient;
    private double longitude, latitude;
    private GoogleMap mMap;
    GPSTracker gpsTracker;
    private LinearLayoutCompat llLocation;
    private TextView tvLocation;
    private LinearLayoutCompat lytSearchview;
    private EditText searchview;
    private FragmentContainerView map;
    private RecyclerView rvRestaurant;

    public DiscoverFragment() {
        super(R.layout.fragment_discover);
    }

    private void initView(View view) {
        llLocation = (LinearLayoutCompat) view.findViewById(R.id.llLocation);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        lytSearchview = (LinearLayoutCompat) view.findViewById(R.id.lytSearchview);
        searchview = (EditText) view.findViewById(R.id.searchview);
        rvRestaurant = (RecyclerView) view.findViewById(R.id.rvRestaurant);
    }


    public static final class Companion {
        private Companion() {
        }

        public static final DiscoverFragment instance() {
            return new DiscoverFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(root);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);
        initRestaurant();
        //temp lat long
        latitude = 12.9352;
        longitude = 77.6245;

        mapReadyCallback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.setMapType(mapType);
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .title(getString(R.string.current_location) + ":- \n" + getAddress(latitude, longitude, getActivity())));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            }
        };

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mapFragment.getMapAsync(mapReadyCallback);

        return root;
    }

    private void initRestaurant() {
        rvRestaurant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        String name = DiscoverFragment.class.getName();
        rvRestaurant.setAdapter(new HomeRestaurantAdapter(getActivity(),name));
        rvRestaurant.setNestedScrollingEnabled(false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        //Moving the map
        moveMap(false);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        moveMap(false);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        LatLng latLng;
        latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMapType(mapType);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                //Moving the map
                mMap.clear();
                moveMap(true);
            }
        });
        tvLocation.setText(getAddress(latitude, longitude, getActivity()));
    }

    @SuppressLint("SetTextI18n")
    private void moveMap(boolean isfirst) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title(getString(R.string.set_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (isfirst) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
        tvLocation.setText(getAddress(latitude, longitude, getActivity()));
        //  text.setText("Latitude - " + latitude + "\nLongitude - " + longitude);
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.getMapAsync(mapReadyCallback);
        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(root.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
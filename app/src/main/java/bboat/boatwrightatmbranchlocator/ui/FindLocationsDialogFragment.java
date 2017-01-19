package bboat.boatwrightatmbranchlocator.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import bboat.boatwrightatmbranchlocator.R;

public class FindLocationsDialogFragment extends DialogFragment implements View.OnClickListener {
    private LatLng mCurrentLocation;
    private OnFragmentInteractionListener mListener;
    private TabHost mHost;

    private Button mSubmitButton;
    private EditText mStreetAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZip;
    private EditText mLat;
    private EditText mLng;

    public FindLocationsDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FindLocationsDialogFragment.
     */
    public static FindLocationsDialogFragment newInstance(LatLng currentLocation) {
        FindLocationsDialogFragment fragment = new FindLocationsDialogFragment();
        fragment.mCurrentLocation = currentLocation;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_locations, container, false);
        setupTabHost(view);
        setupUi(view);

        return view;
    }

    private void setupUi(View view) {
        mStreetAddress = (EditText) view.findViewById(R.id.fl_et_streetAddress);
        mCity = (EditText) view.findViewById(R.id.fl_et_city);
        mState = (EditText) view.findViewById(R.id.fl_et_state);
        mZip = (EditText) view.findViewById(R.id.fl_et_zip);

        mLat = (EditText) view.findViewById(R.id.fl_et_lat);
        mLat.setText(String.valueOf(mCurrentLocation.latitude));

        mLng = (EditText) view.findViewById(R.id.fl_et_lng);
        mLng.setText(String.valueOf(mCurrentLocation.longitude));

        mSubmitButton = (Button) view.findViewById(R.id.fl_btn_submit);
        mSubmitButton.setOnClickListener(this);
    }

    private void setupTabHost(View view) {
        mHost = (TabHost) view.findViewById(android.R.id.tabhost);

        mHost.setup();

        TabHost.TabSpec tabpage1 = mHost.newTabSpec("Near Me Tab")
                .setContent(R.id.nearMeTab)
                .setIndicator("Near Me");

        TabHost.TabSpec tabpage2 = mHost.newTabSpec("Address Tab")
                .setContent(R.id.addressTab)
                .setIndicator("By Address");

        TabHost.TabSpec tabpage3 = mHost.newTabSpec("Coords Tab")
                .setContent(R.id.coordsTab)
                .setIndicator("By Coordinates");

        mHost.addTab(tabpage1);
        mHost.addTab(tabpage2);
        mHost.addTab(tabpage3);
    }

    private void onSubmitPressed() {
        String curTabTag = mHost.getCurrentTabTag();
        LatLng location;

        if (curTabTag.equalsIgnoreCase("Near Me Tab")) {
            location = mCurrentLocation;

        } else if (curTabTag.equalsIgnoreCase("Address Tab")) {
            location = getLatLngFromAddress();
        } else {
            double lat = Double.parseDouble(mLat.getText().toString());
            double lng = Double.parseDouble(mLng.getText().toString());
            location = new LatLng(lat, lng);
        }

        if (mListener != null) {
            mListener.onLocationSubmitPressed(location);
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_btn_submit:
                onSubmitPressed();
        }

    }

    public LatLng getLatLngFromAddress() {
        String strAddress = String.format("%s %s %s %s",
                mStreetAddress.getText(), mCity.getText(), mState.getText(), mZip.getText());

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng coords = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            coords = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception e) {
            //Again, would typically handle this properly- but just printing stack to save time
            e.printStackTrace();
        }
        return coords;
    }

    public interface OnFragmentInteractionListener {
        void onLocationSubmitPressed(LatLng location);
    }
}

package bboat.boatwrightatmbranchlocator.webcalls;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import bboat.boatwrightatmbranchlocator.datamodel.ChaseApiResponse;

/**
 * Calls the JPMChase API to retrieve branch and atm locations.
 *
 * Created by Bob Boatwright on 1/16/2017.
 */

public class GetChaseLocationsAsync extends AsyncTask<Void, Void, ChaseApiResponse> {
    private static final String url = "https://m.chase.com/PSRWeb/location/list.action";
    private static final String charset = "UTF-8";

    private OnGetChaseLocationsFinished mOnFinished;
    private double mLat;
    private double mLng;

    public GetChaseLocationsAsync(OnGetChaseLocationsFinished onFinished, final LatLng location) {
        mOnFinished = onFinished;
        mLat = location.latitude;
        mLng = location.longitude;
    }

    @Override
    protected ChaseApiResponse doInBackground(Void... params) {
        try {
            String query = String.format("lat=%s&lng=%s",
                    URLEncoder.encode(String.valueOf(mLat), charset),
                    URLEncoder.encode(String.valueOf(mLng), charset));

            URLConnection connection = new URL(url + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();

            return new Gson().fromJson(new InputStreamReader(response), ChaseApiResponse.class);

        } catch (IOException e) {
            // Would generally handle the exceptions individually and appropriately, but
            // in the interest of saving time, just printing the stack trace.
            e.printStackTrace();
        }
        // returning a default object to avoid potential NPE.
        return new ChaseApiResponse();
    }

    @Override
    protected void onPostExecute(ChaseApiResponse chaseApiResponse) {
        super.onPostExecute(chaseApiResponse);
        mOnFinished.onGetChaseApiReponseFinished(chaseApiResponse);
    }

    public interface OnGetChaseLocationsFinished {
        void onGetChaseApiReponseFinished(ChaseApiResponse chaseApiResponse);
    }
}

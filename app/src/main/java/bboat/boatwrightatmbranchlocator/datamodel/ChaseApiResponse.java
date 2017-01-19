package bboat.boatwrightatmbranchlocator.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class holding the branch and ATM locations near the provided coordinates.
 * <p>
 * Created by BobSSD on 1/16/2017.
 */

public class ChaseApiResponse {
    @SerializedName("errors")
    private List<Errors> mErrors;
    @SerializedName("locations")
    private List<ChaseLocation> mLocations;

    public List<Errors> getErrors() {
        return mErrors;
    }

    public void setErrors(List<Errors> mErrors) {
        this.mErrors = mErrors;
    }

    public List<ChaseLocation> getLocations() {
        return mLocations;
    }

    public void setLocations(List<ChaseLocation> mLocations) {
        this.mLocations = mLocations;
    }

}

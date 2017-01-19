package bboat.boatwrightatmbranchlocator.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChaseLocation {
    @SerializedName("state")
    private String mState;
    @SerializedName("locType")
    private String mLoctype;
    @SerializedName("label")
    private String mLabel;
    @SerializedName("address")
    private String mAddress;
    @SerializedName("city")
    private String mCity;
    @SerializedName("zip")
    private String mZip;
    @SerializedName("name")
    private String mName;
    @SerializedName("lat")
    private double mLat;
    @SerializedName("lng")
    private double mLng;
    @SerializedName("bank")
    private String mBank;
    @SerializedName("type")
    private String mType;
    @SerializedName("lobbyHrs")
    private List<String> mLobbyhrs;
    @SerializedName("driveUpHrs")
    private List<String> mDriveuphrs;
    @SerializedName("atms")
    private int mAtms;
    @SerializedName("services")
    private List<String> mServices;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("distance")
    private double mDistance;
    @SerializedName("access")
    private String mAccess;

    public String getState() {
        return mState;
    }

    public void setState(String mState) {
        this.mState = mState;
    }

    public String getLoctype() {
        return mLoctype;
    }

    public void setLoctype(String mLoctype) {
        this.mLoctype = mLoctype;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String mZip) {
        this.mZip = mZip;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double mLng) {
        this.mLng = mLng;
    }

    public String getBank() {
        return mBank;
    }

    public void setBank(String mBank) {
        this.mBank = mBank;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public List<String> getLobbyhrs() {
        return mLobbyhrs;
    }

    public void setLobbyhrs(List<String> mLobbyhrs) {
        this.mLobbyhrs = mLobbyhrs;
    }

    public List<String> getDriveuphrs() {
        return mDriveuphrs;
    }

    public void setDriveuphrs(List<String> mDriveuphrs) {
        this.mDriveuphrs = mDriveuphrs;
    }

    public int getAtms() {
        return mAtms;
    }

    public void setAtms(int mAtms) {
        this.mAtms = mAtms;
    }

    public List<String> getServices() {
        return mServices;
    }

    public void setServices(List<String> mServices) {
        this.mServices = mServices;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public String getAccess() {
        return mAccess;
    }

    public void setAccess(String mAccess) {
        this.mAccess = mAccess;
    }
}

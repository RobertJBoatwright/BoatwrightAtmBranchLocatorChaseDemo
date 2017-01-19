package bboat.boatwrightatmbranchlocator.datamodel;

import com.google.gson.annotations.SerializedName;

public class Errors {
    @SerializedName("issueType")
    private String mIssuetype;
    @SerializedName("actionCode")
    private String mActioncode;
    @SerializedName("code")
    private int mCode;
    @SerializedName("attributes")
    private String mAttributes;
    @SerializedName("message")
    private String mMessage;

    public String getIssuetype() {
        return mIssuetype;
    }

    public void setIssuetype(String mIssuetype) {
        this.mIssuetype = mIssuetype;
    }

    public String getActioncode() {
        return mActioncode;
    }

    public void setActioncode(String mActioncode) {
        this.mActioncode = mActioncode;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int mCode) {
        this.mCode = mCode;
    }

    public String getAttributes() {
        return mAttributes;
    }

    public void setAttributes(String mAttributes) {
        this.mAttributes = mAttributes;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}

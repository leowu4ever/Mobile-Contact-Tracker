package com.uk.location.activity;

public class RegistrationData {
    public boolean isTracking;

    public RegistrationData () {
        isTracking = false;
    }

    public void setTrackingState (boolean trackingState) {
        this.isTracking = trackingState;
    }

    public boolean getTrackingState () {
        return isTracking;
    }
}

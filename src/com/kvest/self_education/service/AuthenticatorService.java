package com.kvest.self_education.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.kvest.self_education.platform.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: roman
 * Date: 12/17/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private Authenticator authenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        authenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}

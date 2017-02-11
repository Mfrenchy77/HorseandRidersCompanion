package com.frenchfriedtechnology.horseandriderscompanion;

import com.squareup.otto.Bus;

/**
 * Provides a single instance of an Event Bus which is used to relay messages throughout the app
 */
public final class BusProvider {

    private static final Bus mInstance = new Bus();

    private BusProvider() {
    }

    public static Bus getBusProviderInstance() {
        return mInstance;
    }
}
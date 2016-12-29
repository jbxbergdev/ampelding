package de.bolz.ampelding.device;

import android.os.Build;

/**
 * Created by Johannes Bolz on 26.12.16.
 */

public class DeviceManager {
    
    private static Device device;

    public static synchronized Device getDevice() {
        if (device == null) {
            switch (Build.DEVICE) {
                case "rpi3":
                    device = new RaspberryPi3Device();
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported device: " + Build.DEVICE);
            }
        }
        return device;
    }
}

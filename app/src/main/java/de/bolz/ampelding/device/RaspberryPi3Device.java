package de.bolz.ampelding.device;

import de.bolz.ampelding.BuildIndicator;
import de.bolz.ampelding.LedIndicator;

/**
 * Created by Johannes Bolz on 26.12.16.
 */

public class RaspberryPi3Device implements Device {

    private final BuildIndicator[] indicators = new BuildIndicator[] {
            new LedIndicator("BCM4", "BCM17"),
            new LedIndicator("BCM27", "BCM22"),
            new LedIndicator("BCM23", "BCM24"),
            new LedIndicator("BCM25", "BCM5"),
            new LedIndicator("BCM6", "BCM12"),
            new LedIndicator("BCM19", "BCM16"),
            new LedIndicator("BCM26", "BCM20")
    };

    @Override
    public BuildIndicator[] getBuildIndicators() {
        return indicators;
    }
}

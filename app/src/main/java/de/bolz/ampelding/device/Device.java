package de.bolz.ampelding.device;

import de.bolz.ampelding.BuildIndicator;

/**
 * Created by Johannes Bolz on 26.12.16.
 */

public interface Device {
    BuildIndicator[] getBuildIndicators();
}

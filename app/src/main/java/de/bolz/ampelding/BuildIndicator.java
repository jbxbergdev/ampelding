package de.bolz.ampelding;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Johannes Bolz on 26.12.16.
 */

public interface BuildIndicator extends Closeable {
    int STATE_UNKNOWN = 0;
    int STATE_POSITIVE = 1;
    int STATE_NEGATIVE = 2;
    int STATE_INDETERMINATE = 3;

    void open() throws IOException;
    void changeState(int state) throws IOException;
}

package de.bolz.ampelding;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by Johannes Bolz on 26.12.16.
 */

public class LedIndicator implements BuildIndicator {

    private static final long ANIMATION_INTERVAL = 1000;
    private String redLedPin, greenLedPin;
    private Gpio redLed, greenLed;
    private ValueAnimator ledAnimator = ObjectAnimator.ofInt(1, 0, 1);
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        private int animationState = 0;
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int newState = (int) valueAnimator.getAnimatedValue();
            if (newState != animationState) {
                animationState = newState;
                try {
                    greenLed.setValue(animationState == 1);
                    redLed.setValue(animationState != 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public LedIndicator(String redLedPin, String greenLedPin) {
        this.redLedPin = redLedPin;
        this.greenLedPin = greenLedPin;
        initAnimator();
    }

    private void initLeds() throws IOException {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        redLed = peripheralManagerService.openGpio(redLedPin);
        greenLed = peripheralManagerService.openGpio(greenLedPin);
        redLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        greenLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
    }

    private void initAnimator() {
        ledAnimator.setRepeatMode(ValueAnimator.RESTART);
        ledAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ledAnimator.setDuration(ANIMATION_INTERVAL);
        ledAnimator.addUpdateListener(animatorUpdateListener);
    }

    @Override
    public void open() throws IOException {
        initLeds();
    }

    @Override
    public void changeState(int state) {
        try {
            switch (state) {
                case STATE_UNKNOWN:
                    ledAnimator.cancel();
                    greenLed.setValue(false);
                    redLed.setValue(false);
                    break;
                case STATE_NEGATIVE:
                    ledAnimator.cancel();
                    greenLed.setValue(false);
                    redLed.setValue(true);
                    break;
                case STATE_POSITIVE:
                    ledAnimator.cancel();
                    redLed.setValue(false);
                    greenLed.setValue(true);
                    break;
                case STATE_INDETERMINATE:
                    ledAnimator.start();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        ledAnimator.cancel();
        redLed.close();
        greenLed.close();
    }
}

/*
 * Copyright 2013-2014 Colin McDonough, University of South Florida
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usf.cutr.android.walkbright;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * WalkBright - A pedestrian lighting app
 */
public class WalkBright extends Activity implements Eula.OnEulaAgreedTo, SurfaceHolder.Callback {

    private static final String TAG = "WalkBright";

    private static final int COLOR_DARK = 0xCC000000;

    private static final int COLOR_LIGHT = 0xCCBFBFBF;
    //private static final int COLOR_WHITE = 0xFFFFFFFF;

    private Camera mCamera;

    private boolean lightOn;

    private boolean previewOn;

    private boolean eulaAgreed;

    private View button;

    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    private WakeLock wakeLock;

    private static WalkBright torch;

    boolean active = true;

    // Amount of time between flashes
    private int[] waitTime = {100, 100, 400};

    // Amount of time light is left on for single flash, in milliseconds
    private static final int FLASH_TIME_ON = 75;

    private int counter = 0;

    private int[] policeColors = {Color.BLUE, Color.RED, Color.WHITE};

    public WalkBright() {
        super();
        torch = this;
    }

    public static WalkBright getTorch() {
        return torch;
    }

    private void getCamera() {
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
            } catch (RuntimeException e) {
                Log.i(TAG, "Camera.open() failed: " + e.getMessage());
            }
        }
    }

    /*
     * Called by the view (see main.xml)
     */
    public void toggleLight(View view) {
        toggleLight();
    }

    private void toggleLight() {
        if (lightOn) {
            turnLightOff();
        } else {
            turnLightOn();
        }
    }

    private void turnLightOn() {
        if (!eulaAgreed) {
            return;
        }

        lightOn = true;

        // Use the screen as a flashlight
        button.setBackgroundColor(policeColors[counter % 3]);

        // Keep screen on
        if (surfaceView != null) {
            surfaceView.setKeepScreenOn(true);
        }

        Parameters parameters = mCamera.getParameters();

        if (mCamera == null || parameters == null) {
            // Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG);
            return;
        }

        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null || parameters.getFlashMode() == null) {
            return;
        }

        String flashMode = parameters.getFlashMode();
        Log.i(TAG, "Flash mode: " + flashMode);
        Log.i(TAG, "Flash modes: " + flashModes);
        if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
                // button.setBackgroundColor(COLOR_WHITE);

            } else {
                // Toast.makeText(this, "Flash mode (torch) not supported",
                //    Toast.LENGTH_LONG);
                // Use the screen as a flashlight (next best thing)
                // button.setBackgroundColor(COLOR_WHITE);
                Log.e(TAG, "FLASH_MODE_TORCH not supported");
            }
        }
    }

    private void turnLightOff() {
        if (lightOn) {
            // set the background to dark
            button.setBackgroundColor(COLOR_DARK);
            // Stop wakelock
            if (surfaceView != null) {
                surfaceView.setKeepScreenOn(false);
            }
            lightOn = false;
            if (mCamera == null) {
                return;
            }
            Parameters parameters = mCamera.getParameters();
            if (parameters == null) {
                return;
            }
            List<String> flashModes = parameters.getSupportedFlashModes();
            String flashMode = parameters.getFlashMode();
            // Check if camera flash exists
            if (flashModes == null) {
                return;
            }
            Log.i(TAG, "Flash mode: " + flashMode);
            Log.i(TAG, "Flash modes: " + flashModes);
            if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
                // Turn off the flash
                if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
                    parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);

                } else {
                    Log.e(TAG, "FLASH_MODE_OFF not supported");
                }
            }
        }
    }

    private void startPreview() {
        if (!previewOn && mCamera != null) {
            mCamera.startPreview();
            previewOn = true;
        }
    }

    private void stopPreview() {
        if (previewOn && mCamera != null) {
            mCamera.stopPreview();
            previewOn = false;
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Eula.show(this)) {
            eulaAgreed = true;
        }
        setContentView(R.layout.main);
        button = findViewById(R.id.button);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        disablePhoneSleep();
        Log.i(TAG, "onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (active) {
                    if (surfaceView == null) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            turnLightOn();

                        }
                    });

                    Log.d(TAG, "Flashing for " + FLASH_TIME_ON + "ms");

                    try {
                        Thread.sleep(FLASH_TIME_ON);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            turnLightOff();
                        }
                    });

                    try {
                        Log.d(TAG, "Sleeping for " + waitTime[counter % 3] + "ms");
                        Thread.sleep(waitTime[counter % 3]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Counter = " + counter);
                    counter++;
                }
            }
        }).start();
    }

    private void disablePhoneSleep() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        getCamera();
        startPreview();
    }

    @Override
    public void onResume() {
        super.onResume();
        turnLightOn();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        turnLightOff();
        // Stop wakelock
        if (surfaceView != null) {
            surfaceView.setKeepScreenOn(false);
        }
        active = false;
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCamera != null) {
            stopPreview();
            mCamera.release();
            mCamera = null;
        }
        ;
        torch = null;
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        if (mCamera != null) {
            turnLightOff();
            stopPreview();
            mCamera.release();
        }
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    /** {@InheritDoc} * */
    @Override
    public void onEulaAgreedTo() {
        Log.d(TAG, "onEulaAgreedTo");
        eulaAgreed = true;
        turnLightOn();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // When the search button is long pressed, quit
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int I, int J, int K) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
    }
}
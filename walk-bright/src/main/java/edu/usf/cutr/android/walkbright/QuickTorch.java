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
import android.content.Intent;
import android.util.Log;

public class QuickTorch extends Activity {

    private static final String TAG = QuickTorch.class.getSimpleName();

    /**
     * Start WalkBright when triggered
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        if (WalkBright.getTorch() == null) {
            Log.d(TAG, "torch == null");
            Intent intent = new Intent(this, WalkBright.class);
            startActivity(intent);
        } else {
            Log.d(TAG, "torch != null");
        }
        finish();
    }
}

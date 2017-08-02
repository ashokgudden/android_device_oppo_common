/*
 * Copyright (c) 2017 The MoKee Open Source Project
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

package com.cyanogenmod.pocketmode;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import cyanogenmod.providers.CMSettings;

import org.cyanogenmod.internal.util.FileUtils;

import static org.cyanogenmod.platform.internal.R.bool.config_proximityCheckOnWake;
import static org.cyanogenmod.platform.internal.R.bool.config_proximityCheckOnWakeEnabledByDefault;

class Utils {

    static boolean isSupported(Context context) {
        return context.getResources().getBoolean(config_proximityCheckOnWake) &&
                FileUtils.isFileWritable(Constants.FP_DISABLE_NODE);
    }

    private static boolean isEnabledByDefault(Context context) {
        return context.getResources().getBoolean(
                config_proximityCheckOnWakeEnabledByDefault);
    }

    private static boolean isProximityCheckEnabled(Context context) {
        return CMSettings.System.getInt(context.getContentResolver(),
                CMSettings.System.PROXIMITY_ON_WAKE,
                isEnabledByDefault(context) ? 1 : 0) != 0;
    }

    private static boolean isFingerprintEnabled(Context context) {
        final FingerprintManager fm = (FingerprintManager) context
                .getSystemService(Context.FINGERPRINT_SERVICE);
        return fm.hasEnrolledFingerprints();
    }

    static boolean isEnabled(Context context) {
        return isProximityCheckEnabled(context) &&
                isFingerprintEnabled(context);
    }

}

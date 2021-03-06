/*
 * Copyright (C) 2017 The cyanogenmod Open Source Project
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

package org.cyanogenmod.settings.device.slider;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.util.Log;
import android.util.SparseIntArray;

import org.cyanogenmod.settings.device.SliderControllerBase;

public final class NotificationController extends SliderControllerBase {

    public static final int ID = 1;

    private static final String TAG = "NotificationController";

    private static final int NOTIFICATION_TOTAL_SILENCE = 10;
    private static final int NOTIFICATION_ALARMS_ONLY = 11;
    private static final int NOTIFICATION_PRIORITY_ONLY = 12;
    private static final int NOTIFICATION_NONE = 13;
    private static final int NOTIFICATION_VIBRATE = 14;
    private static final int NOTIFICATION_RING = 15;

    private static final String PROP_IGNORE_AUTO = "persist.op.slider_ignore_auto";

    private static final SparseIntArray MODES = new SparseIntArray();
    static {
        MODES.put(NOTIFICATION_TOTAL_SILENCE,
                Settings.Global.ZEN_MODE_NO_INTERRUPTIONS);
        MODES.put(NOTIFICATION_ALARMS_ONLY,
                Settings.Global.ZEN_MODE_ALARMS);
        MODES.put(NOTIFICATION_PRIORITY_ONLY,
                Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS);
        MODES.put(NOTIFICATION_NONE,
                Settings.Global.ZEN_MODE_OFF);
        MODES.put(NOTIFICATION_VIBRATE,
                AudioManager.RINGER_MODE_VIBRATE);
        MODES.put(NOTIFICATION_RING,
                AudioManager.RINGER_MODE_NORMAL);
    }

    private final NotificationManager mNotificationManager;
    private final AudioManager mAudioManager;

    public NotificationController(Context context) {
        super(context);
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE);
        mAudioManager = getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected boolean processAction(int action) {
        Log.d(TAG, "slider action: " + action);
        if (MODES.indexOfKey(action) >= 0) {
            boolean ignoreAuto = SystemProperties.get(PROP_IGNORE_AUTO).equals("true");
            boolean isAutoModeActive = false;

            if (ignoreAuto) {
                ZenModeConfig zmc = mNotificationManager.getZenModeConfig();
                int len = zmc.automaticRules.size();
                for (int i = 0; i < len; i++) {
                    if (zmc.automaticRules.valueAt(i).isAutomaticActive()) {
                        isAutoModeActive = true;
                        break;
                    }
                }
            }

            if (!isAutoModeActive) {
                if (action <= NOTIFICATION_NONE) {
                    mNotificationManager.setZenMode(MODES.get(action), null, TAG);
                } else {
                    mAudioManager.setRingerModeInternal(MODES.get(action));
                    mNotificationManager.setZenMode(Settings.Global.ZEN_MODE_OFF, null, TAG);
                }
            }

		    if (action == NOTIFICATION_NONE) {
		        mAudioManager.setRingerModeInternal(MODES.get(NOTIFICATION_RING));
		    }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        mNotificationManager.setZenMode(Settings.Global.ZEN_MODE_OFF, null, TAG);
    }
}

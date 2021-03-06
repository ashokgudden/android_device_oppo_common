/*
 * Copyright (C) 2015 The CyanogenMod Project
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

package com.cyanogenmod.settings.device.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cyanogenmod.settings.device.utils.FileUtils;

public class Constants {

    // Preference keys
    public static final String TOUCHPAD_STATE_KEY = "touchpad_enable_state";
    public static final String TOUCHPAD_LONG_PRESS_STATE_KEY = "touchpad_long_click_state";
    public static final String TOUCHPAD_DOUBLE_CLICK_STATE_KEY = "touchpad_double_tap_state";
    public static final String TOUCHPAD_DOUBLETAP_KEY = "touchpad_enable_doubletap";
    public static final String TOUCHPAD_LONGPRESS_KEY = "touchpad_enable_longpress";
    public static final String TOUCHSCREEN_CAMERA_GESTURE_KEY = "touchscreen_gesture_camera";
    public static final String TOUCHSCREEN_MUSIC_GESTURE_KEY = "touchscreen_gesture_music";
    public static final String TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY =
            "touchscreen_gesture_flashlight";
    public static final String BUTTON_SWAP_KEY = "button_swap";
    public static final String NOTIF_SLIDER_PANEL_KEY = "notification_slider";
    public static final String NOTIF_SLIDER_USAGE_KEY = "slider_usage";
    public static final String NOTIF_SLIDER_ACTION_TOP_KEY = "action_top_position";
    public static final String NOTIF_SLIDER_ACTION_MIDDLE_KEY = "action_middle_position";
    public static final String NOTIF_SLIDER_ACTION_BOTTOM_KEY = "action_bottom_position";

    // Proc nodes
    public static final String TOUCH_PAD_NODE = "/proc/touchpad/enable";
    public static final String TOUCHSCREEN_CAMERA_NODE = "/proc/touchpanel/letter_o_enable";
    public static final String TOUCHSCREEN_DOUBLE_SWIPE_NODE = "/proc/touchpanel/double_swipe_enable";
    public static final String TOUCHSCREEN_LEFT_ARROW = "/proc/touchpanel/left_arrow_enable";
    public static final String TOUCHSCREEN_RIGHT_ARROW = "/proc/touchpanel/right_arrow_enable";
    public static final String TOUCHSCREEN_FLASHLIGHT_NODE = "/proc/touchpanel/down_arrow_enable";

    // Array of music-related gestures
    public static final String[] TOUCHSCREEN_MUSIC_GESTURES_ARRAY = {TOUCHSCREEN_DOUBLE_SWIPE_NODE, TOUCHSCREEN_LEFT_ARROW, TOUCHSCREEN_RIGHT_ARROW};

    // Proc nodes default values
    public static final boolean TOUCH_PAD_DEFAULT = false;
    public static final boolean TOUCHSCREEN_CAMERA_DEFAULT = true;
    public static final boolean TOUCHSCREEN_MUSIC_DEFAULT = true;
    public static final boolean TOUCHSCREEN_FLASHLIGHT_DEFAULT = true;

    // Button nodes
    public static final String BUTTON_SWAP_NODE = "/proc/s1302/key_rep";
    public static final String NOTIF_SLIDER_NODE = "/sys/class/switch/tri-state-key/state";

    public static final String NOTIF_SLIDER_FOR_NOTIFICATION = "1";
    public static final String NOTIF_SLIDER_FOR_FLASHLIGHT = "2";
    public static final String NOTIF_SLIDER_FOR_BRIGHTNESS = "3";
    public static final String NOTIF_SLIDER_FOR_ROTATION = "4";
    public static final String NOTIF_SLIDER_FOR_RINGER = "5";
    public static final String SLIDER_FOR_CAFFEINE = "6";

    public static final String ACTION_UPDATE_SLIDER_SETTINGS
            = "com.cyanogenmod.settings.device.UPDATE_SLIDER_SETTINGS";

    public static final String EXTRA_SLIDER_USAGE = "usage";
    public static final String EXTRA_SLIDER_ACTIONS = "actions";

    // Holds <preference_key> -> <proc_node> mapping
    public static final Map<String, String> sBooleanNodePreferenceMap = new HashMap<>();
    public static final Map<String, String> sStringNodePreferenceMap = new HashMap<>();

    // Holds <preference_key> -> <default_values> mapping
    public static final Map<String, Object> sNodeDefaultMap = new HashMap<>();

    public static final String[] sGesturePrefKeys = {
        TOUCHSCREEN_CAMERA_GESTURE_KEY,
        TOUCHSCREEN_MUSIC_GESTURE_KEY,
        TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY
    };

    public static final String[] sButtonPrefKeys = {
        BUTTON_SWAP_KEY,
    };

    static {
        sBooleanNodePreferenceMap.put(TOUCHPAD_STATE_KEY, TOUCH_PAD_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_CAMERA_GESTURE_KEY, TOUCHSCREEN_CAMERA_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_MUSIC_GESTURE_KEY, TOUCHSCREEN_DOUBLE_SWIPE_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY,
                TOUCHSCREEN_FLASHLIGHT_NODE);
        sBooleanNodePreferenceMap.put(BUTTON_SWAP_KEY, BUTTON_SWAP_NODE);

        sNodeDefaultMap.put(TOUCHPAD_STATE_KEY, TOUCH_PAD_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_CAMERA_GESTURE_KEY, TOUCHSCREEN_CAMERA_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_MUSIC_GESTURE_KEY, TOUCHSCREEN_MUSIC_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY, TOUCHSCREEN_FLASHLIGHT_DEFAULT);
        sNodeDefaultMap.put(TOUCHPAD_DOUBLETAP_KEY, false);
        sNodeDefaultMap.put(TOUCHPAD_LONGPRESS_KEY, false);
        sNodeDefaultMap.put(BUTTON_SWAP_KEY, false);

    }

    public static boolean isPreferenceEnabled(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, (Boolean) sNodeDefaultMap.get(key));
    }

    public static String getPreferenceString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, (String) sNodeDefaultMap.get(key));
    }

    public static boolean isNotificationSliderSupported() {
        return FileUtils.fileExists(NOTIF_SLIDER_NODE);
    }
}

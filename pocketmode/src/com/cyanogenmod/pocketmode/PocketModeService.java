/*
 * Copyright (c) 2016 The CyanogenMod Project
 *               2017 The LineageOS Project
 *               2017 The MoKee Open Source Project
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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import org.cyanogenmod.internal.util.FileUtils;

public class PocketModeService extends Service {

    private static final String TAG = "PocketModeService";
    private static final boolean DEBUG = false;

    private SettingObserver mSettingObserver;
    private ScreenReceiver mScreenReceiver;
    private ProximitySensor mProximitySensor;

    private boolean mIsListeningScreen = false;
    private boolean mIsListeningProximity = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) Log.d(TAG, "Creating service");

        mSettingObserver = new SettingObserver(this);
        mScreenReceiver = new ScreenReceiver(this);
        mProximitySensor = new ProximitySensor(this);

        mSettingObserver.enable();

        if (Utils.isProximityCheckEnabled(this)) {
            mScreenReceiver.enable();
            mIsListeningScreen = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) Log.d(TAG, "Destroying service");

        mSettingObserver.disable();

        if (mIsListeningScreen) {
            mScreenReceiver.disable();
            mIsListeningScreen = false;
        }

        if (mIsListeningProximity) {
            mProximitySensor.disable();
            mIsListeningProximity = false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.d(TAG, "Starting service");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void onSettingChange() {
        if (DEBUG) Log.d(TAG, "Proximity setting changed");
        if (Utils.isProximityCheckEnabled(this)) {
            mScreenReceiver.enable();
            mIsListeningScreen = true;
        } else if (mIsListeningScreen) {
            mScreenReceiver.disable();
            mIsListeningScreen = false;
        }
    }

    void onDisplayOn() {
        if (DEBUG) Log.d(TAG, "Display on");
        if (mIsListeningProximity) {
            mProximitySensor.disable();
            mIsListeningProximity = false;
        }
    }

    void onDisplayOff() {
        if (DEBUG) Log.d(TAG, "Display off");
        if (Utils.isFingerprintEnabled(this)) {
            mProximitySensor.enable();
            mIsListeningProximity = true;
        }
    }

    void onProximityNear(boolean isNear) {
        if (DEBUG) Log.d(TAG, "onProximityNear: " + isNear);
        FileUtils.writeLine(Constants.FP_DISABLE_NODE, isNear ? "1" : "0");
    }
}

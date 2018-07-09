/*
 * Copyright (C) 2014 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.dialer.app.settings;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.dialer.callrecord.impl.CallRecorderService;

import com.android.dialer.app.R;

public class OtherSettingsFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  private static final String ENABLE_POST_CALL = "enable_post_call";

  private SharedPreferences mPrefs;
  private boolean mEnabled;

  private SwitchPreference mEnablePostcall;
  private SwitchPreference enableDndInCall;

  private NotificationManager notificationManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.other_settings);

    Context context = getActivity();

    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    mEnabled = mPrefs.getBoolean(ENABLE_POST_CALL, true);

    mEnablePostcall = (SwitchPreference) findPreference(ENABLE_POST_CALL);
    mEnablePostcall.setChecked(mEnabled);
    mEnablePostcall.setOnPreferenceChangeListener(this);

    enableDndInCall = (SwitchPreference) findPreference("incall_enable_dnd");
    enableDndInCall.setOnPreferenceChangeListener(this);

    if (!CallRecorderService.isEnabled(getActivity())) {
      getPreferenceScreen().removePreference(
          findPreference(context.getString(R.string.call_recording_category_key)));
    }
    notificationManager = context.getSystemService(NotificationManager.class);

  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object objValue) {
    if (preference == mEnablePostcall) {
        boolean value = (Boolean) objValue;
        mPrefs
          .edit()
          .putBoolean(ENABLE_POST_CALL, value)
          .apply();
        return true;
    } else if (preference == enableDndInCall) {
      boolean newValue = (Boolean) objValue;
      if (newValue && !notificationManager.isNotificationPolicyAccessGranted()) {
        new AlertDialog.Builder(getContext())
            .setMessage(R.string.incall_dnd_dialog_message)
            .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
              }
            })
            .setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            })
            .show();

        // At this time, it is unknown whether the user granted the permission
        return false;
      }
      return true;
    }
    return false;
  }
}

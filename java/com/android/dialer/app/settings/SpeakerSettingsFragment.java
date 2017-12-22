/*
 * Copyright (C) 2016 The Pure Nexus Project
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

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.dialer.R;

import java.util.Arrays;

public class SpeakerSettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PROXIMITY_AUTO_SPEAKER  = "proximity_auto_speaker";
    private static final String PROXIMITY_AUTO_SPEAKER_DELAY  = "proximity_auto_speaker_delay";
    private static final String PROXIMITY_AUTO_SPEAKER_INCALL_ONLY  = "proximity_auto_speaker_incall_only";
    private static final String PROXIMITY_AUTO_ANSWER_INCALL_ONLY  = "proximity_auto_answer_incall_only";
    private static final String AUTO_ANSWER_CALL_KEY  = "auto_answer_call_key";
    private static final String AUTO_ANSWER_DELAY  = "auto_answer_delay";

    private SwitchPreference mProxSpeaker;
    private ListPreference mProxSpeakerDelay;
    private ListPreference mAutoAnswerDelay;

    private SwitchPreference mProxSpeakerIncallOnly;
    private SwitchPreference mProxAnswer;
    private SwitchPreference mAutoAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.speaker_settings);
        final ContentResolver resolver = getActivity().getContentResolver();

	mAutoAnswer = (SwitchPreference) findPreference(AUTO_ANSWER_CALL_KEY);
        mAutoAnswer.setChecked(Settings.System.getInt(resolver,
                Settings.System.AUTO_ANSWER_CALL_KEY, 0) == 1);
        mAutoAnswer.setOnPreferenceChangeListener(this);

        mProxSpeaker = (SwitchPreference) findPreference(PROXIMITY_AUTO_SPEAKER);
        mProxSpeaker.setChecked(Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_SPEAKER, 0) == 1);
        mProxSpeaker.setOnPreferenceChangeListener(this);

        mProxSpeakerDelay = (ListPreference) findPreference(PROXIMITY_AUTO_SPEAKER_DELAY);
        int proxDelay = Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_SPEAKER_DELAY, 3000);
        mProxSpeakerDelay.setValue(String.valueOf(proxDelay));
        mProxSpeakerDelay.setOnPreferenceChangeListener(this);
        updateProximityDelaySummary(proxDelay);

	mAutoAnswerDelay = (ListPreference) findPreference(AUTO_ANSWER_DELAY);
        int ansDelay = Settings.System.getInt(resolver,
                Settings.System.AUTO_ANSWER_DELAY, 100);
        mAutoAnswerDelay.setValue(String.valueOf(ansDelay));
        mAutoAnswerDelay.setOnPreferenceChangeListener(this);
        updateAnswerDelaySummary(ansDelay);

        mProxSpeakerIncallOnly = (SwitchPreference) findPreference(PROXIMITY_AUTO_SPEAKER_INCALL_ONLY);
        mProxSpeakerIncallOnly.setChecked(Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_SPEAKER_INCALL_ONLY, 0) == 1);
        mProxSpeakerIncallOnly.setOnPreferenceChangeListener(this);

	mProxAnswer = (SwitchPreference) findPreference(PROXIMITY_AUTO_ANSWER_INCALL_ONLY);
        mProxAnswer.setChecked(Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_ANSWER_INCALL_ONLY, 0) == 1);
        mProxAnswer.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mProxSpeaker) {
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_SPEAKER,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
	} else if (preference == mAutoAnswer) {
            Settings.System.putInt(resolver, Settings.System.AUTO_ANSWER_CALL_KEY,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mProxSpeakerDelay) {
            int proxDelay = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_SPEAKER_DELAY, proxDelay);
            updateProximityDelaySummary(proxDelay);
            return true;
	} else if (preference == mAutoAnswerDelay) {
            int ansDelay = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver, Settings.System.AUTO_ANSWER_DELAY, ansDelay);
            updateAnswerDelaySummary(ansDelay);
            return true;
        } else if (preference == mProxSpeakerIncallOnly) {
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_SPEAKER_INCALL_ONLY,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mProxAnswer) {
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_ANSWER_INCALL_ONLY,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
	}
        return false;
    }

    private void updateProximityDelaySummary(int value) {
        String summary = getResources().getString(R.string.prox_auto_speaker_delay_summary, value);
        mProxSpeakerDelay.setSummary(summary);
    }

    private void updateAnswerDelaySummary(int value) {
        String summary = getResources().getString(R.string.auto_answer_delay_summary, value);
        mAutoAnswerDelay.setSummary(summary);
    }

}

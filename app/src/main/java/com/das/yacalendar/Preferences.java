/**
 *
 */
package com.das.yacalendar;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author yaturner
 */
public class Preferences extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}

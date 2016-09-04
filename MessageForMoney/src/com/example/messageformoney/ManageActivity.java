package com.example.messageformoney;

import com.example.messageformoney.extra.AddPhoneDialog;
import com.example.messageformoney.extra.MessageFormatDialog;
import com.example.messageformoney.extra.PhoneListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.widget.ArrayAdapter;

public class ManageActivity extends PreferenceActivity {
	public static String[] keyword = { "@food", "@traffic", "@allowance", "@total",
			"@year", "@month" };
	private ArrayAdapter<String> adapter;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.manage);
		// getActionBar().setTitle("설정");
		Preference list_phone = (Preference) findPreference("send_phonenum");
		Preference add_phonenum = (Preference) findPreference("add_phonenum");
		Preference msg_format = (Preference) findPreference("msg_format");
		
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, keyword);
		
		list_phone
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						startActivity(new Intent(ManageActivity.this,
								PhoneListActivity.class));
						return true;
					}
				});
		add_phonenum
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						new AddPhoneDialog().show(getFragmentManager(),
								"Add Phone");
						return true;
					}
				});
		msg_format
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						new MessageFormatDialog(adapter).show(getFragmentManager(),
								"Message Format");
						return true;
					}
				});
	}

}

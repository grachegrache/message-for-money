package com.example.messageformoney.extra;


import com.example.messageformoney.MainActivity;
import com.example.messageformoney.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetValueDialog extends DialogFragment{
	TextView output;
	String type;
	EditText input;
	
	public SetValueDialog(TextView output, String type) {
		this.output = output;
		this.type = type;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_set_value, container);
		LinearLayout parent = (LinearLayout) view;
		TextView txt = (TextView) parent.getChildAt(0);
		input = (EditText) parent.getChildAt(1);
		LinearLayout ll = (LinearLayout) parent.getChildAt(2);
		Button btnConfirm = (Button) ll.getChildAt(0);
		Button btnCancel = (Button) ll.getChildAt(1);
		
		txt.setTextColor(output.getTextColors());
		txt.setText(type);
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String value = input.getText().toString();
				if(value != null){
					Editor editor = MainActivity.sp.edit();
					editor.putString(type, value);
					editor.commit();
					
					MainActivity.setViews();
				}
				SetValueDialog.this.dismiss();
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SetValueDialog.this.dismiss();
			}
		});
		return view;
	}
}

package com.example.messageformoney.extra;

import java.util.HashSet;

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


public class AddPhoneDialog extends DialogFragment{
	EditText input;
	
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
		View view = inflater.inflate(R.layout.dialog_add_phonenum, container);
		LinearLayout parent = (LinearLayout) view;
		input = (EditText) parent.getChildAt(1);
		LinearLayout ll = (LinearLayout) parent.getChildAt(2);
		Button btnConfirm = (Button) ll.getChildAt(0);
		Button btnCancel = (Button) ll.getChildAt(1);
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = input.getText().toString();
				if(phone != null){
					HashSet<String> list = new HashSet<String>();
					list = (HashSet<String>) MainActivity.sp.getStringSet("phonenum", list);
					list.add(phone);
					Editor editor = MainActivity.sp.edit();
					editor.putStringSet("phonenum", list);
					editor.commit();
				}
				getDialog().dismiss();
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		return view;
	}
}

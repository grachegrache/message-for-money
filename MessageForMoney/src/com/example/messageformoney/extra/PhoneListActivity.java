package com.example.messageformoney.extra;

import java.util.HashSet;

import com.example.messageformoney.MainActivity;
import com.example.messageformoney.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PhoneListActivity extends ListActivity{
	String selected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phone_list);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setPreferenceList();
	}
	
	@SuppressLint("NewApi")
	private void setPreferenceList(){
		HashSet<String> list = new HashSet<String>();
		list = (HashSet<String>) MainActivity.sp.getStringSet("phonenum", list);
		if(!list.isEmpty()){
			Object[] objList = list.toArray();
			String[] strList = new String[list.size()];
			for(int i=0;i<list.size();i++)
				strList[i] = (String) objList[i];
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strList));
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		selected = ((TextView) v).getText().toString();
		
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("전화번호 삭제").setMessage("삭제하시겠습니까?")
		.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(selected != null){
					HashSet<String> list = new HashSet<String>();
					list = (HashSet<String>) MainActivity.sp.getStringSet("phonenum", list);
					list.remove(selected);
					Editor editor = MainActivity.sp.edit();
					editor.putStringSet("phonenum", list);
					editor.commit();
					
					setPreferenceList();
				}
				dialog.dismiss();
			}
		}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		dialog.show();
	}
}

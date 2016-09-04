package com.example.messageformoney;

import java.util.Calendar;
import java.util.HashSet;

import com.example.messageformoney.extra.CalculateCalendar;
import com.example.messageformoney.extra.SetValueDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	public static SharedPreferences sp;
	LinearLayout layout_food;
	LinearLayout layout_traffic;
	LinearLayout layout_allowance;
	static TextView txt_food;
	static TextView txt_traffic;
	static TextView txt_allowance;
	static TextView txt_total;
	FrameLayout img_manage;
	FrameLayout img_send;
	static DatePicker picker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		txt_food = (TextView) findViewById(R.id.txt_food);
		txt_traffic = (TextView) findViewById(R.id.txt_traffic);
		txt_allowance = (TextView) findViewById(R.id.txt_allowance);
		txt_total = (TextView) findViewById(R.id.txt_total);
		img_manage = (FrameLayout) findViewById(R.id.img_manage);
		img_send = (FrameLayout) findViewById(R.id.img_send);
		layout_food = (LinearLayout) findViewById(R.id.layout_food);
		layout_traffic = (LinearLayout) findViewById(R.id.layout_traffic);
		layout_allowance = (LinearLayout) findViewById(R.id.layout_allowance);
		picker = (DatePicker) findViewById(R.id.datePicker);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
			if(daySpinnerId != 0){
				View daySpinner = picker.findViewById(daySpinnerId);
				if(daySpinner != null)
					daySpinner.setVisibility(View.GONE);
			}
		}
		
	    Calendar c = Calendar.getInstance();
	    picker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
	    		, new DatePicker.OnDateChangedListener() {
					
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						setTotal(year, monthOfYear+1);
					}
				});
	    
		layout_food.setOnClickListener(this);
		layout_traffic.setOnClickListener(this);
		layout_allowance.setOnClickListener(this);
		img_manage.setOnClickListener(this);
		img_send.setOnClickListener(this);
		
//		int totalDay = setViews();
//		Toast.makeText(this, "총 일수: "+totalDay, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		int totalDay = setViews();
		Toast.makeText(this, "총 일수: "+totalDay, Toast.LENGTH_SHORT).show();
	}
	
	public static int setViews(){
		String txtForFood = sp.getString("food", "5000");
		if(sp.getBoolean("include_other", true))
			txtForFood += " + " + sp.getString("money_for_other", "2000");
		
		txt_food.setText(txtForFood);
		txt_traffic.setText(sp.getString("traffic", "3100"));
		txt_allowance.setText(sp.getString("allowance", "50000"));
		
		return setTotal(picker.getYear(), picker.getMonth());
	}
	
	public static int setTotal(int year, int month){
		int totalDay = getTotalDay(year, month);
		int money_food = Integer.parseInt(sp.getString("food", "5000"));
		if(sp.getBoolean("include_other", true))
			money_food += Integer.parseInt(sp.getString("money_for_other", "2000"));
		int money_traffic = Integer.parseInt(sp.getString("traffic", "3100"));
		int money_allowance = Integer.parseInt(sp.getString("allowance", "50000"));
		
		txt_total.setText(String.valueOf((money_food+money_traffic)*totalDay+money_allowance));
		
		return totalDay;
	}
	
	public static int getTotalDay(int year, int month){
		int totalDay = 0;
		CalculateCalendar calculate = new CalculateCalendar();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		boolean isExcept;
		if(month == 12)
			month = 0;
		
		while(cal.get(Calendar.DAY_OF_MONTH) <= lastday && cal.get(Calendar.MONTH) == month){
			isExcept = false;
			
			if(calculate.isHolidayLunar(cal))
				isExcept = true;
			if(calculate.isHolidaySolar(cal))
				isExcept = true;
			if(!sp.getBoolean("sunday", false))
				if(calculate.isWeekday(cal, Calendar.SUNDAY))
					isExcept = true;
			if(!sp.getBoolean("monday", true))
				if(calculate.isWeekday(cal, Calendar.MONDAY))
					isExcept = true;
			if(!sp.getBoolean("tuesday", true))
				if(calculate.isWeekday(cal, Calendar.TUESDAY))
					isExcept = true;
			if(!sp.getBoolean("wednesday", true))
				if(calculate.isWeekday(cal, Calendar.WEDNESDAY))
					isExcept = true;
			if(!sp.getBoolean("thursday", true))
				if(calculate.isWeekday(cal, Calendar.THURSDAY))
					isExcept = true;
			if(!sp.getBoolean("friday", true))
				if(calculate.isWeekday(cal, Calendar.FRIDAY))
					isExcept = true;
			if(!sp.getBoolean("saturday", false))
				if(calculate.isWeekday(cal, Calendar.SATURDAY))
					isExcept = true;
			
			if(!isExcept)
				totalDay++;
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return totalDay;
	}
	
	@Override
	public void onClick(View v) {
		TextView isOutput = getOutputTextView(v);
		if(isOutput != null)
			new SetValueDialog(isOutput, getTypeString(v)).show(getFragmentManager(), "Set Value");
		
		else if(v == img_manage){
			startActivity(new Intent(MainActivity.this, ManageActivity.class));
			
		}else if(v == img_send){
			sendMessage();
		}
	
	}
	
	private void sendMessage(){
		SmsManager smsManager = SmsManager.getDefault();
		HashSet<String> phonelist = new HashSet<String>();
		phonelist = (HashSet<String>)sp.getStringSet("phonenum", phonelist);
		Object[] objList = phonelist.toArray();
		String[] strList = new String[objList.length];
		for(int i=0;i<objList.length;i++)
			strList[i] = (String) objList[i];
		
		String prefix = sp.getString("msg_prefix", null);
		String postfix = sp.getString("msg_postfix", null);
		
		String msg = " " + txt_total.getText().toString() + " ";
		if(prefix != null)
			msg = prefix + msg;
		if(postfix != null)
			msg += postfix;
		
		for(String phone : strList){
			smsManager.sendTextMessage(phone, null, msg, null, null);
			Toast.makeText(this, "메시지 전송: "+phone, Toast.LENGTH_SHORT).show();
		}
	}
	
	private TextView getOutputTextView(View v){
		if(v == layout_food)
			return txt_food;
		else if(v == layout_traffic)
			return txt_traffic;
		else if(v == layout_allowance)
			return txt_allowance;
		else
			return null;
	}
	
	private String getTypeString(View v){
		if(v == layout_food)
			return "food";
		else if(v == layout_traffic)
			return "traffic";
		else if(v == layout_allowance)
			return "allowance";
		else
			return null;
	}
}

package com.example.messageformoney.extra;

import com.example.messageformoney.MainActivity;
import com.example.messageformoney.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

public class MessageFormatDialog extends DialogFragment {
	ArrayAdapter<String> adapter;
	MultiAutoCompleteTextView msg_format;

	public MessageFormatDialog(ArrayAdapter<String> adapter) {
		this.adapter = adapter;
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
		View view = inflater.inflate(R.layout.dialog_msg_format, container);
		LinearLayout parent = (LinearLayout) view;
		msg_format = (MultiAutoCompleteTextView) parent.getChildAt(1);
		LinearLayout ll = (LinearLayout) parent.getChildAt(2);
		Button btnConfirm = (Button) ll.getChildAt(0);
		Button btnCancel = (Button) ll.getChildAt(1);

		msg_format.setAdapter(adapter);
		msg_format.setTokenizer(new SpaceTokenizer());

		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String value = msg_format.getText().toString();
				if (value != null) {
					Editor editor = MainActivity.sp.edit();
					editor.putString("msg", value);
					editor.commit();

					MainActivity.setViews();
				}
				MessageFormatDialog.this.dismiss();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MessageFormatDialog.this.dismiss();
			}
		});
		return view;
	}

	private class SpaceTokenizer implements Tokenizer {
		public int findTokenStart(CharSequence text, int cursor) {
			int i = cursor;

			while (i > 0 && text.charAt(i - 1) != ' ') {
				i--;
			}
			while (i < cursor && text.charAt(i) == ' ') {
				i++;
			}

			return i;
		}

		public int findTokenEnd(CharSequence text, int cursor) {
			int i = cursor;
			int len = text.length();

			while (i < len) {
				if (text.charAt(i) == ' ') {
					return i;
				} else {
					i++;
				}
			}

			return len;
		}

		public CharSequence terminateToken(CharSequence text) {
			int i = text.length();

			while (i > 0 && text.charAt(i - 1) == ' ') {
				i--;
			}

			if (i > 0 && text.charAt(i - 1) == ' ') {
				return text;
			} else {
				if (text instanceof Spanned) {
					SpannableString sp = new SpannableString(text + " ");
					TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
							Object.class, sp, 0);
					return sp;
				} else {
					return text + " ";
				}
			}
		}
	}
	
	private class ColorTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
}

package com.mindspree.days.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class ActualEditText extends EditText {

	private OnBackPressListener _listener;

	public ActualEditText(Context context) {
		super(context);
	}

	public ActualEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ActualEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && _listener != null) {
			_listener.onBackPress();
		}

		return super.onKeyPreIme(keyCode, event);
	}

	public void setOnBackPressListener(OnBackPressListener $listener) {
		_listener = $listener;
	}

	public interface OnBackPressListener {
		public void onBackPress();
	}
}

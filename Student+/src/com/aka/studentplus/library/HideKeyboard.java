package com.aka.studentplus.library;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboard {

	public HideKeyboard(Context context, View view) {

		super();
		InputMethodManager ceapa = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		ceapa.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}

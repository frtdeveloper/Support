package com.dfh.support.activity.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.dfh.support.R;


public class ListConfirmDialog extends Dialog {
	private Context context;

	public ListConfirmDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public ListConfirmDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_confirm_confirm);

	}
}

package com.aidiapp.salonbike.ui;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.ui.utils.TypefaceSpan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CreditsDialog extends DialogFragment {
private Dialog dialog;
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
View v=inflater.inflate(R.layout.creditsdialog, null);
SpannableString s = new SpannableString(this.getActivity().getResources().getText(R.string.credits));
s.setSpan(new TypefaceSpan(this.getActivity(), "vitor.otf"), 0, s.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
((TextView)v.findViewById(R.id.credits_title)).setText(s);
builder.setView(v);
this.dialog=builder.create();
		return this.dialog;
	}
}

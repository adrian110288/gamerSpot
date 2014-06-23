package com.gamerspot.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gamerspot.R;
import com.gamerspot.extra.App;
import com.gamerspot.extra.CommonUtilities;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class SearchDialogFragment extends DialogFragment implements View.OnClickListener{

    private TextView title;
    private TextView radioGroupHeader;
    private EditText editText;
    private RadioGroup radioGroup;
    private RadioButton radio1;
    private RadioButton radio2;
    private Button button;

    private CommonUtilities utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = App.getUtils(getActivity());
    }

    //TODO Create singleton for this dialog fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        title = (TextView) view.findViewById(R.id.search_title);
        editText = (EditText) view.findViewById(R.id.search_edittext);
        radioGroupHeader = (TextView) view.findViewById(R.id.search_header);
        radioGroup = (RadioGroup) view.findViewById(R.id.search_radioGroup);
        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        button = (Button) view.findViewById(R.id.search_button);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setTypeface(utils.getThemeFont());
        title.setTextSize(20);
        radioGroupHeader.setTypeface(utils.getTextFont());
        radioGroupHeader.setTextSize(18);
        radio1.setTypeface(utils.getTextFont());
        radio1.setTextSize(18);
        radio2.setTypeface(utils.getTextFont());
        radio2.setTextSize(18);
        button.setTypeface(utils.getTextFont());
    }

    @Override
    public void onClick(View v) {

        String phrase = editText.getText().toString();

        Intent i = new Intent();

        Bundle b = new Bundle();
        b.putString("phrase", phrase);
        b.putInt("radio", radioGroup.getCheckedRadioButtonId());

        i.putExtras(b);

        getTargetFragment().onActivityResult(1, Activity.RESULT_OK, i);
        dismiss();
    }
}

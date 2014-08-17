package com.gamerspot.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gamerspot.R;
import com.gamerspot.database.DAO;
import com.gamerspot.extra.GamerSpotApplication;
import com.gamerspot.extra.CommonUtilities;

import java.util.ArrayList;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class SearchDialogFragment extends DialogFragment implements View.OnClickListener{

    private TextView title;
    private TextView radioGroupHeader;
    private AutoCompleteTextView editText;
    private RadioGroup radioGroup;
    private RadioButton radio1;
    private RadioButton radio2;
    private Button button;

    private DAO dao;
    private CommonUtilities utils;
    private ArrayAdapter<String> autocompleteAdapter;
    private ArrayList<String> phrases;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = GamerSpotApplication.getUtils(getActivity());
        dao = new DAO(getActivity());

        phrases = new ArrayList<String>();
        autocompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, phrases);
        autocompleteAdapter.setNotifyOnChange(true);

    }

    //TODO Create singleton for this dialog fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        title = (TextView) view.findViewById(R.id.search_title);
        editText = (AutoCompleteTextView) view.findViewById(R.id.search_edittext);
        editText.setAdapter(autocompleteAdapter);
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
        editText.setTypeface(utils.getTextFont());
        radioGroupHeader.setTypeface(utils.getTextFont());
        radioGroupHeader.setTextSize(18);
        radio1.setTypeface(utils.getTextFont());
        radio1.setTextSize(18);
        radio2.setTypeface(utils.getTextFont());
        radio2.setTextSize(18);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 1) {

                    phrases = dao.getPhrases(s.toString());
                    editText.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, phrases));
                    dao.close();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        String phrase = editText.getText().toString();

        Intent i = new Intent();

        Bundle b = new Bundle();
        b.putString("phrase", phrase);
        b.putInt("radio", radioGroup.getCheckedRadioButtonId());

        i.putExtras(b);

        boolean inserted = dao.insertPhrase(phrase);

        getTargetFragment().onActivityResult(1, Activity.RESULT_OK, i);
        dismiss();
    }
}

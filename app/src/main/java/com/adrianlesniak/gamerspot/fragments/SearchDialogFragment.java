package com.adrianlesniak.gamerspot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.activities.SearchResultActivity;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.database.DAO;
import com.adrianlesniak.gamerspot.extra.CommonUtilities;
import com.adrianlesniak.gamerspot.views.GamerSpotButton;

import java.util.ArrayList;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class SearchDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView title;
    private AutoCompleteTextView editText;
    private GamerSpotButton button;
    private RelativeLayout checkboxGroup;
    private DAO dao;
    private ArrayAdapter<String> autocompleteAdapter;
    private ArrayList<String> phrases;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = CommonUtilities.getDatabaseAccessor();

        phrases = new ArrayList<String>();
        autocompleteAdapter = new ArrayAdapter<String>(getActivity(), R.layout.search_dropdown_item, phrases);
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
        button = (GamerSpotButton) view.findViewById(R.id.search_button);
        button.setOnClickListener(this);
        checkboxGroup = (RelativeLayout) view.findViewById(R.id.checkbox_group_layout);
        setCheckboxes();

        return view;
    }

    private void setCheckboxes() {

        for (int i = 0; i < checkboxGroup.getChildCount(); i++) {

            final CheckBox checkBox = (CheckBox) checkboxGroup.getChildAt(i);

            checkBox.setTypeface(CommonUtilities.getTextFont());

            if (i == 0) {

                checkBox.setChecked(true);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            checkAllBoxes(!isChecked);
                            checkBox.setChecked(isChecked);
                        } else {

                            boolean uncheck = false;

                            for (int i = 1; i < checkboxGroup.getChildCount(); i++) {

                                CheckBox childCB = (CheckBox) checkboxGroup.getChildAt(i);

                                if (childCB.isChecked()) {
                                    uncheck = true;
                                }

                                checkBox.setChecked(!uncheck);
                            }
                        }
                    }
                });

            } else {
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        int numberChecked = 0;
                        CheckBox firstChild = ((CheckBox) checkboxGroup.getChildAt(0));

                        for (int i = 1; i < checkboxGroup.getChildCount(); i++) {

                            CheckBox childCB = (CheckBox) checkboxGroup.getChildAt(i);

                            if (childCB.isChecked()) {
                                numberChecked++;
                            }
                        }

                        if (isChecked) {

                            if (firstChild.isChecked()) {
                                firstChild.setChecked(!isChecked);
                            }


                            if (numberChecked == 5) {
                                checkAllBoxes(!isChecked);
                                firstChild.setChecked(isChecked);
                            } else if (numberChecked == 0) {
                                checkAllBoxes(!isChecked);
                                checkBox.setChecked(isChecked);
                            }
                        } else {
                            if (numberChecked == 0) {
                                checkAllBoxes(isChecked);
                                firstChild.setChecked(!isChecked);
                            }
                        }


                    }
                });
            }
        }
    }

    private void checkAllBoxes(boolean isChecked) {

        for (int i = 0; i < checkboxGroup.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) checkboxGroup.getChildAt(i);
            checkBox.setChecked(isChecked);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setTypeface(CommonUtilities.getThemeFont());
        title.setTextSize(20);
        editText.setTypeface(CommonUtilities.getTextFont());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 1) {

                    phrases = dao.getPhrases(s.toString());
                    editText.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.search_dropdown_item, phrases));
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

        if (phrase.length() > 3) {
            ArrayList<NewsFeed> list = dao.searchArticles(phrase, getBoxesChecked());
            dao.insertPhrase(phrase);

            Intent i = new Intent(getActivity(), SearchResultActivity.class);

            if (list != null && list.size() != 0) {
                i.putExtra("resultList", list);
                i.putExtra("searchPhrase", phrase);
                dismiss();
                startActivity(i);
            } else {
                CommonUtilities.showToast("No results");
            }


        } else {
            CommonUtilities.showToast("Phrase too short");
        }
    }

    public ArrayList<Integer> getBoxesChecked() {

        ArrayList<Integer> checkedList = new ArrayList<Integer>(6);

        for (int i = 0; i < checkboxGroup.getChildCount(); i++) {

            CheckBox checkBox = (CheckBox) checkboxGroup.getChildAt(i);

            if (i == 0 && checkBox.isChecked()) {
                break;
            } else if (checkBox.isChecked()) {
                checkedList.add(i);
            }
        }

        Log.i("PLATFORMS", checkedList.toString());

        return checkedList;
    }
}

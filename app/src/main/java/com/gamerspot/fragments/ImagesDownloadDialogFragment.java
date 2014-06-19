package com.gamerspot.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class ImagesDownloadDialogFragment extends DialogFragment {

    private static final String DOWNLOAD = "Downloading images ...";
    private static final String TITLE = "Just a second ...";
    private static ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle(TITLE);
        dialog.setIndeterminate(true);
        dialog.setMessage(DOWNLOAD);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}

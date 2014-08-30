package com.adrianlesniak.gamerspot.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.extra.CommonUtilities;
import com.adrianlesniak.gamerspot.views.GamerSpotButton;

/**
 * Created by Adrian on 24-Aug-14.
 */
public class AboutDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button imageGooglePlus;
    private GamerSpotButton playstoreButton;
    private GamerSpotButton emailMe;
    private RelativeLayout dismissButton;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        title = (TextView) view.findViewById(R.id.about_title);
        title.setTypeface(CommonUtilities.getThemeFont());
        imageGooglePlus = (Button) view.findViewById(R.id.me_image_button);
        imageGooglePlus.setOnClickListener(new GooglePlusClickListener());
        playstoreButton = (GamerSpotButton) view.findViewById(R.id.playstoreButton);
        playstoreButton.setOnClickListener(new PlaystoreClickListener());
        emailMe = (GamerSpotButton) view.findViewById(R.id.emailme_button);
        emailMe.setOnClickListener(new EmailOnClickListener());
        dismissButton = (RelativeLayout) view.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dismissButton) {
            dismiss();
        }
    }

    private class PlaystoreClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            try {
                intent.setData(Uri.parse("market://search?q=pub:Adrian%20Lesniak"));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                intent.setData(Uri.parse("http://play.google.com/store/search?q=pub:Adrian%20Lesniak"));
                startActivity(intent);
            }
        }
    }

    private class GooglePlusClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://plus.google.com/+AdrianLe%C5%9Bniak/posts"));
            startActivity(intent);
        }
    }

    private class EmailOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"adrian110288@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "GamerSpot in-app user feedback");

            try {
                startActivity(intent);
            } catch (Exception e) {
                CommonUtilities.showToast("Please install email client");
            }

        }
    }
}

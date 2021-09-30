package com.asaru.mcq;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {

    Button changeLanguage;
    private String radiobutton;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        changeLanguage = view.findViewById(R.id.changeLang);

        changeLanguage.setOnClickListener(v -> openChangeLanguage());
        return view;
    }

    private void openChangeLanguage() {
        String[] language = new String[]{"සිංහල", "தமிழ்", "English"};
        radiobutton = language[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose the Language");
        builder.setSingleChoiceItems(language, 0, (dialog, which) -> radiobutton = language[which]);
        builder.setPositiveButton("Save", (dialog, which) -> changeLanguageSave());
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void changeLanguageSave() {
        String lang;
        if (radiobutton.equals("English")) {
            lang = "en";
            context = ChangeLanguage.setLocale(getActivity(), "en");


        } else {
            if (radiobutton.equals("සිංහල")) {
                lang = "si";
                context = ChangeLanguage.setLocale(getActivity(), "si");

            } else {
                lang = "ta";
                context = ChangeLanguage.setLocale(getActivity(), "ta");

            }
        }
        SharedPreferences settings = getContext().getSharedPreferences("language", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("Language");
        editor.putString("Language", lang);
        Log.d("Language", lang);
        editor.apply();
    }

}
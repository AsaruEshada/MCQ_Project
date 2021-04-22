package com.asaru.mcq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asaru.mcq.R;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class HomeFragment extends Fragment {


    Context context;
    Resources resources;
    TextView Al,Ol,Grade5;
    LinearLayout al,ol,grade5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Al=view.findViewById(R.id.Al);
        Ol=view.findViewById(R.id.Ol);
        Grade5=view.findViewById(R.id.Grade5);
        al=view.findViewById(R.id.al);
        ol=view.findViewById(R.id.ol);
        grade5=view.findViewById(R.id.grade5);

        setLanguage();
        //Al tab click
        al.setOnClickListener(v -> {
            nextActivity("AL");
        });

        //Ol tab click
        ol.setOnClickListener(v -> {
            nextActivity("OL");
        });
        //Grade 5 tab click
        grade5.setOnClickListener(v -> {
            nextActivity("Grade5");
        });



        return view;
    }
 public void setLanguage(){
     SharedPreferences language = this.getActivity().getSharedPreferences("language", 0);
     String lang=language.getString("Language","");
     context=ChangeLanguage.setLocale(this.getActivity(),lang);
     resources=context.getResources();
     Al.setText(resources.getText(R.string.Al));
     Ol.setText(resources.getText(R.string.oL));
     Grade5.setText(resources.getText(R.string.Grade_5));
 }
 public void nextActivity(String ExamType){

         if(InternetConnection.checkConnection(context)){
             Intent i =new Intent(context,SelectPaperActivity.class);
             i.putExtra("ExamType",ExamType);
             startActivity(i);
         }else{
             Toast.makeText(context,"You Are Offline ",Toast.LENGTH_LONG).show();
         }

 }

    public static class InternetConnection {


        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            return false;
        }
    }
}
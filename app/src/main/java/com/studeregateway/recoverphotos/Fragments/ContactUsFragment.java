package com.studeregateway.recoverphotos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.studeregateway.recoverphotos.R;


/**
 * Created by mnaum on 8/20/2017.
 */

public class ContactUsFragment extends Fragment {
    private TextView email;
    private LinearLayout llContactNum, llWeb, llFiverr, llFacebook, llYoutube;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.AdView adView;
    private AdView mAdView;
    private com.facebook.ads.InterstitialAd interstitialAd;
    private AdRequest adRequest;


    public static String FACEBOOK_URL = "https://www.facebook.com/StudereGateway";
    public static String FACEBOOK_PAGE_ID = "792626421083384";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact,null);

        loadAd(view);
        init(view);
        listeners();

        String mailText = "<a href='mailto:mail@studeregateway.com'</a>mail@studeregateway.com";
        email.setText(Html.fromHtml(mailText));
        email.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    public void displayInterstitial(){
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        } else if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }

    public void loadAd(View view){


        mAdView= view.findViewById(R.id.adView);


//        adView = new com.facebook.ads.AdView(getActivity(), "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
        adView = new com.facebook.ads.AdView(getActivity(), "983327165210131_983392251870289", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        final LinearLayout adContainer = view.findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);



        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
//                Toast.makeText(getActivity(), "Banner Error", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
                adContainer.setVisibility(View.GONE);

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
//                Toast.makeText(getActivity(), "Banner Loaded", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.GONE);
                adContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
//                Toast.makeText(getActivity(), "Banner Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
//                Toast.makeText(getActivity(), "Banner Logged", Toast.LENGTH_SHORT).show();
            }
        });


        // Request an ad
        adView.loadAd();


//        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544/6300978111");      //test ad
        MobileAds.initialize(getActivity(), "ca-app-pub-5455259317336088/2834675383");      //test ad
        adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


//        interstitialAd = new com.facebook.ads.InterstitialAd(getActivity(), "YOUR_PLACEMENT_ID");        //test ad
        interstitialAd = new com.facebook.ads.InterstitialAd(getActivity(), "983327165210131_983869818489199");


        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
//                Toast.makeText(getActivity(), "Interstitial Displayed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
//                Toast.makeText(getActivity(), "Interstitial Dismissed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback

//                Toast.makeText(getActivity(), "Interstitial Error", Toast.LENGTH_SHORT).show();

                mInterstitialAd = new InterstitialAd(getActivity());
//                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");      //test ad
        mInterstitialAd.setAdUnitId("ca-app-pub-5455259317336088/9968480822");
                mInterstitialAd.loadAd(adRequest);
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded() {
                        displayInterstitial();
                    }
                });
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
//                Toast.makeText(getActivity(), "Interstitial Loaded", Toast.LENGTH_SHORT).show();
                displayInterstitial();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
//                Toast.makeText(getActivity(), "Interstitial Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
//                Toast.makeText(getActivity(), "Interstitial Logged", Toast.LENGTH_SHORT).show();
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();


    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();


        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app

                ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo("com.facebook.katana",0);
                boolean appStatus = ai.enabled;
                if (appStatus) {
                    return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else {
                    return FACEBOOK_URL; //normal web url
                }
            } else { //older versions of fb app
                ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo("com.facebook.katana",0);
                boolean appStatus = ai.enabled;
                if (appStatus) {
                    return "fb://page/" + FACEBOOK_PAGE_ID;
                } else {
                    return FACEBOOK_URL; //normal web url
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    private void listeners() {

        llContactNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+923314264860"));
                startActivity(intent);
            }
        });


        llWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.studeregateway.com"));
                startActivity(browserIntent);
            }
        });
        llFiverr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fiverr.com/h_mateen"));
                startActivity(browserIntent);
            }
        });

        llFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getActivity());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        llYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCAyRERoIt5A4WEzjBwQfY5A"));
                startActivity(intent);
            }
        });

    }

    private void init(View view) {

        llContactNum = view.findViewById(R.id.llContactNum);
        email = view.findViewById(R.id.email);
        llWeb = view.findViewById(R.id.llWeb);
        llFiverr = view.findViewById(R.id.llFiverr);
        llFacebook = view.findViewById(R.id.llFacebook);
        llYoutube = view.findViewById(R.id.llYoutube);
    }
}

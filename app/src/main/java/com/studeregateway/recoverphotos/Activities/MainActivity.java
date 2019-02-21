package com.studeregateway.recoverphotos.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.studeregateway.recoverphotos.Fragments.ContactUsFragment;
import com.studeregateway.recoverphotos.R;
import com.studeregateway.recoverphotos.Utils.AppConsts;
import com.studeregateway.recoverphotos.Utils.Config;

import java.io.File;
import com.facebook.ads.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private RelativeLayout rlParent;
    private CardView cvScan, cvRestored, cvContactUs, cvShare, cvRateApp, cvMoreApps;
    private AdView mAdView, mAdViewheader;
    private View header;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.AdView adView;
    private com.facebook.ads.InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadAd();
        listeners();
        checkRunTimePermission();

    }


    public void loadAd(){


        mAdView= findViewById(R.id.adView);
        mAdViewheader= header.findViewById(R.id.adView);


//        adView = new com.facebook.ads.AdView(MainActivity.this, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);  //test ads
        adView = new com.facebook.ads.AdView(MainActivity.this, "983327165210131_983392251870289", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        final LinearLayout adContainer = findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);



        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
//                Toast.makeText(MainActivity.this, "Banner Error", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
                adContainer.setVisibility(View.GONE);

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
//                Toast.makeText(MainActivity.this, "Banner Loaded", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.GONE);
                adContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
//                Toast.makeText(MainActivity.this, "Banner Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
//                Toast.makeText(MainActivity.this, "Banner Logged", Toast.LENGTH_SHORT).show();
            }
        });


        // Request an ad
        adView.loadAd();


//        MobileAds.initialize(MainActivity.this, "ca-app-pub-3940256099942544/6300978111");      //test ad
        MobileAds.initialize(MainActivity.this, "ca-app-pub-5455259317336088/2834675383");
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdViewheader.loadAd(adRequest);


//        interstitialAd = new com.facebook.ads.InterstitialAd(this, "YOUR_PLACEMENT_ID");        //test ad
        interstitialAd = new com.facebook.ads.InterstitialAd(this, "983327165210131_983869818489199");


        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
//                Toast.makeText(MainActivity.this, "Displayed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
//                Toast.makeText(MainActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
//                Toast.makeText(MainActivity.this, "Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
//                Toast.makeText(MainActivity.this, "Logged", Toast.LENGTH_SHORT).show();
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();

        mInterstitialAd = new InterstitialAd(MainActivity.this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");      //test ad
        mInterstitialAd.setAdUnitId("ca-app-pub-5455259317336088/6330195125");
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.show();
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // do nothing
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header=navigationView.getHeaderView(0);


        rlParent = findViewById(R.id.rlParent);
        cvScan = findViewById(R.id.cvScan);
        cvRestored = findViewById(R.id.cvRestored);
        cvContactUs = findViewById(R.id.cvContactUs);
        cvShare = findViewById(R.id.cvShare);
        cvRateApp = findViewById(R.id.cvRateApp);
        cvMoreApps = findViewById(R.id.cvMoreApps);



    }
    private void listeners(){

        cvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanForPhotos();
            }
        });
        cvRestored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
                if (!fileDirectory.exists()){
                    fileDirectory.mkdirs();
                }
                scanForRestoredPhotos();
            }
        });
        cvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.mainFrame, new ContactUsFragment());
                transaction.addToBackStack(null).commit();

            }
        });
        cvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Recover Deleted Photos";
                String shareSub = "Let me recommend you this application\n\nRecover Deleted Photos\n";
                shareSub += "https://play.google.com/store/apps/details?id=com.studeregateway.recoverphotos";
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));

            }
        });
        cvRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.studeregateway.recoverphotos"));
                startActivity(intent);

            }
        });
        cvMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Studere Gateway"));
                startActivity(intent);

            }
        });

    }


    public void displayInterstitial(){

        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        } else if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_scan) {

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                if (checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED

                ) {
                    requestPermissions(AppConsts.PERMISSIONS, AppConsts.requestForPermission);
                } else {
                    scanForPhotos();
                }
            }

        } else if (id == R.id.nav_restored) {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(AppConsts.PERMISSIONS,AppConsts.requestForPermission);
                } else {

                    File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
                    if (!fileDirectory.exists()){
                        fileDirectory.mkdirs();
                    }
                    scanForRestoredPhotos();
                }
            }

        } else if (id == R.id.nav_contact_us) {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainFrame, new ContactUsFragment());
            transaction.addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {

            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "Recover Deleted Photos";
            String shareSub = "Let me recommend you this application\n\nRecover Deleted Photos\n";
            shareSub += "https://play.google.com/store/apps/details?id=com.studeregateway.recoverphotos";
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
            startActivity(Intent.createChooser(myIntent, "Share using"));

        } else if (id == R.id.nav_rate) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.studeregateway.recoverphotos"));
            startActivity(intent);

        } else if (id == R.id.nav_more_apps) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Studere Gateway"));
            startActivity(intent);

        } else if (id == R.id.nav_privacy_policy) {

            displayInterstitial();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://studeregateway.com/PrivacyPolicies/Recover_Deleted_Photos_Privacy_Policy.pdf"));
            startActivity(browserIntent);

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void scanForPhotos(){

        startActivity(new Intent(MainActivity.this,ScannerActivity.class));

    }
    private void scanForRestoredPhotos(){

        startActivity(new Intent(MainActivity.this,RestoredScannerActivity.class));

    }

    private void checkRunTimePermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED

            ){

                requestPermissions(AppConsts.PERMISSIONS,AppConsts.requestForPermission);
            }else{
                // allowed
                rlParent.setVisibility(View.VISIBLE);
            }
        } else {
            // less than Marshmallow
            rlParent.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConsts.requestForPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                        if(checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED
                        ){
                            Toast.makeText(this, "Permission must be required for this app", Toast.LENGTH_SHORT).show();
                            requestPermissions(AppConsts.PERMISSIONS, AppConsts.requestForPermission);
                        }else{
                            //allowed
                            rlParent.setVisibility(View.VISIBLE);

                        }
                    }
                } else {
                }

                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

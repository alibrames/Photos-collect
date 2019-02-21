package com.studeregateway.recoverphotos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.studeregateway.recoverphotos.Adapters.AdapterRestoredImages;
import com.studeregateway.recoverphotos.AsyncTask.ScanImagesAsyncTask;
import com.studeregateway.recoverphotos.Pojo.ImageData;
import com.studeregateway.recoverphotos.R;
import com.studeregateway.recoverphotos.Utils.Config;

import java.io.File;
import java.util.ArrayList;

public class RestoredScannerActivity extends AppCompatActivity {


    private ImageView ivLoading;
    private AdapterRestoredImages adapterImage;
    private MyDataHandler myDataHandler;
    private GridView gvAllPics;
    private ArrayList<ImageData> alImageData = new ArrayList();
    private TextView tvScannedPics;
    public static Toolbar toolbar;
    private AdView mAdView;
    private com.facebook.ads.AdView adView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_scanner);
        loadAd();
        init();
    }

    public void init(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDataHandler = new MyDataHandler();
        gvAllPics = findViewById(R.id.gvGallery);
        adapterImage = new AdapterRestoredImages(this, this.alImageData);
        gvAllPics.setAdapter(this.adapterImage);
        tvScannedPics = findViewById(R.id.tvItems);
        ivLoading = findViewById(R.id.iv_start_scan_anim);
        ivLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scan_animation));
        new ScanImagesAsyncTask(this, this.myDataHandler).execute("restored");
    }


    public void loadAd(){


        mAdView= findViewById(R.id.adView);


//        adView = new com.facebook.ads.AdView(RestoredScannerActivity.this, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
        adView = new com.facebook.ads.AdView(RestoredScannerActivity.this, "983327165210131_983392251870289", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        final LinearLayout adContainer = findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);



        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
//                Toast.makeText(RestoredScannerActivity.this, "Banner Error", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
                adContainer.setVisibility(View.GONE);

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
//                Toast.makeText(RestoredScannerActivity.this, "Banner Clicked", Toast.LENGTH_SHORT).show();

                mAdView.setVisibility(View.GONE);
                adContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
//                Toast.makeText(RestoredScannerActivity.this, "Banner Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
//                Toast.makeText(RestoredScannerActivity.this, "Banner Logged", Toast.LENGTH_SHORT).show();
            }
        });


        // Request an ad
        adView.loadAd();


//        MobileAds.initialize(RestoredScannerActivity.this, "ca-app-pub-3940256099942544/6300978111");      //test ad
        MobileAds.initialize(RestoredScannerActivity.this, "ca-app-pub-5455259317336088/2834675383");
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(RestoredScannerActivity.this);
            builder.setMessage("Delete selected items?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deletePictures(adapterImage.getSelectedItem());
                }
            });
            builder.create();
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deletePictures(ArrayList<ImageData> arrayList) {
        for (int i=0;i<arrayList.size();i++){
            File delFile = new File(arrayList.get(i).getFilePath());
            if (delFile.delete()){

                RestoredScannerActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(delFile)));

//                callBroadCast();
            }
        }



        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 19) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory()+"/RestoredPhotos".toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }


    public class MyDataHandler extends Handler {

        class AnimationHandlerClass implements Animation.AnimationListener {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            AnimationHandlerClass() {
            }

            public void onAnimationEnd(Animation animation) {
                ivLoading.setVisibility(View.GONE);
            }
        }

//        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == Config.DATA) {
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(500);
                animation.setFillAfter(true);
                animation.setAnimationListener(new AnimationHandlerClass());
                alImageData.clear();
                alImageData.addAll((ArrayList) message.obj);
                adapterImage.notifyDataSetChanged();
                tvScannedPics.setVisibility(View.GONE);
                ivLoading.clearAnimation();
                ivLoading.startAnimation(animation);
            } else if (message.what == Config.REPAIR) {
                adapterImage.notifyDataSetChanged();
            } else if (message.what == Config.UPDATE) {
                TextView tvFoundItems = tvScannedPics;
                StringBuilder sbProgressMessage = new StringBuilder();
                sbProgressMessage.append(message.obj.toString());
                sbProgressMessage.append(" ");
                sbProgressMessage.append("files found");
                tvFoundItems.setText(sbProgressMessage.toString());
            }
        }
    }
}
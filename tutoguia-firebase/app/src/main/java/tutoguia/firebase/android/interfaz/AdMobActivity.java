package tutoguia.firebase.android.interfaz;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import tutoguia.firebase.android.R;

public class AdMobActivity extends AppCompatActivity{

    private AdView bannerAdView;
    private RewardedVideoAd rewardedVideoAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_mob);

        //MobileAds.initialize(this, "ca-app-pub-9925564845537552~1433594031");
        MobileAds.initialize(this, "ca-app-pub-5673951569153335~6091985773");

        adRequest = new AdRequest.Builder().build();   // Production
        //adRequest = new AdRequest.Builder().addTestDevice("169FF41C30EBDD4BFA3558AAABE864E2").build();  // Debug

        loadBanner();
        loadRewardedVideo();
    }

    /**
     * Carga el banner superior
     */
    private void loadBanner(){
        final Context context = this;
        bannerAdView = findViewById(R.id.bannerAdView);
        bannerAdView.loadAd(adRequest);
        bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v("AdMob", "The user is about to return to the app after tapping on an ad");
                Toast.makeText(context, "onAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.v("AdMob", "The ad request fails");
                Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.v("AdMob", "The user has left the app");
                Toast.makeText(context, "onAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.v("AdMob", "The ad opens an overlay that covers the screen");
                Toast.makeText(context, "onAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.v("AdMob", "The ad finishes loading");
                Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga el vídeo recompensado
     */
    private void loadRewardedVideo(){
        final Context context = this;
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(context, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(context, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(context, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(context, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
                // Load a new video
                loadRewardedVideo();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(context, "onRewarded! currency: " + rewardItem.getType() + "  amount: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(context, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(context, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }
        });
        //rewardedVideoAd.loadAd("ca-app-pub-9925564845537552/8212902600", adRequest);
        rewardedVideoAd.loadAd("ca-app-pub-5673951569153335/3134649946", adRequest);
    }

    /**
     * Si el vídeo se ha cargado correctamente, lo abre
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void openVideo(View view){
        if(rewardedVideoAd.isLoaded())
            rewardedVideoAd.show();
    }
}

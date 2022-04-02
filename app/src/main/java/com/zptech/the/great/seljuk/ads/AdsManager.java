package com.zptech.the.great.seljuk.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.zptech.the.great.seljuk.PreferenceManager;
import com.zptech.the.great.seljuk.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class AdsManager {

    private Activity context;
    private InterstitialAd mInterstitialAd = null;
    private String unityGameID = "4388239";
    private AdView adView = null;

    private MaxInterstitialAd maxInterstitialAd;

    private MaxNativeAdLoader maxNativeAdLoader;
    private MaxAd             maxNativeAd;

    public AdsManager(Activity context) {
        this.context = context;
    }

    public void initializeUnityAds() {
        final UnityAdsListener myAdsListener = new UnityAdsListener();
        // Add the listener to the SDK:
        UnityAds.addListener(myAdsListener);
        // Initialize the SDK:
        UnityAds.initialize(context, unityGameID);
    }

    public void loadNativeLarge(FrameLayout frameLayout) {
        AdLoader.Builder builder = new AdLoader.Builder(context, new PreferenceManager(context).getADMOB_native());

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {

                        NativeAdView adView =
                                (NativeAdView) context.getLayoutInflater().inflate(R.layout.native_large, null);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    public void loadNativeBannerMax(AdView adView, FrameLayout relativeLayout) {
        maxNativeAdLoader = new MaxNativeAdLoader( new PreferenceManager(context).getMaxNativeSmall(), context );
        maxNativeAdLoader.setNativeAdListener( new MaxNativeAdListener()
        {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad)
            {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if ( maxNativeAd != null )
                {
                    maxNativeAdLoader.destroy( maxNativeAd );
                }

                // Save ad for cleanup.
                maxNativeAd = ad;

                // Add ad view to view.
                relativeLayout.removeAllViews();
                relativeLayout.addView( nativeAdView );
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error)
            {
                loadNativeBanner(adView,relativeLayout);
                // We recommend retrying with exponentially higher delays up to a maximum delay
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad)
            {
                // Optional click callback
            }
        } );

        maxNativeAdLoader.loadAd(createNativeAdView());

    }

    private MaxNativeAdView createNativeAdView()
    {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder( R.layout.max_native_small )
                .setTitleTextViewId( R.id.title_text_view )
                .setBodyTextViewId( R.id.body_text_view )
//                .setAdvertiserTextViewId( R.id.advertiser_textView )
                .setIconImageViewId( R.id.icon_image_view )
//                .setMediaContentViewGroupId( R.id.media_view_container )
                .setOptionsContentViewGroupId( R.id.options_view )
                .setCallToActionButtonId( R.id.cta_button )
                .build();

        return new MaxNativeAdView( binder, context );
    }

    public void loadNativeBanner(AdView adView, FrameLayout relativeLayout) {
        this.adView = adView;
        BannerView bottomBanner = new BannerView(context, "Banner_Android", new UnityBannerSize(UnityBannerSize.getDynamicSize(context).getWidth(), UnityBannerSize.getDynamicSize(context).getHeight()));
        bottomBanner.setListener(new UnityBannerListener());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomBanner.setLayoutParams(layoutParams);
        relativeLayout.addView(bottomBanner);
        bottomBanner.load();
    }

    private void loadBanner() {
        if (adView != null) {
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(new PreferenceManager(context).getADMOB_native_banner());
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });

            adView.loadAd(adRequest);
        }
    }

    public void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, new PreferenceManager(context).getAdmobInterstitial(), adRequest, new InterstitialAdLoadCallback() {

            @Override

            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;

            }

            @Override

            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
                initializeUnityAds();

            }

        });

    }

    public void loadInterstitialSplash() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, new PreferenceManager(context).getAdmobInterstitial(), adRequest, new InterstitialAdLoadCallback() {

            @Override

            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;

            }

            @Override

            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;

            }

        });

    }

    public void showInterstitial() {

        if (maxInterstitialAd.isReady()) {
            maxInterstitialAd.showAd();
        } else if (mInterstitialAd != null) {

            mInterstitialAd.show(context);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                @Override

                public void onAdDismissedFullScreenContent() {

                    //do something when interstitial ad is closed by user, or leave it blank


                }

                @Override

                public void onAdShowedFullScreenContent() {

                    //add below line
                    mInterstitialAd = null;
                    loadMaxInterstitial();

                }

            });

        } else {
            if (UnityAds.isReady()) {
                UnityAds.show(context);
            } else {
                loadMaxInterstitial();
            }
        }
    }

    public void loadMaxInterstitial() {

        maxInterstitialAd = new MaxInterstitialAd(new PreferenceManager(context).getMaxInterstitial(), context);
        maxInterstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d("TAG", "onAdLoaded: ");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                maxInterstitialAd.loadAd();
                Log.d("TAG", "onAdHidden: ");
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d("TAG", "onAdClicked: ");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d("TAG", "onAdLoadFailed: ");
                loadInterstitial();
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });
        maxInterstitialAd.loadAd();
    }

    public void showInterstitialSplash() {
        if (mInterstitialAd != null) {

            mInterstitialAd.show(context);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                @Override

                public void onAdDismissedFullScreenContent() {

                    //do something when interstitial ad is closed by user, or leave it blank


                }

                @Override

                public void onAdShowedFullScreenContent() {

                    //add below line

                    mInterstitialAd = null;

                }

            });

        }
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        }
    }

    public void loadBannerMAxMediation(@NotNull RelativeLayout relTopBanner) {
        MaxAdView adView = new MaxAdView( new PreferenceManager(context).getMaxBanner(), context );
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                relTopBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;

        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = context.getResources().getDimensionPixelSize( R.dimen.banner_height );

        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );

        relTopBanner.addView(adView);

        // Load the ad
        adView.loadAd();
    }

    private class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String adUnitId) {
            // Implement functionality for an ad being ready to show.
        }

        @Override
        public void onUnityAdsStart(String adUnitId) {
            // Implement functionality for a user starting to watch an ad.
        }

        @Override
        public void onUnityAdsFinish(String adUnitId, UnityAds.FinishState finishState) {
            // Implement functionality for a user finishing an ad.
            loadMaxInterstitial();
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
            // Implement functionality for a Unity Ads service error occurring.
            loadMaxInterstitial();
        }
    }


    private class UnityBannerListener implements BannerView.IListener {
        @Override
        public void onBannerLoaded(BannerView bannerAdView) {
            // Called when the banner is loaded.
        }

        @Override
        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
            // Note that the BannerErrorInfo object can indicate a no fill (see API documentation).
            loadBanner();
        }

        @Override
        public void onBannerClick(BannerView bannerAdView) {
            // Called when a banner is clicked.
        }

        @Override
        public void onBannerLeftApplication(BannerView bannerAdView) {
            // Called when the banner links out of the application.
        }
    }

}

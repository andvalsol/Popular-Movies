package com.example.luthiers.popularmovies.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.example.luthiers.popularmovies.CinephileApplication;

/*
 * For better user experience, retrieve the best quality image according to the user's current latency
 * */
public class LatencyGauging {
    
    /**
     * @return active network type
     */
    public static String checkLatency() {
        //Create a connectivity manager instance from the current Activity, which will always be the Main Activity
        ConnectivityManager connectivityManager = (ConnectivityManager) CinephileApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) CinephileApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        
        //Check that the connectivityManager instance is not null
        if (connectivityManager != null) {
            @SuppressLint("MissingPermission") //TODO add proper permission
                    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            
            //Get the current activeNetwork type
            switch (activeNetwork.getType()) {
                //We could only set the Mobile network type case and the default case, but for better readability set the Wifi type case also
                case (ConnectivityManager.TYPE_WIFI): {
                    //Apply standard latency strategy
                    return Constants.MEDIUM_RES_IMAGE_SIZE;
                }
                
                //The mobile type case has various types of network
                case (ConnectivityManager.TYPE_MOBILE): {
                    /*
                     * According to a post on LinkedIn => https://www.linkedin.com/pulse/differences-between-gprs-edge-3g-hsdpa-hspa-4g-lte-ransilu
                     * We can set an explanation for the various types of mobile network
                     * */
                    
                    //Check that the telephony manager is not null
                    if (telephonyManager != null) {
                        switch (telephonyManager.getNetworkType()) {
                            case (TelephonyManager.NETWORK_TYPE_LTE): {
                                //LTE is a 4G communication standard that supports HD video streaming, download speed as high as 299.6Mbps
                                return Constants.HIGH_RES_IMAGE_SIZE;
                            }
                            
                            case (TelephonyManager.NETWORK_TYPE_EDGE): {
                                //EDGE is significantly faster with a download speed of up to 384Kbps
                                return Constants.MEDIUM_RES_IMAGE_SIZE;
                            }
                            
                            case (TelephonyManager.NETWORK_TYPE_GPRS): {
                                //GPRS is a packet-based* wireless communication service. It is a 2G technology network that support a download speed of up to 114Kbps.
                                //Apply lower latency strategy
                                return Constants.LOW_RES_IMAGE_SIZE;
                            }
                            
                            default:
                                return Constants.MEDIUM_RES_IMAGE_SIZE;
                        }
                    }
                }
                
                default:
                    return Constants.MEDIUM_RES_IMAGE_SIZE;
            }
        } else {
            //The connectivityManager instance was null, but return the MEDIUM_RES_IMAGE_SIZE
            return Constants.MEDIUM_RES_IMAGE_SIZE;
        }
    }
}
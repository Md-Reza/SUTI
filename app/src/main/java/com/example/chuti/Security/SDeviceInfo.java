package com.example.chuti.Security;

import static android.content.Context.WIFI_SERVICE;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class SDeviceInfo {
    static String deviceName, ipAddress;

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getDeviceName(Context context) {
        try {
            deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return deviceName;
    }

    public static String getIPAddress(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
}

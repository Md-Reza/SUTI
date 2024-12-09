package com.example.chuti.Security;

import static android.content.Context.WIFI_SERVICE;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class SDeviceInfo {
    static String deviceName, ipAddress;

    public static String getMacAddress() {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = networkInterface.getHardwareAddress();
                    if (macBytes == null) {
                        return "02:00:00:00:00:00"; // Default MAC address if unavailable
                    }

                    StringBuilder macAddressBuilder = new StringBuilder();
                    for (byte b : macBytes) {
                        macAddressBuilder.append(String.format("%02X:", b));
                    }

                    if (macAddressBuilder.length() > 0) {
                        macAddressBuilder.deleteCharAt(macAddressBuilder.length() - 1); // Remove trailing colon
                    }

                    return macAddressBuilder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00"; // Default MAC address if retrieval fails
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER; // e.g., "Samsung"
            String model = Build.MODEL; // e.g., "Galaxy S21"

            if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
                return capitalize(model); // Avoid repeating manufacturer if included in the model
            } else {
                return capitalize(manufacturer) + " " + model;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return deviceName;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    public static String getDeviceUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getIPAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();

            // Convert the integer IP to a string format (IPv4)
            return InetAddress.getByAddress(new byte[]{
                    (byte) (ip & 0xff),
                    (byte) (ip >> 8 & 0xff),
                    (byte) (ip >> 16 & 0xff),
                    (byte) (ip >> 24 & 0xff)
            }).getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public static String getMobileIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress() && address instanceof java.net.Inet4Address) {
                        return address.getHostAddress(); // Return IPv4 address
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unable to get IP Address";
    }

    public static String getWifiIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipInt = wifiInfo.getIpAddress();

            // Convert the integer IP to a readable format
            return InetAddress.getByAddress(new byte[]{
                    (byte) (ipInt & 0xff),
                    (byte) (ipInt >> 8 & 0xff),
                    (byte) (ipInt >> 16 & 0xff),
                    (byte) (ipInt >> 24 & 0xff)
            }).getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to get IP Address";
        }
    }
    public static String getDeviceIpAddress(Context context) {
        String ipAddress = getWifiIpAddress(context); // First try Wi-Fi
        if ("Unable to get IP Address".equals(ipAddress) || ipAddress.isEmpty()) {
            ipAddress = getMobileIpAddress(); // Fallback to mobile network
        }
        return ipAddress;
    }
}

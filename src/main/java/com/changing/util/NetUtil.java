package com.changing.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuchangqing on 2016/12/28.
 */
public class NetUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);

    // 本机IP
    public static volatile String LOCAL_IP = null;

    /**
     * 获取本机的IP
     */
    public static String getMyIPLocal() {
        if (StringUtils.isNotBlank(LOCAL_IP)) {
            return LOCAL_IP;
        }

        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            List<String> addressList = new ArrayList<String>();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        addressList.add(ip.getHostAddress());
                    }
                }
            }

            //String plantSystemName = System.getProperty("os.name").toLowerCase();
            /*windows 和 linux 上不一样。 linux上netInterface.name=eth0 ,windows上是eth4
            为了兼容，先获取所有IP地址，在过滤*/
            String ipAddress = "";
            for (String address : addressList) {
                if (!"127.0.0.1".equals(address)) {
                    ipAddress = address;
                }
            }

            LOCAL_IP = ipAddress;
            return ipAddress;
        } catch (SocketException e) {
            LOGGER.error("get NetworkInterfaces error : ", e);
            return "";
        }
    }

    public static String getMacAddr() {
        String MacAddr = "";
        String str = "";
        try {
            Enumeration allNetInterfaces = null;
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                byte[] buf = netInterface.getHardwareAddress();
                if (buf != null) {
                    for (int i = 0; i < buf.length; i++) {
                        str = str + byteHEX(buf[i]);
                    }
                    MacAddr = str.toUpperCase();
                }
            }

        } catch (SocketException e) {
            LOGGER.error("get macAddr error : ", e);
        }
        return MacAddr;
    }

    public static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}

package lab1.Util;

import lab1.Exception.ErrorCode;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String encode(byte[] bytes) {
        byte[] secretBytes;
        try {
            // 加密
            secretBytes = MessageDigest.getInstance("md5").digest(bytes);
            // 改为16进制的字符串
            StringBuilder md5 = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
            for (int i = 0; i < 32 - md5.length(); i++) {
                md5.insert(0, "0");
            }
            return md5.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(ErrorCode.getErrorText(ErrorCode.MD5_FAILED));
        }
        return null;
    }

}

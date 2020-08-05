package com.tools;


/**
 * 异或运算加密和解密
 *
 * @author Host-0222
 */
public class XwhXorSecurity {
    // 密钥
    public static final String SECURITY = "MDbGUVkWecymbrveAkwXwRVhAYRUDParAPuSftqdmaeNmNcansDOyeVpnMSOSwTC";

    /**
     * 加密
     *
     * @param resultByte
     * @return
     */
    public static byte[] encryption(byte[] resultByte) {
        return xorSecurity(resultByte);
    }

    /**
     * 解密
     *
     * @param resultByte
     * @return
     */
    public static byte[] decrypt(byte[] resultByte) {
        return xorSecurity(resultByte);
    }

    /**
     * 异或逻辑操作
     *
     * @param resultByte
     * @return
     */
    private static byte[] xorSecurity(byte[] resultByte) {
        if (resultByte == null)
            return null;
        int length = resultByte.length;
        byte[] key = getDynamicKey(length);
        int len = key.length;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (resultByte[i] ^ key[i % len]);
        }
        return bytes;
    }

    /**
     * 获取动态密值
     *
     * @param length
     * @return
     */
    private static byte[] getDynamicKey(int length) {
        int len = length % SECURITY.length();
        StringBuilder builder = new StringBuilder();
        for (int i = len; i < SECURITY.length(); i++) {
            char c = SECURITY.charAt(i);
            builder.append(c);
        }
        for (int i = 0; i < len; i++) {
            char c = SECURITY.charAt(i);
            builder.append(c);
        }
        System.out.println("key:" + builder.toString());
        return builder.toString().getBytes();
    }
}

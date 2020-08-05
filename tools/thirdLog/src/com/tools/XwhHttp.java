package com.tools;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;

/**
 * 私有操作HTTP方法信息
 *
 * @author Host-0222
 */
public class XwhHttp {
    /**
     * 进行一次GET类型的 HTTP 连接的方法
     *
     * @param urlParam 链接参数
     */
    public static String sendGet(String urlParam) throws Exception {
        byte[] resultByte = makeHttpConnect(urlParam, null, null, "GET", "utf-8", null, 60000, 60000);
        if (resultByte != null) {
            return new String(resultByte, "utf-8");
        }
        return null;
    }

    /**
     * 表单发送数据
     */
    public static String sendFromPost(String urlParam, String json) throws Exception {
        byte[] buffer = json.getBytes("utf-8");
        byte[] resultByte = makeHttpConnect(urlParam, null, null, "POST", "utf-8", buffer, 0, 0);
        if (resultByte != null) {
            return new String(resultByte, "utf-8");
        }
        return null;
    }

    /**
     * json格式发送POST请求
     *
     * @param url     访问地址
     * @param param   JSON对象
     * @param charset 解析编码格式
     * @return
     */
    public static String sendPostByGson(String url, Object param, String charset) throws Exception {

        byte[] sendByte = JSONObject.toJSONBytes(param);
        byte[] resultByte = makeHttpConnect(url, "close", "application/octet-stream", "POST", charset, sendByte, 60000, 30000);
        if (resultByte != null) {
            return new String(resultByte, charset);
        }
        return null;
    }

    /**
     * 操作一次HTTP链接
     *
     * @param urlParam       访问链接地址
     * @param connection     当次链接状态，HTTP1.1默认为保持（Keep-Alive），设置Connection: Close关闭
     * @param contentType    [type]/[subtype]，后面的文档属于什么MIME类型
     * @param method         当次访问访问类型
     * @param charset        编号格式
     * @param buffer         访问参数
     * @param connectTimeout 链接超时时间
     * @param readTimeout    读取超时时间
     * @return
     */
    public static byte[] makeHttpConnect(String urlParam, String connection, String contentType, String method, String charset, byte[] buffer, int connectTimeout, int readTimeout) {
        try {
            URL url = new URL(urlParam);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            if (connectTimeout > 0) {
                httpConn.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                httpConn.setReadTimeout(readTimeout);
            }

            httpConn.setRequestMethod(method);
            if (contentType != null) {
                httpConn.setRequestProperty("Content-Type", contentType);
            }
            if (connection != null) {
                httpConn.setRequestProperty("Connection", connection);
            }

            httpConn.setRequestProperty("Charset", charset);
            if (buffer != null) {
                httpConn.setRequestProperty("Content-length", String.valueOf(buffer.length));
                OutputStream out = new DataOutputStream(httpConn.getOutputStream());
                out.write(buffer);
                out.flush();
                out.close();
            }
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                byte[] readBytes = new byte[4 * 1024];
                int readed = 0;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((readed = inStream.read(readBytes)) != -1) {
                    byteStream.write(readBytes, 0, readed);
                }
                byte[] b = byteStream.toByteArray();
                byteStream.close();
                inStream.close();
                return b;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回结果值
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public String sendHttps(String xml, String interface_url, String p12, String password) throws Exception {
        try {
            URL url = new URL(interface_url);
            TrustManager[] trust = new TrustManager[]{new EmptyX509TrustManager()};
            KeyManager[] tm = createKeyMager(p12, password);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(tm, trust, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setSSLSocketFactory(ssf);
            httpsConn.setSSLSocketFactory(ssf);
            httpsConn.setDoOutput(true);
            httpsConn.setDoInput(true);
            httpsConn.setUseCaches(false);
            httpsConn.setRequestMethod("POST");
            httpsConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            OutputStream outputStream = httpsConn.getOutputStream();
            // 注意编码格式
            outputStream.write(xml.getBytes("UTF-8"));
            outputStream.close();
            InputStream ins = httpsConn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            do {
                byte[] temp = new byte[1024];
                int bufferLength = ins.read(temp);
                if (bufferLength < 0) {
                    break;
                }
                bos.write(temp, 0, bufferLength);
            } while (true);

            ins.close();
            httpsConn.disconnect();

            byte[] getXml = bos.toByteArray();
            return new String(getXml, "utf-8");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 检验服务器
     *
     * @param stream
     * @param password
     * @return
     */
    public TrustManager[] createTrustManager(InputStream stream, String password) {
        try {
            if (stream != null) {
                // String type = KeyStore.getDefaultType();
                // System.out.println(type);
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(stream, password.toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(ks);
                return trustManagerFactory.getTrustManagers();
            } else {
                return new TrustManager[]{new EmptyX509TrustManager()};
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置KeyMager
     *
     * @param p12
     * @param password
     * @return
     */
    private KeyManager[] createKeyMager(String p12, String password) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(p12), password.toCharArray());
            KeyManagerFactory trustManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ks, password.toCharArray());
            return trustManagerFactory.getKeyManagers();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}

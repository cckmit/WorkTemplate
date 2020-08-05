package com.tool;

import java.io.*;
import java.util.zip.*;

/**
 * 通用压缩数据模块
 *
 * @author Vinnes
 */
public class CmZip {

    /**
     * 压缩
     *
     * @param data 待压缩数据
     * @return byte[] 压缩后的数据
     */
    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        return output;
    }

    /**
     * 压缩
     *
     * @param data 待压缩数据
     * @param os   输出流
     */
    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);

        try {
            dos.write(data, 0, data.length);

            dos.finish();

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩
     *
     * @param data 待压缩的数据
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(byte[] data) {
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        decompresser.end();
        return output;
    }

    /**
     * 解压缩
     *
     * @param is 输入流
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        try {
            int i = 1024;
            byte[] buf = new byte[i];

            while ((i = iis.read(buf, 0, i)) > 0) {
                o.write(buf, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }

    /**
     * 压缩字节流数据
     *
     * @param bytes 传入需要压缩的字节流
     * @return 压缩的数据
     * @throws IOException
     */
    public static byte[] zipBytes(byte[] bytes) throws IOException {
        ByteArrayOutputStream tempOStream = null;
        BufferedOutputStream tempBOStream = null;
        ZipOutputStream tempZStream = null;
        ZipEntry tempEntry = null;
        byte[] tempBytes = null;

        tempOStream = new ByteArrayOutputStream(bytes.length);
        tempBOStream = new BufferedOutputStream(tempOStream);
        tempZStream = new ZipOutputStream(tempBOStream);
        tempEntry = new ZipEntry(String.valueOf(bytes.length));
        tempEntry.setMethod(ZipEntry.DEFLATED);
        tempEntry.setSize((long) bytes.length);

        tempZStream.putNextEntry(tempEntry);
        tempZStream.write(bytes, 0, bytes.length);
        tempZStream.flush();
        tempBOStream.flush();
        tempOStream.flush();
        tempZStream.close();
        tempBytes = tempOStream.toByteArray();
        tempOStream.close();
        tempBOStream.close();
        return tempBytes;
    }

    /**
     * 解压数据流
     *
     * @param bytes 传入数据流
     * @return 解压的数据
     * @throws IOException
     */
    public static byte[] unzipBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream tempIStream = null;
        BufferedInputStream tempBIStream = null;
        ZipInputStream tempZIStream = null;
        ZipEntry tempEntry = null;
        long tempDecompressedSize = -1;
        byte[] tempUncompressedBuf = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        tempIStream = new ByteArrayInputStream(bytes, 0, bytes.length);
        tempBIStream = new BufferedInputStream(tempIStream);
        tempZIStream = new ZipInputStream(tempBIStream);
        tempEntry = tempZIStream.getNextEntry();

        if (tempEntry != null) {
            tempDecompressedSize = tempEntry.getCompressedSize();

            if (tempDecompressedSize < 0) {
                tempDecompressedSize = Long.parseLong(tempEntry.getName());
            }

            int size = (int) tempDecompressedSize;
            tempUncompressedBuf = new byte[size];
            int num = 0, count = 0;
            while (true) {
                count = tempZIStream.read(tempUncompressedBuf, 0, size - num);
                baos.write(tempUncompressedBuf, 0, count);
                num += count;
                if (num >= size)
                    break;
            }
        }

        tempUncompressedBuf = baos.toByteArray();
        baos.flush();
        baos.close();
        tempZIStream.close();
        return tempUncompressedBuf;
    }
}

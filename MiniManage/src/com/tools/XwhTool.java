/**
 * 
 */
package com.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Vinnes
 * 
 */
public class XwhTool
{
	/**
	 * 从二进制到字符串的方法
	 * 
	 * @param hexString
	 *            二进制信息
	 * @return 得到的字符串信息
	 */
	public static String getStringFromHex(String hexString)
	{
		byte[] bytes = new byte[hexString.length() / 2];

		for (int i = 0, j = 0; i < hexString.length(); i += 2, j++)
		{
			String value = "0x" + hexString.substring(i, i + 2);

			bytes[j] = Integer.decode(value).byteValue();
		}

		return new String(bytes);
	}

	/**
	 * 从字符串到二进制流的方法
	 * 
	 * @param content
	 *            源字串信息
	 * @return 得到的二进制流的文本描述
	 */
	public static String getStringToHex(String content)
	{
		byte[] bytes = content.getBytes();
		StringBuilder sb = new StringBuilder();
		String temp = null;

		for (int i = 0; i < bytes.length; i++)
		{
			temp = Integer.toHexString(bytes[i] & 0xff);
			if (temp.length() == 1)
			{
				sb.append("0");
			}
			sb.append(temp);
		}

		return sb.toString();
	}

	/**
	 * 通过字串长度间隔添加下划线
	 * 
	 * @param str
	 * @param lastLen
	 * @return
	 */
	public static String addStrUnderlineByLastLen(String str, int lastLen)
	{
		StringBuilder name = new StringBuilder();
		for (int n = 0; n < str.length(); n++)
		{
			if (n == str.length() - lastLen)
			{
				name.append("_");
			}
			name.append(str.charAt(n));
		}
		return name.toString();
	}

	/**
	 * 获取系统毫秒级时间
	 * 
	 * @return
	 */
	public static long getMillisecondTime()
	{
		Date date = new Date();
		return date.getTime();
	}
	/**
	 * 获取固定日期时间戳
	 * 
	 * @return
	 */
	public static long getMillisecondDateTime(String date)
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date dates = null;
		try {
			dates = sm.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dates.getTime();
	}

	/**
	 * 获取当前时间格式的方法 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 得到的时间值
	 */
	public static String getCurrentDateTimeValue()
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String temp = sm.format(date);
		
		return temp;
	}
	
	/**
	 * 获取当前时间格式的方法
	 * 
	 * @return 得到的时间值
	 */
	public static int getCurrentDateValue()
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date(System.currentTimeMillis());
		String temp = sm.format(date);

		return Integer.parseInt(temp);
	}
	/**
	 * 获取前一天格式的方法
	 * 
	 * @return 得到的时间值
	 */
	public static String getPriDateStr()
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date(System.currentTimeMillis());
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, -1);
		String temp = sm.format(startDT.getTime());
		
		return temp;
	}
	/**
	 * 获取今天格式的方法
	 * 
	 * @return 得到的时间值
	 */
	public static String getPriDateWithChar()
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, -1);
		String temp = sm.format(startDT.getTime());
		return temp;
	}
	/**
	 * 获取今天格式的方法
	 * 
	 * @return 得到的时间值
	 */
	public static String getPriDateWithChar(int sum)
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, sum);
		String temp = sm.format(startDT.getTime());
		return temp;
	}
	/**
	 * 获取当天格式的方法
	 * 
	 * @return 得到的时间值
	 */
	public static String getPriDateWithCharS()
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String temp = sm.format(date);
		return temp;
	}
	/**
	 * 获取当前时间格式的方法
	 * 
	 * @return 得到的时间值
	 */
	public static int getCurrentHour()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 读取流数据
	 * 
	 * @param in
	 * @return
	 */
	public static String readInputStream(InputStream in)
	{
		try
		{
			int length = in.available();
			byte[] bytes = new byte[length];
			in.read(bytes);
			in.close();
			return new String(bytes,"utf-8");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读入一个文本的方法
	 * 
	 * @param path
	 *            路径信息
	 * @return 得到的文本信息
	 */
	public static String readFileString(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			return null;
		}
		try
		{
			FileInputStream fis = new FileInputStream(file);
			return readInputStream(fis);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 进行获取java文件所处位置
	 * 
	 * @param srcPath
	 * @param name
	 * @return
	 */
	public static File getSrcPath(File srcPath, String name)
	{
		// 获取当前源路径下所有文件
		Vector<File> allFiles = getAllFileByDir(srcPath);
		for (File file : allFiles)
		{
			String filename = file.getName();
			if (filename.equals(name))
			{
				return file;
			}
		}
		return null;
	}

	/**
	 * 获取所有文件信息
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Vector<File> getAllFileByDir(File srcPath)
	{
		Vector<File> all = new Vector<>();
		if (srcPath.isFile())
		{
			all.add(srcPath);
		} else
		{
			for (File file : srcPath.listFiles())
			{
				all.addAll(getAllFileByDir(file));
			}
		}
		return all;
	}

	/**
	 * 通过IMSI获取运营商
	 * 
	 * @param imsi
	 * @return 0-移动，1-联通，2-电信,-1其他
	 */
	public static int getOperatorType(String imsi)
	{

		if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007"))
		{
			return 0;
		} else if (imsi.startsWith("46001") || imsi.startsWith("46006"))
		{
			return 1;
		} else if (imsi.startsWith("46003") || imsi.startsWith("46005"))
		{
			return 2;
		}
		return -1;
	}

	/**
	 * 将数据写入文件
	 * 
	 * @param path
	 *            文件路径
	 * @param content
	 *            数据内容
	 * @param isAppead
	 *            false 覆盖文件内容，ture 文件尾部添加
	 */
	public static void writeFile(String path, String content)
	{
		try
		{
			File file = new File(path);
			FileOutputStream fos = new FileOutputStream(file);

			byte[] bytes = content.getBytes();

			fos.write(bytes);
			fos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 创建上一级目录
	 * 
	 * @param srcPath
	 * @return
	 */
	public static File createNextDir(File srcPath, String... dirs)
	{
		for (String dirname : dirs)
		{
			srcPath = new File(srcPath, dirname);
			if (!srcPath.exists())
			{
				srcPath.mkdir();
			}
		}
		return srcPath;
	}

	/**
	 * 检查是否是工作日. true-工作日 工作日规定为周一到周五 时间点定位早上8:00 am-8:00pm
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWorkdingDay(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (weekDay >= 2 && weekDay <= 6 && hour >= 8 && hour <= 20)
		{
			return true;
		}
		return false;
	}

	/**
	 * 获取转义INT字串
	 * 
	 * @param value
	 * @return
	 */
	public static int getIntValue(String value)
	{
		try
		{
			if (value != null && XWHMathTool.isNumeric(value.trim()))
			{
				return Integer.valueOf(value.trim());
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return 0;
	}

	/**
	 * 获取转义INT字串
	 * 
	 * @param value
	 * @return
	 */
	public static long getLongValue(String value)
	{
		try
		{
			if (value != null && XWHMathTool.isNumeric(value.trim()))
			{
				return Long.valueOf(value.trim());
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return 0;
	}

	/**
	 * 获取文件md5值
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException
	{
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try
		{
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			value = byteArrayToHexString(md5.digest());
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (null != in)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * 进行计算32进制MD5
	 * 
	 * @param source
	 * @return
	 */
	public static String getMD5(byte[] source)
	{
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++)
			{ // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 通过字串获取对应MD5
	 * 
	 * @param sign
	 * @return
	 */
	public static String getMD5Encode(String sign)
	{
		try
		{
			String result = new String(sign);
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(result.getBytes()));
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 进行byte计算
	 * 
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte b[])
	{
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	/**
	 * 进行混淆byte
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b)
	{
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 解析协议字段
	 * 
	 * @param info
	 * @return
	 */
	public static String filterSpecialInfo(String info)
	{
		try
		{
			info = info.trim();
			info = info.toLowerCase();
			char[] tempArray = info.toCharArray();
			String number = "";

			for (int i = 0; i < tempArray.length; i++)
			{
				char code = tempArray[i];

				if (code >= '0' && code <= '9')
				{
					number += code;
				} else if (code >= 'a' && code <= 'z')
				{
					number += code;
				} else if (code >= 'A' && code <= 'Z')
				{
					number += code;
				} else if (code == ',' || code == '.')
				{
					number += code;
				}
			}
			return number;

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 解析操作系统版本
	 * 
	 * @param version
	 * @return
	 */
	public static int getSystemVersionInt(String version)
	{
		try
		{
			int[] versionList = { 0, 0, 0, 0 };

			String[] parseList = version.split("[.]");

			int spaceValue = 1000;
			for (int i = 0; i < parseList.length; i++)
			{
				try
				{
					int value = Integer.parseInt(parseList[i]);
					value *= spaceValue;
					versionList[i] = value;

					spaceValue /= 10;
				} catch (Exception e)
				{
					// TODO: handle exception
				}
			}

			int value = 0;
			for (int i = 0; i < versionList.length; i++)
			{
				value += versionList[i];
			}

			return value;
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		return 0;
	}

	public static <T> T parseConfigString(String content, Class<T> c)
	{
		return parseConfigString(content, c, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 解析配置字符串的方法
	 * 
	 * @param <T>
	 * 
	 * @param <V>
	 * @param <T>
	 * 
	 * @param content
	 *            字符串内容信息
	 * @return 得到的解析结果
	 */
	public static <T> T parseConfigString(String content, Class<T> c, String dateformat)
	{

		if (null == content)
		{
			return null;
		}
		try
		{
			Gson gson = null;
			if (dateformat != null)
				gson = new GsonBuilder().setDateFormat(dateformat).create();
			else
				gson = new Gson();
			return gson.fromJson(content, c);
		} catch (Exception e)
		{
			ObjectMapper objectMapper = new ObjectMapper();
			try
			{
				return objectMapper.readValue(content, c);
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 进行转JSON字串
	 * 
	 * @param map
	 * @return
	 */
	public static String getJsonValue(Object object)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e)
		{
			e.printStackTrace();
		} catch (JsonMappingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 通过GSON架包获取JSON字串
	 * 
	 * @param object
	 * @return
	 */
	public static String getGsonValue(Object object, String dateformat)
	{
		Gson gson = null;
		if (dateformat != null)
			gson = new GsonBuilder().setDateFormat(dateformat).create();
		else
			gson = new Gson();
		return gson.toJson(object);
	}

	public static String getGsonValue(Object object)
	{
		return getGsonValue(object, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将一个字符串去皮的方法
	 * 
	 * @param text
	 *            字符串文本信息
	 * @return 去皮后的字符串
	 */
	public static String trimExt(String text)
	{
		StringBuilder sb = new StringBuilder(text);
		for (int i = 0; i < sb.length(); i++)
		{
			char c = sb.charAt(i);
			if (c == '\n' || c == '\r')
			{
				sb.setCharAt(i, ' ');
				continue;
			}
			break;
		}
		for (int i = sb.length() - 1; i >= 0; i--)
		{
			char c = sb.charAt(i);
			if (c == '\n' || c == '\r')
			{
				sb.setCharAt(i, ' ');
				continue;
			}
			break;
		}
		String temp = sb.toString();
		return temp.trim();
	}

	/**
	 * 服务器转码字符
	 */

	public static byte[] decoderUTFString(String info)
	{
		try
		{
			return info.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			return info.getBytes();
		}
	}

	/**
	 * 解码客户端提交数据
	 * 
	 * @param bytes
	 * @return
	 */
	public static String decoderUTFBytes(byte[] bytes)
	{
		try
		{
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 压缩字节流数据
	 * 
	 * @param bytes
	 *            传入需要压缩的字节流
	 * @return 压缩的数据
	 * @throws IOException
	 */
	public static byte[] pmZipBytes(byte[] bytes) throws IOException
	{
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
	 * @param bytes
	 *            传入数据流
	 * @return 解压的数据
	 * @throws IOException
	 */
	public static byte[] unzipBytes(byte[] bytes) throws IOException
	{
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

		if (tempEntry != null)
		{
			tempDecompressedSize = tempEntry.getCompressedSize();

			if (tempDecompressedSize < 0)
			{
				tempDecompressedSize = Long.parseLong(tempEntry.getName());
			}

			int size = (int) tempDecompressedSize;
			tempUncompressedBuf = new byte[size];
			int num = 0, count = 0;
			while (true)
			{
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

	/**
	 * 获取需要处理的路径名称
	 * 
	 * @param encryptionPath
	 * @return
	 */
	public static String getDealFilePath(String encryptionPath, String suffix, int nextlen)
	{
		String path = encryptionPath + (nextlen == 0 ? "" : "(" + nextlen + ")") + suffix;
		if (new File(path).exists())
		{
			return getDealFilePath(encryptionPath, suffix, ++nextlen);
		} else
		{
			return path;
		}
	}

	/**
	 * 创建密钥
	 * 
	 * @return
	 */
	public static String createSecurity()
	{
		// 获取密钥
		StringBuilder builder = new StringBuilder();
		int length = 'z' - 'a';
		// 密钥32位
		for (int i = 0; i < 64; i++)
		{
			int random = new Random().nextInt(length);
			char c = (char) ('a' + random);
			if (new Random().nextBoolean())
			{
				c -= 32;
			}
			builder.append(c);
		}
		return builder.toString();
	}
	
	/**
	 * 下载方法
	 * 
	 * @param response
	 * @param request
	 * @param path
	 *            文件路径
	 */
	public static final void downLoadFile(HttpServletResponse response,
			HttpServletRequest request, String path) {
		File downloadFile = new File(path);
		if (downloadFile != null && downloadFile.exists()) {
			if (downloadFile.isFile()) {
				if (downloadFile.length() > 0) {
				} else {
					System.out.println("请求下载的文件是一个空文件");
					return;
				}
				if (!downloadFile.canRead()) {
					System.out.println("请求下载的文件不是一个可读的文件");
					return;
				} else {
				}
			} else {
				System.out.println("请求下载的文件是一个文件夹");
				return;
			}
		} else {
			System.out.println("请求下载的文件不存在！");
			return;
		}
		long fileLength = downloadFile.length(); // 记录文件大小
		long pastLength = 0; // 记录已下载文件大小
		int rangeSwitch = 0; // 0：从头开始的全文下载；1：从某字节开始的下载（bytes=27000-）；2：从某字节开始到某字节结束的下载（bytes=27000-39000）
		long toLength = 0; // 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
		long contentLength = 0; // 客户端请求的字节总量
		String rangeBytes = ""; // 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容
		RandomAccessFile raf = null; // 负责读取数据
		OutputStream os = null; // 写出数据
		OutputStream out = null; // 缓冲
		byte b[] = new byte[1024]; // 暂存容器

		if (request.getHeader("Range") != null) { // 客户端请求的下载的文件块的开始字节
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			System.out.println("request.getHeader(\"Range\")="
					+ request.getHeader("Range"));
			rangeBytes = request.getHeader("Range").replaceAll("bytes=", "");
			if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {// bytes=969998336-
				rangeSwitch = 1;
				rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				pastLength = Long.parseLong(rangeBytes.trim());
				contentLength = fileLength - pastLength; // 客户端请求的是 969998336
															// 之后的字节
			} else { // bytes=1275856879-1275877358
				rangeSwitch = 2;
				String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				String temp2 = rangeBytes.substring(
						rangeBytes.indexOf('-') + 1, rangeBytes.length());
				pastLength = Long.parseLong(temp0.trim()); // bytes=1275856879-1275877358，从第
															// 1275856879
															// 个字节开始下载
				toLength = Long.parseLong(temp2); // bytes=1275856879-1275877358，到第
													// 1275877358 个字节结束
				contentLength = toLength - pastLength; // 客户端请求的是
														// 1275856879-1275877358
														// 之间的字节
			}
		} else { // 从开始进行下载
			contentLength = fileLength; // 客户端要求全文下载
		}

		/**
		 * 如果设设置了Content -Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。 响应的格式是:
		 * Content - Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
		 * ServletActionContext.getResponse().setHeader("Content- Length", new
		 * Long(file.length() - p).toString());
		 */
		response.reset(); // 告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes
		response.setHeader("Accept-Ranges", "bytes");// 如果是第一次下,还没有断点续传,状态是默认的
														// 200,无需显式设置;响应的格式是:HTTP/1.1
														// 200 OK
		if (pastLength != 0) {
			// 不是从最开始下载,
			// 响应的格式是:
			// Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
			System.out
					.println("----------------------------不是从开始进行下载！服务器即将开始断点续传...");
			switch (rangeSwitch) {
			case 1: { // 针对 bytes=27000- 的请求
				String contentRange = new StringBuffer("bytes ")
						.append(new Long(pastLength).toString()).append("-")
						.append(new Long(fileLength - 1).toString())
						.append("/").append(new Long(fileLength).toString())
						.toString();
				response.setHeader("Content-Range", contentRange);
				break;
			}
			case 2: { // 针对 bytes=27000-39000 的请求
				String contentRange = rangeBytes + "/"
						+ new Long(fileLength).toString();
				response.setHeader("Content-Range", contentRange);
				break;
			}
			default: {
				break;
			}
			}
		} else {
			// 是从开始下载
			System.out.println("----------------------------是从开始进行下载！");
		}

		try {
			String filename = URLEncoder
					.encode(downloadFile.getName(), "utf-8");
			System.out.println(filename);
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			response.setContentType(CommonUtil.setContentType(downloadFile
					.getName())); // set the MIME type.
			response.addHeader("Content-Length", String.valueOf(contentLength));
			os = response.getOutputStream();
			out = new BufferedOutputStream(os);
			raf = new RandomAccessFile(downloadFile, "r");
			try {
				switch (rangeSwitch) {
				case 0: { // 普通下载，或者从头开始的下载
							// 同1
				}
				case 1: { // 针对 bytes=27000- 的请求
					raf.seek(pastLength); // 形如 bytes=969998336- 的客户端请求，跳过
											// 969998336 个字节
					int n = 0;
					while ((n = raf.read(b, 0, 1024)) != -1) {
						out.write(b, 0, n);
					}
					break;
				}
				case 2: { // 针对 bytes=27000-39000 的请求
					raf.seek(pastLength); // 形如 bytes=1275856879-1275877358
											// 的客户端请求，找到第 1275856879 个字节
					int n = 0;
					long readLength = 0; // 记录已读字节数
					while (readLength <= contentLength - 1024) {// 大部分字节在这里读取
						n = raf.read(b, 0, 1024);
						readLength += 1024;
						out.write(b, 0, n);
					}
					if (readLength <= contentLength) { // 余下的不足 1024 个字节在这里读取
						n = raf.read(b, 0, (int) (contentLength - readLength));
						out.write(b, 0, n);
					}
					break;
				}
				default: {
					break;
				}
				}
				out.flush();
				System.out.println("------------------------------下载结束");
			} catch (IOException ie) {
				// ignore
				System.out
						.println("#提醒# 向客户端传输时出现IO异常，但此异常是允许的的，有可能客户端取消了下载，导致此异常，不用关心！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成发票序列号
	 * @return
	 */
	public static String generateBillSericode(){
		DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
		String bgdate = df.format(new Date()); 
		System.out.println(bgdate);
		return bgdate;
	}
	
	/**
	 * 获取从给定的年-月到现在的所有年-月
	 * @param startYearAndMonth
	 * @return
	 */
	public static List<String> getAllMonthFrom(String startYearAndMonth){
		List<String> months = new ArrayList<>();
		if(!Pattern.compile("^[0-9]{4}-[0-9]{2}$").matcher(startYearAndMonth).matches()){
			return months;
		}
		int startYear = Integer.parseInt(startYearAndMonth.split("-")[0]);
		int startMonth = Integer.parseInt(startYearAndMonth.split("-")[1]);
		
		Calendar calendar = Calendar.getInstance();
		int nowYear = calendar.get(Calendar.YEAR);
		int nowMonth = calendar.get(Calendar.MONTH) + 1;
		for(int i = nowYear;i >= startYear;i--){
			int minMonth = i == startYear ? startMonth : 1;
			int maxMonth = i == nowYear ? nowMonth : 12;
			for(int j = maxMonth;j >= minMonth;j--){
				months.add(i+"-"+(j<10?"0"+j:j));
			}
		}
		return months;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getAllMonthFrom("2015-01"));
	}
}

package com.kk.lp.utils;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 处理字符串工具类
 * 
 * @author:PengGuoHua
 * @see:
 * @since:
 * @copyright © soufun.com
 * @Date:2014-10-31
 */
public class StringUtils{

	/**
	 * 判断是否为空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNullOrEmpty(String text) {
		if (text == null || "".equals(text.trim()) || text.trim().length() == 0 || "null".equals(text.toLowerCase().trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得MD5加密字符串
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			UtilsLog.w("signature", e.toString());
		} catch (UnsupportedEncodingException e) {
			UtilsLog.w("signature", e.toString());
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 得到字符串长度
	 * 
	 * @param text
	 * @return
	 */
	public static int getCharCount(String text) {
		String Reg = "^[\u4e00-\u9fa5]{1}$";
		int result = 0;
		for (int i = 0; i < text.length(); i++) {
			String b = Character.toString(text.charAt(i));
			if (b.matches(Reg))
				result += 2;
			else
				result++;
		}
		return result;
	}

	/**
	 * 获取截取后的字符串
	 * 
	 * @param text
	 *            原字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String getSubString(String text, int length) {
		return getSubString(text, length, true);
	}

	/**
	 * 获取截取后的字符串
	 * 
	 * @param text
	 *            原字符串
	 * @param length
	 *            截取长度
	 * @param isOmit
	 *            是否加上省略号
	 * @return
	 */
	public static String getSubString(String text, int length, boolean isOmit) {
		if (isNullOrEmpty(text)) {
			return "";
		}
		if (getCharCount(text) <= length + 1) {
			return text;
		}

		StringBuffer sb = new StringBuffer();
		String Reg = "^[\u4e00-\u9fa5]{1}$";
		int result = 0;
		for (int i = 0; i < text.length(); i++) {
			String b = Character.toString(text.charAt(i));
			if (b.matches(Reg)) {
				result += 2;
			} else {
				result++;
			}

			if (result <= length + 1) {
				sb.append(b);
			} else {
				if (isOmit) {
					sb.append("...");
				}
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 邮箱验证
	 * 
	 * @param mail
	 *            邮箱
	 * @return
	 */
	public static boolean validateEmail(String mail) {
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = pattern.matcher(mail);
		return m.matches();
	}

	/**
	 * 验证字符串内容是否合法
	 * 
	 * @param content
	 *            字符串内容
	 * @return
	 */
	public static boolean validateLegalString(String content) {
		String illegal = "`~!#%^&*=+\\|{};:'\",<>/?○●★☆☉♀♂※¤╬の〆";
		boolean legal = true;
		L1: for (int i = 0; i < content.length(); i++) {
			for (int j = 0; j < illegal.length(); j++) {
				if (content.charAt(i) == illegal.charAt(j)) {
					legal = false;
					break L1;
				}
			}
		}
		return legal;
	}

	/**
	 * 验证密码内容是否合法
	 * 
	 * @param content
	 *            字符串内容
	 * @return
	 */
	public static boolean validatePassword(String content) {

		boolean legal = false;
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) <= 'z' && content.charAt(i) >= 'a' || content.charAt(i) <= 'Z' && content.charAt(i) >= 'A' || content.charAt(i) <= '9' && content.charAt(i) >= '0' || content.charAt(i) == '_') {
				legal = true;
			} else {
				legal = false;
				break;
			}

		}
		return legal;
	}

	/**
	 * 如果为空显示暂无信息
	 * 
	 * @param tv
	 *            控件名
	 * @param str
	 *            信息
	 */
	public static void viewText(TextView tv, String str) {
		if (isNullOrEmpty(str)) {
			tv.setText("暂无资料");
		} else {
			tv.setText(str);
		}
	}

	/**
	 * 对流转化成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String getContentByString(InputStream is) {
		try {
			if (is == null)
				return null;
			byte[] b = new byte[1024];
			int len = -1;
			StringBuilder sb = new StringBuilder();
			while ((len = is.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			UtilsLog.w("signature", e.toString());
		}
		return null;
	}

	/**
	 * 对流转化成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String getStringByStream(InputStream is) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line + "\n");
			}
			return buffer.toString().replaceAll("\n\n", "\n");
		} catch (Exception e) {
			UtilsLog.w("signature", e.toString());
		}
		return null;
	}

	/**
	 * 截取字符串，去掉sign后边的
	 * 
	 * @param source
	 *            原始字符串
	 * @param sign
	 * @return
	 */
	public static String splitByIndex(String source, String sign) {
		String temp = "";
		if (isNullOrEmpty(source)) {
			return temp;
		}
		int length = source.indexOf(sign);
		if (length > -1) {
			temp = source.substring(0, length);
		} else {
			return source;
		}
		return temp;
	}

	/**
	 * 截取字符串，返回sign分隔的字符串
	 * 
	 */
	public static String splitNumAndStr(String res, String sign) {
		StringBuffer buffer;
		String reg = "\\d+";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(res);
		if (m.find()) {
			buffer = new StringBuffer();
			String s = m.group();
			buffer.append(s);
			buffer.append(sign);
			buffer.append(res.replace(s, ""));
			return buffer.toString();
		}
		return null;
	}

	/**
	 * 保留小数点后一位
	 * 
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(double d) {
		try {
			DecimalFormat df = new DecimalFormat("#,##0.0");
			return df.format(d);
		} catch (Exception e) {
		}
		return "";
	}

	public static String formatNumber(String d) {
		return formatNumber(Double.parseDouble(d));
	}

	/*
	 * 获取字符串格式的当前时间
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		String dateString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(currentTime);
		return dateString;
	}

	/*
	 * 时间格式转换，yyyy-MM-dd xx:xx:xx为：MM-dd xx:xx 不要年和秒
	 */
	public static String getStringForDate(long date) {

		SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");
		Date d = new Date(date);
		String dateString = f.format(d);
		return dateString;

	}

	/*
	 * 时间格式转换，yyyy-MM-dd xx:xx:xx为：MM-dd xx:xx 不要年和秒
	 */
	public static String getStringForDate_yyyy_MM_dd() {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date(System.currentTimeMillis());
		String dateString = f.format(d);
		return dateString;

	}

	/*
	 * 时间格式转换，yyyy-MM-dd xx:xx:xx为：MM-dd xx:xx 不要年和秒
	 */
	public static String getStringForDate(String date) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");
		Date d = new Date();
		try {
			d = formatter.parse(date);
		} catch (Exception e) {
			UtilsLog.w("signature", e.toString());
		}
		String dateString = f.format(d);
		return dateString;

	}

	/**
	 * 判断是否全为数字
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isAllNumber(String content) {

		if (isNullOrEmpty(content)) {
			return false;
		}
		Pattern p = Pattern.compile("\\-*\\d+");
		Matcher m = p.matcher(content);
		return m.matches();
	}

	/**
	 * 整数转字节数组
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToByte(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}

	/**
	 * 字节数组转整数
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	/**
	 * 对小数点后两位及两位以内的数据进行取整
	 */
	public static String getInt(String string) {

		return (int) (Double.parseDouble(string) * 100) / 10 / 10 + "";

	}

	/**
	 * 对小数点后的数据如果是零取整不是返回原值
	 */
	public static String round(String str) {

		float f = Float.parseFloat(str);
		int i = (int) f;
		if (f == i) {
			return i + "";
		} else {
			return f + "";

		}
	}

	/**
	 * 号码的宅电和手机判定
	 * 
	 * @param number
	 * @return
	 */
	public static boolean phoneOrMobile(String number) {
		if (number == null || number.equals("")) {
			return false;
//		}else{
//			//本地不做正则验证
//			return true;
		}
		else if (number.length() > 11) {
			return false;
		} else {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
			Matcher m = p.matcher(number);
			return m.matches();
		}
	}

	/**
	 * 半角字符转化为全角字符
	 * 
	 * @param input
	 *            要转化的字符
	 * @return 转化后的结果
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 根据value值获取对应name
	 * 
	 *            要转化的字符
	 * @return 转化后的结果
	 */
	public static String valueChangeToName(String[] values, String[] names, String value) {
		String s = "";
		if (!StringUtils.isNullOrEmpty(value)) {
			for (int i = 0; i < values.length; i++) {
				if (value.equals(values[i])) {
					value = values[i];
					s = names[i];
					break;
				}
			}
			return s;
		} else {
			return s;
		}
	}

	/**
	 * @Description:身份证号 正则验证表达式
	 * @param str
	 * @return
	 * @author: LiuZhao
	 * @date:2014年11月4日
	 */

	public static boolean identity(String str) {
		String regx = "([0-9]{17}([0-9]|X))|([0-9]{15})";
		Pattern pattern = Pattern.compile(regx);
		return pattern.matcher(str).matches();
	}

	/**
	 * @Description:返回星号“*”的长度
	 * @param strLength
	 * @return
	 * @author: LiuZhao
	 * @date:2014年11月7日
	 */

	public static String getXingHao(int strLength) {
		String xinghao = "";
		for (int i = 0; i < strLength; i++) {
			xinghao = xinghao + "*";
		}
		return xinghao;
	}

	/**
	 * 获取更新的时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getDateString(Date date) {
		Calendar calendar = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (calendar.get(Calendar.YEAR) - cal.get(Calendar.YEAR) > 0) {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
		} else if (calendar.get(Calendar.MONTH) - cal.get(Calendar.MONTH) > 0) {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
		} else if (calendar.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) > 6) {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
		} else if ((calendar.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) > 0) && (calendar.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) < 6)) {
			int i = calendar.get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY);
			return i + "天前";
		} else if (calendar.get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY) > 0) {
			int i = calendar.get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY);
			return i + "小时前";
		} else if (calendar.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE) > 0) {
			int i = calendar.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE);
			return i + "分钟前";
		} else if (calendar.get(Calendar.SECOND) - cal.get(Calendar.SECOND) > 0) {
			int i = calendar.get(Calendar.SECOND) - cal.get(Calendar.SECOND);
			return i + "秒前";
		} else if (calendar.get(Calendar.SECOND) - cal.get(Calendar.SECOND) == 0) {
			return "刚刚";
		} else {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
		}
	}

	/**
	 * 判断某个字符串是否匹配某个正则表达式
	 * 
	 * @param str
	 *            要匹配的字符串
	 * @param reg
	 *            正则
	 * @return 如果匹配则返回true，否则返回false
	 * @author LiXiaoSong
	 */
	public static boolean matchRegular(String str, String reg) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 返回按需要大小的图片地址
	 * 
	 *            正则
	 * @return String
	 * @author Liubing
	 */
	public static String newPicUrl(String url, int a, int b) {
		int m = url.indexOf("/");
		int n = url.indexOf("/", m + 1);
		int l = url.indexOf("/", n + 1);
		StringBuffer sb = new StringBuffer(url);
		sb.insert(l + 1, "viewimage/");
		sb.insert(sb.length() - 4, "/" + a + "x" + b);
		UtilsLog.w("url", sb.toString());
		return sb.toString();
	}

	/**
	 * @Description:获取进度值
	 * @author LZ
	 * @date:2014年11月13日
	 */
	public static int getPlan(String plan) {
		int progress = 0;
		if (StringUtils.isNullOrEmpty(plan)) {
			return progress;
		}
		try {
			BigDecimal decimal = new BigDecimal(plan).multiply(new BigDecimal(100));
			progress = decimal.intValue();
		} catch (Exception e) {
			progress = 0;
		}
		return progress;
	}

	/**
	 * @Description:将String 1,000.00转换成 浮点数
	 * @author LZ
	 * @date:2014年11月13日
	 */
	public static float getFloatByString(String number) {
		float progress = 0f;
		if (StringUtils.isNullOrEmpty(number)) {
			return progress;
		}
		try {
			BigDecimal decimal = new BigDecimal(number.replace(",", ""));
			progress = decimal.floatValue();
		} catch (Exception e) {
			progress = 0f;
		}
		return progress;
	}

	/**
	 * @Description:输入小数返回保留两位小数的百分比比如10.00%
	 * @param strNumber
	 *            输入的小数
	 * @param digit
	 *            保留位数
	 * @return
	 * @author: LiuZhao
	 * @date:2014年11月13日
	 */

	public static String getPerNumber(String strNumber, int digit) {
		BigDecimal bigdec = null;
		try {
			bigdec = new BigDecimal(strNumber).multiply(new BigDecimal(100)).setScale(digit, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return "0.00%";
		}
		return bigdec + "%";
	}
	
	/**
	 * @Description 输入姓名字符串判断是否符合“不超过10字符，5个汉字；支持汉字和英文字母，不支持数字”的标准
	 * @author Zhang Ying
	 * @creation 2015年4月1日
	 * @param name
	 * @return
	 */
	public static boolean checkName(String name){
		if (name.length()>10) {
			return false;
		}
		if (!isChOrEn(name)) {
			return false;
		}
		if (!isLegalLenth(name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断输入的字符长度是否符合"总共不超过10字符5个汉字"的标准
	 * @author Zhang Ying
	 * @creation 2015年4月1日
	 * @param str
	 * @return
	 */
	private static boolean isLegalLenth(String str){
		int chCount = getChCount(str);
		if ((str.length()+chCount)>10) 
			return false;
		return true;
	}
	/**
	 * 判断输入字符串是否只包含中文和英文
	 * @author Zhang Ying
	 * @creation 2015年4月1日
	 * @param str
	 * @return
	 */
	private static boolean isChOrEn(String str) {
		String regex = "^[\\p{L}'-]+$";
		boolean isName = Pattern.matches(regex, str);
		return isName;
	}
	
	/**
	 * 获取输入字符串中中文的个数
	 * @author Zhang Ying
	 * @creation 2015年4月1日
	 * @param str
	 * @return
	 */
	private static int getChCount(String str){
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (isChinese(str.substring(i,i+1))) {
				++count;
			}
		}
		return count;
	}
	
	/**
	 * 判断输入字符是否为汉字
	 * @author Zhang Ying
	 * @creation 2015年4月1日
	 * @param str
	 * @return
	 */
	private static boolean isChinese(String str) {
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断输入字符是否为汉字
	 * @author Zhang Ying
	 * @creation 2015年4月1日
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
		|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
		|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}
}

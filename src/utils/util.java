package utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class util {

	public static String nowTime() {
		// 获取系统当前时间
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(dt);
	}

	public static String getTime() {
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(dt);
	}

	public static String getDate() {
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dt);
	}
}

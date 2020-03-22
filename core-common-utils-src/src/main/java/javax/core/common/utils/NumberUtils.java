package javax.core.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 提供高精度的运算支持. 所以函数以double为参数类型，兼容int与float.
 * @author Tanyongde
 */
public class NumberUtils {

	private NumberUtils() {}

	/**
	 * 精确的加法运算.
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 精确的加法运算.
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
		return b1.add(b2);
	}

	/**
	 * 精确的加法运算，并对运算结果截位.
	 */
	public static double add(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 精确的加法运算，并对运算结果截位.
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 
	 * 精确的减法运算.
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 
	 * 精确的减法运算.
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 */
	public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
		return b1.subtract(b2);
	}
	
	/**
	 * 提供精确的乘法运算.
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算.
	 */
	public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
		return b1.multiply(b2);
	}
	
	/**
	 * 提供精确的乘法运算，并对运算结果截位.
	 * 
	 * @param scale
	 *            运算结果小数后精确的位数
	 */
	public static double multiply(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}
	
	/**
	 * 提供精确的乘法运算，并对运算结果截位.
	 * 
	 * @param scale
	 *            运算结果小数后精确的位数
	 */
	public static BigDecimal multiply(BigDecimal b1, BigDecimal b2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 提供（相对）精确的除法运算.
	 * 
	 * @see #divide(double, double, int)
	 */
	public static double divide(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算.
	 * 
	 * @see #divide(double, double, int)
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
		return b1.divide(b2);
	}
	
	/**
	 * 提供（相对）精确的除法运算. 由scale参数指定精度，以后的数字四舍五入.
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位
	 */
	public static double divide(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 提供（相对）精确的除法运算. 由scale参数指定精度，以后的数字四舍五入.
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 提供精确的小数位四舍五入处理.
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 提供精确的小数位四舍五入处理.
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 */
	public static BigDecimal round(BigDecimal v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		return v.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 阿拉伯数字转换成罗马数字
	 * 
	 * @param int2Roman
	 *            阿拉伯数字
	 * @return 字符串形式的罗马数字
	 */
	public static String int2RomanNum(int int2Roman) {
		String lm_result = "";
		int temp = int2Roman;

		for (int i = 1; i <= temp / 1000; i++) {
			lm_result = lm_result + 'M';
			int2Roman = int2Roman - 1000;
		}
		temp = int2Roman;

		for (int i = 1; i <= temp / 500; i++) {
			lm_result = lm_result + 'D';
			int2Roman = int2Roman - 500;
		}
		temp = int2Roman;

		for (int i = 1; i <= temp / 100; i++) {
			lm_result = lm_result + 'C';
			int2Roman = int2Roman - 100;
		}
		temp = int2Roman;

		for (int i = 1; i <= temp / 50; i++) {
			lm_result = lm_result + 'L';
			int2Roman = int2Roman - 50;
		}
		temp = int2Roman;

		for (int i = 1; i <= temp / 10; i++) {
			lm_result = lm_result + 'X';
			int2Roman = int2Roman - 10;
		}
		temp = int2Roman;

		for (int i = 1; i <= temp / 5; i++) {
			lm_result = lm_result + 'V';
			int2Roman = int2Roman - 5;
		}
		temp = int2Roman;

		for (int i = 1; i <= temp / 1; i++) {
			lm_result = lm_result + 'I';
			int2Roman = int2Roman - 1;
		}
		return lm_result;
	}

	/**
	 * 罗马数字转换成阿拉伯数字
	 * 
	 * @param roman2Int
	 *            罗马数字
	 * @return 阿拉伯数字
	 */
	public static int romanNum2Int(String roman2Int) {
		int int_result = 0;

		int i = 0;
		while (i <= roman2Int.length() - 1) {
			switch (roman2Int.charAt(i)) {
			case 'M':
				int_result = int_result + 1000;
				i++;
				break;
			case 'D':
				int_result = int_result + 500;
				i++;
				break;
			case 'C':
				int_result = int_result + 100;
				i++;
				break;
			case 'L':
				int_result = int_result + 50;
				i++;
				break;
			case 'X':
				int_result = int_result + 10;
				i++;
				break;
			case 'V':
				int_result = int_result + 5;
				i++;
				break;
			case 'I':
				int_result = int_result + 1;
				i++;
				break;
			}
		}
		return int_result;
	}

	static String[] units = { "", "十", "百", "千", "万", "十", "百", "千", "亿" };

	static String[] nums = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };

	/**
	 * 数字转中文
	 * 
	 * @param theNum
	 *            你要转换的原始数字
	 * @return String 中文字符串
	 */
	public static String int2Ch(int theNum) {
		String result = "";
		if (theNum < 0) {
			result = "负";
			theNum = Math.abs(theNum);
		}
		String t = String.valueOf(theNum);
		for (int i = t.length() - 1; i >= 0; i--) {
			int r = (int) (theNum / Math.pow(10, i));
			if (r % 10 != 0) {
				String s = String.valueOf(r);
				String l = s.substring(s.length() - 1, s.length());
				result += nums[Integer.parseInt(l) - 1];
				result += (units[i]);
			} else {
				if (!result.endsWith("零")) {
					result += "零";
				}
			}
		}
		return result;
	}

	/**
	 * double类型的数字转化为百分之
	 * 
	 * @param doubleNum
	 *            double类型
	 * @return eg:类似21%的百分之
	 */
	public static String double2Percent(double doubleNum) {
		DecimalFormat df = new DecimalFormat("##.00%");
		if (doubleNum == 0.0)
			return "0%";
		return df.format(doubleNum);
	}
	
	/**
	 * 将数字转换成带逗号的字符串（"###,##0.## "）
	 * @param reg 格式化串
	 * @param number 需要转换的数字
	 * @return
	 */
	public static String numberFormart(String reg,double number){
		DecimalFormat bf = new DecimalFormat(reg);
		return bf.format(number);
	}

	/**
	 * 文件大小单位转换
	 * @param size
	 * @return
	 */
	public static String getRemain(double size) {
		if (size == 0) return "0 Bytes";
		String sizeNames[] = {" B", " KB", " MB", " GB", " TB", " PB", " EB", " ZB", " YB"};
		int i = (int) Math.floor(Math.log(size) / Math.log(1024));
		return new Double(divide(size, Math.pow(1024, Math.floor(i)), 2)).toString() + sizeNames[i];
	}
	
	/**
	 * 获取指定范围的随机数
	 * 
	 * @author grxie
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<Integer> getNumbers(int min,int max){
        max += 1;
        Integer temp = null;
        List<Integer> list = new ArrayList<Integer>(max-min);
        Set<Integer> s = new HashSet<Integer>();
        for(;max>min && s.size()<max-min;){
            temp = new Integer((int) ( Math.random()*max))+min;
            s.add(temp);
            if(list.size() < s.size())list.add(temp);
        }
        return list;
    }
}

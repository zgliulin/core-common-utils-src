package javax.core.common.utils;


/**
 * @author Tanyongde
 */
public class PrintHelper {
	
	public static final short FOREGROUND_BLACK = 0x0;
	public static final short FOREGROUND_BLUE = 0x1;
	public static final short FOREGROUND_GREEN = 0x2;
	public static final short FOREGROUND_RED = 0x4;
	public static final short FOREGROUND_WHITE = 0x7;
	public static final short FOREGROUND_INTENSITY = 0x8;
	public static final short BACKGROUND_BLUE = 0x10;
	public static final short BACKGROUND_GREEN = 0x20;
	public static final short BACKGROUND_RED = 0x40;
	public static final short BACKGROUND_INTENSITY = 0x80;
	
	public static final String LOG_SET = "[SET]    ";
	public static final String LOG_READ = "[READ]   ";
	public static final String LOG_EMPTY = "         ";
	public static final String LOG_OWNED = "        " + (char) 26;
	public static final String LOG_THREAD = "[THREAD] ";

	
	private PrintHelper(){}
	
	public static void cls() {

	}

	public static void print(String str) {
		System.out.print(str);
	}

	public static void println(String str) {
		System.out.println(str);
	}

	public static void println(String Str, short ForeColor, short BackColor) {
		System.out.println(Str);
	}

	public static void println(String Str, short ForeColor, short BackColor, short x,
			short y) {
		System.out.println(Str);
	}

	public static void println(String Str, short[] xy) {
		System.out.println(Str);
	}
	
	public static void print(String Str, short ForeColor, short BackColor) {
		System.out.print(Str);
	}

	public static void print(String Str, short ForeColor, short BackColor, short x, short y) {
		System.out.print(Str);
	}

	public static void print(String Str, short[] xy) {
		System.out.print(Str);
	}

	public static void error(String Str) {
		System.out.println(Str);
	}

	public static void log(String type, String Str) {
		System.out.print(type);
		System.out.println(" " + Str);
	}

}

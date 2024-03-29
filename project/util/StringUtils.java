package project.util;

public class StringUtils {

	public static char[] alignString(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (isFull(s.charAt(i)))
				count++;
		}

		char[] c = new char[s.length() + count];

		count = 0;
		for (int i = 0; i < s.length(); i++) {
			c[i + count] = s.charAt(i);
			if (isFull(s.charAt(i))) {
				c[i + ++count] = 24;
			}
		}

		return c;
	}

	public static boolean isFull(char c) {
		return c >= 0xFF00 || (c >= 0x3000 && c < 0x3100);
	}

	public static char[] alignString(String s, int limit) {
		char[] c = alignString(s);
		if (c.length > limit) {
			char[] d = new char[limit];
			for (int i = 0; i < limit; i++)
				d[i] = c[i];
			return d;
		} else
			return c;
	}

	public static String alignedToString(char[] c) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < c.length; i++) {
			sb.append(c[i]);
			if (isFull(c[i]))
				i++;
		}
		return sb.toString();
	}

	public static int[] Split2Int(String str, String token) {
		String[] temp = str.split(token);
		int[] arr = new int[temp.length];
		for (int i = 0; i < temp.length; ++i) {
			try {
				arr[i] = Integer.parseInt(temp[i]);
			} catch (NumberFormatException ex) {
				System.out.println("Wrong Format Please Check your input string");
				return null;
			}
		}
		return arr;
	}

}

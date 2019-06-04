package project.util;

public class StringUtils {

	public static char[] alignString(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= 0xFF00)
				count++;
		}

		char[] c = new char[s.length() + count];

		count = 0;
		for (int i = 0; i < s.length(); i++) {
			c[i + count] = s.charAt(i);
			if (s.charAt(i) >= 0xFF00)
				count++;
		}

		return c;
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
	public static int[] Split2Int(String str, String token){
		String[] temp=str.split(token);
		int[] arr=new int[temp.length];
		for(int i=0;i<temp.length;++i){
			arr[i]=Integer.parseInt(temp[i]);
		}
		return arr;
	}
}

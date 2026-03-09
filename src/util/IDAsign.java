package util;

public class IDAsign {
	public static String asignar(String prefix, int count) {
		int longit = String.valueOf(count).length();
		for(int i = 0; i<(4-longit);i++) {
			prefix = prefix + "0";
		}
		return prefix + String.valueOf(count);
	}
}

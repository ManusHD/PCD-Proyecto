package pcd.util;

public class Traza {
	static int modo=0;
	
	public Traza (int _modo) {
		modo = _modo;
	}
	
	public Traza () {
		
	}
	
	public static void traza (char simbolo, int nivel, String s) {
		if (nivel <= modo) {
			for (int i=0;i<nivel;i++)
				s=simbolo+s;
			System.out.println (s);
		}
	}
}

package de.illigalspigot.neoprotect.cpscounter;

public class CPSCounter {
	
	private static int cpsCounter = 0;
	
	public static void add() {
		cpsCounter++;
	}
	
	public static void clear() {
		cpsCounter = 0;
	}
	
	public static int get() {
		return cpsCounter;
	}

}

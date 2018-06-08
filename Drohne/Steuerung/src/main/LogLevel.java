package main;

public enum LogLevel {
	MESSAGE(0), WARNING(1), ERROR(2);
	private final int value;
	
	private LogLevel(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}

package main;
	

public class Message {
	
	
	private Message() {};
	
	static LogLevel level;
	
	
	
	static void log(String message, LogLevel level) {
		if (level.getValue() > Message.level.getValue()) {
			System.out.println(message);
		}
	}
	
}

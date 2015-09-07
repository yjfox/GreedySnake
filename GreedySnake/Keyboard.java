package GreedySnake;

public class Keyboard {
	String value;
	private static Keyboard instance = null;

	private Keyboard() {
		this.value = new String();
	}

	static Keyboard getInstance() {
		if (instance == null) {
			synchronized(Keyboard.class) {
				if (instance == null) {
					instance = new Keyboard();
				}
			}
		}
		return instance;
	}
}

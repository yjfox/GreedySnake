package GreedySnake;

import java.util.Scanner;

public class InputKey implements Runnable {
	Keyboard key;
	Scanner scan;
	boolean end;

	public InputKey(Keyboard key) {
		this.key = key;
		this.scan = new Scanner(System.in);
		this.end = false;
	}

	@Override
	public void run() {
		while (key.value.contains("q")) {
			key.value = scan.next();
		}
		end = true;
	}
}

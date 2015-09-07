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
		do {
			key.value = scan.next();
		} while (key.value.contains("q"));
		end = true;
	}
}

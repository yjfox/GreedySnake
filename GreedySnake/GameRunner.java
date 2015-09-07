package GreedySnake;

public class GameRunner {
	
	public static void main(String[] args) {
		int M = 10;
		int N = 10;
		InputKey input;
		PanelBoard panel;
		Keyboard key = Keyboard.getInstance();
		input = new InputKey(key);
		panel = new PanelBoard(key, M, N);
		execute(key, input, panel);
	}
	
	private static void execute(Keyboard key, InputKey input, PanelBoard panel) {
		Thread inputThread = new Thread(input);
		Thread panelThread = new Thread(panel);
		inputThread.start();
		panelThread.start();
	}
}

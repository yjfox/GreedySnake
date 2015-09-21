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
		execute(input, panel);
	}
	
	private static void execute(InputKey input, PanelBoard panel) {
		Thread inputThread = new Thread(input, "inputThread");
		Thread panelThread = new Thread(panel, "panelThread");
		inputThread.start();
		panelThread.start();
	}
}

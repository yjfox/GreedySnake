package GreedySnake;

public class PanelBoard implements Runnable {
	static class GraphNode {
		boolean isSnake;
		char direction; // 'L' - left, 'R' - Right, 'U' - up, 'D' - down
		int row, col;

		public GraphNode(int m, int n, char direction, boolean isSnake) {
			this.isSnake = isSnake;
			this.direction = direction;
			this.row = m;
			this.col = n;
		}
	}

	Keyboard key;
	GraphNode[][] graph;
	GraphNode head, tail;
	private final int M, N;

	public PanelBoard(Keyboard key, int m, int n) {
		this.M = m;
		this.N = n;
		this.key = key;
		this.graph = new GraphNode[m][n];
		initialSnake();
	}

	private void initialSnake() {
		graph[0][1] = new GraphNode(0, 1, 'R', true);
		graph[0][0] = new GraphNode(0, 0, 'R', true);
		head = graph[0][1];
		tail = graph[0][0];
	}

	@Override
	public void run() {
		char direction = 'R';
		while (!key.value.contains("q")) {
			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					if (graph[i][j] != null && graph[i][j].isSnake) {
						System.out.print("- ");
					} else {
						System.out.print(". ");
					}
				}
				System.out.println();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			moveSnake(direction);
		}
	}

	private void moveSnake(char direction) {
		int headRow = 0;
		int headCol = 0;
		switch (head.direction) {
		case 'L':
			headRow = head.row;
			headCol = head.col - 1;
			break;
		case 'R':
			if (head.col == N - 1) {
				headCol = 0;
			} else {
				headRow = head.row;
				headCol = head.col + 1;
			}
			break;
		case 'U':
			headRow = head.row + 1;
			headCol = head.col;
			break;
		case 'D':
			headRow = head.row - 1;
			headCol = head.col;
			break;
		default:
			break;
		}
		if (graph[headRow][headCol] == null) {
			graph[headRow][headCol] = new GraphNode(headRow, headCol,
					direction, true);
		} 
		head = graph[headRow][headCol];
		head.isSnake = true;
		head.direction = direction;
		char tailDirect = tail.direction;
		int tailRow = 0, tailCol = 0;
		tail.isSnake = false;
		tail.direction = ' ';
		switch(tailDirect) {
		case 'L':
			if (tail.col == 0) {
				tailCol = N - 1;
			} else {
				tailCol = tail.col - 1;
			}
			break;
		case 'R':
			if (tail.col == M - 1) {
				tailCol = 0;
			} else {
				tailCol = tail.col + 1;
			}
		}
		tail = graph[tailRow][tailCol];
	}
}

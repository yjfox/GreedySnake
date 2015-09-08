package GreedySnake;

import java.util.Random;

public class PanelBoard implements Runnable {
	static class GraphNode {
		boolean isSnake;
		boolean isHead;
		boolean isBean;
		char direction; // 'L' - left, 'R' - Right, 'U' - up, 'D' - down
		int row, col;

		public GraphNode(int m, int n, char direction, boolean isSnake) {
			this.isSnake = isSnake;
			this.isHead = false;
			this.isBean = false;
			this.direction = direction;
			this.row = m;
			this.col = n;
		}

		public GraphNode(int m, int n) {
			this.isSnake = false;
			this.isHead = false;
			this.isBean = false;
			this.direction = ' ';
			this.row = m;
			this.col = n;
		}
	}

	static class BeanSetter implements Runnable {
		GraphNode[][] graph;
		Random rand;
		volatile boolean flag;

		public BeanSetter(GraphNode[][] graph) {
			this.graph = graph;
			rand = new Random();
			flag =true;
		}

		@Override
		public void run() {
			int row = 0, col = 0;
			while (flag) {
				synchronized (this) {
					do {
						row = 0;
						col = rand.nextInt(graph[0].length);
						if (graph[row][col] == null) {
							graph[row][col] = new GraphNode(row, col);
						}
						if (!graph[row][col].isSnake) {
							graph[row][col].isBean = true;
						} else {
							continue;
						}
					} while (graph[row][col].isSnake);
					try {
						this.wait(1000 * graph.length / 2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						graph[row][col].isBean = false;
					}
				}
			}
		}
		
		void setFlag(boolean value) {
			flag = value;
		}
	}

	Keyboard key;
	GraphNode[][] graph;
	GraphNode head, tail;
	BeanSetter beanSetter;
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
		head.isHead = true;
		tail = graph[0][0];
		beanSetter = new BeanSetter(graph);
	}

	@Override
	public void run() {
		char direction = 'R';
		Thread threadBean = new Thread(beanSetter, "threadBean");
		threadBean.start();
		while (!key.value.contains("q")) {
			printPanel();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (beanSetter) {
				moveSnake(direction, beanSetter);
			}
		}
		beanSetter.setFlag(false);
	}

	private void moveSnake(char direction, BeanSetter beanSetter) {
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
		if (graph[headRow][headCol] != null && graph[headRow][headCol].isSnake) {
			System.out.println("Game Over!");
			System.exit(0);
		}
		if (graph[headRow][headCol] == null) {
			graph[headRow][headCol] = new GraphNode(headRow, headCol,
					direction, true);
		}
		head.isHead = false;
		head = graph[headRow][headCol];
		head.isSnake = true;
		head.isHead = true;
		head.direction = direction;
		if (head.isBean) {
			beanSetter.notify();
			return;
		}
		char tailDirect = tail.direction;
		int tailRow = 0, tailCol = 0;
		tail.isSnake = false;
		tail.direction = ' ';
		switch (tailDirect) {
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

	private void printPanel() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (graph[i][j] != null && graph[i][j].isSnake) {
					if (graph[i][j].isHead) {
						System.out.print("+");
					} else {
						System.out.print("-");
					}
				} else if (graph[i][j] != null && graph[i][j].isBean) {
					System.out.print("o");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}

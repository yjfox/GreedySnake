package GreedySnake;

import java.util.Random;

public class PanelBoard implements Runnable {

	private final String QUIT = "q";
	private final String AUTO = "auto";

	static class GraphNode {
		boolean isSnake;
		boolean isHead;
		boolean isBean;
		char direction; // 'L' - left, 'R' - Right, 'U' - up, 'D' - down
		int row, col;

		public GraphNode(int m, int n, char direction) {
			this.isSnake = true;
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

		public GraphNode() {
			this.isSnake = false;
			this.isHead = false;
			this.isBean = false;
			this.direction = ' ';
			this.row = -1;
			this.col = -1;
		}
	}

	// a thread that setting bean randomly
	static class BeanSetter implements Runnable {
		GraphNode[][] graph;
		Random rand;
		private static final GraphNode bean = new GraphNode();
		volatile boolean flag;

		public BeanSetter(GraphNode[][] graph) {
			this.graph = graph;
			rand = new Random();
			flag = true;
		}

		@Override
		public void run() {
			int row = 0, col = 0;
			while (flag) {
				synchronized (this) {
					do {
						row = rand.nextInt(graph.length);
						col = rand.nextInt(graph[0].length);
						if (graph[row][col] == null) {
							graph[row][col] = new GraphNode(row, col);
						}
						if (!graph[row][col].isSnake) {
							graph[row][col].isBean = true;
							bean.row = row;
							bean.col = col;
						} else {
							continue;
						}
					} while (graph[row][col].isSnake);
					try {
						this.wait(1000 * graph.length);
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
		graph[0][1] = new GraphNode(0, 1, 'R');
		graph[0][0] = new GraphNode(0, 0, 'R');
		head = graph[0][1];
		head.isHead = true;
		tail = graph[0][0];
		beanSetter = new BeanSetter(graph);
	}

	@Override
	public void run() {
		boolean autoRun = false;
		Thread threadBean = new Thread(beanSetter, "threadBean");
		threadBean.start();
		while (key.value.length() < 1 || !key.value.contains(QUIT)) {
			printPanel();
			if (!autoRun && key.value.length() >= 4 && key.value.contains(AUTO)) {
				autoRun = true;
			}
			if (autoRun) {
				setHeadDirection(autoDirection(BeanSetter.bean));
			} else {
				setHeadDirection(key.value);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (beanSetter) {
				moveSnake(beanSetter);
			}
		}
		beanSetter.setFlag(false);
	}

	private void moveSnake(BeanSetter beanSetter) {
		GraphNode newHead = getNextNode(head);
		if (newHead.isSnake) {
			System.out.println("Game Over");
			System.exit(1);
		}
		newHead.isSnake = true;
		newHead.direction = head.direction;
		newHead.isHead = true;
		head.isHead = false;
		head = newHead;
		if (head.isBean) {
			beanSetter.notify();
			return;
		}
		GraphNode newTail = getNextNode(tail);
		tail.isSnake = false;
		tail.direction = ' ';
		tail = newTail;
	}

	private GraphNode getNextNode(GraphNode snake) {
		int headRow = snake.row;
		int headCol = snake.col;
		switch (snake.direction) {
		case 'L':
			if (snake.col == 0) {
				headCol = N - 1;
			} else {
				headRow = snake.row;
				headCol = snake.col - 1;
			}
			break;
		case 'R':
			if (snake.col == N - 1) {
				headCol = 0;
			} else {
				headRow = snake.row;
				headCol = snake.col + 1;
			}
			break;
		case 'U':
			if (snake.row == 0) {
				headRow = M - 1;
			} else {
				headRow = snake.row - 1;
				headCol = snake.col;
			}
			break;
		case 'D':
			if (snake.row == M - 1) {
				headRow = 0;
			} else {
				headRow = snake.row + 1;
				headCol = snake.col;
			}
			break;
		default:
			break;
		}
		if (graph[headRow][headCol] == null) {
			graph[headRow][headCol] = new GraphNode(headRow, headCol);
		}
		return graph[headRow][headCol];
	}

	private void setHeadDirection(String dir) {
		if (dir.contains("L") || dir.contains("l")) {
			if (head.direction == 'R' || head.direction == 'L') {
				return;
			} else {
				head.direction = 'L';
				return;
			}
		}
		if (dir.contains("R") || dir.contains("r")) {
			if (head.direction == 'R' || head.direction == 'L') {
				return;
			} else {
				head.direction = 'R';
				return;
			}
		}
		if (dir.contains("U") || dir.contains("u")) {
			if (head.direction == 'U' || head.direction == 'D') {
				return;
			} else {
				head.direction = 'U';
				return;
			}
		}
		if (dir.contains("D") || dir.contains("d")) {
			if (head.direction == 'U' || head.direction == 'D') {
				return;
			} else {
				head.direction = 'D';
				return;
			}
		}
	}

	private String autoDirection(GraphNode bean) {
		int row = head.row, col = head.col;
		if (row < bean.row
				&& (graph[(row + 1) % M][col] == null || (graph[(row + 1) % M][col] != null && graph[(row + 1)
						% M][col].isSnake != true))) {
			return "d";
		}
		if (row > bean.row
				&& (graph[(row - 1) % M][col] == null || (graph[(row - 1) % M][col] != null && graph[(row - 1)
						% M][col].isSnake != true))) {
			return "u";
		}
		if (col < bean.col
				&& (graph[row][(col + 1) % N] == null || (graph[row][(col + 1)
						% N] != null && graph[row][(col + 1) % N].isSnake != true))) {
			return "r";
		}
		if (graph[row][(col - 1) % N] == null
				|| (graph[row][(col - 1) % N] != null && graph[row][(col - 1)
						% N].isSnake != true)) {
			return "l";
		}
		if (graph[(row + 1) % M][col] == null || graph[(row + 1) % M][col].isSnake != true) {
			return "d";
		} else if (graph[(row - 1) % M][col] == null || graph[(row - 1) % M][col].isSnake != true) {
			return "u";
		} else if (graph[row][(col + 1) % N] == null || graph[row][(col + 1) % N].isSnake != true) {
			return "r";
		} else {
			return "l";
		}
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

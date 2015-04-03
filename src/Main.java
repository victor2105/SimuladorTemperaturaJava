import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 
 * @author Victor Henrique dos Santos
 *
 */

public class Main {

	private static int SIMULATION_NUMBER = 100000;
	private static int instanteOfChange = -1;
	private static boolean hasChanged = false;

	private final static double mur[][] = new double[2][9];
	private static int turn = 1;
	private final static double dx = 0.04; // cm
	private final static double dt = 600; // s
	private final static double T0 = 20; // C
	private final static double T1 = 55; // C
	// La conductivite thermique do materiau en W/(m.k)

	static double Li = 0.04, Lm = 0.84;
	static double pi = 30, pm = 1400;
	static double ci = 900, cm = 840;

	// (Le/pc)
	private final static double Cm = (Lm * dt) / (pm * cm * dx * dx); // C du
																		// mur
	private final static double Ci = (Li * dt) / (pi * ci * dx * dx); // C du
																		// isolant

	
	private static CyclicBarrier barrier;
	private static CyclicBarrier barrierOfEndExecution;

	private static int iterations = 1;
	private static int hour;

	static long tempoInicial;
	static long tempoFinal;

	/**
	 * 
	 * T(x,t+e) = T(x,t) + (Le/pc)*((T(x+dx) + T(x-dx) + 2T(x))/(dx^2)) ou
	 * T(x,t+e) = T(x,t) + C *((T(x+dx) + T(x-dx) + 2T(x))/(dx^2))
	 * 
	 * @param gauche
	 * @param temp
	 * @param droite
	 * @return T(x,t+e)
	 */

	public static double newTemperature(double gauche, double t, double droite,
			double C) {
		return t + C * ((gauche + droite - (2 * t)));
	}

	/**
	 * 
	 * @author Victor Henrique dos Santos
	 *
	 */

	// Runnable of simulator
	static class Simulator implements Runnable {

		private int identity;

		public Simulator(int id) {
			identity = id;
		}

		@Override
		public void run() {
			int i = this.identity;

			for (int j = 0; j < SIMULATION_NUMBER; j++) {
				if (i < 5) {
					mur[turn][i] = newTemperature(mur[1 - turn][i - 1],
							mur[1 - turn][i], mur[1 - turn][i + 1], Cm);
				} else if (i == 5) {
					mur[turn][i] = mur[1 - turn][5]
							+ (Cm * (mur[1 - turn][4] - mur[1 - turn][5]) + Ci
									* (mur[1 - turn][6] - mur[1 - turn][5]));
				} else if (i > 5 && i < 8) {
					mur[turn][i] = newTemperature(mur[1 - turn][i - 1],
							mur[1 - turn][i], mur[1 - turn][i + 1], Ci);
				}
				if (i == 7) {
					if (((int) mur[turn][i]) == 21 && !hasChanged) {
						instanteOfChange = j;
						hasChanged = true;
					}
				}

				iterations = j + 1;

				// Barrier
				try {
					barrier.await();
				} catch (InterruptedException ex) {
					return;
				} catch (BrokenBarrierException ex) {
					return;
				}
			}

			// Barrier
			try {
				barrierOfEndExecution.await();
			} catch (InterruptedException ex) {
				return;
			} catch (BrokenBarrierException ex) {
				return;
			}

		}
	}

	// Inictialization of value
	public static void initTemp() {

		mur[0][0] = 2 * T1;
		mur[1][0] = 2 * T1;
		for (int i = 1; i < 9; i++) {
			mur[0][i] = T0;
			mur[1][i] = T0;
		}
	}

	public static void solver() {
		// Barrier configuration
		int numberOfThreads = 7;
		barrier = new CyclicBarrier(numberOfThreads, new Runnable() {
			public void run() {
				if (iterations % 6 == 0 && hour < 10) {
					hour++;
					Sortie.print(hour, mur, turn);
				}
				turn = 1 - turn;
			}
		});

		// Used to count the execution time
		barrierOfEndExecution = new CyclicBarrier(numberOfThreads,
				new Runnable() {
					@Override
					public void run() {
						tempoFinal = System.currentTimeMillis();
						System.out.println("Execution time: "
								+ (tempoFinal - tempoInicial));
					}
				});

		// Starting all threads
		for (int i = 0; i < numberOfThreads; i++) {
			new Thread(new Simulator(i + 1)).start();
		}
	}

	public static void main(String[] args) {

		initTemp();

		tempoInicial = System.currentTimeMillis();

		Sortie.print(0, mur, turn);

		// Solution
		solver();

		
	}
}

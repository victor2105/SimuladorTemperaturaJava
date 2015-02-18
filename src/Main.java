import java.io.PrintWriter;

public class Main {

	// Tl = 100 - Tr = 20

	/**
	 * 1 Une Thread Data - 3 Março
	 */

	/**
	 * 2 - Barrier - Use Barrier Java
	 * 
	 */

	/**
	 * RdV - Semaphore - Plus rapide / Muniteur - Plus facile
	 */

	/**
	 * Change de modele
	 */

	private static int SIMULATION_NUMBER = 100000000;

	static double mur[][] = new double[2][6];
	static double isolant[][] = new double[2][4];

	int ligne = 0;

	double dx = 4; // cm
	static double dt = 0.1; // s

	static double T0 = 20; // ºC
	static double T1 = 55; // ºC


	// La conductivité thermique do matériau en W/(m.k)
	double lambdaLV = 0.04; // Laine de verre
	double lambdaB = 0.84; // Brique
	double lambdaG = 2.2; // Granite
	
	static double Li = 0.04, Lm = (0.84+2.2)/2;
	static double pi = 30, pm = (1400+2700)/2;
	static double ci = 900, cm = (840+790)/2;
	
	// (Le/pc)
	static double Cm = (Lm*dt)/(pm*cm);   // C du mur
	static double Ci = (Li*dt)/(pi*ci);  // C du isolant


	
	/**
	 * 
	 * T(x,t+e) = T(x,t) + (Le/pc)*((T(x+dx) + T(x-dx) + 2T(x))/(dx^2)) ou
	 * T(x,t+e) = T(x,t) +    C   *((T(x+dx) + T(x-dx) + 2T(x))/(dx^2))
	 * @param g
	 * @param m
	 * @param d
	 * @return T(x,t+e)
	 */
	
	public static double newTemperature(double g, double t, double d, double C) {
		return t + C*((g+d-2*t)/16);
	}

	public static void simulation(int turn){
		
		
		mur[turn][0] = newTemperature(T1, mur[1-turn][0], mur[1-turn][1], Cm);
		
		for(int i=1;i<5;i++){
			mur[turn][i] = newTemperature(mur[1-turn][i-1], mur[1-turn][i], mur[1-turn][i+1], Cm);
		}
		
		// mur - isolant - C
		
		mur[turn][5] = newTemperature(mur[1-turn][4], mur[1-turn][5], isolant[1-turn][0], Cm);
		isolant[turn][3] = newTemperature(mur[1-turn][5], isolant[1-turn][0], isolant[1-turn][1], Ci);
		
		for(int i=1;i<3;i++){
			isolant[turn][i] = newTemperature(isolant[1-turn][i-1], isolant[1-turn][i], isolant[1-turn][i+1], Ci);
		}

		isolant[turn][3] = newTemperature(isolant[1-turn][2], isolant[1-turn][3], T0, Ci);
	}

	public static void main(String[] args) {

		int turn = 1;
		System.out.println("Cm = "+ Cm);
		System.out.println("Ci = "+ Ci);
		Cm = 0.0009;   // C du mur
		Ci = 0.001;  // C du isolant
		System.out.println("Cm = "+ Cm);
		System.out.println("Ci = "+ Ci);
		
		for (int i = 0; i < SIMULATION_NUMBER; i++) {
			simulation(turn);
			Sortie.print(mur, isolant, turn);
			turn = 1 - turn;
		}
	}
}

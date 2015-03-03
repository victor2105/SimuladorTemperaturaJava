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

	private static int SIMULATION_NUMBER = 100000;

	static double mur[][] = new double[2][10];
	
	static double dx = 4; // cm
	static double dt = 1; // s
	static double T0 = 20; // ºC
	static double T1 = 55; // ºC
	// La conductivité thermique do matériau en W/(m.k)
	
	static double Li = 0.04, Lm = 2.2;
	static double pi = 30,   pm = 2700;
	static double ci = 900,  cm = 790;
	
	// (Le/pc)
	static double Cm = (Lm*dt)/(pm*cm);   // C du mur
	static double Ci = (Li*dt)/(pi*ci);  // C du isolant
	static double EPSILON = 0.0000001;
	
	/**
	 * 
	 * T(x,t+e) = T(x,t) + (Le/pc)*((T(x+dx) + T(x-dx) + 2T(x))/(dx^2)) ou
	 * T(x,t+e) = T(x,t) +    C   *((T(x+dx) + T(x-dx) + 2T(x))/(dx^2))
	 * @param g
	 * @param m
	 * @param d
	 * @return T(x,t+e)
	 */
	
	public static double newTemperature(double gauche, double t, double droite, double C) {
		return t + C*((gauche+droite-(2*t))/(dt*dt));
	}

	public static void simulation(int turn){	
		
		for(int i=1;i<9;i++){
			if(i < 6)
				mur[turn][i] = newTemperature(mur[turn][i-1], mur[1-turn][i], mur[1-turn][i+1], Cm);
			else if(i >= 6)
				mur[turn][i] = newTemperature(mur[turn][i-1], mur[1-turn][i], mur[1-turn][i+1], Ci);
		}
		mur[turn][9] = newTemperature(mur[1-turn][8], mur[1-turn][9], T0, Ci);
	}

	public static void initTemp(){

		mur[0][0] = 110;
		mur[1][0] = 110;
		for(int i=1;i<10;i++){
			mur[0][i] = 20;
			mur[1][i] = 20;
		}
	}
		
	public static void main(String[] args) {
		
		initTemp();
		
		int instanteOfChange = -1;
		boolean notChanged = true;
		
	    JavaWebSocketServer.getInstance();// Init the server.
	    try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int turn = 1;
		//Redefinition of Cm and Ci
		Cm = 0.000009;
		Ci = 0.00001;
		//long tempoInicial = System.currentTimeMillis();  
		for (int i = 0; i < SIMULATION_NUMBER; i++) {
			simulation(turn);
			if(mur[turn][6]-mur[1-turn][6] >= EPSILON && notChanged){
				notChanged = false;
				instanteOfChange = i;
			}
			
			if(i % 500 == 0 ){
				Sortie.print(mur, turn);
				for (int j=0; j<10; j++) {
		    		String message = "<elt>";
		    		message += "<time>" + i + "</time>";
		    		message += "<X>" + j + "</X>";
		    		message += "<value>" + ((int) mur[turn][j]) + "</value>";
		    		message += "</elt>";
		    		
		    		JavaWebSocketServer.getInstance().broadcastMessage(message);
		    	}
			}
			turn = 1 - turn;
		}
		//long tempoFinal = System.currentTimeMillis();  
		//System.out.println( tempoFinal - tempoInicial ); 
		
		System.out.println("Instant of change = "+instanteOfChange);
		System.out.println("Cm = "+ Cm);
		System.out.println("Ci = "+ Ci);	
	    System.out.println("fin simulation");
	}
}

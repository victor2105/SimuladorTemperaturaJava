/**
 * 
 * @author Victor Henrique dos Santos
 *
 */


public class Sortie {

	
	// http://erebe-vm15.i3s.unice.fr/visulabprogconc/
	//JavaWebSocketServer jwss;
	
	public static void print(int t, double mur[][], int turn){
		System.out.print("t="+t+".0 heure(s) "+(int) mur[turn][0]);
		for(int i=1;i<4;i++){
			System.out.print(","+ ((int) mur[turn][i]));
		}
		System.out.print(","+((int) mur[turn][5])+"-"+((int) mur[turn][5]));
		for(int i=6;i<9;i++){
			System.out.print(","+ ((int) mur[turn][i]));
		}
		System.out.println();
		
		
	}
	
}


public class Sortie {

	
	// http://erebe-vm15.i3s.unice.fr/visulabprogconc/
	//JavaWebSocketServer jwss;
	
	public static void print(double mur[][], int turn){
		
		
		System.out.print((int) mur[turn][0]);
		for(int i=1;i<6;i++){
			System.out.print(","+ ((int) mur[turn][i]));
		}
		System.out.print("-"+((int) mur[turn][6]));
		for(int i=7;i<10;i++){
			System.out.print(","+ ((int) mur[turn][i]));
		}
		System.out.println();
		
		
	}
	
}

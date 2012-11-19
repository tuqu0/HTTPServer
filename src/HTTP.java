import java.util.Scanner;


public class HTTP {

	// Teste si une chaîne peut être parsée en entier
	public static Boolean tryParseInt(String st)  {  
		try  {  
			Integer.parseInt(st);
			return true;  
		} catch(NumberFormatException e) {  
			return false;  
		}  
	}
	
	// Lancement du serveur web + configuration du port
	public static void main(String[] args) {
		String port;
		Scanner sc = new Scanner(System.in);
		Server srv;
		
		System.out.print("port : ");
		if (tryParseInt((port = sc.next())))
			srv = new Server(Integer.parseInt(port));
		else
			srv = new Server();
		
		srv.start();
		sc.close();
	}

}

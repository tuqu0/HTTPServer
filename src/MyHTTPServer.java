import java.util.Scanner;

public class MyHTTPServer {
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
		String token;
		Scanner sc;
		TCPServer server;

		sc = new Scanner(System.in);

		System.out.println("*** HTTPServer ***");
		System.out.println("Choix du mode serveur : \n");
		System.out.println("Defaut : 3");
		System.out.println("1	: Affiche du HTML brut non interprété");
		System.out.println("2	: Affiche du HTML brut non interprété du fichier index.hmtl");
		System.out.println("3	: Affiche du HTML interprété d'un fichier demandé");
		System.out.println("4	: Affiche la requête HTTP du navigateur client");
		System.out.println("5	: Affiche de pages différentes selon le navigateur (Firefox / IE / Autres)\n");

		server = new TCPServer();

		System.out.print("mode : ");
		if (tryParseInt((token = sc.next())))
			server.setServerMode(Integer.parseInt(token));

		System.out.print("port : ");
		if (tryParseInt((token = sc.next())))
			server.setPort(Integer.parseInt(token));

		sc.close();
		server.start();
	}

}

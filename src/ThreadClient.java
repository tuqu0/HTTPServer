import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class ThreadClient  extends Thread {
	private Socket socket;
	PrintStream out;
	BufferedReader reader;
	String line;
	
	// Constructeur premenant en paramètre une socket cliente
	public ThreadClient(Socket s) {
		socket = s;
		this.start();
	}
	
	// Surcharge de la méthode run : Redéfinition du flux de sortie pour la socket client
	public void run() {
		httpdServerV2();
	}
	
	// PART1 : Affiche du contenu HTML brut
	public void httpdServerV1() {
		try {
			out = new PrintStream(socket.getOutputStream());
			out.println("<HTML>Exemple of HTML file with <BLINK>blinking content</BLINK></HTML>");
			socket.close();
		} catch (IOException e) {
			out.println("erreur: " + e.getMessage());
		}
	}
	
	/*
	 * PART 2 : Affiche le contenu HTML brut du fichier index.html
	 * situé dans le répertoire courante du serveur
	 */
	public void httpdServerV2() {
		try {
			out = new PrintStream(socket.getOutputStream());
			FileReader file = new FileReader("index.html");
			reader = new BufferedReader(new FileReader("index.html"));
			
			while ((line = reader.readLine()) != null)
				out.println(line);
			
			file.close();
			socket.close();
		} catch (IOException e) {
			out.println("erreur: " + e.getMessage());
		}
	}
	
	public void httpdServerV3() {
		
	}
}

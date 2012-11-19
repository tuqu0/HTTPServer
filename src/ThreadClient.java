import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ThreadClient  extends Thread {
	Socket socket;
	PrintStream out;
	Scanner in;
	BufferedReader reader;
	StringTokenizer st;
	String line, token;
	Hashtable<String, String> data;
	
	/*
	 *  Constructeur premenant en paramètre une socket cliente
	 *  Définition des flux input/output en fonction de la socket cliente
	 */
	public ThreadClient(Socket s) {
		socket = s;
		try {
			out = new PrintStream(socket.getOutputStream());
			in = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			data = new Hashtable<String, String>();
		} catch (Exception e) {
			System.out.println("erreur :" + e.getMessage());
		}
		this.start();
	}

	// Surcharge de la méthode run : Redéfinition du flux de sortie pour la socket client
	public void run() {		
		try {
			httpdServerV5();
			socket.close();
		}
		catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}
	}

	// PART1 : Affiche du contenu HTML brut
	public void httpdServerV1() {
		try {
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
			FileReader file = new FileReader("index.html");
			reader = new BufferedReader(file);

			while ((line = reader.readLine()) != null)
				out.println(line);

			file.close();
			socket.close();
		} catch (IOException e) {
			out.println("erreur: " + e.getMessage());
		}
	}

	/*
	 * PART 3 : Affiche le contenu d'un fichier HTML demandé
	 * Si le fichier n'existe pas, on affiche par défaut "index.html".
	 * Si "index.html" n'existe pas on affiche une erreur 404 File Not Found
	 */
	public void httpdServerV3() {	
	
		while (!(line = in.nextLine()).equals("")) {
			st = new StringTokenizer(line);		
			while (st.hasMoreTokens()) {
				token = st.nextToken();

				if (token.equals("GET") && st.hasMoreTokens()) {
					data.put("METHOD", token);
					data.put("FILE", st.nextToken());
				}
			}
		}

		if (data.containsKey("METHOD") && data.get("METHOD").equals("GET") && data.containsKey("FILE")) {
			try {
				File f = new File(data.get("FILE").replace("/", ""));		
				if (!f.exists())
					f = new File("index.html");

				if (f.exists()) {
					FileReader file = new FileReader(f);
					reader = new BufferedReader(file);

					out.println("HTTP/1.0 200 OK");
					out.println("Content-Type: text/HTML");
					out.println("");
					while ((line = reader.readLine()) != null && !line.isEmpty())
						out.println(line);
					out.println("");
					
					file.close();
				}
				else {
					out.println("HTTP/1.0 404");
					out.println("Content-Type: text/PLAIN");
					out.println("");
					out.println(Server.HttpErrors.Error404);
					out.println("");
				}	
			}
			catch (Exception e) {
				System.out.println("erreur : " + e.getMessage());
			}
		}
	}

	/*
	 * PART 4 : Affiche les requêtes http des clients
	 */
	public void httpdServerV4() {
		while (!(line = in.nextLine()).equals(""))
			out.println(line);
	}

	/*
	 * Check du champs "User-Agent" du navigateur client et affiche firefox.html si le navigateur est Firefox, ie.html si IE
	 * sinon on affiche index.hmtl
	 */
	public void httpdServerV5() {
		File f = null;
		
		while (!(line = in.nextLine()).equals("")) {
			if (line.contains("User-Agent:"))
				data.put("User-Agent", line);
		}
			
		try {			
			if (data.containsKey("User-Agent")) {
				if (data.get("User-Agent").contains("Firefox"))
					f = new File("firefox.html");
				else if (data.get("User-Agent").contains("MSIE"))
					f = new File("ie.html");
			}
			if (f == null)
				f = new File("index.html");
			
			if (f.exists()) {
				FileReader file = new FileReader(f);
				reader = new BufferedReader(file);

				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/HTML");
				out.println("");
				while ((line = reader.readLine()) != null && !line.isEmpty())
					out.println(line);
				out.println("");

				file.close();
			}
			else {
				out.println("HTTP/1.0 404");
				out.println("Content-Type: text/PLAIN");
				out.println("");
				out.println(Server.HttpErrors.Error404);
				out.println("");
			}	
		}
		catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}			
	}
	
}

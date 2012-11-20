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
	Hashtable<String, String> data;
	
	/*
	 *  Constructeur premenant en paramètre une socket cliente
	 *  Définition des flux input/output en fonction de la socket cliente
	 */
	public ThreadClient(Socket s) {
		try {
			socket = s;
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
			switch (TCPServer.mode) {
			case 1:
				httpdServerV1();
				break;
			case 2:
				httpdServerV2();
				break;
			case 3:
				httpdServerV3();
				break;
			case 4:
				httpdServerV4();
				break;
			case 5:
				httpdServerV5();
				break;
			default:
				httpdServerV3();
				break;
			}
			
			socket.close();
		}
		catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}
	}

	// PART1 : Affiche du contenu HTML brut
	public void httpdServerV1() {
		out.println("<HTML>Exemple of HTML file with <BLINK>blinking content</BLINK></HTML>");
	}

	/*
	 * PART 2 : Affiche le contenu HTML brut du fichier index.html
	 * situé dans le répertoire courante du serveur
	 */
	public void httpdServerV2() {
		File file;
		FileReader fileReader;
		BufferedReader bufReader;
		String line;
		
		try {
			file = new File("index.html");

			if (file.exists()) {
				fileReader = new FileReader("index.html");
				bufReader = new BufferedReader(fileReader);

				while ((line = bufReader.readLine()) != null)
					out.println(line);
				
				fileReader.close();
			}
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
		File file;
		FileReader fileReader;
		BufferedReader bufReader;
		StringTokenizer st;
		String line, token;
		
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
				file = new File(data.get("FILE").replace("/", ""));

				if (!file.exists())
					file = new File("index.html");
				
				if (file.exists()) {	
					fileReader = new FileReader(file);
					bufReader = new BufferedReader(fileReader);

					out.println("HTTP/1.0 200 OK");
					out.println("Content-Type: text/HTML");
					out.println("");
					while ((line = bufReader.readLine()) != null && !line.isEmpty())
						out.println(line);
					out.println("");
					fileReader.close();
				}
				else {
					out.println("HTTP/1.0 404");
					out.println("Content-Type: text/PLAIN");
					out.println("");
					out.println(TCPServer.HttpErrors.Error404);
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
		String line;
		
		while (!(line = in.nextLine()).equals(""))
			out.println(line);
	}

	/*
	 * Check du champs "User-Agent" du navigateur client et affiche firefox.html si le navigateur est Firefox, ie.html si IE
	 * sinon on affiche index.hmtl
	 */
	public void httpdServerV5() {
		File file = null;
		FileReader fileReader;
		BufferedReader bufReader;
		String line;
		
		while (!(line = in.nextLine()).equals("")) {
			if (line.contains("User-Agent:"))
				data.put("User-Agent", line);
		}
			
		try {			
			if (data.containsKey("User-Agent")) {
				if (data.get("User-Agent").contains("Firefox"))
					file = new File("firefox.html");
				else if (data.get("User-Agent").contains("MSIE"))
					file = new File("ie.html");
			}
			
			if (file == null)
				file = new File("index.html");
			
			if (file.exists()) {
				fileReader = new FileReader(file);
				bufReader = new BufferedReader(fileReader);

				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/HTML");
				out.println("");
				while ((line = bufReader.readLine()) != null && !line.isEmpty())
					out.println(line);
				out.println("");

				fileReader.close();
			}
			else {
				out.println("HTTP/1.0 404");
				out.println("Content-Type: text/PLAIN");
				out.println("");
				out.println(TCPServer.HttpErrors.Error404);
				out.println("");
			}			
		}
		catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}			
	}
	
}

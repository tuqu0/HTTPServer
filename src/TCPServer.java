import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private static final int portDefault = 1111;
	public static int mode;
	private ServerSocket socketServer = null;
	private Socket socketClient;
	
	
	// Enum des messages associés aux codes de retour dans le protocole HTTP
	public enum  HttpErrors {
		Error404 {
			public String toString() {
				return "404 - File Not Found";
			}
		}
	}
		
	// Constructeur par défaut (port par défaut défini pour le serveur)
	TCPServer(int mode) {
		try {
			TCPServer.mode = mode;
			socketServer = new ServerSocket(portDefault);
		} catch (IOException e) {
			System.out.println("erreur: impossible de se connecter sur le port " + portDefault);
		}
	}
	
	// Constructeur permettant de définir le numéro de port d'écoute du serveur
	TCPServer(int port, int mode) {
		try {
			TCPServer.mode = mode;
			socketServer = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("erreur: impossible de se connecter sur le port " + port);
		}

	}
	
	// Lancement du serveur - Acceptation de clients et lancements d'un nouveau thread pour chaque client
	public void start() {
	
		while (true){			
			try {
				socketClient = socketServer.accept();
				new ThreadClient(socketClient);
			} catch (IOException e) {
				System.out.println("erreur: impossible de lancer un nouveau thread client");
			}
		}
	}
}

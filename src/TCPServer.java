import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private int port;
	private int serverMode;
	private ServerSocket socketServer = null;

	// Enum des messages associés aux codes de retour dans le protocole HTTP
	public enum  HttpErrors {
		Error404 {
			public String toString() {
				return "404 - File Not Found";
			}
		}
	}

	// Constructeur par défaut (définition d'un port et mode par défaut)
	TCPServer() {
		port = 1111;
		serverMode = 3;
	}

	// Getter for port
	public int getPort() {
		return port;
	}

	// Setter for port
	public void setPort(int p) {
		port = p;
	}

	// Getter for serverMode
	public int getServerMode() {
		return serverMode;
	}

	// Setter for serverMode
	public void setServerMode(int m) {
		if (m > 0 && m < 6)
			serverMode = m;
	}

	// Lancement du serveur - Acceptation de clients et lancements d'un nouveau thread pour chaque client
	public void start() {
		Socket socketClient;

		try {
			socketServer = new ServerSocket(port);
			System.out.println("\nLancement du serveur HTTP sur le port " + socketServer.getLocalPort() + " en mode " + serverMode);

			while (true){			
				socketClient = socketServer.accept();
				new ThreadClient(socketClient, serverMode);
			}
		}
		catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}
	}
}

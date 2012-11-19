import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	static final int portDefault = 1111;
	ServerSocket socketsrv = null;
	
	// Constructeur par défaut (port par défaut défini pour le serveur)
	Server() {
		try {
			socketsrv = new ServerSocket(portDefault);
		} catch (IOException e) {
			System.out.println("erreur: impossible de se connecter sur le port " + portDefault);
		}
	}
	
	// Constructeur permettant de définir le numéro de port d'écoute du serveur
	Server(int port) {
		try {
			socketsrv = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("erreur: impossible de se connecter sur le port " + port);
		}

	}
	
	// Lancement du serveur - Acceptation de clients et lancements d'un nouveau thread pour chaque client
	public void start() {
		while (true){
			Socket socketclt;
			try {
				socketclt = socketsrv.accept();
				new ThreadClient(socketclt);

			} catch (IOException e) {
				System.out.println("erreur: impossible de lancer un nouveau thread client");
			}
		}
	}
}

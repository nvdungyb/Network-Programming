package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void serverStart() {
		try {
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("New client Connected");

				ClientHander clientHander = new ClientHander(socket);
				Thread thread = new Thread(clientHander);
				thread.start();
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}

	public void closeServer() {
		try {
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(9000);
		Server server = new Server(serverSocket);
		server.serverStart();
	}
	
}
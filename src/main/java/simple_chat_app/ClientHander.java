package simple_chat_app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHander implements Runnable {
	public static ArrayList<ClientHander> ClientHanders = new ArrayList<>();
	public Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String name;

	public ClientHander(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.name = bufferedReader.readLine();
			ClientHanders.add(this);

			boradcastMessage("SERVER " + name + " has entered in the room");
		} catch (IOException e) {
			e.printStackTrace();
			closeAll(socket, bufferedReader, bufferedWriter);
		}
	}

	@Override
	public void run() {
		String messageFromClient;
		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				boradcastMessage(messageFromClient);
			} catch (IOException e) {
				e.printStackTrace();
				closeAll(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	public void boradcastMessage(String message) throws IOException {
		for (ClientHander clientHander : ClientHanders) {
			try {
				if (!clientHander.name.equals(this.name)) {
					clientHander.bufferedWriter.write(message);
					clientHander.bufferedWriter.newLine();
					clientHander.bufferedWriter.flush();
//					System.out.println(message);
				}
			} catch (IOException e) {
				closeAll(socket, bufferedReader, bufferedWriter);
			}
		}
	}

	public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if (bufferedReader != null)
				bufferedReader.close();
			if (bufferedWriter != null)
				bufferedWriter.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package simple_chat_app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String name;

	public Client(Socket socket, String name) {
		try {
			this.socket = socket;
			this.name = name;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
//			e.printStackTrace();
			closeAll(socket, bufferedReader, bufferedWriter);
		}
	}

	public void readMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String messageFromFroupChat;

				while (socket.isConnected()) {
					try {
						messageFromFroupChat = bufferedReader.readLine();
						System.out.println(messageFromFroupChat);
					} catch (IOException e) {
//						e.printStackTrace();
						closeAll(socket, bufferedReader, bufferedWriter);
					}
				}
			}

		}).start();
	}

	public void sendMessage() {
//		new Thread(new Runnable() {
//			public void run() {
				try {
					bufferedWriter.write(name);
					bufferedWriter.newLine();
					bufferedWriter.flush();

					Scanner sc = new Scanner(System.in);
					while (socket.isConnected()) {
						String message = sc.nextLine();
						bufferedWriter.write(name + ": " + message);
						bufferedWriter.newLine();
						bufferedWriter.flush();
					}
					
					sc.close();
				} catch (IOException e) {
					closeAll(socket, bufferedReader, bufferedWriter);
				}
//			}
//		}).start();
	}

	public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if (socket != null)
				socket.close();
			if (bufferedReader != null)
				bufferedReader.close();
			if (bufferedWriter != null)
				bufferedWriter.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter your name: ");
		String name = sc.nextLine();
		Socket socket = new Socket("localhost", 9000);
		Client client = new Client(socket, name);
		client.readMessage();
		client.sendMessage();

		sc.close();
	}
}

/*
 * Client phải readMessage trước khi sendMessage thì mới Client khác mới nhận được tin mà Client này gửi.?? tại sao.
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class server {

	private static final int MAX_CLIENTS = 50;
	private static Socket s;

	private static ServerSocket server;

	public static void main(String[] args) {

		
		ArrayList<ClientThread> threads = new ArrayList<ClientThread>();

		System.out.println("This is server");

		try {
			server = new ServerSocket(4000);

			while (true) {
				try {
					s = server.accept();
				} catch (Exception e) {
					e.printStackTrace();
				}
				PrintStream out = new PrintStream(s.getOutputStream());
				if (threads.size() < MAX_CLIENTS) {
					ClientThread thread = new ClientThread(s, threads);

					thread.start();
					threads.add(thread);
					System.out.println("We have a connect");
				} else {

					out.print("Server fulls");
					out.close();
					s.close();
				}

				// server.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class ClientThread extends Thread {
	private Socket s;
	private ArrayList<ClientThread> threads;
	private BufferedReader in;
	private PrintStream out;
	private String name;
	private String clientName;

	public ClientThread(Socket s, ArrayList<ClientThread> threads) {
		this.s = s;
		this.threads = threads;
		
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintStream(s.getOutputStream());
			;
			while (true) {

				out.println("Please Enter Your Name");
				name = in.readLine().trim();

				if (name.indexOf("@") == -1) {
					break;
				} else {
					out.println("The name should not contain '@' character");
				}
			}

			out.println("Welcome " + name
					+ " join your room! You can enter /quit to quit room");

			this.clientName = '@' + name;

			synchronized (this) {
				for (int i = 0; i < threads.size(); i++) {
					if (threads.get(i) != this) {
						threads.get(i).out
								.println("*** a new user has name is " + name
										+ " enter the char room1!!");

					}
				}

			}

			while (true) {
				String line = in.readLine();

				if (line.startsWith("/quit")) {
					break;
				}

				/*
				 * If the message is give to private client
				 */
				if (line.startsWith("@")) {
					String[] words = line.split("\\s", 2);
					if (words.length > 1 && words[1] != null) {
						synchronized (this) {
							words[1] = words[1].trim();
							ClientThread targetClient;
							for (Iterator<ClientThread> it = threads.iterator(); it.hasNext();) {
								if ((targetClient = it.next()).clientName.equals(words[0])) {
									targetClient.out.println("From " + this.name + " send you with love "+ words[1]);
									break;
								}
							}
						}
					}
				} else {
					/*
					 * The message is give to all client
					 */
					synchronized (this) {
						for (Iterator<ClientThread> it = threads.iterator(); it.hasNext();) {
							if (it != this) {
								it.next().out.println(name + ": " + line);
							}
						}
					}

				}

			}

			// out

			this.out.println(" Bye! See you again!");

			synchronized (this) {

				for (Iterator<ClientThread> it = threads.iterator(); it
						.hasNext();) {
					if (it != this) {
						it.next().out.println("*** " + name
								+ " out of chat room!!!");
					}
				}

			}

			threads.remove(this);
			// close stream
			in.close();
			out.close();
			s.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
package client;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class client extends JFrame implements Runnable, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Socket s;
	private static PrintStream out;
	private static BufferedReader in;
	
	private boolean connectionB;
	private JButton enter;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JTextPane chatbox;
	private JTextPane clientbox;
	private JEditorPane textbox;
	private JButton jButton1;
	
	

	public static void main(String[] args) {
		try {
			s = new Socket("localhost", 4000);
			if (s != null) {
				new Thread(new client()).start();
				System.out.println("Connect success");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public client() {
		
		super("ClientChat");
		
		jScrollPane2 = new javax.swing.JScrollPane();
        chatbox = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        clientbox = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textbox = new javax.swing.JEditorPane();
        textbox.addKeyListener(new textboxListener());
        jButton1 = new javax.swing.JButton();

        jPanel1.setAlignmentX(4.0F);
        jPanel1.setAlignmentY(4.0F);
        setPreferredSize(new java.awt.Dimension(480, 320));
        setLayout(new java.awt.BorderLayout());

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(320, 260));
        jScrollPane2.setViewportView(chatbox);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(80, 260));
        jScrollPane3.setViewportView(clientbox);

        add(jScrollPane3, java.awt.BorderLayout.LINE_END);

        jPanel1.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        jScrollPane1.setViewportView(textbox);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Enter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String line = textbox.getText().trim();
				if (line.startsWith("/quit"))
				{
					connectionB = false;
				}
				textbox.setText("");
				out.println(line);
            }
        });
        jPanel1.add(jButton1, java.awt.BorderLayout.LINE_END);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);
        
		this.setPreferredSize(new Dimension(480,320));
		this.setSize(480,320);
		
		
		


	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		this.setVisible(true);
		

		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintStream(s.getOutputStream());
			connectionB = true;

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> clients = new ArrayList<String>();
		clients.add("My Name");
		int clientN = 0;
		clientbox.setText(clients.get(clientN));
		out.println(clients.get(clientN));
		String line = "";
		while(connectionB)
		{
			try {
				line = in.readLine();
				System.out.println("Connect success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try
			{
			if (line.contains("*** a new user has name is"))
			{
				clientN++;
				clients.add(line.substring(26, line.indexOf(" enter the char room1!!")));
				clientbox.setText("");
				for (int j =0; j< clientN++; j++)
				{
					clientbox.setText(clientbox.getText() + clients.get(j) + "\n");
				}
				
			}
			} catch (Exception e) {e.printStackTrace();}
			if (line.endsWith("out of chat room!!!"))
			{
				
				for (int i =0; i<clientN; i++)
				{
					if (clients.get(i).equalsIgnoreCase(line.substring(4, line.indexOf("out of chat room!!!"))))
					{
						clients.remove(i);
						
						clientN--;
						for (int j =0; j< clientN++; j++)
						{
							clientbox.setText(clientbox.getText() + clients.get(j) + "\n");
						}
						break;
					}
				}
				
			}
			
			chatbox.setText(chatbox.getText() + line+"\n");
		}
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public class textboxListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent k) {
			// TODO Auto-generated method stub
			if (k.getKeyCode() == KeyEvent.VK_ENTER  )
			{
				String line = textbox.getText().trim();
				if (line.startsWith("/quit"))
				{
					connectionB = false;
				}
				textbox.setText("");
				out.println(line);
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		out.println("Tao quit day");
		out.println("/quit");
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

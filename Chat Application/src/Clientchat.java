/*
 Clientchat.java :- it's simple class to describe client functionality 
 in this Chat application
 
 All the necessary library imports
 
 */


import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 Clientchat extends JFrame and implements ActionListener for button click event 
 and Runnable for Thread generation.
 
 */

public class Clientchat extends JFrame implements ActionListener,Runnable{
	
	/*
	 static Variable Declaration of all Swing class and others networking class
	 */
	
	JTextArea textarea1;
	private static JPanel panel;
	private static JTextField textfield,usernametextfield,hostaddresstextfield;
	private static JButton send,Login,LogOff;
	private static JLabel chatlog,message;
	private static JLabel username,hostaddress;
	
	
	private static DatagramSocket clientsocket;
	private static InetAddress address;
	private static DatagramPacket packetsend;
	int port;
	MulticastSocket socketformulticasting = null;

	/*
	 
	 Constructor to implements Frame Functionality
	 
	 */
	
	Clientchat(String Frmae_title)
	{
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600,600);
		setTitle(Frmae_title);
		setResizable(true);
		setDefaultLookAndFeelDecorated(true);
		makeGUI();           // Call makeGUI() method to display frame and other components
		setVisible(true);
	}
	

	
	public void makeGUI()                   // implementation of makeGUI() method
	{
		
		Container container = getContentPane();              // initialize container to store swing component
		panel = new JPanel();								//  create a panel
		container.add(panel);								// add panel into container
		panel.setLayout(null);
		
		username = new JLabel("Username :");				// username label
		username.setBounds(10,20,88,20);					// set component into perfect position of container using setBounds
		panel.add(username);								// add into panel
		
		usernametextfield = new JTextField(15);				// usename TextField
		usernametextfield.setBounds(100, 20, 190, 20);		// set component into perfect position of container using setBounds
		panel.add(usernametextfield);						// add into panel
		
		Login = new JButton("Log In");						// Log In button
		Login.setBounds(320, 20, 100, 40);					// set component into perfect position of container using setBounds
		panel.add(Login);									// add into panel
		Login.addActionListener(this);					// Register a listener interface to notify if the event is occured
		
		hostaddress = new JLabel("Host Address :");
		hostaddress.setBounds(10, 45, 88, 20);				// label for Host Address
		panel.add(hostaddress);
		
		hostaddresstextfield = new JTextField(15);
		hostaddresstextfield.setBounds(100,45,190,20);		//TextField for host address
		panel.add(hostaddresstextfield);
		
		LogOff = new JButton("Log Out");
		LogOff.setBounds(440,20,100,40);					// Log Out Button and register a listener to notify if the event is occured
		panel.add(LogOff);
		LogOff.addActionListener(this);
		
	
		chatlog = new JLabel("Chat Log");
		chatlog.setBounds(10,68,80,40);
		panel.add(chatlog);
		
		
		textarea1 = new JTextArea(50,50);
		textarea1.setBounds(10, 100, 350, 350);				// Textarea for displaying chat messages
		textarea1.setEditable(false);
		panel.add(textarea1);
		textarea1.setWrapStyleWord(true);
		textarea1.setLineWrap(true);
		
		
		
		
		message = new JLabel("Message");
		message.setBounds(10,470,80,40);					// Message Label
		panel.add(message);
		
		textfield = new JTextField(30);						// Textfield for sending messages
		textfield.setBounds(10,500,350,35);
		panel.add(textfield);
		
		
	
		send = new JButton("Send");
		send.setBounds(370, 500, 100, 40);					// send button is a source to generate an event
		panel.add(send);
		send.addActionListener(this);
		

	}
	
	
	/*
	 * 
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * run() method which is the Entry Point of Thread
	 */
	
	
	// multicast Receiver Logic used for Receiving messages from multiple client through Server
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
			try {
				clientsocket = new DatagramSocket();
				
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
			}
			
					// multicast socket is used for receiving a message as multicastly
				 
				while(true)
				{
					
					
				try{
					
				socketformulticasting = new MulticastSocket(6789);
					
				address = InetAddress.getByName("229.5.6.7");		// Any group address between 224.0.0.0 to 239.255.255.255 
												
				socketformulticasting.joinGroup(address);			// 224.0.0.0 is reserved as a Base Address
					
			//	System.out.println("Start client recieve");	
				
				 byte receivemessage[] = new byte[1024];			// Buffer to store received message
				 
				 packetsend = new DatagramPacket(receivemessage, receivemessage.length);		// create a packet to receive a message
				 
				 socketformulticasting.receive(packetsend);										// receive a message multicastly
				 
				 String receivedtext = new String(packetsend.getData(),0,packetsend.getLength());
				 
				 System.out.println(receivedtext);
				 
					 
				 textarea1.append(receivedtext+"\n");
				
				 
				 
				
				} 
					 
				catch(Exception e)
				{
					e.printStackTrace();
				}
			
		}
		
	}
	
	
	// This method is used for specific boundary space between Component and Container.

	
	public Insets getInsets()
	{
		return new Insets(20, 20, 20, 20);
	}
	
	
	
	
/*
 * 	(non-Javadoc)
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 * 
 * This is the method used for Handing an Events which is generated by button
 */

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		
		
		Object src = e.getSource();
		
		/*
		 *  Take the username and hostaddress from client Textfield
		 *  Enter any address from 127.0.0.1 through 127.255.255.254
		 */
		
		String	username = usernametextfield.getText();
		String hostaddress = hostaddresstextfield.getText();
		
		
		/*
		 *  When the Login Button Clicked connection established through IPAddress and Port Number
		 *  DatagramSocket is used to send user name inside packet over network
		 *  DatagramPacket is used to create a packet
		 */
		
		if(src == Login)
		{
			if(!username.isEmpty() && !hostaddress.isEmpty())
			{
				
			try {
				
				clientsocket = new DatagramSocket();
				
				byte sendusername[] = new byte[1024];
				
				
				address = InetAddress.getByName(hostaddress);
				
				String encryptedusername = username + " : " + " has Joinned";
				
				sendusername = encryptedusername.getBytes();
				
				packetsend = new DatagramPacket(sendusername, sendusername.length,address,4445);
				
				usernametextfield.setEditable(false);
				
				Login.setEnabled(false);
				
				clientsocket.send(packetsend);
				
		//		System.out.println("username send successfully");
				
				
				
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please Enter Username and Hostaddress for Log In","Important Message",JOptionPane.WARNING_MESSAGE);
				
				System.out.println("");
				
			}
			
			
			
		}
	
		/* 
		 * When the send Button Clicked connection established through IPAddress and Port Number
		 *  DatagramSocket is used to send Client message inside packet over network
		 *  DatagramPacket is used to create a packet
		 * 
		 */
		
		else if(src == send )
		{
			String message = textfield.getText().trim();
			
			
			if(!message.isEmpty())
			{
			
			String newmwssage = username + " : " + message;
			
			byte sendmessage[] = new byte[1024]; 
			
			try {
				
				
				address = InetAddress.getByName(hostaddress);
				
				
			} catch (UnknownHostException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			sendmessage = newmwssage.getBytes();
			
			packetsend = new DatagramPacket(sendmessage,sendmessage.length,address,4445);   
			
			textfield.setText("");
			
			try {
				
				clientsocket.send(packetsend);
				
				
					} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//	System.out.println("End of client");
			}	
			else
			{
				JOptionPane.showMessageDialog(null,"Please insert a message in TextField","Important Message",JOptionPane.WARNING_MESSAGE);
				System.out.println("Please Enter the message in TextField");
			}
		}
		
		/* 
		 * When the Log Off Button Clicked connection established through IPAddress and Port Number
		 *  DatagramSocket is used to send SignOff message inside packet over network
		 *  DatagramPacket is used to create a packet
		 *  DatagramSocket is closed after successfully signOff
		 * 
		 */
		
		else
		{
			
			String signoff = username + " : " + "has left";
			
			byte signoffmeessage[] = new byte[1024];
			
			try {
				
				address = InetAddress.getByName(hostaddress);		
				
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			signoffmeessage = signoff.getBytes();
			
			packetsend = new DatagramPacket(signoffmeessage,signoffmeessage.length,address,4445);
			
			textfield.setEditable(false);
			
			send.setEnabled(false);
			LogOff.setEnabled(false);
			
			try {
				clientsocket.send(packetsend);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			clientsocket.close();
			
		}
		
		
		
	}
	
	/*
	 * 	main method to start thread and call Clientchat class
	 */
	
	public static void main(String args[])
	{
			
		(new Thread(new Clientchat("Client Chat"))).start();
		
	}

}

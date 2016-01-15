/*
 Serverchat.java :- it's simple class to describe server functionality 
 in this Chat application
 
 All the necessary library imports
 
 */


import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;



/*
Serverchat extends JFrame and implements ActionListener for button click event 
and Runnable for Thread generation.

*/

public class Serverchat extends JFrame implements ActionListener,Runnable{
	
	
	/*
	 static Variable Declaration of all Swing class and others networking class
	 */
	
	
	private static JTextArea textarea;
	private static JPanel panel;
	private static JButton Close;
	private static JLabel chatlog;
	private static JLabel userlist;
	private static JList<String> currentuser;
	DefaultListModel<String> currentuserlist;
	
	private DatagramSocket serversocket;
	private DatagramPacket serverpacket;
	int port;

	
	/*
	 
	 Constructor to implements Frame Functionality
	 
	 */
	
	
	Serverchat(String Frmae_title) throws IOException
	{
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600,600);
		
		setTitle(Frmae_title);
		setResizable(true);
		
		makeGUI();									 // Call makeGUI() method to display frame and other components
		
		setVisible(true);
		
	}
	

	
	public void makeGUI() throws IOException			// implementation of makeGUI() method
	{
		
		Container container = getContentPane();			// initialize container to store swing component
		panel = new JPanel();							//  create a panel
		container.add(panel);							// add panel into container
		panel.setLayout(null);							// set layout is null because later all component explicitly store into container using setBounds()
		
		Close = new JButton("Close");
		Close.setBounds(440,20,100,40);					// Close Button and register a listener to notify if the event is occured
		panel.add(Close);
		Close.addActionListener(this);
		
		
	
		chatlog = new JLabel("Chat Log");
		chatlog.setBounds(10,68,80,40);
		panel.add(chatlog);
		
		userlist = new JLabel("Online Users");			
		userlist.setBounds(380,68,80,40);
		panel.add(userlist);
		
		currentuserlist = new DefaultListModel<String>();		// implementation of JList used to display current online users
		currentuser = new JList<String>(currentuserlist);
		currentuser.setBounds(380,100,150,350);
		panel.add(currentuser);
		
		textarea = new JTextArea(50,50);							// TextArea which is display chat message
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textarea.setBorder(BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10,10,10,10)));
		textarea.setBounds(10, 100, 350, 350);
		textarea.setEditable(false);
		panel.add(textarea);
		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	
	// Receive client message and then send message back to all the connected clients
	
	public void run() {
		// TODO Auto-generated method stub
		try{
			
			serversocket = new DatagramSocket(4445);		// open a DatagramSocket for receive a message from client
			
			
			
			
			while(true)
			{
				
				byte receivemessage[] = new byte[1024];			// create a buffer for receive client message
				
				serverpacket = new DatagramPacket(receivemessage, receivemessage.length);
				
				serversocket.receive(serverpacket);											// Receive a message from client
				
				String messagefromclient = new String(serverpacket.getData()).trim();
				
				System.out.println(messagefromclient);
				
				String username = null;
				
			// Display a message when client login 
				
				if(messagefromclient.endsWith("Joinned"))
				{
					
				
					
					username = messagefromclient.substring(0, messagefromclient.lastIndexOf(':')).trim();
					
					System.out.println(username);
					
			
				
					currentuserlist.addElement(username);
				
					
					textarea.append(messagefromclient.trim()+"\n");
					
					
					System.out.println(username+" Login Successfully");
					
					
				
					
					
					
				}
				
				// Display a message when client logOff
				
				
				else if(messagefromclient.endsWith("left"))
				{
					
					username = messagefromclient.substring(0,messagefromclient.lastIndexOf(':')).trim();
					
					System.out.println(username);
					
					currentuserlist.removeElement(username);
					
					textarea.append(messagefromclient.trim()+"\n");
					
				
					
				}
				
				// Display an actual client chat message
				
				else{
					
					
					
					textarea.append(messagefromclient+"\n");
					
				}
				
			//	System.out.println(list);
				
				InetAddress address = InetAddress.getByName("229.5.6.7");		// Any group address between 224.0.0.0 to 239.255.255.255 
				
			
																				// 224.0.0.0 is reserved as a Base Address
				System.out.println(address);
					
				receivemessage = messagefromclient.getBytes();					// Convert String into Bytes
				
				serverpacket = new DatagramPacket(receivemessage, receivemessage.length,address,6789);
				
				serversocket.send(serverpacket);								// Send a packet unicastly over network with group IP address
				
				System.out.println("end of server receiving");
				
				
				
			}
			
			}
			catch(EOFException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
	}

	// This method is used for specific boundary space between Component and Container.
	
	
	public Insets getInsets()
	{
		return new Insets(20, 20, 20, 20);
	}
	
	
/*
 * (non-Javadoc)
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	
	//  This is the method used for Handing an Events which is generated by button

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		Object src = e.getSource();
		
		if(src == Close)
		{
			System.exit(0);
			
				
			
		}
	
		
		
	}
	
	/*
	 * 	main method to start thread and call Serverchat class
	 */
	
	public static void main(String args[]) throws IOException
	{
		(new Thread(new Serverchat("Server Chat"))).start();
		
		System.out.println("Waiting for a Clients");
	}



	
	
	


}

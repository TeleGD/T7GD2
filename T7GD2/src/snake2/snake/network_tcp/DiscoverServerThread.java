package snake2.snake.network_tcp;

import java.io.IOException;
import java.net.*;

public class DiscoverServerThread extends Thread implements Runnable{

	private static final String MESSAGE_TO_DETECT = "SomeRequestData";
	private static final String MESSAGE_DETECTED = "SomeRequestData";

	private static final int PORT = 8888;
	private int timeout;
	private int nbRetry;
	
	public DiscoverServerThread(int timeout, int nbRetry){
		this.timeout = timeout;
		this.nbRetry = nbRetry;
	}
	
	@Override
	public void run(){
        DatagramSocket socket;
		try { 
			DatagramPacket sendPacket,receivePacket;
			byte[] recvBuf;
			for(int i=0;i<nbRetry;i++){

		        
		        //ON BROADCASTE A TOUS LE MONDE SUR LE PORT "PORT", LE MESSAGE "MESSAGE_TO_DETECT"
		        byte[] sendData = MESSAGE_TO_DETECT.getBytes();
		        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), PORT);
		        
				socket = new DatagramSocket();
		        socket.setBroadcast(true);
		        socket.setSoTimeout(10000);
		        socket.send(sendPacket);
		        socket.close();
		        
				System.out.println("MESSAGE BROADCASTE PAR LE SERVEUR A TOUT LE MONDE:"+MESSAGE_TO_DETECT);

				long time=System.currentTimeMillis();

				while((System.currentTimeMillis()-time)<timeout){
			        try{
			        	//ON ATTEND LES REPONSES DES GENS QUI RECHERCHE CE SERVEUR
						System.out.println("EN ATTENTE D'UN RETOUR CLIENT");
	
				        recvBuf = new byte[15000];
				        receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
				        
						socket = new DatagramSocket(PORT, InetAddress.getByName("0.0.0.0"));
				        socket.setSoTimeout(timeout/2);
				        socket.receive(receivePacket);
				        socket.close();
				        
				        //MESSAGE RECU, ANALYSONS
						String received = new String(receivePacket.getData(), 0,receivePacket.getLength());
						System.out.println("MESSAGE EN REPONSE DU BROADCAST : "+received);
						
						if(received.equals(MESSAGE_DETECTED)){
							//YEEESS NOUVEAU CLIENT.
				        }
						
			        }catch (SocketTimeoutException e) {
			        	socket.close();
		            }
				}
			}
			
	        
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

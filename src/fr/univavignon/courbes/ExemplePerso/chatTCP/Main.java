package fr.univavignon.courbes.chatTCP;


public class Main {

	public static void main(String[] args) {
		 String host = "192.168.0.14";
	     int port = 2345;
	      
	      ServerTCP ts = new ServerTCP(host, port);
	      ts.open();
	      
	      /*System.out.println("Serveur initialisé.");*/
	      
	         Thread t = new Thread(new clientTCP(host, port));
	         t.start();
	         
	         /*String ip = InetAddress.getLocalHost().getHostAddress().toString();
		 		System.out.println("Votre IP est : " + ip);*/ // méthode A (obtenir ip locale)
		 	  
		         //méthode B (marche tout le temps !)
		         /*String ip;
		 	    try {
		 	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		 	        while (interfaces.hasMoreElements()) {
		 	            NetworkInterface iface = interfaces.nextElement();
		 	            // filters out 127.0.0.1 and inactive interfaces
		 	            if (iface.isLoopback() || !iface.isUp())
		 	                continue;

		 	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
		 	            while(addresses.hasMoreElements()) {
		 	                InetAddress addr = addresses.nextElement();
		 	                ip = addr.getHostAddress();
		 	                if (ip.startsWith("192.168."))
		 	                System.out.println(iface.getDisplayName() + " " + ip);
		 	            }
		 	        }
		 	    } catch (SocketException e) {
		 	        throw new RuntimeException(e);
		 	    }*/
	}

}

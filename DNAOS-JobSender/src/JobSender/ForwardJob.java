package JobSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ForwardJob {
    
    private InetAddress ipaddr;
    private int port;
    
    public ForwardJob(InetAddress ip, int port)
    {
        ipaddr = ip;
        this.port = port;
        sendMessages();
    }
    
    private void sendMessages()
    {
        try
        {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[1000];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            while(true)
            {
                System.out.println("Enter what you would like to say:\n");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String out = in.readLine().trim();
                if(out.equals("quit"))
                {
                    System.out.println("Closing down");
                    System.exit(0);
                }
                buffer = out.getBytes();
                DatagramPacket dpout = new DatagramPacket(buffer, buffer.length, ipaddr, port);
                socket.send(dpout);
                socket.receive(dp);
                String recvd = new String(dp.getData(), 0, dp.getLength());
                System.out.print("\nLoad Balancer: " + recvd + "\n");
            }
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

}

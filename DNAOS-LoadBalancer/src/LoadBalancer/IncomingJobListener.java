 package LoadBalancer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class IncomingJobListener implements Runnable {
   
    private final int port_no;
    ArrayList<Node> nodes = NodeManager.getInstance().nodes;
    
    public IncomingJobListener(int port)
    {
        port_no = port;
    }
    
    @Override
    public void run()
    {
        try
        {
            byte[] buffer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            DatagramSocket socket;
            socket = new DatagramSocket(port_no);
            while (true)
            {
                socket.receive(dp);
                String recv = new String(dp.getData(), 0, dp.getLength());
                System.out.println("Job Initator: Incoming Job '" + recv + "'");
                String out = analyseRecvMessage(recv);
                buffer = out.getBytes();
                DatagramPacket dpo = new DatagramPacket(buffer, buffer.length, dp.getAddress(), dp.getPort());
                socket.send(dpo);
            }
        }
        catch(IOException e)
        {
            System.out.println("An error occured connecting to Job Sender: " + e.getMessage());
        }
    }

    public String analyseRecvMessage(String s) throws IOException
    {
        String[] args = s.split(" ");
        String cmd = args[0].trim().toLowerCase();
        switch(cmd)
        {
            case("quitlb"):
                quitLB();
                return "ShuttingDown";
            case("job"):
                if(args.length == 3)
                {
                    try
                    {
                        Integer.parseInt(args[2]);
                        JobManager.getInstance().queueJob(s);
                        return "Job Being Sent Off!";
                    }
                    catch(NumberFormatException e)
                    {
                        return "Invalid Input! Usage: job <Job Name (One word)> <Length of Job (in seconds)>";
                    }
                }
                else
                {
                    return "Invalid Arguements! Usage: job <Job Name (One word)> <Length of Job (in seconds)>";
                }
            default:
                System.out.println("Unknown Command");
                return "Unknown Command";
        }
    }
    
    public void quitLB()
    {
        System.out.println("\n--------------------------\nLoad Balancer SHUTTING DOWN...\n--------------------------");
        System.exit(0);
    }
}

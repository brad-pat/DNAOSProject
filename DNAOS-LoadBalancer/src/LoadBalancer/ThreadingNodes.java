package LoadBalancer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ThreadingNodes implements Runnable {
    
    int port;
    int total_nodes;
    InetAddress mainhost;
    
    public ThreadingNodes(int port, int total_nodes_current, InetAddress mainhostip)
    {
        this.port = port;
        total_nodes = total_nodes_current;
        mainhost = mainhostip;
    }
    
    @Override
    public void run()
    {
        try
        {
            System.out.println("LoadBalancer: Thread Started '" + Thread.currentThread().getName() + "'");
            DatagramSocket socket;
            byte[] buffer = new byte[1024];
            DatagramPacket recvdPacket = new DatagramPacket(buffer, buffer.length);
            
            socket = new DatagramSocket(port);
            while(true)
            {
                socket.receive(recvdPacket);
                String recvd = new String(recvdPacket.getData(), 0, recvdPacket.getLength());
                String out =  analyzeMessage(recvd);
                buffer = out.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, recvdPacket.getAddress(), recvdPacket.getPort());
                socket.send(sendPacket);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void registerNode(int jobs)
    {
        ArrayList<Node> nodes = NodeManager.getInstance().nodes;
        int node_number = total_nodes;
        Node n = new Node("Node"+node_number, mainhost, port, 0, jobs, Thread.currentThread().getName());
        nodes.add(n);
        System.out.println("LoadBalancer: " + n.getNodeName() + " has been registered with a weighting of: " + jobs + " at a time.");
    }
    
    public String giveJob()
    {
        ArrayList<String> jobs = JobManager.getInstance().jobs_to_execute;
        while(!jobs.isEmpty())
        {
            String job = "";
            try
            {
                job = jobs.get(0);
            }
            catch(IndexOutOfBoundsException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
            String[] jobparts = job.split(" ");
            String threadname = Thread.currentThread().getName().toLowerCase();
            if(threadname.equalsIgnoreCase(jobparts[0] + "Thread"))
            {
                String fulljob = jobparts[1] + " " + jobparts[2] + " " + jobparts[3];
                System.out.println("LoadBalancer: Sending Job '" + jobparts[2] + "' to " + jobparts[0]);
                jobs.remove(0);
                return fulljob;
            }
            else
            {
                return "";
            }
        }
        return "";
    }
    
    public String analyzeMessage(String s)
    {
        String[] parts = s.split(" ");
        String threadname = Thread.currentThread().getName();
        switch(parts[0].toLowerCase())
        {
            case "register":
                System.out.println(threadname + ": Registration Message incoming..");
                int totaljobs = 0;
                try
                {
                    totaljobs = Integer.parseInt(parts[1]);
                }
                catch(NumberFormatException e)
                {
                    System.out.println("LoadBalancer: Error Registering node");
                }
                registerNode(totaljobs);
                return "Registered";
            case "pending":
                String job = giveJob();
                return job;
            case "completed":
                System.out.println(threadname + ": '" + parts[1] + "' has been completed!");
                String nodenamewthread = Thread.currentThread().getName();
                String[] nodenameps = nodenamewthread.split("T");
                String nodename = nodenameps[0];
                JobManager.getInstance().completeJob(nodename);
                String jobb = giveJob();
                return jobb;
            case "failed":
                System.out.println(threadname + ": '" + parts[1] + "' has failed.");
                String jobbb = giveJob();
                return jobbb;
            default:
                System.out.println("Default section");
                return "Unknown";
        }
    }
}
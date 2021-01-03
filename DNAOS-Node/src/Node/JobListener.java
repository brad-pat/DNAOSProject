package Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class JobListener extends Thread {
   
    private final InetAddress host;
    private final int port;
    boolean firstmessagesent = false;
    String income;
    ArrayList<String> jobs = Storage.getInstance().jobs;

    
    public JobListener(InetAddress ip, int port)
    {
        host = ip;
        this.port = port;
    }
    
    public void start_thread()
    {
        start();
    }
    
    @Override
    public void run()
    {
        try
        {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while(true)
            {
                String output = analyzeMessage(income);
                if(firstmessagesent == false)
                {
                    output = registration();
                }
                buffer = output.getBytes();
                DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, host, port);
                socket.send(outpacket);
                socket.receive(packet);
                income = new String(packet.getData(), 0, packet.getLength());
                performJob();
                //System.out.println(income);
                
            }
        }
        catch(IOException e)
        {
            e.getMessage();
        }
    }

    private String completedJobs()
    {
        ArrayList<String> completed = Storage.getInstance().complete_jobs;
        if(!completed.isEmpty())
        {
            String compjob = completed.get(0);
            int index = completed.indexOf(compjob);
            completed.remove(index);
            return "completed " + compjob;
        }
        else
        {
            return "pending";
        }        
    }
    
    private void performJob()
    {
        while(!jobs.isEmpty())
        {
            String job = jobs.get(0);
            String[] jobparts = job.split(":");
            try
            {
                String jobname = jobparts[0];
                long time = Long.parseLong(jobparts[1]);
                ProcessJob pj = new ProcessJob(jobname, time);
                pj.start();
                int index = jobs.indexOf(job);
                jobs.remove(index);
            }
            catch(NumberFormatException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private String registration()
    {
        if(firstmessagesent == false)
        {
            firstmessagesent = true;
            return "register " + Storage.getInstance().total_jobs;
        }
        return null;
    }
    
    public String analyzeMessage(String s)
    {
        if(s == null)
        {
            System.out.println("Node: Registration String Sent");
            return "register " + Storage.getInstance().total_jobs;
        }
        String[] args = s.split(" ");
        String cmd = args[0];
        switch(cmd.toLowerCase())
        {
            case("job"):
                System.out.println("LoadBalancer: Job incoming..");
                try
                {
                    String store = args[1]+":"+args[2];
                    jobs.add(store);
                    System.out.println("Node: Job added to be performed! ");
                    return "pending";
                }
                catch(NumberFormatException e)
                {
                    System.out.println("Error: " + e.getMessage());
                    return "failed " + args[1];
                }
            case("registered"):
                System.out.println("Node: Successfully Registered.");
                return "pending";
            case("quit"):
                System.out.println("Node: Quiting this Node!");
                System.exit(0);
                return "quitting"; //Line will never get reached, but has to be present for the sake of the switch and method.
            case "":
                String result = completedJobs();
                return result;
            default:
                System.out.println("Unknown Command");
                return "Unknown Command";
        }
    }
}

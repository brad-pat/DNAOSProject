package Node;

import java.io.IOException;
import java.net.InetAddress;

public class NodeMain {
    
    

    public static void main(String[] args)
    {
        if(args.length == 4 && args[0].toLowerCase().equalsIgnoreCase("connect"))
        {
            try
            {
                String ip_address = args[1];
                int port = Integer.parseInt(args[2]);
                int maxjobs = Integer.parseInt(args[3]);
                System.out.println("Node: Total Jobs on this node - " + maxjobs);
                InetAddress ip = InetAddress.getByName(ip_address);
                Storage.getInstance().setTotalJobsThatCanBePerformed(maxjobs);
                //System.out.println(Storage.getInstance().getTotalJobsThatCanBePerformed());
                JobListener jobhandler = new JobListener(ip, port);
                jobhandler.start_thread();
            }
            catch(IOException | NumberFormatException e)
            {
                System.out.println("Error: " + e.getMessage());
                System.exit(0);
            }
        }
        else
        {
            System.out.println("Invalid Arguements or Command. Use: connect <IP Address> <Port>");
            System.exit(0);
        }
    }
    


}

package LoadBalancer;

import java.io.IOException;
import java.net.InetAddress;

public class MainLoadBanancer {
    
    public static void main(String[] args)
    {
        if(args.length > 3)
        {
            if(args[0].equalsIgnoreCase("open"))
            {
                String ipaddress = args[1];
                int init_port;
                try
                {
                    //Initiator Stuff
                    init_port = Integer.parseInt(args[2]);
                    InetAddress ip = InetAddress.getByName(ipaddress);
                    //Node Stuff
                    int total_nodes = args.length-3;
                    System.out.println("Creating threads for the " + total_nodes + " Nodes to register.");
                    int node_counter = 1;
                    for(int i = 3; i < args.length; i++)
                    {
                        System.out.println("Creating Node: Node" + node_counter + " on Port: " + args[i]);
                        int node_port = Integer.parseInt(args[i]);
                        Thread thread = new Thread(new ThreadingNodes(node_port, node_counter, ip));
                        thread.setName("Node" + node_counter + "Thread");
                        thread.start();
                        node_counter++;
                    }
                    Thread incoming_thread = new Thread(new IncomingJobListener(init_port));
                    incoming_thread.setName("IncomingMessageThread");
                    incoming_thread.start();
                }
                catch(NumberFormatException | IOException e)
                {
                    System.out.println("Error! " + e.getMessage());
                    System.out.println("Shutting Down..");
                    System.exit(0);
                }
            }
            else
            {
                System.out.println("Invalid Command! Usage: open <IP Address> <Job Initiator Port> <Node 1 Port> [Node 2 Port] [Node 3 Port] etc");
                System.out.println("Shutting Down..");
                System.exit(0);
            }
        }
        else
        {
            System.out.println("Invalid Configuration! Usage: open <IP Address> <Job Initiator Port> <Node 1 Port> [Node 2 Port] [Node 3 Port] etc");
            System.out.println("Shutting Down..");
            System.exit(0);
        }
        
    }
}

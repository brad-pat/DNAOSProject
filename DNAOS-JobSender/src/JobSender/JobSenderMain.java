package JobSender;

import java.io.IOException;
import java.net.InetAddress;

public class JobSenderMain {
    
    public static void main(String[] args) throws IOException
    {
        if(args.length == 3)
        {
            if(args[0].toLowerCase().equals("connect"))
            {
                String ip = args[1];
                int port = 0;
                try
                {
                    port = Integer.parseInt(args[2]);
                }
                catch(NumberFormatException e)
                {
                    System.out.println("Error: Port has to be a number!");
                    System.exit(0);
                }
                InetAddress host = InetAddress.getByName(ip);
                System.out.println("Job Initiator: Enter a job like so:\n Job <Job Name> <Time(sec)>");
                new ForwardJob(host, port);
            }
            else
            {
                System.out.println("Unknown Command. Use: connect <IP Address> <Port>");
                System.exit(0);
            }
        }
        else
        {
            System.out.println("Invalid Arguements. Use: connect <IP Address> <Port>");
            System.exit(0);
        }
    }
}

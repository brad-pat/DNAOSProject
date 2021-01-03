package Node;

import java.util.concurrent.TimeUnit;

public class ProcessJob extends Thread {
    
    private final String jobname;
    private final long time;
    
    public ProcessJob(String name, long time)
    {
        jobname = name;
        this.time = time;
    }
    
    public void start_thread()
    {
        start();
    }
    
    public void terminate_thread()
    {
        stop();
    }
    
    @Override
    public void run()
    {
        execute();
    }
    
    private void execute()
    {
        try
        {
            System.out.println("Node: " + jobname + " for " + time + " seconds.");
            System.out.println("Node: '" + jobname + "' has started!");
            TimeUnit.SECONDS.sleep(time);
            System.out.println("Node: '" + jobname + "' has been completed!");
            Storage.getInstance().completeJob(jobname);
            terminate_thread();
        }
        catch(InterruptedException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

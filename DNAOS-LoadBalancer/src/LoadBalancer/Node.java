package LoadBalancer;

import java.net.InetAddress;

public class Node {
    
    private String node_name;
    private InetAddress node_ipaddress;
    private int node_port;
    private int node_current_jobs;
    private int node_total_jobs;
    private String thread_name;
    
    public Node(String name, InetAddress ipaddress, int port, int currentjobs, int totaljobs, String threadname)
    {
        node_name = name;
        node_ipaddress = ipaddress;
        node_port = port;
        node_current_jobs = currentjobs;
        node_total_jobs = totaljobs;
        thread_name = threadname;
    }
    
    public String getNodeName()
    {
        return node_name;
    }
    
    public InetAddress getNodeIPAddress()
    {
        return node_ipaddress;
    }
    
    public int getNodePort()
    {
        return node_port;
    }
    
    public int getNodeCurrentAmountOfJobs()
    {
        return node_current_jobs;
    }
    
    public int getNodeTotalJobsAllowed()
    {
        return node_total_jobs;
    }
    
    public String getNodeThreadName()
    {
        return thread_name;
    }
    
    public void setNodeName(String new_name)
    {
        node_name = new_name;
    }
    
    public void setNodeIPAddress(InetAddress new_ip)
    {
        node_ipaddress = new_ip;
    }
    
    public void setNodePort(int new_port)
    {
        node_port = new_port;
    }
    
    public void setNodeCurrentJobs(int jobs)
    {
        node_current_jobs = jobs;
    }
    
    public void setNodeTotalJobsAllowed(int jobs)
    {
        node_total_jobs = jobs;
    }
    
    public void setNodeThreadName(String name)
    {
        thread_name = name;
    }
}

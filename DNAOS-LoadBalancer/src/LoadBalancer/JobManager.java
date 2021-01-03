package LoadBalancer;

import java.util.ArrayList;

public class JobManager {
    
    private static JobManager instance = null;
    
    private JobManager()
    {
        
    }
    
    public static JobManager getInstance()
    {
        if(instance == null)
        {
            instance = new JobManager();
        }
        return instance;
    }
    
    ArrayList<Node> nodes = NodeManager.getInstance().nodes;
    public ArrayList<String> jobs_to_execute = new ArrayList<>();
    int index = 0;
    
    public void queueJob(String job)
    {
        Node n = selectNodeWRR();
        String node_name = n.getNodeName();
        String job_string_with_node = node_name + " " + job;
        jobs_to_execute.add(job_string_with_node);
        //System.out.println("LoadBalancer: '" + job + "' has been added to list to execute");
    }
    
    public void completeJob(String node)
    {
        Node n = NodeManager.getInstance().getNodeObjectByName(node);
        n.setNodeCurrentJobs(n.getNodeCurrentAmountOfJobs()-1);
        printWeightings();
    }

    //Original Round Robin method no longer in use
    private Node selectNodeRR()
    {
        int max = nodes.size();
        if(max != 0)
        {
            if(max == index)
            {
                index = 0;
            }
            Node n = nodes.get(index);
            System.out.println("LoadBalancer: " + n.getNodeName() + " has been selected!");
            index++;
            return n;
        }
        else
        {
            System.out.println("LoadBalancerError: No Nodes Seem to be registered");
            return null;
        }
    }
    
    private void printWeightings()
    {
        System.out.println("------ Node Weightings ------");
        for(Node n : nodes)
        {
            System.out.println(n.getNodeName() + ": " + n.getNodeCurrentAmountOfJobs() + " / " + n.getNodeTotalJobsAllowed());
        }
        System.out.println("-----------------------------");
    }
    
    private Node selectNodeWRR()
    {
        Node node = null;
        //printWeightings();
        for(Node n : nodes)
        {
            int free = n.getNodeTotalJobsAllowed()-n.getNodeCurrentAmountOfJobs();
            if(node == null){ node = n; }
            int orgnodetotal = node.getNodeTotalJobsAllowed()-node.getNodeCurrentAmountOfJobs();
            if(orgnodetotal < free) { node = n; }
        }
        if(node != null)
        {
            node.setNodeCurrentJobs(node.getNodeCurrentAmountOfJobs()+1);
            System.out.println(node.getNodeName() + " selected: " + node.getNodeCurrentAmountOfJobs() + "/" + node.getNodeTotalJobsAllowed());
            printWeightings();
        }
        return node;
    }
}

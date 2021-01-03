package Node;

import java.util.ArrayList;

public class Storage {
    
    private static Storage instance = null;
    public ArrayList<String> jobs = new ArrayList<>();
    public ArrayList<String> complete_jobs = new ArrayList<>();
    public int total_jobs;
    
    public void completeJob(String job)
    {
        complete_jobs.add(job);
    }
    
    public String getJobByName(String name)
    {
        for(String s : jobs)
        {
            String[] split = s.split(":");
            if(split[0].toLowerCase().equalsIgnoreCase(name))
            {
                return s;
            }
        }
        return "";
    }
    
    public void setTotalJobsThatCanBePerformed(int t)
    {
        total_jobs = t;
    }
    
    public int getTotalJobsThatCanBePerformed()
    {
        return total_jobs;
    }
    
    private Storage(){}
    
    public static Storage getInstance()
    {
        if(instance == null)
        {
            instance = new Storage();
        }
        return instance;
    }

}

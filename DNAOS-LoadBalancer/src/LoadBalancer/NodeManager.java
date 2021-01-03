package LoadBalancer;

import java.util.ArrayList;

public class NodeManager {
    
    public ArrayList<Node> nodes = new ArrayList<>();
    private static NodeManager instance = null;
    
    private NodeManager()
    {
        
    }
    
    public static NodeManager getInstance()
    {
        if(instance == null)
        {
            instance = new NodeManager();
        }
        return instance;
    }
    
    public void addNode(Node n)    
    {
        nodes.add(n);
    }

    public void deleteNode(Node n)
    {
        int index = nodes.indexOf(n);
        nodes.remove(index);
    }
    
    public Node getNodeObjectByName(String name)
    {
        for(Node n : nodes)
        {
            if(n.getNodeName().equals(name)) 
            {
                return n;
            }
        }
        return null;
    }
}

import java.util.ArrayList; 

import java.util.Collections;   

public class NodeSorter {     

  ArrayList<Node> nodes = new ArrayList<>();       

  public NodeSorter(ArrayList<Node> nodes) {         

    this.nodes = nodes;     

  }       

  public ArrayList<Node> getSortedNodesByEvaluation() {         

    Collections.sort(nodes);

    return nodes;     

  } 

}
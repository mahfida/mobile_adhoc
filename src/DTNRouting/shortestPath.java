package DTNRouting;

import java.util.ArrayList;


// A Java program for Dijkstra's 
// single source shortest path  
// algorithm. The program is for 
// adjacency matrix representation 
// of the graph. 
  
public class shortestPath { //Dijkstra algorithm
  
    private  final int NO_PARENT = -1; 
    //public  ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> paths;
    //public  ArrayList<Integer> single_path =new ArrayList<Integer>();
    public  double dest_distance[];
    public  int dest_number[];
    // Function that implements Dijkstra's 
    // single source shortest path 
    // algorithm for a graph represented  
    // using adjacency matrix 
    // representation 
    private  void dijkstra(double[][] adjacencyMatrix, 
                                        int startVertex,
                                        int [] destinations) 
    { 
        int nVertices = adjacencyMatrix[0].length; 
        // shortestDistances[i] will hold the 
        // shortest distance from src to i 
        double[] shortestDistances = new double[nVertices]; 
        // added[i] will true if vertex i is 
        // included / in shortest path tree 
        // or shortest distance from src to  
        // i is finalized 
        boolean[] added = new boolean[nVertices]; 
  
        // Initialize all distances as  
        // INFINITE and added[] as false 
        for (int vertexIndex = 0; vertexIndex < nVertices;  
                                            vertexIndex++) 
        { 
            shortestDistances[vertexIndex] = 10000.0; 
            added[vertexIndex] = false; 
        } 
          
        // Distance of source vertex from 
        // itself is always 0 
        shortestDistances[startVertex] = 0; 
  
        // Parent array to store shortest 
        // path tree 
        int[] parents = new int[nVertices]; 
  
        // The starting vertex does not  
        // have a parent 
        parents[startVertex] = NO_PARENT; 
  
        // Find shortest path for all  
        // vertices 
        for (int i = 1; i < nVertices; i++) 
        { 
  
            // Pick the minimum distance vertex 
            // from the set of vertices not yet 
            // processed. nearestVertex is  
            // always equal to startNode in  
            // first iteration. 
            int nearestVertex = -1; 
            double shortestDistance = 10000.0; 
            for (int vertexIndex = 0; 
                     vertexIndex < nVertices;  
                     vertexIndex++) 
            { 
                if (!added[vertexIndex] && 
                    shortestDistances[vertexIndex] <  
                    shortestDistance)  
                { 
                    nearestVertex = vertexIndex; 
                    shortestDistance = shortestDistances[vertexIndex]; 
                } 
            } 
  
            // Mark the picked vertex as 
            // processed 
            if(nearestVertex < 0) break;
            added[nearestVertex] = true; 
  
            // Update dist value of the 
            // adjacent vertices of the 
            // picked vertex. 
            for (int vertexIndex = 0; 
                     vertexIndex < nVertices;  
                     vertexIndex++)  
            { 
                double edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex]; 
                  
                if (edgeDistance > 0
                    && ((shortestDistance + edgeDistance) <  
                        shortestDistances[vertexIndex]))  
                { 
                    parents[vertexIndex] = nearestVertex; 
                    shortestDistances[vertexIndex] = shortestDistance +  
                                                       edgeDistance; 
                } 
            } 
        } 
  
    
          
        for (int vertexIndex = 0;  
                 vertexIndex < destinations.length;  
                 vertexIndex++)  { 
            if (destinations[vertexIndex] != startVertex)  
            { 
                dest_number[vertexIndex]= destinations[vertexIndex];
                paths.add(new ArrayList<Integer>());
                if(shortestDistances[destinations[vertexIndex]] < 1000.0)
                	displayPath(destinations[vertexIndex], parents, vertexIndex);
                else
                	paths.get(vertexIndex).add(startVertex); //No path	
                dest_distance[vertexIndex] = shortestDistances[destinations[vertexIndex]];
                //single_path.clear();
                //System.out.println("Paths: "+ paths.get(vertexIndex));
                
            } }
        
    } 
  
    // Function to print shortest path 
    // from source to currentVertex 
    // using parents array 

private  void displayPath(int currentVertex, 
            int[] parents, int vertexIndex) 
{ 
ArrayList<Integer> dummy_path= new ArrayList<Integer>();
while (currentVertex != NO_PARENT) 
{ 
 
 dummy_path.add( currentVertex);
 currentVertex = parents[currentVertex];
} 

for(int i=dummy_path.size()-1; i >= 0 ; i --) {
	paths.get(vertexIndex).add(dummy_path.get(i));
}

} 
    
    
        // Driver Code 
    public  void runDijkstra (double adjacencyMatrix[][],int[] destinations, int source, Node n)
    { 
    	 paths= new ArrayList<>(destinations.length);
    	 dest_distance =new double[destinations.length];
    	 dest_number =new int[destinations.length];
    
         dijkstra(adjacencyMatrix, source, destinations); 
         n.ptD.paths= paths;
         n.ptD.dest_distance= dest_distance;
         n.ptD.dest_number= dest_number;
			/*
			 * System.out.print("Vertex\t Distance\tPath"); for (int vertexIndex = 0;
			 * vertexIndex < destinations.length; vertexIndex++) {
			 * 
			 * System.out.print("\n" + source + " -> ");
			 * System.out.print(destinations[vertexIndex] + " \t\t ");
			 * System.out.print(dest_distance[vertexIndex] + "\t\t");
			 * System.out.println(paths.get(vertexIndex));}
			 */
			
     } // End of method
    
}// End of class 
  

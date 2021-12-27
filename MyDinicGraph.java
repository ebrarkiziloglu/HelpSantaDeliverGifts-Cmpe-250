import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import static java.lang.System.out;

public class MyDinicGraph {
	
	private final int numOfVertex;
    private int[] level;
    private ArrayList<ArrayList<Edge>> adjacency;
    private int totalNumberOfPresents;

    // Getters and Setters:

    // Constructor:
    public MyDinicGraph(int numOfVertex, int totalNumberOfPresents){
        this.adjacency = new ArrayList<>();
        for(int i = 0; i < numOfVertex; i++){
            this.adjacency.add(new ArrayList<>());
        }
        this.numOfVertex = numOfVertex;
        this.level = new int[numOfVertex];
        this.totalNumberOfPresents = totalNumberOfPresents;
    }

    // Add edge:
    public void addEdge(int source, int sink, int capacity){
        // forward edge:
        Edge forwardEdge = new Edge(sink, 0, capacity, this.adjacency.get(sink).size());
        // back edge:
        Edge reverseEdge = new Edge(source, 0, 0, this.adjacency.get(source).size());
        this.adjacency.get(source).add(forwardEdge);
        this.adjacency.get(sink).add(reverseEdge);
    }

    // BFS function checks whether there is still a path from source to sink:
    public boolean BFS(int s, int t){
        for(int i = 0; i < this.numOfVertex; i++) this.level[i] = -1;
        this.level[s] = 0;

        Queue<Integer> bfsQueue = new LinkedList<>();
        bfsQueue.add(s);

        while(!bfsQueue.isEmpty()){
            int u = bfsQueue.poll();
            for(Edge edge : this.adjacency.get(u)){
                if(level[edge.sinkVertices] < 0 && edge.flow < edge.capacity){
                    level[edge.sinkVertices] = level[u] + 1;
                    bfsQueue.add(edge.sinkVertices);
                }
            }
        }
        return level[t] >= 0;
    }

    // This function finds the maximum possible flow for the given path
    public int sendFlow(int u, int flow, int t, int start[]){
        if(u == t) return flow;         // sink is reached

        // Traverse all adjacent edges one-by-one
        for( ; start[u] < adjacency.get(u).size(); start[u]++){

            // Pick next edge in the adjacency.get(u)
            Edge edge = adjacency.get(u).get(start[u]);
            if(level[edge.sinkVertices] == level[u] + 1 && edge.flow < edge.capacity){
                // find minimum flow from u to t:
                int currentFlow = Math.min(flow, edge.capacity - edge.flow);
                int tempFlow = sendFlow(edge.sinkVertices, currentFlow, t, start);

                if(tempFlow > 0){
                    edge.flow += tempFlow;
                    adjacency.get(edge.sinkVertices).get(edge.reverseEdge).flow -= tempFlow;
                    return tempFlow;
                }
            }
        }
        return 0;
    }

    // this function is the core of this implementation.
    // As long as BFS function returns true, this function finds the flow using sendFlow function and adds it to the totalFlow possible;
    public int dinicMaxFlow(/*int s, int t*/){
        int s = 0;
        int t = numOfVertex - 1;
        int flow;
        if(s == t) return -1;

        int totalFlow = 0;

        while(BFS(s,t)){
            int[] start = new int[this.numOfVertex + 1];
            for(int i = 0; i <= numOfVertex; i++) start[i] = 0;

            while((flow = sendFlow(s, Integer.MAX_VALUE, t, start)) > 0){
                totalFlow += flow;
            }
        }
        return  this.totalNumberOfPresents - totalFlow;
    }

    public void print(){
        for(int i = 0; i < numOfVertex; i++){
            ArrayList<Edge> edgeList = adjacency.get(i);
            out.println("vertex " + i + " : ");
            for(Edge edge : edgeList){
                out.println("\t\tsink: " + edge.sinkVertices + "  || capacity: " + edge.capacity + "  || flow: " + edge.flow + "  || reverseIndex: " + edge.reverseEdge);
            }
            out.println("\n\n");
        }
    }
}

class Edge{
    int sinkVertices;
    int flow;
    int capacity;
    int reverseEdge;

    public Edge(int sinkVertices, int flow, int capacity, int reverseEdge){
        this.sinkVertices = sinkVertices;
        this.flow = flow;
        this.capacity = capacity;
        this.reverseEdge = reverseEdge;
    }
}

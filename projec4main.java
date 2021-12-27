import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

// Project4/src/*.java -d Project4/bin --release 16
// java -cp Project4/bin project4main /Users/gk/eclipse-workspace/test_cases_4/inputs/input_0.txt myoutput.txt

// java -cp Project4/bin project4main /Users/gk/Desktop/Study/CMPE250.01/Project-4/input_deneme.txt /Users/gk/Desktop/Study/CMPE250.01/Project-4/output_deneme.txt
// java -cp bin project4main /Users/gk/Desktop/Study/CMPE250.01/Project-4/input_deneme.txt /Users/gk/Desktop/Study/CMPE250.01/Project-4/output_deneme.txt

public class project4main {
	
	public static void main(String[] args) throws FileNotFoundException {
        // To keep track of time:
        long start = System.currentTimeMillis();
        // Taking file paths as arguments:
        File inputFile = new File(args[0]);
        //File inputFile = new File("/Users/gk/Desktop/Study/CMPE250.01/Project-4/input_deneme.txt");
        File outputFile = new File(args[1]);
        PrintStream print = new PrintStream(outputFile);
        // Taking the graph object created from the supportive method:
        MyDinicGraph myGraph = constructGraph(inputFile);

        // Control:
        // myGraph.print();

        // Processing the algorithm:
        if(myGraph != null) {
        	print.println(myGraph.dinicMaxFlow());
        } else {
        	print.println(0);
        }
        


        // Printing the output:


        long end = System.currentTimeMillis();
        long time = end - start;
        // out.println("Time: " + time + " milliseconds.");
        print.close();


        // TRIAL:
        // graph1();
        // graph2();



    }

    // This function reads the input from the file, construct the main graph of the project and sends this graph object back to the main function:
    public static MyDinicGraph constructGraph(File inputFile) throws FileNotFoundException {

        // Creating reading object:
        Scanner sc = new Scanner(inputFile);

        int numOfVertices = 0;

        // Reading input data:
        int numOfGreenTrains = sc.nextInt();
        ArrayList<Integer> greenTrainCaps = new ArrayList<>();
        int i = numOfGreenTrains;
        while(i-- > 0){
            int capacity = sc.nextInt();
            greenTrainCaps.add(capacity);
        }
        numOfVertices += numOfGreenTrains;

        int numOfRedTrains = sc.nextInt();
        ArrayList<Integer> redTrainCaps = new ArrayList<>();
        i = numOfRedTrains;
        while(i-- > 0){
            int capacity = sc.nextInt();
            redTrainCaps.add(capacity);
        }
        numOfVertices += numOfRedTrains;

        int numOfGreenReindeers = sc.nextInt();
        ArrayList<Integer> greenReindeerCaps = new ArrayList<>();
        i = numOfGreenReindeers;
        while(i-- > 0){
            int capacity = sc.nextInt();
            greenReindeerCaps.add(capacity);
        }
        numOfVertices += numOfGreenReindeers;

        int numOfRedReindeers = sc.nextInt();
        ArrayList<Integer> redReindeerCaps = new ArrayList<>();
        i = numOfRedReindeers;
        while(i-- > 0) {
            int capacity = sc.nextInt();
            redReindeerCaps.add(capacity);
        }
        numOfVertices += numOfRedReindeers;

        int numOfPresents = 0;
        int numOfBags = sc.nextInt();
        if(numOfBags == 0) {
        	return null;
        }
        int totalBags = 0;
        HashMap<String, List<Integer>> bags = new HashMap<>();
        i = 0;
        sc.nextLine();
        String[] bagInfo = sc.nextLine().split(" ");
        while(i < 2 * numOfBags){
            String type = bagInfo[i];
            int capacity = Integer.parseInt(bagInfo[i+1]);
            numOfPresents += capacity;
            if(! bags.containsKey(type)) bags.put(type, new ArrayList<>());

            if(type.charAt(0) == 'a') {
                bags.get(type).add(capacity);
                totalBags++;
            }
            else {
                if (bags.get(type).isEmpty()) {
                    bags.get(type).add(capacity);
                    totalBags++;
                } else {
                    capacity += bags.get(type).get(0);
                    bags.get(type).clear();
                    bags.get(type).add(capacity);
                }
            }
            i+= 2;
        }

        numOfVertices += totalBags;
        numOfVertices += 2;         // for additional source and sink nodes.

        // Creating the Graph object:
        MyDinicGraph myGraph = new MyDinicGraph(numOfVertices, numOfPresents);

        // Adding edges:
        int sourceIndex = 0;
        int sinkIndex = numOfVertices -1;
        int greenTrainIndices = totalBags;
        i = greenTrainIndices + 1;
        // indices of Green Trains: (greenTrainIndices+1), (greenTrainIndices+2), ... , (greenTrainIndices+ numOfGreenTrains)
        for(; i <= greenTrainIndices + numOfGreenTrains; i++){
            myGraph.addEdge(i, sinkIndex, greenTrainCaps.get(i-greenTrainIndices-1));
        }
        int redTrainIndices = greenTrainIndices + numOfGreenTrains;
        i = redTrainIndices + 1;
        // indices of Red Trains: (redTrainIndice+1), (redTrainIndice+2), ... , (redTrainIndice+numOfRedTrains)
        for(; i <= redTrainIndices + numOfRedTrains; i++){
            myGraph.addEdge(i, sinkIndex, redTrainCaps.get(i-redTrainIndices-1));
        }
        int greenReindeerIndices = redTrainIndices + numOfRedTrains;
        i = greenReindeerIndices + 1;
        // indices of Green Reindeers : (greenReindeerIndices+1), (greenReindeerIndices+2), ... , (greenReindeerIndices+numOfGreenReindeers)
        for(; i <= greenReindeerIndices + numOfGreenReindeers; i++){
            myGraph.addEdge(i, sinkIndex, greenReindeerCaps.get(i-greenReindeerIndices-1));
        }
        int redReindeerIndices = greenReindeerIndices + numOfGreenReindeers;
        i = redReindeerIndices + 1;
        // indices of Red Reindeers : (redReindeerIndeces+1), (redReindeerIndeces+2), ... , (redReindeerIndeces+numOfRedReindeers)
        for(; i <= redReindeerIndices + numOfRedReindeers; i++){
            myGraph.addEdge(i, sinkIndex, redReindeerCaps.get(i-redReindeerIndices-1));
        }

        int indice = 1;
        // indices of bags: 1, 2, 3, ..., totalBags
        //           k ->  0     1     2     3     4     5      6      7      8     9    10   11   12   13    14    15    16
        String[] types = {"a", "ab", "ac", "ad", "ae", "abd", "abe", "acd", "ace", "b", "c", "d", "e", "bd", "be", "cd", "ce"};
        for(int k = 0; k < 9; k++){
            String type = types[k];
            if(bags.containsKey(type) && !(bags.get(type).isEmpty())){
                for(int capacity: bags.get(type)){
                    // add edge from source to the node:
                    myGraph.addEdge(sourceIndex, indice, capacity);
                    // add edge from the node to green trains if needed:
                    if(k == 0 || k == 1 || k == 3 || k == 5){
                        for(int j = greenTrainIndices+1; j <= redTrainIndices; j++){
                            myGraph.addEdge(indice, j, 1);
                        }
                    }
                    // add edge from the node to red trains if needed:
                    if(k == 0 || k == 2 || k == 3 || k == 7){
                        for(int j = redTrainIndices+1; j <= greenReindeerIndices; j++){
                            myGraph.addEdge(indice, j, 1);
                        }
                    }
                    // add edge from the node to green reindeers if needed:
                    if(k == 0 || k == 1 || k == 4 || k == 6){
                        for(int j = greenReindeerIndices+1; j <= redReindeerIndices; j++){
                            myGraph.addEdge(indice, j, 1);
                        }
                    }
                    // add edge from the node to red reindeers if needed:
                    if(k == 0 || k == 2 || k == 4 || k == 8){
                        for(int j = redReindeerIndices+1; j <= redReindeerIndices + numOfRedReindeers; j++){
                            myGraph.addEdge(indice, j, 1);
                        }
                    }
                    indice++;
                }

            }
        }
        for(int k = 9; k < 17; k++){
            String type = types[k];
            if(bags.containsKey(type) && !(bags.get(type).isEmpty())){
                int capacity = bags.get(type).get(0);
                // add edge from source to the node:
                myGraph.addEdge(sourceIndex, indice, capacity);
                // add edge from the node to green trains if needed:
                if(k == 9 || k == 11 || k == 13){
                    for(int j = greenTrainIndices+1; j <= redTrainIndices; j++){
                        myGraph.addEdge(indice, j, capacity);
                    }
                }
                // add edge from the node to red trains if needed:
                if(k == 10 || k == 11 || k == 15){
                    for(int j = redTrainIndices+1; j <= greenReindeerIndices; j++){
                        myGraph.addEdge(indice, j, capacity);
                    }
                }
                // add edge from the node to green reindeers if needed:
                if(k == 9 || k == 12 || k == 14){
                    for(int j = greenReindeerIndices+1; j <= redReindeerIndices; j++){
                        myGraph.addEdge(indice, j, capacity);
                    }
                }
                // add edge from the node to red reindeers if needed:
                if(k == 10 || k == 12 || k == 16){
                    for(int j = redReindeerIndices+1; j <= redReindeerIndices + numOfRedReindeers; j++){
                        myGraph.addEdge(indice, j, capacity);
                    }
                }
                indice++;
            }
        }

        sc.close();
        return myGraph;

    }

}

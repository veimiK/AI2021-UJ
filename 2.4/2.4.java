import java.util.*;
import java.io.*;
import java.math.*;

class Node{
    int id;
    boolean checkGate;

    ArrayList<Node> parentList;
    ArrayList<Node> childrenList;
    ArrayList<Node> linkList;

    void setGate (boolean v){
        this.checkGate = v;
    }

    //parent & children lists for connections
    void addParent (Node p){
        parentList.add(p);
    }
    void addChild (Node c){
        childrenList.add(c);
    }
    //for each child or parent adding a link
    void createLinks () {
        for (Node node: parentList) {
            linkList.add(node);
        }
        for (Node node: childrenList) {
            linkList.add(node);
        }
        //System.err.println("Node " + id + " links to following: " + linkList);
    }

    Node(){
        childrenList = new ArrayList<Node>();
        parentList = new ArrayList<Node>();
        linkList = new ArrayList<Node>();
    }

}
//connections to be used later
class Conn{
    int id;
    int id2;

    Conn (int id, int id2){
        this.id = id;
        this.id2 = id2;
    }
}

class Graph{

    ArrayList<Node> nodeList;
    ArrayList<Conn> connectionList;
    ArrayList<Node> gatewayList;
    //bfs lists
    ArrayList<Node> visited;
    ArrayList<Node> queue;


    Graph () {
        this.nodeList = new ArrayList<Node>();
        this.connectionList = new ArrayList<Conn>();
        this.gatewayList = new ArrayList<Node>();
        //init bfs lists
        this.visited = new ArrayList<Node>();
        this.queue = new ArrayList<Node>();
    }

    void createConnections (int id, int id2){
        //using parent & child for connections
        nodeList.get(id).addChild(nodeList.get(id2));
        nodeList.get(id2).addParent(nodeList.get(id));
        Conn conn = new Conn(id, id2);
        connectionList.add(conn);
    }

    Conn BFS (int BadGuyID){
        visited.add(nodeList.get(BadGuyID));
        for (Node node : nodeList.get(BadGuyID).linkList) {
            queue.add(node);
        }
        System.err.println("Visited: " + visited);
        System.err.println("Queue: " + queue);

        for (Node node : gatewayList) {
            if (queue.contains(node) && node.linkList.contains(nodeList.get(BadGuyID))){
                Conn conn = new Conn(BadGuyID, node.id);
                return conn;
            }
        }

        Conn conn = new Conn(visited.get(visited.size() - 1).id, queue.get(0).id);

        return conn;
    }

    void makeGates(){
        for (Node node : nodeList) {
            if (node.checkGate){
                gatewayList.add(node);
            }
        }
    }


}


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Graph graph = new Graph();
        Scanner in = new Scanner(System.in);

        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways

        
        for (int i = 0; i < N; i++){ // put all nodes in nodeList in graph object
            Node n = new Node();
            n.id = i;
            graph.nodeList.add(n);
        }

        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            graph.createConnections(N1, N2); // add connection to graph

        }
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            graph.nodeList.get(EI).checkGate = true; // set the correct gateways

        }

        graph.makeGates();
        for (Node node : graph.nodeList) { // create links in graph object
            node.createLinks();
        }


        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Bobnet agent is positioned this turn

            // Write an action using System.err.println()
            // To debug: System.err.println("Debug messages...");


            // Example: 0 1 are the indices of the nodes you wish to sever the link between

            Conn conn = graph.BFS(SI);
            System.out.println(conn.id + " " + conn.id2);

        }
    }
}
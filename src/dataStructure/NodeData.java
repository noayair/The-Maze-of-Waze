package dataStructure;

import org.w3c.dom.Node;
import utils.Point3D;

import java.util.Collection;
import java.util.HashMap;

/**
 *This class represents the set of operations applicable on a
 *node (vertex) in a (directional) weighted graph.
 * @author Noa Yair and Oriya Kronfeld
 */

public class NodeData implements node_data {
    private int key; //the id of the node
    private double weight; //the weight of the node
    private Point3D location; //the location of the node
    private String info; //indicates which node I came from, used for functions in Graph_Algo
    private int tag; //indicates if I was already at this vertex or not, used for functions in Graph_Algo
    private HashMap<Integer,edge_data> edge_hash; //hash map of all the edges that emerge from the node

    //default constructor
    public NodeData(){
        this.key = 0;
        this.weight = Double.POSITIVE_INFINITY;
        this.location = new Point3D(0,0,0);
        this.info = "";
        this.tag = 0;
        this.edge_hash = new HashMap<Integer, edge_data>();
    }
    //constructors

    public NodeData (int key , double weight , Point3D location , String info , int tag){
        this.key = key;
        this.weight = weight;
        this.location = location;
        this.info = info;
        this.tag = tag;
        this.edge_hash = new HashMap<Integer, edge_data>();
    }

    public NodeData(int key){
        this.key = key;
        this.tag = 0;
        this.info = null;
        this.weight = Double.POSITIVE_INFINITY;
        this.edge_hash = new HashMap<Integer, edge_data>();
        this.location = null;
    }

    //Getters and Setters

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public Point3D getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Point3D p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public edge_data getEdge(int dest){
        if(!this.edge_hash.containsKey(dest)){
            return null;
        }
        return this.edge_hash.get(dest);
    }

    //functions

    /**
     * function that add a new edge to the hash map
     */
    public void add_edge(int dest , edge_data e){
        this.edge_hash.put(dest , e);
    }

    /**
     * This method return a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @return Collection<edge_data>
     */
    public Collection<edge_data> getE() {
        return this.edge_hash.values();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key - the ID of the node we want to delete.
     */
    public edge_data remove_node(int key){
        for (edge_data edge : edge_hash.values()){
            if(edge.getDest() == key){
                return this.edge_hash.remove(edge.getDest());
            }
        }
        return null;
    }

    /**
     * Delete the edge from the graph,
     * @return the data of the removed edge (null if none).
     */
    public edge_data remove_edge(int dest){
        return this.edge_hash.remove(dest);
    }

    /**
     * @return the number of edges (assume directional graph).
     */
    public int edgeSize() {
        return this.edge_hash.size();
    }

    public String toString(){
        return String.valueOf(this.key);
    }
}
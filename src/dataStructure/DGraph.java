package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents a directional weighted graph.
 * @author Noa Yair and Oriya Kronfeld
 */

public class DGraph implements graph , Serializable {
	private HashMap<Integer,node_data> node_hash;
	private int counter;

	//constructor

	public DGraph(){
		this.node_hash = new HashMap<Integer, node_data>();
		counter = 0;
	}

	//Getters and Setters

	/**
	 * return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		if(!this.node_hash.containsKey(key)){
			return null;
		}
		return this.node_hash.get(key);
	}

	/**
	 * @return the data of the edge (src,dest), null if none.
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		return ((NodeData)node_hash.get(src)).getEdge(dest); // go to function getEdge in the NodeData and return the requested edge
	}

	//functions

	/**
	 * add a new node to the graph with the given node_data.
	 * @param n
	 */
	@Override
	public void addNode(node_data n) {
		if(this.node_hash.containsKey(n.getKey())){ // if this key is already exists in the graph
			throw new RuntimeException("this key is already exists");
		}
		this.node_hash.put(n.getKey() , n);
		counter++;
	}

	/**
	 * Connect an edge with weight w between node src to node dest.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if(!node_hash.containsKey(src)){
			throw new RuntimeException("Error - the source node does not exist");
		}
		if(!node_hash.containsKey(dest)){
			throw new RuntimeException("Error - the destination node does not exist");
		}
		EdgeData e = new EdgeData(src , dest , w);
		((NodeData)node_hash.get(src)).add_edge(dest , e);
		counter++;
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_data> getV() {
		return this.node_hash.values();
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of
	 * the given node (all the edges starting (source) at the given node).
	 * @return Collection<edge_data>
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		return ((NodeData)node_hash.get(node_id)).getE(); // go to function in NodeData
	}

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * @return the data of the removed node (null if none).
	 * @param key - the ID of the node we want to delete.
	 */
	@Override
	public node_data removeNode(int key) {
		if(!this.node_hash.containsKey(key)){
			throw new RuntimeException("Error - the node you want to remove does not exist");
		}
		for (node_data n : node_hash.values()){
			((NodeData)n).remove_node(key);
		}
		counter++;
		return this.node_hash.remove(key);
	}

	/**
	 * Delete the edge from the graph.
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		counter++;
		if(!node_hash.containsKey(src) || !node_hash.containsKey(dest)){
			throw new RuntimeException("Error - the edge you want to remove does not exist");
		}
		return ((NodeData)this.node_hash.get(src)).remove_edge(dest);
	}

	/**
	 * @return the number of vertices (nodes) in the graph.
	 */
	@Override
	public int nodeSize() {
		return this.node_hash.size();
	}

	/**
	 * @return the number of edges (assume directional graph).
	 */
	@Override
	public int edgeSize() {
		int size = 0;
		for (node_data n : node_hash.values()){
			size += ((NodeData)n).edgeSize();
		}
		return size;
	}

	/**
	 * @return the Mode Count - for testing changes in the graph.
	 */
	@Override
	public int getMC() {
		return counter;
	}

	public String toString(){
		String s = "";
		for (node_data n : this.node_hash.values()){
			s += ((NodeData)n).toString() + " ";
		}
		return s;
	}

	public static void main(String[] args) {
		DGraph graph = new DGraph();
		NodeData n1 = new NodeData(1);
		NodeData n2 = new NodeData(2);
		NodeData n3 = new NodeData(3);
		NodeData n4 = new NodeData(4);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
//		graph.connect(2,2,4);
		System.out.println("node size: " + graph.nodeSize());
		graph.connect(1 ,2 , 10.4);
		graph.connect(4 , 3 , 22);
		System.out.println("edge from 1: " + graph.getE(1));
		System.out.println("edge size: " + graph.edgeSize());
		graph.connect(3 , 1 , 78.3);
		graph.connect(1 , 4 , 20.2);
		System.out.println("edge from 1: " + graph.getE(1));
		System.out.println("edge size: " + graph.edgeSize());
//		graph.removeNode(1);
		System.out.println("node size: " + graph.nodeSize());
		System.out.println("edge size: " + graph.edgeSize());
//		graph.removeEdge(4 , 3);
//		graph.removeEdge(9,2);
		System.out.println(graph.toString());
		System.out.println(graph.getMC());
		System.out.println(n3.toString());
		System.out.println(graph.getEdge(1,2).toString());
	}

}

package algorithms;


import java.io.*;
import java.util.*;

import dataStructure.*;

/**
 This class implemets graph_algorithms.
 represents the "regular" Graph Theory algorithms including-
 Contains a set of algorithems and methods that can be performed on a given graph.
 * @author Noa Yair and Oriya Kronfeld
 *
 */
public class Graph_Algo implements graph_algorithms , Serializable {
	private graph graph;

	// constructors
	public Graph_Algo(graph g) {
		init(g);
	}

	public Graph_Algo() {
		this.graph = null;
	}

	//Getters

	public graph getGraph()
	{
		return graph;
	}

	/**
	 * Init this set of algorithms on the parameter - graph.
	 * @param g
	 */
	@Override
	public void init(graph g) {
		this.graph = g;
	}

	/**
	 * Init a graph from file
	 * @param file_name
	 */
	@Override
	public void init(String file_name) {
		try
		{
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			graph = (graph)in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized");

		}

		catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println("IOException is caught");
		}

		catch(ClassNotFoundException ex)
		{
			System.out.println("ClassNotFoundException is caught");
		}
	}

	/** Saves the graph to a file.
	 *
	 * @param file_name
	 */
	@Override
	public void save(String file_name) {
		try
		{
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(graph);

			out.close();
			file.close();

			System.out.println("Object has been serialized");
		}
		catch(IOException ex)
		{
			System.out.println("IOException is caught");
		}

	}

	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return
	 */
	@Override
	public boolean isConnected() {
		Queue<node_data> queue = new LinkedList<>();
		for (node_data n : graph.getV()) {
			n.setTag(1);
			queue.add(n);
			while (!queue.isEmpty()) {
				for (edge_data e : graph.getE(n.getKey())) {
					if(graph.getNode(e.getDest()).getTag() != 1) {
						queue.add(graph.getNode(e.getDest()));
						graph.getNode(e.getDest()).setTag(1);
					}
				}
				queue.remove();
				n = queue.peek();
			}
			for (node_data node : graph.getV()) {
				if (node.getTag() == 0) {
					return false;
				}
			}
			clear(graph);
		}
		return true;
	}

	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if (src == dest) {
			return 0;
		}
		for (node_data n : graph.getV()) { //update all the nodes weight to infinity
			n.setWeight(Double.POSITIVE_INFINITY);
		}
		graph.getNode(src).setWeight(0); //update the src weight to 0
		int counter = 0;
		node_data node = new NodeData();
		while (counter < graph.nodeSize()) {
			node = find_min(graph);
			if(node.getKey() == 0){ // if the way is not exist return infinity
				return Double.POSITIVE_INFINITY;
			}
			node.setTag(1);
			cost_way(node);
			counter++;
			if (node.getKey() == dest) {
				clear(this.graph);
				return node.getWeight();
			}
		}
		clear(this.graph);
		return node.getWeight();
	}

	public node_data find_min(graph g) { //find the node with the minimum weight
		node_data ans = new NodeData();
		ans.setWeight(10000);
		for (node_data n : g.getV()) {
			if (n.getWeight() < ans.getWeight() && n.getTag() == 0) {
				ans = n;
			}
		}
		return ans;
	}

	public void cost_way(node_data node) { //calculates the weight of the node and updates so that it is minimal
		double w = 0;
		for (edge_data e : graph.getE(node.getKey())) {
			w = node.getWeight() + e.getWeight();
			if (w < graph.getNode(e.getDest()).getWeight()) {
				graph.getNode(e.getDest()).setWeight(w);
				graph.getNode(e.getDest()).setInfo(Integer.toString(node.getKey()));
			}
		}
	}

	public void clear(graph g) { // set all the node tag to 0
		for (node_data n : g.getV()) {
			n.setTag(0);
		}
	}
	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		LinkedList<node_data> list = new LinkedList<node_data>();
		Stack<node_data> stack = new Stack<node_data>();
		double d = shortestPathDist(src, dest);
		if(d == Double.POSITIVE_INFINITY){
			return null;
		}
		node_data node = graph.getNode(dest);
		stack.push(node);
		int key = 0;
		while (node.getKey() != src) {
			key = Integer.parseInt(node.getInfo());
			node = graph.getNode(key);
			stack.push(node);
		}
		while (!stack.isEmpty()) {
			list.add(stack.pop());
		}
		return list;
	}

	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem,
	 * as you can visit a node more than once, and there is no need to return to source node -
	 * just a simple path going over all nodes in the list.
	 * @param targets
	 * @return
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets) {
		int src;
		int dest;
		int i = 0;
		node_data n = new NodeData();
		List<node_data> shortest_path = new LinkedList<>();
		List<node_data> ans = new LinkedList<>();
		if (!this.isConnected()){
			return null;
		}
		while(!targets.isEmpty()){
			if(targets.size() == 1){
				if(!ans.contains(this.graph.getNode(targets.get(0)))) {
						ans.add(this.graph.getNode(targets.get(0)));
						break;
					}else{
						break;
					}
			}
			i = 0;
			src = targets.get(0);
			dest = targets.get(1);
			shortest_path = this.shortestPath(src , dest);
			Iterator<node_data> iter = shortest_path.listIterator();
			ans.add(this.graph.getNode(src));
			targets.remove(i);
			while(iter.hasNext()){
				n = iter.next();
				if(!ans.contains(n) || ans.indexOf(n) != ans.size()-1) {
					ans.add(n);
				}
				if(targets.contains(n.getKey())){
					i = targets.indexOf(n.getKey());
					targets.remove(i);
				}
			}
		}
		return ans;
	}

	@Override
	public graph copy() {
		graph ans = new DGraph();
		for(node_data n : this.graph.getV()){
			ans.addNode(n);
		}
		return ans;
	}

	public String toString(){
		return ((DGraph)graph).toString();
	}

	public static void main(String[] args) {
		DGraph graph = new DGraph();
		Graph_Algo g = new Graph_Algo(graph);
		NodeData n1 = new NodeData(1);
		NodeData n2 = new NodeData(2);
		NodeData n3 = new NodeData(3);
		NodeData n4 = new NodeData(4);
		NodeData n5 = new NodeData(5);
		NodeData n6 = new NodeData(6);
		NodeData n7 = new NodeData(7);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
		graph.addNode(n5);
		graph.addNode(n6);
		graph.addNode(n7);
		graph.connect(2, 1, 4);
		graph.connect(1, 3, 3);
		graph.connect(1, 5, 20);
		graph.connect(3, 2, 6);
		graph.connect(2, 4, 5);
		graph.connect(4, 6, 2);
		graph.connect(6, 7, 3);
		graph.connect(7, 5, 5);
		graph.connect(3, 4, 11);
		graph.connect(4, 5, 10);
		graph.connect(5, 3, 8);
		double d = g.shortestPathDist(4, 1);
		System.out.println(d);
//		LinkedList<node_data> list = new LinkedList<>();
//		list = (LinkedList<node_data>) g.shortestPath(1, 7);
//		System.out.println(list);
//		boolean b = g.isConnected();
//		System.out.println(b);
		List<Integer> target = new LinkedList<>();
		target.add(1);
		target.add(2);
		target.add(3);
		target.add(3);
//		target.add(4);
		LinkedList<node_data> list1 = new LinkedList<>();
		list1 = (LinkedList<node_data>) g.TSP(target);
		System.out.println(list1);
//		List<Integer> target1 = new LinkedList<>();
//		target1.add(7);
//		target1.add(2);
//		target1.add(1);
//		target1.add(4);
//		target1.add(1);
//		target1.add(7);
//		list1 = (LinkedList<node_data>) g.TSP(target1);
//		System.out.println(list1);
//		g.copy();
//		DGraph dg = new DGraph();
//		Graph_Algo gr = new Graph_Algo(dg);
//		NodeData node1 = new NodeData(1);
//		NodeData node2 = new NodeData(2);
//		NodeData node3 = new NodeData(3);
//		NodeData node4 = new NodeData(4);
//		NodeData node5 = new NodeData(5);
//		dg.addNode(node1);
//		dg.addNode(node2);
//		dg.addNode(node3);
//		dg.addNode(node4);
//		dg.addNode(node5);
//		dg.connect(1,3,1);
//		dg.connect(3,5,2);
//		dg.connect(5,4,3);
//		dg.connect(4,2,4);
//		dg.connect(2,1,5);
//		boolean bool = gr.isConnected();
//		System.out.println(bool);
	}
}
package algorithms;


import java.io.*;
import java.util.*;

import dataStructure.*;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author Noa Yair and Oriya Kronfeld
 *
 */
public class Graph_Algo implements graph_algorithms{
	private graph graph;

	public Graph_Algo(graph g){
		init(g);
	}

	@Override
	public void init(graph g) {
		this.graph = g;
	}

	@Override
	public void init(String file_name) throws FileNotFoundException {
		try
		{
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			graph = (DGraph)in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized");
		}
		catch(IOException ex)
		{
			System.out.println("IOException is caught");
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println("ClassNotFoundException is caught");
		}
	}

	@Override
	public void save(String file_name) {
		try
		{
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(this.graph);

			out.close();
			file.close();

			System.out.println("Object has been serialized");
			System.out.println(graph);
		}

		catch(IOException ex)
		{
			System.out.println("IOException is caught");
		}
	}

	@Override
	public boolean isConnected() {
		Queue<node_data> queue = new LinkedList<>();
		for(node_data n : graph.getV()){
			while (!graph.getE(n.getKey()).isEmpty()){
				n.setTag(1);
				queue.add(n);
				for (edge_data e : graph.getE(n.getKey())){
					queue.add(graph.getNode(e.getDest()));
					graph.getNode(e.getDest()).setTag(1);
				}
				queue.remove();
				n = queue.peek();
			}
			for (node_data node : graph.getV()){
				if(node.getTag() == 0){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if (src == dest){
			return 0;
		}
		for (node_data n : graph.getV()){ //update all the nodes weight to infinity
			n.setWeight(10000);
		}
		graph.getNode(src).setWeight(0); //update the src weight to 0
		int counter = 0;
		node_data node = new NodeData();
		while(counter < graph.nodeSize()){
			node = find_min(graph);
			node.setTag(1);
			cost_way(node);
			counter++;
			if(node.getKey() == dest){
				clear(this.graph);
				return node.getWeight();
			}
		}
		clear(this.graph);
		return node.getWeight();
	}

	public node_data find_min(graph g){ //find the node with the minimum weight
		node_data ans = new NodeData();
		ans.setWeight(10000);
		for(node_data n : g.getV()){
			if(n.getWeight() < ans.getWeight() && n.getTag() == 0){
				ans = n;
			}
		}
		return ans;
	}

	public void cost_way(node_data node){//calculates the weight of the node and updates so that it is minimal
		double w = 0;
		for(edge_data e : graph.getE(node.getKey())){
			w = node.getWeight() + e.getWeight();
			if (w < graph.getNode(e.getDest()).getWeight()) {
				graph.getNode(e.getDest()).setWeight(w);
				graph.getNode(e.getDest()).setInfo(Integer.toString(node.getKey()));
			}
		}
	}

	public void clear(graph g){
		for(node_data n : g.getV()){
			n.setTag(0);
		}
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		LinkedList<node_data> list = new LinkedList<node_data>();
		Stack<node_data> stack = new Stack<node_data>();
		shortestPathDist(src , dest);
		node_data node = graph.getNode(dest);
		stack.push(node);
		int key = 0;
		while(node.getKey() != src){
			key = Integer.parseInt(node.getInfo());
			node = graph.getNode(key);
			stack.push(node);
		}
		while (!stack.isEmpty()){
			list.add(stack.pop());
		}
		return list;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public graph copy() {
		return null;
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
		graph.connect(1 , 2 , 4);
		graph.connect(1 , 3 , 3);
		graph.connect(1 , 5 , 20);
		graph.connect(3 , 2 , 6);
		graph.connect(2 , 4 , 5);
		graph.connect(4 , 6 , 2);
		graph.connect(6 , 7 , 3);
		graph.connect(7 , 5 , 5);
		graph.connect(3 , 4 , 11);
		graph.connect(4 , 5 , 10);
		graph.connect(5 , 3 , 8);
		double d = g.shortestPathDist(1,7);
		System.out.println(d);
		LinkedList<node_data> list = new LinkedList<>();
		list = (LinkedList<node_data>) g.shortestPath(1,7);
		System.out.println(list);
		boolean b = g.isConnected();
		System.out.println(b);
	}

}

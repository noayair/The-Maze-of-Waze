# Ex2
The project written by:

Noa Yair - 313431702

Oriya Kronfeld - 314078585

Our project enable the user to build Graph , and run algorithms (for example, check the short path from point to point, check if the graph is connected) ,and draw them on the screen.
We defined it so that every node holds a hash-map of edge that coming out of it.


The project has 4 main packages: 

**dataStructure** - the package contain classes named NodeData (to craet node), EdgeData(to creat edge), DGraph(to craet graph).

**algorithms** - The package contains class named graph_algo. this class contains functions that running on the graph: write and read the graph to or from file, find the shortest way between 2 points and return the 'cost', find the shortest way between 2 points and return the way, chek if the graph is connect and computes a relatively short path which visit each node in the targets List.

**gui** -  The package contains class named Graph_GUI. this class can show graph and algorithms actions on it and draw them on the screen.

**utils** - Structured classes of Java. (Point3D, Range, StdDrow). We used StdDrow to draw graph on the screen.

![גרף](https://user-images.githubusercontent.com/57597109/71783613-d0819b00-2ff1-11ea-9c29-b5c54e6b9586.jpeg)


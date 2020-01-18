# Ex3
The project written by:

Noa Yair - 313431702

Oriya Kronfeld - 314078585

we will create a game in which a group of robots (with a size of 1-5 robots) has to perform fruit picking tasks on a graph. Given a weighted graph on its sides there is a collection of fruits. We want to place them, and plan their movement in real time so that at the end of the mission (30 or 60 seconds) the total points that all robots collect together will be maximum. The more fruits you eat, the more points you get.

There are two options for the game-
 One by server is an automatic game.
 Two by a manual game where the user can move the robot to fruit and get points.


The project has 6 main packages:

**dataStructure** - the package contain classes named NodeData (to craet node), EdgeData(to creat edge), DGraph(to craet graph).

**algorithms** - The package contains class named graph_algo. this class contains functions that running on the graph: write and read the graph to or from file, find the shortest way between 2 points and return the 'cost', find the shortest way between 2 points and return the way, chek if the graph is connect and computes a relatively short path which visit each node in the targets List.

**elements**- The package contains class named frutis (The points of the game), robot- (The players in the game).

**gameClient**- The package contains class named MyGameAlgo- The automatic play , MyGameGUI- Manual game.
How to use them, and the algorithms that run it.

**gui** - The package contains class named Graph_GUI. this class can show graph and algorithms actions on it and draw them on the screen.

**utils** - Structured classes of Java. (Point3D, Range, StdDrow). We used StdDrow to draw graph on the screen.


![game](https://user-images.githubusercontent.com/58184656/72669345-439b0080-3a39-11ea-9146-32725eb3697e.png)

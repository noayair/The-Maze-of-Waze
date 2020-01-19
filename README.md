# Ex3
The project written by:

Noa Yair - 313431702

Oriya Kronfeld - 314078585

we will create a game in which a group of robots (with a size of 1-5 robots) has to perform fruit picking tasks on a graph. Given a weighted graph on its sides there is a collection of fruits. We want to place them, and plan their movement in real time so that at the end of the mission (30 or 60 seconds) the total points that all robots collect together will be maximum. The more fruits you eat, the more points you get.

There are two options for the game-
 One by server is an automatic game.
 Two by a manual game where the user can move the robot to fruit and get points.

**the progress of the game** - The user opens the game and chooses a manual game type or automatic game with the server. Then he chooses a level, and the game begins. He also manually selects the location of the robots and then starts playing. To move the robot to the next vertex, the user must click one of the vertices adjacent to it. Only so the robot can move Each game has a set time that appears on screen. At the end of the game the user sees the number of points he has received.

**KML**- KML file defines various properties like location, image, polygon, 3D model, texture, description, etc. Mark. The position must have latitude and longitude coordinates. Other parameters determine the camera display, including tilt, orientation and height. Because this is a XML file type, so here's the case, the parameter IDs can be entered exactly. The user can create KML files to point locations, we make an option to make that file from the game, and locate the game in Google Earth.

The project has 6 main packages:

**dataStructure** - the package contain classes named NodeData (to craet node), EdgeData(to creat edge), DGraph(to craet graph).

**algorithms** - The package contains class named graph_algo. this class contains functions that running on the graph: write and read the graph to or from file, find the shortest way between 2 points and return the 'cost', find the shortest way between 2 points and return the way, chek if the graph is connect and computes a relatively short path which visit each node in the targets List.

**elements**- The package contains class named frutis (The points of the game), robot- (The players in the game).

**gameClient**- The package contains class named MyGameAlgo- The automatic play , MyGameGUI- Manual game.
How to use them, and the algorithms that run it.

**gui** - The package contains class named Graph_GUI. this class can show graph and algorithms actions on it and draw them on the screen.

**utils** - Structured classes of Java. (Point3D, Range, StdDrow). We used StdDrow to draw graph on the screen.


![game](https://user-images.githubusercontent.com/58184656/72669345-439b0080-3a39-11ea-9146-32725eb3697e.png)

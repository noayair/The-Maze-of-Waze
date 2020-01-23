# The Maze of Waze
The project written by:

Noa Yair - 313431702

Oriya Kronfeld - 314078585

we will create a game in which a group of robots (with a size of 1-5 robots) has to perform fruit picking tasks on a graph. Given a weighted graph on its sides there is a collection of fruits. We want to place them, and plan their movement in real time so that at the end of the mission (30 or 60 seconds) the total points that all robots collect together will be maximum. The more fruits you eat, the more points you get.

There are several options for the game -
One to play in server of other players. Succeed in levels and make a comparison against their results.

Second play by server is an automatic game or by manual play where the user can move the robot to fruit and get points.


**the progress of the game** - 
In the manual game the user need to move the robot to the next node, the user must click one of the vertices adjacent to it. Only so the robot can move Each game has a set time that appears on screen. At the end of the game the user sees the number of points he has received. 
At the end of the game the user can create KML files to point locations, we make an option to make that file from the game, and locate the game in Google Earth.

The project has 6 main packages:

**dataStructure** - the package contain classes named NodeData (to craet node), EdgeData(to creat edge), DGraph(to craet graph).

**algorithms** - The package contains class named graph_algo. this class contains functions that running on the graph: write and read the graph to or from file, find the shortest way between 2 points and return the 'cost', find the shortest way between 2 points and return the way, chek if the graph is connect and computes a relatively short path which visit each node in the targets List.

**elements**- The package contains class named frutis (The points of the game), robot- (The players in the game).

**gameClient**- The package contains class named MyGameAlgo- The automatic play , MyGameGUI- Manual game.
How to use them, and the algorithms that run it.

**gui** - The package contains class named Graph_GUI. this class can show graph and algorithms actions on it and draw them on the screen.

**utils** - Structured classes of Java. (Point3D, Range, StdDrow). We used StdDrow to draw graph on the screen.

**How to play?**

In the first stage the user chooses if to connect to the server and compare his results with other players.

![1](https://user-images.githubusercontent.com/57597109/72924708-106ab100-3d5a-11ea-9601-bcf57be95bc5.png)

If he clicks yes, he will write his ID.  
![2](https://user-images.githubusercontent.com/57597109/72924715-11034780-3d5a-11ea-8276-4d40f3068421.png)

The user has to choose a level where he wants to play between 0-23. (if playing with the server, he will have to choose a level that is open to him.If he has not yet passed the level he is in, the next level will not open to him).

![3](https://user-images.githubusercontent.com/57597109/72924714-11034780-3d5a-11ea-8538-8c9ba3b11295.png)

The user has to choose if to play a manual game or an automatic game.
![4](https://user-images.githubusercontent.com/57597109/72924711-106ab100-3d5a-11ea-84eb-515a7889fe90.png)

If the user chooses a manual game, he or she must choose which robot to place at the beginning of the game (if there is more than one robot to place them all).

**Tip** - The user should choose a node of a edge that has fruit.

![5](https://user-images.githubusercontent.com/57597109/72924709-106ab100-3d5a-11ea-8173-43fd4c2aac63.png)

**excellent! now you can start to play**
![game](https://user-images.githubusercontent.com/58184656/72669345-439b0080-3a39-11ea-9146-32725eb3697e.png)

At the end of the game the user can save the game to the KML files.

![6](https://user-images.githubusercontent.com/57597109/72929744-16b15b00-3d63-11ea-88eb-a61fd598ec1c.png)

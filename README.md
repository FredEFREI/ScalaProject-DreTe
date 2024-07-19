# ScalaProject-DreTe

This project focus on Graph creation and manipulation. The main purpose of this project is to learn and pratice the scala langage with the ZIO and scalatest libraries.

## Setup

Install sbt \
Run sbt in a terminal at the root of the project

To run the testsuites, execute the command ```test```.\
To run the App, execute the command ```project App``` then execute ```run```.

## Design decisions

For the Graph implementation: 
- **2 traits**: one for the the graphs with a Weight and one for the graphe without the Weight
    - In the definition of the argument, in ```edges``` each tuples representing a edge will be accompagned by the weight if the graph is weighted.

- **2 classes per traits**: On for the directed graphes and one for the non-directed graphes.
    - For each type of graph (Directed or Non-Directed) implement the function ```getShortestPath()``` with their respective algorithm (```dijkstra()``` for Non-Directed Graphes and  ```floyd()``` for Directed Graphes)

For the ZIO Application:
- **Using Directed and Weighted graphes**: We have established that the more interesting graphes to manipulate are the directed and weighted graphes. The ZIO application only use the class ```GraphDirectedWeighted```.
- **Using type Int**: For the same reason of simplicity, the ZIO Application does not implement the genericity of the graphes. Every data should be an Int.

## Usage Example

On the ZIO Application, a generic graph will be created. The users have multiple choice from there:
- **Create/Load a graph** : Load a Graph from a .json from the given path. Do nothing if the file is not found or if the load is unsuccessfull.
- **Save graph** : Save a graph in a .json to the given path.
- **Modify/Display graph** : Open a new menu.
    - **Display**: display the Graph as a .dot String. <span style="color:green">(Can be copy/paste to an graphviz viewers of your choice)</span>
    - **Clear**: Created a new empty Graph
    - **New node**: Add a node to the Graph
    - **New edge**: Add a edge from A to B, with a weight. If A or B does not exist, create them.
    - **Remove node**: Remove a Node. Remove all edges containing this node.
    - **Remove edge**: Remove a edge from A to B.
    - **Find shortest path**: Display the list of node for the shortest path and the total distance of that path.
    - **Detect cycle**: Display is the graph has a cycle or not.
    - **Topological sort**: Display the topological sort of the graph.
    - **Back**: Return to the previous menu.
- **Exit** : Leave the Application <span style="color:red">(WARNING : it also close sbt)</span>

## Team

Frédéric Casier: Graph implementation \
Mattis Desvilles: ZIO Application + Project Setup\
Julien de Laharpe: Testsuites implementation
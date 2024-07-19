import lib.*
// Value of node is generic

val test: GraphDirectedWeighted[Int] = GraphDirectedWeighted[Int](List(1, 2, 3), List(((1, 2), 1),((3, 2), 3)))

test.toString

test.getEdges
test.getNodes

val test2 = test.addNode(5).addEdge(1, 3, 4).addEdge(4, 3, 8)
val test3 = test2.addNode(10).addEdge(4, 5, 5).addEdge(5, 2, 2)
val test4 = test3.removeEdge(1, 2).removeEdge(1, 5).addNode(4)
val test5 = test4.addEdge(1, 6, 9).addEdge(7, 9, 7)
val test6 = test5.removeNode(9)


import zio.json._
implicit val encoder: JsonEncoder[GraphDirectedWeighted[Int]] = DeriveJsonEncoder.gen[GraphDirectedWeighted[Int]]

val test7: GraphDirectedWeighted[Int] = GraphDirectedWeighted[Int](List(),List())

val test8 = test7.addNode(3)
val test9 = test8.addNode(3)

test6.addEdge(5,4,7).cycleDetection()

test7.toDot
test7.dfs(1)

test6.toJson
test6.dfs(1)
test6.bfs(1)
test6.toDot
test6.topologicalSort()
test6.cycleDetection()
test6.addEdge(6, 1, 3).cycleDetection()

test6.getShortestPath(4, 2)
test6.getShortestPath(5, 2)


test6.getNeighbours(3)
test6.getNeighbours(1)
test6.getPrevious(3)
test6.getPrevious(1)
test6.getPrevNeigh(3)

val graph = GraphDirectedWeighted[Int](List(2, 4), List(((2, 4), 6)))
val graph_edge = graph.removeEdge(2, 4)

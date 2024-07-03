import lib.*
// Value of node is generic

val test: GraphDirected[Int] = GraphDirected[Int](List(1, 2, 3), List((1, 2),(3, 2)))

test.toString

test.getEdges
test.getNodes

val test2 = test.addNode(5).addEdge(1, 3).addEdge(4, 3)
val test3 = test2.addNode(10).addEdge(4, 5).addEdge(5, 2)

val test4 = test3.removeEdge(1, 2).removeEdge(1, 5)

val test5 = test4.addEdge(1, 6).addEdge(7, 9)

val test6 = test5.removeNode(9)

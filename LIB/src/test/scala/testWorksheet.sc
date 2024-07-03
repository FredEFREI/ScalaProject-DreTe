import lib.*
// Value of node is generic

val test: MyGraph[Int] = GraphDirected[Int](List(1, 2, 3), List((1, 2),(3, 2)))

test.toString
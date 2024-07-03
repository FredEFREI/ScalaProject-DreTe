// Value of node is generic
trait Graph[A]:
  def nodes: List[A]
  def edges: List[(Option[Int], (A , A))]

case class GraphDirected[A](nodes: List[A], edges: List[(Option[Int], (A , A))]) extends Graph[A]

end GraphDirected

val test: Graph[Int] = GraphDirected[Int](List(1, 2, 3), List((Option(1), (1, 2)),(Option(10), (3, 2))))

test.toString
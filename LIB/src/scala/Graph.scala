// Value of node is generic
trait Graph[A]:
  def nodes: List[A]
  def edges: List[(Option[Int], (A , A))]

case class GraphDirected[A](nodes: List[A], edges: List[(Option[Int], (A , A))]) extends Graph[A]

end GraphDirected


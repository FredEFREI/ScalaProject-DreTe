package lib

// Value of node is generic
trait MyGraph[A]:
  def nodes: List[A]
  def edges: List[(A, A)]

case class GraphDirected[A](nodes: List[A], edges: List[(A , A)]) extends MyGraph[A]:
  def getNodes:List[A] = nodes
  def getEdges:List[(A, A)] = edges
  def getNeighbours(node: A):List[A] =
    for {
        edge <- edges
        (from , to) = edge
        if from == node
      } yield to
end GraphDirected


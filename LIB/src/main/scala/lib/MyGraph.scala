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

  def addNode(toAdd: A): GraphDirected[A] =
    GraphDirected(this.nodes.appended(toAdd), this.edges)
  def removeNode(toRemove: A): GraphDirected[A] =
    // remove all edges with said node
    GraphDirected(this.nodes.filterNot(node => node == toRemove), this.edges)

  def addEdge(from: A, to: A):GraphDirected[A] =
    val updatedNode: List[A] = nodes ++ (if !this.nodes.contains(from) then List(from) else Nil) ++ (if !this.nodes.contains(to) then List(to) else Nil)
    GraphDirected(updatedNode, this.edges ++ List(from -> to))

  def removeEdge(from: A, to: A): GraphDirected[A] =
    GraphDirected(this.nodes, this.edges.filterNot(edge => edge == (from -> to)))
end GraphDirected


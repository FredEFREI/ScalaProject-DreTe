package lib

import scala.annotation.tailrec

// Value of node is generic
trait MyGraph[A]:
  def nodes: List[A]
  def edges: List[(A, A)]

  def getNodes: List[A] = nodes
  def getEdges: List[(A, A)] = edges

  def getNeighbours(node: A): List[A]
  def getPrevious(node: A): List[A]

  def addNode(toAdd: A): MyGraph[A]
  def removeNode(toRemove: A): MyGraph[A]
  def addEdge(from: A, to: A): MyGraph[A]
  def removeEdge(from: A, to: A): MyGraph[A]
  def dfs(node: A): List[A]
  def bfs(node: A): List[List[A]]
  def topologicalSort(): List[A]

  def toDot: String

case class GraphDirected[A](nodes: List[A], edges: List[(A , A)]) extends MyGraph[A]:

  def getPrevious(node: A):List[A] =
    for {
      edge <- edges
      (from, to) = edge
      if to == node && from != node
    } yield from

  def getNeighbours(node: A):List[A] =
    for {
        edge <- edges
        (from , to) = edge
        if from == node && to != node
      } yield to

  def addNode(toAdd: A): GraphDirected[A] =
    if nodes.contains(toAdd) then this else GraphDirected(this.nodes.appended(toAdd), this.edges)

  def removeNode(toRemove: A): GraphDirected[A] =
    val updatedEdges:List[(A, A)] = for {
    edge <- edges
    (from, to) = edge
    if !(from == toRemove || to == toRemove)
  } yield edge

    GraphDirected(this.nodes.filterNot(node => node == toRemove), updatedEdges)

  def addEdge(from: A, to: A):GraphDirected[A] =
    val updatedNode: List[A] = nodes ++ (if !this.nodes.contains(from) then List(from) else Nil) ++ (if !this.nodes.contains(to) then List(to) else Nil)
    GraphDirected(updatedNode, this.edges ++ List(from -> to))

  def removeEdge(from: A, to: A): GraphDirected[A] =
    GraphDirected(this.nodes, this.edges.filterNot(edge => edge == (from -> to)))

  implicit def toDot: String =
    "digraph { " ++ nodes.map{a => s"$a "}.mkString ++ edges.map{case(a, b) => s"$a -> $b "}.mkString ++ "}"


  def dfs(node: A): List[A] = {
    def dfsHelper(current: A, visited: List[A]):List[A] =
      if (visited.contains(current)) visited
      else
        val adjacencyList:List[A] = this.getNeighbours(current) filterNot visited.contains
        adjacencyList.foldLeft(current :: visited)((b, a) => dfsHelper(a, b))

    dfsHelper(node, List()).reverse
  }

  def bfs(node: A): List[List[A]] = {
    @tailrec
    def bfsHelper(element: List[A], visited: List[List[A]]): List[List[A]] = {
      val adjacencyList = element.flatMap(node => this.getNeighbours(node)).filterNot(visited.flatten.contains).distinct
      if (adjacencyList.isEmpty)
        visited
      else
        bfsHelper(adjacencyList, adjacencyList::visited)
    }

    bfsHelper(List(node), List(List(node))).reverse
  }

  def topologicalSort(): List[A] ={
    @tailrec
    def topologicalSortHelper(froms: Map[A, Set[A]], done:List[A]): List[A] = {
      val (noFroms, hasFroms) = froms.partition( _._2.isEmpty)
      if (noFroms.isEmpty)
        if (hasFroms.isEmpty) done
        else
          Console.println("Error: " + hasFroms.toString())
          List()
      else
        val found = noFroms.keys
        topologicalSortHelper(hasFroms.map{case (k, v) => k -> (v -- found)}, done ++ found)
    }
     val froms = edges.foldLeft(Map[A, Set[A]]()) { (acc, e) =>
       acc + (e._1 -> acc.getOrElse(e._1, Set())) + (e._2 -> (acc.getOrElse(e._2, Set()) + e._1))
     }
     val isolated = for {
       node <- nodes
       if this.getNeighbours(node).isEmpty && this.getPrevious(node).isEmpty
     } yield node
     isolated ++ topologicalSortHelper(froms, List[A]())
  }

  

end GraphDirected


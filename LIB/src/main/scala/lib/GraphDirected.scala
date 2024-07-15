package lib

import scala.annotation.tailrec

case class GraphDirected[A](nodes: List[A], edges: List[(A , A)]) extends MyGraph[A]:

  override def getPrevious(node: A):List[A] =
    for {
      edge <- edges
      (from, to) = edge
      if to == node
    } yield from

  override def getNeighbours(node: A):List[A] =
    for {
      edge <- edges
      (from , to) = edge
      if from == node
    } yield to

  override def getPrevNeigh(node: A): List[A] =
    getPrevious(node) ++ getNeighbours(node)

  override def addNode(toAdd: A): GraphDirected[A] =
    if nodes.contains(toAdd) then this else GraphDirected(this.nodes.appended(toAdd), this.edges)

  override def removeNode(toRemove: A): GraphDirected[A] =
    val updatedEdges:List[(A, A)] = for {
      edge <- edges
      (from, to) = edge
      if !(from == toRemove || to == toRemove)
    } yield edge

    GraphDirected(this.nodes.filterNot(node => node == toRemove), updatedEdges)

  override def addEdge(from: A, to: A):GraphDirected[A] =
    val updatedNode: List[A] = nodes ++ (if !this.nodes.contains(from) then List(from) else Nil) ++ (if !this.nodes.contains(to) then List(to) else Nil)
    GraphDirected(updatedNode, this.edges ++ List(from -> to))

  override def removeEdge(from: A, to: A): GraphDirected[A] =
    GraphDirected(this.nodes, this.edges.filterNot(edge => edge == (from -> to)))

  override implicit def toDot: String =
    "digraph { " ++ nodes.map{a => s"$a "}.mkString ++ edges.map{case(a, b) => s"$a -> $b "}.mkString ++ "}"


  override def dfs(node: A): List[A] = {
    def dfsHelper(current: A, visited: List[A]):List[A] =
      if (visited.contains(current)) visited
      else
        val adjacencyList:List[A] = this.getNeighbours(current) filterNot visited.contains
        adjacencyList.foldLeft(current :: visited)((b, a) => dfsHelper(a, b))

    dfsHelper(node, List()).reverse
  }

  override def bfs(node: A): List[List[A]] = {
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

  override def topologicalSort(): List[A] ={
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
      if this.getPrevNeigh(node).isEmpty
    } yield node
    isolated ++ topologicalSortHelper(froms, List[A]())
  }

  override def cycleDetection(): Boolean = {

    def dfsCycleHelper(node: A, visited: Set[A], recursiveStack: Set[A]): Boolean =
      val updatedVisited = visited + node
      val updatedStack = recursiveStack + node

      this.getNeighbours(node).exists { neighbour =>
        (!updatedVisited.contains(neighbour) && dfsCycleHelper(neighbour, updatedVisited, updatedStack)) ||
          updatedStack.contains(neighbour) ||
          neighbour == node
      }

    def hasBidirectionalEdge: Boolean =
      nodes.exists { node =>
        this.getNeighbours(node).exists { neighbour =>
          this.getNeighbours(neighbour).contains(node)
        }
      }

    val firstNodes = for {
      node <- nodes
      if this.getPrevious(node).isEmpty
    } yield node

    hasBidirectionalEdge || firstNodes.exists(node => dfsCycleHelper(node, Set(), Set()))
  }

  def floyd(): (Map[(A, A), Int], Map[(A, A), Option[A]])= {

    def updateDistPred(dist: Map[(A, A), Int], pred: Map[(A, A), Option[A]], nodeK: A, nodeI: A, nodeJ: A): (Map[(A, A), Int], Map[(A, A), Option[A]]) = {
      val currentDist = dist((nodeI, nodeK)) + dist((nodeK, nodeJ))
      if (dist((nodeI, nodeJ)) > currentDist) {
        val updatedDist = dist.updated((nodeI, nodeJ), currentDist)
        val updatedPred = pred.updated((nodeI, nodeJ), Some(nodeK))
        (updatedDist, updatedPred)
      } else {
        (dist, pred)
      }
    }

    @tailrec
    def floydHelper(dist: Map[(A, A), Int], pred: Map[(A, A), Option[A]], nodesCurrent: List[A]): (Map[(A, A), Int], Map[(A, A), Option[A]]) =
      nodesCurrent match {
      case nodeK :: restNodes =>
        val (updatedDist, updatedPred) = (for {
          nodeI <- nodes
          nodeJ <- nodes
        } yield (nodeI, nodeJ)).foldLeft((dist, pred)) {
          case ((currentDist, currentPred), (nodeI, nodeJ)) =>
            updateDistPred(currentDist, currentPred, nodeK, nodeI, nodeJ)
        }
        floydHelper(updatedDist, updatedPred, restNodes)
      case Nil => (dist, pred)
    }

    val initialDist: Map[(A, A), Int] = (for {
      edge <- edges
      (a, b) = edge
    } yield (edge, if (a == b) 0 else 1)).toMap.withDefaultValue(999)
    

    val initialPred: Map[(A, A), Option[A]] = (for {
      a <- nodes
      b <- nodes
    } yield ((a, b), if (a == b || edges.contains((a, b))) Some(a) else None)).toMap

    floydHelper(initialDist, initialPred, nodes)

  }
  override def getShortestPath(from: A, to: A): (List[A], Int) = {
    def getPath(pred: Map[(A, A), Option[A]], from: A, to: A): List[A] = {
      @tailrec
      def getPathHelper(path: List[A], current: A): List[A] = pred((from, current)) match {
        case Some(prev) if prev != from => getPathHelper(prev :: path, prev)
        case Some(prev) =>  prev :: path
        case None => path
      }

      if (pred((from, to)).isEmpty)
        List.empty
      else
        getPathHelper(List(to), to)
    }

    val (dist, pred) = floyd()

    (getPath(pred, from, to), dist((from, to)))
  }
end GraphDirected
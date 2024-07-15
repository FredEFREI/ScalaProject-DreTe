package lib

import scala.annotation.tailrec

case class GraphNonDirected[A](nodes: List[A], edges: List[(A , A)]) extends MyGraph[A]:
  override def getPrevious(node: A): List[A] =
    for {
      edge <- edges
      (from, to) = edge
      if to == node
    } yield from

  override def getNeighbours(node: A): List[A] =
    for {
      edge <- edges
      (from, to) = edge
      if from == node
    } yield to

  override def getPrevNeigh(node: A): List[A] =
    getPrevious(node) ++ getNeighbours(node)

  override def addNode(toAdd: A): GraphNonDirected[A] =
    if nodes.contains(toAdd) then this else GraphNonDirected(this.nodes.appended(toAdd), this.edges)

  override def removeNode(toRemove: A): GraphNonDirected[A] =
    val updatedEdges: List[(A, A)] = for {
      edge <- edges
      (from, to) = edge
      if !(from == toRemove || to == toRemove)
    } yield edge

    GraphNonDirected(this.nodes.filterNot(node => node == toRemove), updatedEdges)

  override def addEdge(from: A, to: A): GraphNonDirected[A] =
    val updatedNode: List[A] = nodes ++ (if !this.nodes.contains(from) then List(from) else Nil) ++ (if !this.nodes.contains(to) then List(to) else Nil)
    GraphNonDirected(updatedNode, this.edges ++ List(from -> to))

  override def removeEdge(from: A, to: A): GraphNonDirected[A] =
    GraphNonDirected(this.nodes, this.edges.filterNot(edge => edge == (from -> to)))

  override implicit def toDot: String =
    "graph { " ++ nodes.map { a => s"$a " }.mkString ++ edges.map { case (a, b) => s"$a -- $b " }.mkString ++ "}"

  override def dfs(node: A): List[A] = {
    def dfsHelper(current: A, visited: List[A]):List[A] =
      if (visited.contains(current)) visited
      else
        val adjacencyList:List[A] = this.getPrevNeigh(current) filterNot visited.contains
        adjacencyList.foldLeft(current :: visited)((b, a) => dfsHelper(a, b))

    dfsHelper(node, List()).reverse
  }

  override def bfs(node: A): List[List[A]] = {
    @tailrec
    def bfsHelper(element: List[A], visited: List[List[A]]): List[List[A]] = {
      val adjacencyList = element.flatMap(node => this.getPrevNeigh(node)).filterNot(visited.flatten.contains).distinct
      if (adjacencyList.isEmpty)
        visited
      else
        bfsHelper(adjacencyList, adjacencyList :: visited)
    }
    bfsHelper(List(node), List(List(node))).reverse
  }

  override def topologicalSort(): List[A] = {
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
    def dfsCycleHelper(node: A, parent: Option[A], visited: Set[A]): Boolean = {
      val updatedVisited = visited + node

      this.getPrevNeigh(node).exists { neighbour =>
        if (!updatedVisited.contains(neighbour)) {
          dfsCycleHelper(neighbour, Some(node), updatedVisited)
        } else {
          parent.isEmpty || neighbour != parent.get
        }
      }
    }
    nodes.exists(node => dfsCycleHelper(node, None, Set()))
  }

  def dijkstra(from: A, to: A): Option[(List[A], Int)] = {
    val initialDistances = nodes.map(node => node -> Int.MaxValue).toMap + (from -> 0)
    val initialPredecessors = nodes.map(node => node -> None).toMap

    @tailrec
    def dijkstraHelper(unvisited: Set[A], distances: Map[A, Int], pred: Map[A, Option[A]]): (Map[A, Int], Map[A, Option[A]]) = {
      if (unvisited.isEmpty || !unvisited.contains(to))
        (distances, pred)
      else
        val (currentNode, currentDistance) = unvisited.map(node => node -> distances(node)).minBy(_._2)
        if (currentNode == to)
          (distances, pred)
        else
          val neighbors = this.getPrevNeigh(currentNode)
          val (newDistances, newPredecessors) = neighbors.foldLeft((distances, pred)) { case ((distancesAcc, predecessorsAcc), neighbor) =>
            val alternativePathDistance = currentDistance + 1
            if (alternativePathDistance < distancesAcc(neighbor))
              (distancesAcc + (neighbor -> alternativePathDistance), predecessorsAcc + (neighbor -> Some(currentNode)))
            else
              (distancesAcc, predecessorsAcc)
          }
          dijkstraHelper(unvisited - currentNode, newDistances, newPredecessors)
    }

    val (finalDistances, finalPredecessors) = dijkstraHelper(nodes.toSet, initialDistances, initialPredecessors)
    finalDistances.get(to).filter(_ < Int.MaxValue).map { distance =>
      @tailrec
      def reconstructPath(node: A, path: List[A] = List.empty): List[A] = {
        finalPredecessors(node) match {
          case Some(predecessor) => reconstructPath(predecessor, node :: path)
          case None => from :: path
        }
      }
      val path = reconstructPath(to)
      (path, distance)
    }
  }


  override def getShortestPath(from: A, to: A): (List[A], Int) =
    dijkstra(from, to) match
      case Some((path, distance)) => (path, distance)
      case None =>
        print(s"There is no path from $from to $to")
        (List(), 0)

end GraphNonDirected

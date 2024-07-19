package lib

import lib.*
import org.scalatest.flatspec
import org.scalatest.flatspec.AnyFlatSpec

class TestSuiteNonDirected extends AnyFlatSpec {
  behavior of "A Directed Weighted Graph"

  it should "be initialized" in {
    val graph = GraphNonDirected[Int](List(), List())
    assert(graph == GraphNonDirected[Int](List(), List()))
    assert(graph != null)
  }

  it should "get the list of nodes" in {
    val graph = GraphNonDirected[Int](List(2, 6, 7), List())
    assert(graph.getNodes == List(2, 6, 7))
    val graph1 = GraphNonDirected(List(), List())
    assert(graph1.getNodes == List())
  }

  it should "add an Node" in {
    val graph = GraphNonDirected[Int](List(), List())
    val graph_node = graph.addNode(2)
    assert(graph_node == GraphNonDirected[Int](List(2), List()))
    assert(graph_node.addNode(2) == GraphNonDirected[Int](List(2), List()))
    assert(graph_node.getNodes == List(2))
  }

  it should "remove an Node" in {
    val graph = GraphNonDirected[Int](List(2, 6, 7), List())
    val graph_node = graph.removeNode(2)
    assert(graph_node == GraphNonDirected[Int](List(6, 7), List()))
    assert(graph_node.getNodes == List(6, 7))
  }

  it should "get the list of Edges" in {
    val graph = GraphNonDirected[Int](List(2, 4), List((2, 4)))
    assert(graph.getEdges == List((2, 4)))
    val graph1 = GraphNonDirected(List(), List())
    assert(graph1.getEdges == List())
  }

  it should "add an Edge" in {
    val graph =  GraphNonDirected[Int](List(), List())
    val graph_edge = graph.addEdge(2, 4)
    assert(graph_edge == GraphNonDirected[Int](List(2, 4), List((2, 4))))
    assert(graph_edge.getNodes == List(2, 4))
    assert(graph_edge.getEdges == List((2, 4)))
  }

  it should "remove an Edge" in {
    val graph = GraphNonDirected[Int](List(2, 4), List((2, 4)))
    val graph_edge = graph.removeEdge(2, 4)
    assert(graph_edge.getEdges == List())
    assert(graph_edge == GraphNonDirected[Int](List(2, 4), List()))
  }

  it should "remove all edges linked to a removed node" in {
    val graph = GraphNonDirected[Int](List(2, 6, 7), List((2, 6)))
    val graph_node = graph.removeNode(2)
    assert(graph_node == GraphNonDirected[Int](List(6, 7), List()))
    assert(graph_node.getNodes == List(6, 7))
    assert(graph_node.getEdges == List())
  }

  it should "be able to get all nodes that can be accessed from a selected node" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.getNeighbours(3) == List(2))
    assert(graph.getNeighbours(1) == List(2, 3, 6))
    assert(graph.addNode(9).getNeighbours(9) == List())
    assert(graph.getNeighbours(10) == List())
    assert(graph.removeNode(2).getNeighbours(3) == List())
  }

  it should "be able to get all nodes that have an edge leading to a selected node" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.getPrevious(3) == List(1, 4))
    assert(graph.getPrevious(1) == List())
    assert(graph.getPrevious(5) == List(4))
  }

  it should "transform into a 'Dot' format" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7),List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.toDot == "graph { 1 2 3 5 4 10 6 7 1 -- 2 3 -- 2 1 -- 3 4 -- 3 4 -- 5 5 -- 2 1 -- 6 }")
    val graph2 = GraphNonDirected(List(), List())
    assert(graph2.toDot == "graph { }")
  }

  it should "compute the dfs algorithm" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.dfs(1) == List(1, 2, 3, 4, 5, 6))
    val graph2 = GraphNonDirected(List(1), List())
    assert(graph2.dfs(1) == List(1))
  }

  it should "compute the bfs algorithm" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.bfs(1) == List(List(1), List(2, 3, 6), List(5, 4)))
    val graph1 = GraphNonDirected(List(1), List())
    assert(graph1.bfs(1) == List(List(1)))
  }

  it should "sort nodes using topological sorting algorithm" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.topologicalSort() == List(10, 7, 1, 4, 5, 6, 3, 2))
    val graph1 = GraphNonDirected(List(),List())
    assert(graph1.topologicalSort() == List())
  }

  it should "detect if a graph has a cycle in it" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.cycleDetection())
    assert(graph.addEdge(5, 4).cycleDetection())
  }

  it should "be able to search the shortest path between two nodes" in {
    val graph = GraphNonDirected(List(1, 2, 3, 5, 4, 10, 6, 7), List((1, 2), (3, 2), (1, 3), (4, 3), (4, 5), (5, 2), (1, 6)))
    assert(graph.getShortestPath(4, 2) == (List(4, 3, 2),2))
    assert(graph.getShortestPath(5, 2) == (List(5, 2),1))
  }
}
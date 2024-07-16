import org.scalatest.flatspec
import org.scalatest.flatspec.AnyFlatSpec

import lib.*

class TestSuite extends AnyFlatSpec {
  behavior of "A Directed Weighted Graph"

  it should "be initialized" in {
    val graph = GraphDirectedWeighted[Int](List(), List())
    assert(graph == GraphDirectedWeighted[Int](List(), List()))
  }

  it should "add an Node" in {
    val graph = GraphDirectedWeighted[Int](List(), List())
    val graph_node = graph.addNode(2)
    assert(graph_node == GraphDirectedWeighted[Int](List(2), List()))
    assert(graph_node.getNodes == List(2))
  }

  it should "add an Edge" in {
    val graph =  GraphDirectedWeighted[Int](List(), List())
    val graph_edge = graph.addEdge(2, 4, 6)
    assert(graph_edge == GraphDirectedWeighted[Int](List(2, 4), List(((2, 4), 6))))
    assert(graph_edge.getNodes == List(2, 4))
    assert(graph_edge.getEdges == List(((2, 4), 6)))
  }

}



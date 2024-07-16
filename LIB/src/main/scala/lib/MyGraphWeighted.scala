package lib

import scala.annotation.tailrec

// Value of node is generic
trait MyGraphWeighted[A]:
  def nodes: List[A]
  def edges: List[((A, A), Int)]

  def getNodes: List[A] = nodes
  def getEdges: List[((A, A), Int)] = edges

  def getNeighbours(node: A): List[A]
  def getPrevious(node: A): List[A]
  def getPrevNeigh(node: A): List[A]

  def addNode(toAdd: A): MyGraphWeighted[A]
  def removeNode(toRemove: A): MyGraphWeighted[A]
  def addEdge(from: A, to: A, weight: Int): MyGraphWeighted[A]
  def removeEdge(from: A, to: A): MyGraphWeighted[A]

  def dfs(node: A): List[A]
  def bfs(node: A): List[List[A]]
  def topologicalSort(): List[A]
  def cycleDetection(): Boolean
  
  def getShortestPath(from: A, to: A): (List[A], Int)

  def toDot: String




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




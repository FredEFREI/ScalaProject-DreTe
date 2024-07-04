import lib.GraphDirected
import zio.*

import java.io.IOException
import scala.io.Source.*
import scala.util.{Try, Using}



object Main extends ZIOAppDefault:
  var graph: GraphDirected[Int] = GraphDirected[Int](List(1, 2, 3), List((1, 2),(3, 2)))
  private def loadFile: IO[IOException, String] =
    import zio.json._
    implicit val decoder: JsonDecoder[GraphDirected[Int]] = DeriveJsonDecoder.gen[GraphDirected[Int]]
    for
      fileName <- Console.readLine("Enter the path to the graph file:")
      _ <- Console.printLine("Loading graph from file...")

      lines <- {
        val source = fromFile(fileName)
        val content = source.mkString
        source.close()
        ZIO.succeed(content)
      }

      newGraph <- ZIO.succeed(lines.fromJson[GraphDirected[Int]])

      res  = newGraph match
        case Left(errorMessages) => errorMessages
        case Right(newGraph) =>
          graph = newGraph
          "Graph updated successfully"
    yield res

  private def addDirectedEdge: IO[IOException, String] =
    for {
      Source <- getUserInput("----------------------------------------------------\nEnter a source node name\n")
      Destination <- getUserInput("----------------------------------------------------\nEnter a destination node name\n")
      newGraph <- ZIO.succeed(graph.addEdge(Source.toInt,Destination.toInt))
    } yield {
      graph = newGraph
      s"Edge from $Source to $Destination added successfully"
    }

  private def removeDirectedEdge: IO[IOException, String] =
    for {
      Source <- getUserInput("----------------------------------------------------\nEnter a source node name\n")
      Destination <- getUserInput("----------------------------------------------------\nEnter a destination node name\n")
      newGraph <- ZIO.succeed(graph.removeEdge(Source.toInt,Destination.toInt))
    } yield {
      graph = newGraph
      s"Edge from $Source to $Destination removed successfully"
    }
  private def addDirectedNode: IO[IOException, String] =
    for {
      Node <- getUserInput("----------------------------------------------------\nEnter a node name\n")
      newGraph <- ZIO.succeed(graph.addNode(Node.toInt))
    } yield {
      graph = newGraph
      s"Node $Node added successfully"
    }
  private def removeDirectedNode: IO[IOException,String] =
    for {
      Node <- getUserInput("----------------------------------------------------\nEnter a node name\n")
      newGraph <- ZIO.succeed(graph.removeNode(Node.toInt))
    } yield {
      graph = newGraph
      s"Node $Node removed successfully"
    }
  private def mainMenu: IO[IOException, String] =
    for {
      Option <- getUserInput("----------------------------------------------------\nChoose an option:\n1: Create/Load a graph\n2: Graphs\n3: Exit\n")
      res <- Option match
        case "1" => loadFile <*> mainMenu //TODO
        case "2" => graphMenu <*> mainMenu
        case "3" => Console.printLine("Exiting...")
        case _ => Console.printLine("Invalid option. Please try again.") <*> mainMenu
    } yield Option

  private def graphMenu: IO[IOException, String] =
    for {
      graphOption <- getUserInput("----------------------------------------------------\nChoose an option:\n1: Diplay\n2: Create\n3: New node\n4: New edge\n5: Remove node\n6: Remove edge\n7: Back\n")
      res <- graphOption match
        case "1" => Console.printLine(this.graph.toString) <*> graphMenu //TODO
        case "2" => Console.printLine("Clear graph") <*> graphMenu //TODO
        case "3" => addDirectedNode <*> graphMenu
        case "4" => addDirectedEdge <*> graphMenu
        case "5" => removeDirectedNode <*> graphMenu
        case "6" => removeDirectedEdge <*> graphMenu
        case "7" => Console.printLine("")
        case _ => Console.printLine("Invalid option. Please try again.") <*> graphMenu
    } yield graphOption

  private def getUserInput(message: String): IO[IOException, String] =
    for {
      _ <- Console.printLine(message)
      input <- Console.readLine
    } yield input


  override def run: IO[IOException, Unit] =
    for {
      _ <- Console.printLine("Welcome to our graph app!")
      _ <- mainMenu
    } yield ()
/*
    */




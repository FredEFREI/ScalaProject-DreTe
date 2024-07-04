import lib.GraphDirected
import zio.*

import java.io.IOException
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}



object Main extends ZIOAppDefault:
  var graph: GraphDirected[Int] = GraphDirected[Int](List(1, 2, 3), List((1, 2),(3, 2)))
  private def loadFile: IO[IOException, String] =
    import zio.json._
    implicit val decoder: JsonDecoder[GraphDirected[Int]] = DeriveJsonDecoder.gen[GraphDirected[Int]]
    for {

      fileName: String <- Console.readLine("Enter the path to the graph file:")

      fileContent <- ZIO.fromEither(Using(Source.fromFile(name = fileName)) { s => s.getLines().mkString } match
        case Success(value) =>
          Right(value)
        case Failure(exception) =>
          Left(new IOException(exception))).fold(
        _ => "File load unsucessfull",
        content => content
      )



      result <- ZIO.fromEither(fileContent.fromJson[GraphDirected[Int]].left.map((e: String) => new IOException(e))).fold(
        _ => "File load unsucessfull",
        newGraph =>
          graph = newGraph
          "Graph loaded successfully"
      )
      _ <- Console.printLine(graph.toString)
      _ <- Console.printLine(result)


    } yield result

  private def saveFile: IO[IOException, String] =
    for {
      fileName: String <- Console.readLine("Enter the path to the graph file:")

    //TODO Implement file saving

    } yield fileName

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
      Option <- getUserInput("----------------------------------------------------\nChoose an option:\n1: Create/Load a graph\n2: Save graph\n3: Modify/Display graph\n4: Exit\n")
      res <- Option match
        case "1" => loadFile <*> mainMenu
        case "2" => graphMenu <*> mainMenu
        case "3" => graphMenu <*> mainMenu
        case "4" => Console.printLine("Exiting...")
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




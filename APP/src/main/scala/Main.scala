import lib.*
import zio.*

import java.io.IOException
import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}



object Main extends ZIOAppDefault:
  private var graph: GraphDirectedWeighted[Int] = GraphDirectedWeighted[Int](List(1, 2, 3), List(((1, 2), 1),((3, 2), 3)))
  private def loadFile: IO[IOException, String] =
    import zio.json._
    implicit val decoder: JsonDecoder[GraphDirectedWeighted[Int]] = DeriveJsonDecoder.gen[GraphDirectedWeighted[Int]]
    for {

      fileName: String <- Console.readLine("Enter the path to the graph file:")

      fileContent <- ZIO.fromEither(Using(Source.fromFile(name = fileName)) { s => s.getLines().mkString } match
        case Success(value) =>
          Right(value)
        case Failure(exception) =>
          Left(new IOException(exception))).fold(
        _ => "File load unsuccessful",
        content => content
      )



      result <- ZIO.fromEither(fileContent.fromJson[GraphDirectedWeighted[Int]].left.map((e: String) => new IOException(e))).fold(
        _ => "File load unsuccessful",
        newGraph =>
          graph = newGraph
          "Graph loaded successfully"
      )
      _ <- Console.printLine(graph.toString)
      _ <- Console.printLine(result)


    } yield result

  private def saveFile: IO[IOException, String] =
    import zio.json._
    implicit val encoder: JsonEncoder[GraphDirectedWeighted[Int]] = DeriveJsonEncoder.gen[GraphDirectedWeighted[Int]]
    for {
      fileName: String <- Console.readLine("Enter the path to the graph file:")
      _ <- ZIO.succeed(Files.write(Paths.get(fileName), graph.toJson.getBytes(StandardCharsets.UTF_8)))

      _ <- Console.printLine("Graph saved successfully")
    } yield fileName

  private def addDirectedEdge: IO[IOException, String] =
    for {
      Source <- getUserInput("----------------------------------------------------\nEnter a source node name\n")
      Destination <- getUserInput("----------------------------------------------------\nEnter a destination node name\n")
      weight <- getUserInput("----------------------------------------------------\nEnter a edge weight\n")
      newGraph <- ZIO.succeed(graph.addEdge(Source.toInt,Destination.toInt,weight.toInt))
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

  private def findShortestPath: IO[IOException, String] =
    for {
      source <- getUserInput("----------------------------------------------------\nEnter a source node name\n")
      destination <- getUserInput("----------------------------------------------------\nEnter a destination node name\n")
      res <- ZIO.succeed(graph.getShortestPath(source.toInt, destination.toInt))
      path = res._1
      weight = res._2
    } yield {
      if weight==99999 : Boolean
        then
        s"No path available"
      else
        s"Result:\nPath: ${path.mkString(" -> ")}\nWeight: $weight"
      end if

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
      _ <- Option match
        case "1" => loadFile <*> mainMenu
        case "2" => saveFile <*> mainMenu
        case "3" => graphMenu <*> mainMenu
        case "4" => Console.printLine("Exiting...")
        case _ => Console.printLine("Invalid option. Please try again.") <*> mainMenu
    } yield Option

  private def graphMenu: IO[IOException, String] =
    for {
      graphOption <- getUserInput("----------------------------------------------------\nChoose an option:\n 1: Display\n 2: Clear\n 3: New node\n 4: New edge\n 5: Remove node\n 6: Remove edge\n 7: Find shortest path\n 8: Detect cycle\n 9: Topological sort\n10: Back\n")
      _ <- graphOption match
        case "1" => Console.printLine("Copy the following line and input it in a Graphviz viewer of your choice\n" ++ this.graph.toDot) <*> graphMenu //TODO
        case "2" => Console.printLine("Clear graph") <*> graphMenu
        case "3" => addDirectedNode <*> graphMenu
        case "4" => addDirectedEdge <*> graphMenu
        case "5" => removeDirectedNode <*> graphMenu
        case "6" => removeDirectedEdge <*> graphMenu
        case "7" => findShortestPath.flatMap { res =>
          Console.printLine(res) <*> graphMenu
        }
        case "8" => Console.printLine(this.graph.cycleDetection()) <*> graphMenu
        case "9" => Console.printLine(this.graph.topologicalSort()) <*> graphMenu
        case "10" => Console.printLine("")
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




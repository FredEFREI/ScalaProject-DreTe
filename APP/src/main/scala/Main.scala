import zio._
import java.io.IOException


object Main extends ZIOAppDefault:
  private def loadFile: IO[IOException, String] =
    for
      fileName <- Console.readLine("Enter the path to the graph file:")
      _ <- Console.printLine("Loading graph from file...")


      // TODO Here you would add your logic to actually load the graph


      _ <- Console.printLine("Graph loaded successfully!")
    yield fileName

  private def mainMenu: IO[IOException, String] =
    for {
      Option <- getUserInput("----------------------------------------------------\nChoose an option:\n1: Create/Load a graph\n2: Graphs\n3: Exit\n")
      res <- Option match
        case "1" => loadFile <*> mainMenu
        case "2" => graphMenu <*> mainMenu
        case "3" => Console.printLine("Exiting...")
        case _ => Console.printLine("Invalid option. Please try again.")
    } yield Option

  private def graphMenu: IO[IOException, String] =
    for {
      graphOption <- getUserInput("----------------------------------------------------\nChoose an option:\n1: Diplay\n2: Create\n3: New node\n4: New edge\n5: Back\n")
      res <- graphOption match
        case "1" => Console.printLine("Display") <*> graphMenu
        case "2" => Console.printLine("Clear graph") <*> graphMenu
        case "3" => Console.printLine("New node") <*> graphMenu
        case "4" => Console.printLine("New edge") <*> graphMenu
        case "5" => Console.printLine("")
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




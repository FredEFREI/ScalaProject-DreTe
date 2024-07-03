import zio.*
import zio.Console.*
import java.io.IOException
import Utils.*

object Main extends ZIOAppDefault:

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] =
    for
      _ <- printLine("Welcome to our graph app!")
      option <- readLine("Choose an option:\n1: Load a graph\n2: Exit\n")
      _ <- option match
        case "1" => loadFile
        case "2" => printLine("Exiting...").as(ExitCode.success)
        case _ => printLine("Invalid option. Please try again.").as(ExitCode.success)
    yield ExitCode.success




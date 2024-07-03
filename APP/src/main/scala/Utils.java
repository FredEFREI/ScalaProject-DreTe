import zio.*
import zio.Console.*
import java.io.IOException
public class Utils {
    def loadFile: ZIO[Console, IOException, Unit] =
            for
    path <- readLine("Enter the path to the graph file:")
    _ <- printLine(s"Loading graph from $path...")


    // TODO Here you would add your logic to actually load the graph


    _ <- printLine("Graph loaded successfully!")
    yield ExitCode.success
}

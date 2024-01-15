import javalib.impworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import java.awt.*;
import java.util.ArrayList;

// represents a javalib world that extends World and contains a maze
/**
 * filler documentation
 */
public class MazeWorld extends javalib.impworld.World {

  private boolean started;
  private boolean userMode;

  private boolean won;

  private Maze maze;
  private final int scale;

  private ArrayList<Node> visited;
  private ArrayList<Node> path;

  private Posn player;

  // constructor
  /**
   * filler documentation
   */
  public MazeWorld(Maze maze) {
    this.maze = maze;
    this.scale = this.calculateScale();
    this.player = new Posn(0, 0);
    this.userMode = false;
    this.won = false;
  }

  // calculates the appropriate scale for this maze's cells
  /**
   * filler documentation
   */
  public int calculateScale() {
    int vertScale = 1600 / maze.getHeight();
    int horScale = 2560 / maze.getWidth();
    if (vertScale > horScale) {
      return horScale / 2;
    }
    else {
      return vertScale / 2;
    }
  }

  // changes this maze world according to the key pressed
  /**
   * filler documentation
   */
  public void onKeyEvent(String s) {
    if (!started && s.equals("d")) {
      this.started = true;
      ArrayList<ArrayList<Node>> result = this.maze.searchPath(true);
      this.path = result.get(0);
      this.visited = result.get(1);
    }
    else if (!started && s.equals("b")) {
      this.started = true;
      ArrayList<ArrayList<Node>> result = this.maze.searchPath(false);
      this.path = result.get(0);
      this.visited = result.get(1);
    }
    else if (!started && s.equals("u")) {
      this.started = true;
      this.userMode = true;
    }
    else if (s.equals(" ")) {
      // make a new maze
      this.maze = new Maze(this.maze.getWidth(), this.maze.getHeight());
      started = false;
    }
    else if ((s.equals("w") || s.equals("a") || s.equals("s") || s.equals("d")) && this.userMode) {
      Posn destination = new Utils().changePosn(player, s);
      // this can be done since we have defined that two nodes are equal
      // if their start and end are equal
      // this means we can create a new edge with different nodes
      // and still be able to check contains() for maze.edgesRemaining
      Node start = new Node(player);
      Node end = new Node(destination);
      if (maze.canTravelBetween(start, end)) {
        this.maze.paintNodeAt(this.player, Color.WHITE);
        this.maze.paintNodeAt(destination, new Color(255, 255, 160));
        this.player = destination;
      }
    }
  }

  // alters this maze after every tick
  /**
   * filler documentation
   */
  public void onTick() {

    if (started && !userMode) {
      if (visited.size() != 0) {
        Node curr = visited.get(0);
        curr.paint(new Color(145, 184, 242));
        visited.remove(0);
      }
      else {
        // this paints all the nodes in this.path with a light blue!
        for (Node node : this.path) {
          node.paint(new Color(50, 202, 206));
        }
      }
    }
    else if (started && userMode) {
      if (player.equals(new Posn(maze.getWidth() - 1, maze.getHeight() - 1))) {
        // player is at the end, highlight path
        this.path = this.maze.searchPath(true).get(0);
        // this paints all the nodes in this.path with a light blue!
        for (Node node : this.path) {
          node.paint(new Color(50, 202, 206));
        }
        this.won = true;
      }
    }
  }

  // draws the scene of this World after
  // the maze has been initialized
  /**
   * filler documentation
   */
  public WorldScene makeScene() {
    WorldScene scene;
    if (!started) {
      scene = this.drawInitialScene(scale);
    }
    else {
      scene = this.maze.draw(scale);
      if (won) {
        scene.placeImageXY(new TextImage("You win! :3", 16, Color.PINK),
            this.maze.getWidth() * scale / 2, this.maze.getHeight() * scale / 2);
      }
    }
    return scene;
  }

  // draws the initial scene of this maze with user instructions
  /**
   * filler documentation
   */
  public WorldScene drawInitialScene(int scale) {
    int width = this.maze.getWidth() * scale;
    int height = this.maze.getHeight() * scale;
    WorldScene scene = new WorldScene(width, height);
    TextImage topLine = new TextImage("Press 'd' for Depth First Search", 16, Color.BLACK);
    TextImage middleLine = new TextImage("Press 'b' for Breadth First Search", 16, Color.BLACK);
    TextImage bottomline = new TextImage("Press the space bar to restart and build a new maze", 16,
        Color.BLACK);
    TextImage playerInstruction = new TextImage(
        "To play the maze manually, press 'u'. Use 'wasd' to move.", 16, Color.BLACK);
    AboveImage comb1 = new AboveImage(topLine, middleLine);
    AboveImage comb2 = new AboveImage(comb1, playerInstruction);
    AboveImage comb3 = new AboveImage(comb2, bottomline);
    scene.placeImageXY(comb3, width / 2, height / 2);
    return scene;
  }

  // necessary for testing since we need to test the generated images
  // with handwritten test cases, which need to be scaled correctly

  // returns the scale of this maze
  public int getScale() {
    return scale;
  }

}
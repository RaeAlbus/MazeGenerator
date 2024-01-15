import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import java.awt.*;
import java.util.*;

// represents a maze
/**
 * filler documentation
 */
public class Maze {

  private final int width;
  private final int height;

  private final ArrayList<Edge> allEdges;
  private final ArrayList<Node> allNodes;
  private final ArrayList<Edge> existingEdges;

  // constructor that initializes the entire maze
  /**
   * filler documentation
   */
  public Maze(int width, int height) {
    this.width = width;
    this.height = height;
    this.allEdges = new ArrayList<>();
    this.allNodes = new ArrayList<>();
    makeGraph();
    this.existingEdges = kruskalsAlgorithm();
  }

  // Convenience constructor for testing
  /**
   * filler documentation
   */
  public Maze(int width, int height, boolean forTesting) {
    this.width = width;
    this.height = height;
    this.allEdges = new ArrayList<>();
    this.allNodes = new ArrayList<>();
    this.existingEdges = new ArrayList<>();
  }

  // Convenience constructor for testing
  /**
   * filler documentation
   */
  public Maze(int width, int height, ArrayList<Edge> allEdges, ArrayList<Node> allNodes) {
    this.width = width;
    this.height = height;
    this.allEdges = allEdges;
    this.allNodes = allNodes;
    this.existingEdges = this.kruskalsAlgorithm();
  }

  // returns a 2D arrayList of nodes corresponding to the
  // cells in this maze
  /**
   * filler documentation
   */
  public ArrayList<ArrayList<Node>> makeNodes() {
    ArrayList<ArrayList<Node>> graphNodes = new ArrayList<>();
    // this nested for loop runs a total of this.height * this.width times
    // and creates a new Node each time to build the structure of the graph
    for (int y = 0; y < this.height; y += 1) {
      ArrayList<Node> row = new ArrayList<>();
      for (int x = 0; x < this.width; x += 1) {
        Node node = new Node(new Posn(x, y));
        row.add(node);
      }
      graphNodes.add(row);
    }
    return graphNodes;
  }

  // makes the fully connected graph and initializes this.allNodes
  // and this.allEdges by adding the initialized nodes and edges
  // to it
  /**
   * filler documentation
   */
  public void makeGraph() {

    Utils utils = new Utils();
    ArrayList<ArrayList<Node>> graphNodes = this.makeNodes();

    // this nested for loop runs a total of this.height * this.width times
    // and links up the nodes in graphNdoe by creating edges
    for (int y = 0; y < this.height; y += 1) {
      for (int x = 0; x < this.width; x += 1) {
        Node current = graphNodes.get(y).get(x);
        this.allNodes.add(current);
        ArrayList<String> directions = new ArrayList<>(Arrays.asList("w", "s", "a", "d"));
        // this for loop iterates over all the directions
        // this node can have neighbors in, and links current to its neighbors
        for (String dir : directions) {
          Posn neighborPosn = utils.changePosn(new Posn(x, y), dir);
          if (!new Utils().outOfBounds(neighborPosn, this.height, this.width)) {
            Node neighbor = graphNodes.get(neighborPosn.y).get(neighborPosn.x);
            Edge edge = new Edge(current, neighbor);
            if (new Utils().assignTwoWayNeighbor(current, neighbor, edge)) {
              this.allEdges.add(edge);
            }
          }
        }
      }
    }
  }

  // returns a HashMap<Node, Node> with initialized representatives
  /**
   * filler documentation
   */
  public HashMap<Node, Node> initializeReps() {
    HashMap<Node, Node> representatives = new HashMap<>();
    // this iterates over allNodes and initializes the hashmaps of nodes
    for (Node node : this.allNodes) {
      representatives.put(node, node);
    }
    return representatives;
  }

  // returns a new ArrayList<Edge> which contains the sorted
  // edges of this maze
  /**
   * filler documentation
   */
  public ArrayList<Edge> sortEdges() {
    ArrayList<Edge> edges = new ArrayList<>(this.allEdges);
    edges.sort(new EdgeComparator());
    return edges;
  }

  // runs kruskals algorithm on this maze and returns an ArrayList
  // of edges that form the minimum spanning tree in this maze
  /**
   * filler documentation
   */
  public ArrayList<Edge> kruskalsAlgorithm() {

    Utils util = new Utils();
    HashMap<Node, Node> representatives = this.initializeReps();
    ArrayList<Edge> workList = this.sortEdges();
    ArrayList<Edge> edges = new ArrayList<>();

    while (!util.finished(representatives)) {
      Edge current = workList.get(0);
      workList.remove(0);
      Node toRep = util.find(representatives, current.getTo());
      Node fromRep = util.find(representatives, current.getFrom());
      if (!toRep.equals(fromRep)) {
        edges.add(current);
        util.union(representatives, toRep, fromRep);
      }
    }
    return edges;
  }

  // finds a solution to this maze with either dfs or bfs
  // returns an arraylist of arraylist of nodes of length 2
  // the first element being the found path, the second being
  // the list of visited nodes
  /**
   * filler documentation
   */
  public ArrayList<ArrayList<Node>> searchPath(boolean dfs) {

    Node start = this.allNodes.get(0);
    Node end = this.allNodes.get(this.allNodes.size() - 1);

    ArrayList<Node> path;

    HashMap<Node, Edge> cameFromEdge = new HashMap<>();
    ArrayList<Node> visited = new ArrayList<>();
    AStructure<Node> struct;

    if (dfs) {
      struct = new Stack<>();
    }
    else {
      struct = new Queue<>();
    }

    struct.addTo(start);

    while (!struct.isEmpty()) {
      Node next = struct.removeAt();
      if (next == end) {
        path = this.reconstruct(new ArrayList<>(), cameFromEdge, end, start);
        path.add(start);
        ArrayList<ArrayList<Node>> tuple = new ArrayList<>();
        tuple.add(path);
        tuple.add(visited);
        return tuple;
      }
      else {
        visited.add(next);
        HashMap<Node, Edge> neighborAndFrom = next.edgeNeighbor(this.existingEdges);
        // this for each loop iterates over all accessible neighbors of next
        // and adds it to the accumulator structures if it is not in contains
        for (Node n : neighborAndFrom.keySet()) {
          if (!visited.contains(n)) {
            struct.addTo(n);
            cameFromEdge.put(n, neighborAndFrom.get(n));
          }
        }
      }
    }

    return new ArrayList<>();
  }

  // reconstructs the solution of this maze from the HashMap<Node, Edge>
  /**
   * filler documentation
   */
  public ArrayList<Node> reconstruct(ArrayList<Node> acc, HashMap<Node, Edge> cameFromEdge,
      Node current, Node start) {
    if (current.equals(start)) {
      return acc;
    }
    Edge connectingEdge = cameFromEdge.get(current);
    Node otherNode = connectingEdge.getOther(current);
    acc.add(current);
    return reconstruct(acc, cameFromEdge, otherNode, start);
  }

  // returns true if there is an there is no wall between start
  // and end in this maze
  public boolean canTravelBetween(Node start, Node end) {
    Edge travelEdge = new Edge(start, end);
    return this.existingEdges.contains(travelEdge);
  }

  // draws the maze according to a scale
  /**
   * filler documentation
   */
  public WorldScene draw(int scale) {

    // PLACES THE NODES

    WorldScene scene = new WorldScene(this.width * scale, this.height * scale);

    // accumulates the overall image of the game grid
    WorldImage accumulator = new EmptyImage();
    // accumulates the current horizontal row of the game grid
    WorldImage horAccumulator = new EmptyImage();

    // for loops which iterate over all rows of the arrayList, adding each
    // accumulated row to the large accumulator with aboveImage, and then clearing
    // our horAccumulator
    int index = 0;
    for (int i = 0; i < this.height; i += 1) {
      // for loop which iterates over all columns of the arrayList and
      // accumulates them together with besideImage onto horAccumulator
      for (int j = 0; j < this.width; j += 1) {
        Node n = allNodes.get(index);
        // checks if we're at the start or end and paints it red
        if ((i == 0 && j == 0) || ((i == this.height - 1) && (j == this.width - 1))) {
          n.paint(new Color(254, 120, 104));
        }
        horAccumulator = new BesideImage(horAccumulator, n.draw(scale));
        index += 1;
      }
      accumulator = new AboveImage(accumulator, horAccumulator);
      horAccumulator = new EmptyImage();
    }
    scene.placeImageXY(accumulator, (width * scale) / 2, (height * scale) / 2);

    // PLACES THE EDGES

    ArrayList<Edge> edges = new ArrayList<Edge>();

    // this for loop iterates all the edges in this.allEdges and adds it
    // to edges if it is not in existingEdges
    for (Edge e : this.allEdges) {
      if (!this.existingEdges.contains(e)) {
        edges.add(e);
      }
    }

    // this for loop iterates all the edges in edges and draws them
    for (Edge e : edges) {
      e.drawWall(scene, scale);
    }

    return scene;
  }

  // paints the node at position p with color c
  public void paintNodeAt(Posn p, Color c) {
    Node current = this.allNodes.get(p.y * this.width + p.x);
    current.paint(c);
  }

  // necessary for testing the initial graph building of the maze class

  // returns an arraylist of all the nodes in this maze
  public ArrayList<Node> getAllNodes() {
    return this.allNodes;
  }

  // necessary to scale and draw the canvases to the right size in maze world

  // returns the width of this maze
  public int getWidth() {
    return width;
  }

  // returns the height of this maze
  public int getHeight() {
    return height;
  }

}

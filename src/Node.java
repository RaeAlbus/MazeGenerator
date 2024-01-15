import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

// represents a Node in a graph
/**
 * filler documentation
 */
public class Node {

  private final Posn pos;
  private final ArrayList<Edge> edges;
  private Color c;

  // constructor
  /**
   * filler documentation
   */
  public Node(Posn pos) {
    this.pos = pos;
    this.edges = new ArrayList<>();
    this.c = Color.WHITE;
  }

  // adds an edge to this.edges
  public void addEdge(Edge edge) {
    this.edges.add(edge);
  }

  // checks if this node has edge e1
  /**
   * filler documentation
   */
  public boolean hasEdge(Edge e1) {
    // this for each loop iterates over all the edges of this.edges()
    // and checks a condition for each
    for (Edge e2 : this.edges) {
      if (e1.equals(e2)) {
        return true;
      }
    }
    return false;
  }

  // returns the hashCode of this node
  public int hashCode() {
    return (this.pos.x * 100) + (this.pos.y * 10);
  }

  // checks if this node is equal to Object obj
  /**
   * filler documentation
   */
  public boolean equals(Object obj) {
    if (!(obj instanceof Node)) {
      return false;
    }
    Node that = (Node) obj;
    return this.pos.equals(that.pos);
  }

  // iterates over all the neighbors of this node, and adds them
  // to a HashMap<Node, Edge> if the edge is in the given arrayList
  /**
   * filler documentation
   */
  public HashMap<Node, Edge> edgeNeighbor(ArrayList<Edge> kruskals) {

    HashMap<Node, Edge> nodeEdgeMap = new HashMap<>();
    // this for each loop iterates over all the edges of this.edges()
    // and checks a condition for each
    for (Edge e : this.edges) {
      if (kruskals.contains(e)) {
        if (e.getTo().equals(this)) {
          // this is to, that is from
          nodeEdgeMap.put(e.getFrom(), e);
        }
        else {
          nodeEdgeMap.put(e.getTo(), e);
        }
      }
    }
    return nodeEdgeMap;

  }

  // draws this node with its color
  public WorldImage draw(int scale) {
    return new RectangleImage(scale, scale, OutlineMode.SOLID, this.c);
  }

  // paints this Node with the given colo
  public void paint(Color color) {
    this.c = color;
  }

  // necessary since we need this method in drawWall(), which
  // needs to reference the two node's positions in an edge
  // to place it in the right spot on the canvas

  // returns the position of this node
  public Posn getPos() {
    return this.pos;
  }

  // necessary since we need to iterate over the list of edges
  // during search path

  // returns the edge list of this node
  public ArrayList<Edge> getEdges() {
    return edges;
  }

}

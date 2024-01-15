import java.awt.Color;
import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;

// represents an edge in a graph
/**
 * filler documentation
 */
public class Edge {

  private final Node from;
  private final Node to;
  private final double weight;

  // constructor which assigns a random weight value
  public Edge(Node from, Node to) {
    this(from, to, Math.random());
  }

  // convenience constructor which allows custom weights
  /**
   * filler documentation
   */
  public Edge(Node from, Node to, double weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // checks if this edge is equals to object obj
  /**
   * filler documentation
   */
  public boolean equals(Object obj) {
    if (!(obj instanceof Edge)) {
      return false;
    }
    Edge that = (Edge) obj;
    return (this.to.equals(that.to) && this.from.equals(that.from))
        || (this.from.equals(that.to) && this.to.equals(that.from));
  }

  // computes the hashcode of this edge
  public int hashCode() {
    return this.from.hashCode() * this.to.hashCode();
  }

  // draws a wall that represents this edge
  /**
   * filler documentation
   */
  public void drawWall(WorldScene scene, int size) {

    Posn p1 = this.to.getPos();
    Posn p2 = this.from.getPos();
    String d = new Utils().edgeDirection(p1, p2);

    if (d.equals("top")) {
      scene.placeImageXY(new RectangleImage(size, 1, OutlineMode.SOLID, Color.BLACK),
          (p2.x * size) + size / 2, p2.y * size);
    }
    else if (d.equals("bottom")) {
      scene.placeImageXY(new RectangleImage(size, 1, OutlineMode.SOLID, Color.BLACK),
          (p2.x * size) + size / 2, (p2.y * size) + size);
    }
    else if (d.equals("left")) {
      scene.placeImageXY(new RectangleImage(1, size, OutlineMode.SOLID, Color.BLACK), p2.x * size,
          (p2.y * size) + size / 2);
    }
    else if (d.equals("right")) {
      scene.placeImageXY(new RectangleImage(1, size, OutlineMode.SOLID, Color.BLACK),
          (p2.x * size) + size, (p2.y * size) + size / 2);
    }

  }

  // given one node of this edge, returns the other node
  /**
   * filler documentation
   */
  public Node getOther(Node other) {
    if (this.to.equals(other)) {
      return this.from;
    }
    else {
      return this.to;
    }
  }

  // necessary since many methods in Maze require an edge's
  // to and from, and putting many of those unique functions
  // inside edge to suit those functionalities in Maze or Node
  // will crowd the class Edge, which is unnecessary since an
  // edge should be self contained and should not need to
  // perform maze functionalities

  // returns the from node of this edge
  public Node getFrom() {
    return from;
  }

  // returns the to node of this edge
  public Node getTo() {
    return to;
  }

  // necessary since we are overriding compareTo in EdgeComparator to use sort()
  // on edges, and we cannot put the comparator in the Edge class.

  // returns the weight of this edge
  public double getWeight() {
    return weight;
  }

}
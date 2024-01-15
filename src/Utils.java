import javalib.worldimages.Posn;
import java.util.HashMap;

// Represents the utilities functions
/**
 * filler documentation
 */
public class Utils {

  // Returns a posn that is one step in the given direction from the given posn
  /**
   * filler documentation
   */
  public Posn changePosn(Posn p, String dir) {
    Posn newPosn;
    if (dir.equals("w")) {
      newPosn = new Posn(p.x, p.y - 1);
    }
    else if (dir.equals("s")) {
      newPosn = new Posn(p.x, p.y + 1);
    }
    else if (dir.equals("a")) {
      newPosn = new Posn(p.x - 1, p.y);
    }
    else {
      newPosn = new Posn(p.x + 1, p.y);
    }
    return newPosn;
  }

  // Returns whether the given posn is out of bounds for a maze of the given
  // height and width
  public boolean outOfBounds(Posn neighborPosn, int height, int width) {
    return neighborPosn.x > width - 1 || neighborPosn.x < 0 || neighborPosn.y > height - 1
        || neighborPosn.y < 0;
  }

  // Connects the two given nodes in an edge unless an edge between them already
  // exists
  /**
   * filler documentation
   */
  public boolean assignTwoWayNeighbor(Node current, Node neighbor, Edge edge) {
    if (current.hasEdge(edge)) {
      return false;
    }
    else {
      current.addEdge(edge);
      neighbor.addEdge(edge);
      return true;
    }
  }

  // Returns the representative of the given node
  /**
   * filler documentation
   */
  public Node find(HashMap<Node, Node> representatives, Node n) {
    if (representatives.get(n).equals(n)) {
      return n;
    }
    else {
      return this.find(representatives, representatives.get(n));
    }
  }

  // Connects the two given nodes in the given hashmap
  public void union(HashMap<Node, Node> representatives, Node n1, Node n2) {
    representatives.put(n1, n2);
  }

  // Returns whether the given Hashmap has every key with the same value
  /**
   * filler documentation
   */
  public boolean finished(HashMap<Node, Node> representatives) {
    boolean started = false;
    Node equalNode = new Node(new Posn(-1, -1));
    for (Node n : representatives.keySet()) {
      if (!started) {
        started = true;
        equalNode = this.find(representatives, n);
      }
      else {
        if (this.find(representatives, n) != equalNode) {
          return false;
        }
      }
    }
    return true;
  }

  // Returns a String that says what direction the given from Posn is to the given
  // to Posn
  /**
   * filler documentation
   */
  public String edgeDirection(Posn to, Posn from) {
    if (to.x < from.x) {
      return "left";
    }
    else if (to.y < from.y) {
      return "top";
    }
    else if (to.y > from.y) {
      return "bottom";
    }
    else {
      return "right";
    }
  }

}

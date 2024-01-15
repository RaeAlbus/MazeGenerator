import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// Represents example Mazes
/**
 * filler documentation
 */
public class ExamplesMaze {

  // Small 3 X 2 maze for testing
  private Maze maze;

  // Large 30 X 30 maze for testing
  private Maze bigMaze;

  // Manually constructed of a 2DArraylist of the nodes
  // in the small maze example
  private ArrayList<ArrayList<Node>> allNodes;
  private ArrayList<Node> row1;
  private ArrayList<Node> row2;

  // Manually constructed nodes for the small maze example
  private Node a;
  private Node b;
  private Node c;
  private Node d;
  private Node e;
  private Node f;

  // Manually constructed edges for the small maze example
  private Edge ab;
  private Edge bc;
  private Edge de;
  private Edge ef;
  private Edge ad;
  private Edge be;
  private Edge cf;

  // Initalizes the manually made examples + mazes for testing
  void initNodes() {

    maze = new Maze(3, 2, true);
    bigMaze = new Maze(30, 30);

    allNodes = new ArrayList<>();
    row1 = new ArrayList<>();
    row2 = new ArrayList<>();

    a = new Node(new Posn(0, 0)); // 0
    b = new Node(new Posn(1, 0)); // 100
    c = new Node(new Posn(2, 0)); // 200
    d = new Node(new Posn(0, 1)); // 10
    e = new Node(new Posn(1, 1)); // 110
    f = new Node(new Posn(2, 1)); // 210

    row1.add(a);
    row1.add(b);
    row1.add(c);

    row2.add(d);
    row2.add(e);
    row2.add(f);

    allNodes.add(row1);
    allNodes.add(row2);

  }

  // Initializes the edges for the manually made examples
  void initLink() {

    ab = new Edge(a, b, 10);
    bc = new Edge(b, c, 5);
    de = new Edge(d, e, 3);
    ef = new Edge(e, f, 1);
    ad = new Edge(a, d, 7);
    be = new Edge(b, e, 6);
    cf = new Edge(c, f, 8);

    a.addEdge(ad);
    d.addEdge(ad);

    a.addEdge(ab);
    b.addEdge(ab);

    b.addEdge(be);
    e.addEdge(be);

    b.addEdge(bc);
    c.addEdge(bc);

    c.addEdge(cf);
    f.addEdge(cf);

    d.addEdge(de);
    e.addEdge(de);

    e.addEdge(ef);
    f.addEdge(ef);

  }

  // MAZE TESTS

  // Tests the makeNodes() method
  void testMakeNodes(Tester t) {
    initNodes();
    t.checkInexact(maze.makeNodes(), this.allNodes, 1);
  }

  // Tests the makesGraph() method
  void testMakeGraph(Tester t) {

    initNodes();
    initLink();

    maze.makeGraph();

    // as arrayList ordering might affect the result of the tester
    // library's tests, but do not matter in our program, we check
    // equality through a two-way contains method
    t.checkExpect(maze.getAllNodes().get(0).getEdges().containsAll(a.getEdges()), true);
    t.checkExpect(maze.getAllNodes().get(1).getEdges().containsAll(b.getEdges()), true);
    t.checkExpect(maze.getAllNodes().get(2).getEdges().containsAll(c.getEdges()), true);
    t.checkExpect(maze.getAllNodes().get(3).getEdges().containsAll(d.getEdges()), true);
    t.checkExpect(maze.getAllNodes().get(4).getEdges().containsAll(e.getEdges()), true);
    t.checkExpect(maze.getAllNodes().get(5).getEdges().containsAll(f.getEdges()), true);

    t.checkExpect(a.getEdges().containsAll(maze.getAllNodes().get(0).getEdges()), true);
    t.checkExpect(b.getEdges().containsAll(maze.getAllNodes().get(1).getEdges()), true);
    t.checkExpect(c.getEdges().containsAll(maze.getAllNodes().get(2).getEdges()), true);
    t.checkExpect(d.getEdges().containsAll(maze.getAllNodes().get(3).getEdges()), true);
    t.checkExpect(e.getEdges().containsAll(maze.getAllNodes().get(4).getEdges()), true);
    t.checkExpect(f.getEdges().containsAll(maze.getAllNodes().get(5).getEdges()), true);
  }

  // Tests the initializeReps() method
  void testInitializeReps(Tester t) {

    initNodes();
    initLink();
    maze.makeNodes();
    maze.makeGraph();

    for (Node n : maze.getAllNodes()) {
      t.checkInexact(maze.initializeReps().get(n), n, 1);
    }

  }

  // Tests the sortEdges() method
  void testSortEdges(Tester t) {

    Edge e1 = new Edge(a, b, 4);
    Edge e2 = new Edge(a, b, 1);
    Edge e3 = new Edge(a, b, 10);
    Edge e4 = new Edge(a, b, 5);

    ArrayList<Edge> edges = new ArrayList<>(Arrays.asList(e1, e2, e3, e4));
    ArrayList<Edge> sortedEdges = new ArrayList<>(Arrays.asList(e2, e1, e4, e3));

    edges.sort(new EdgeComparator());
    t.checkInexact(edges, sortedEdges, 0.01);

  }

  // Tests the kruskalsAlgorithm() method
  void testKruskalsAlgorithm(Tester t) {

    initNodes();
    initLink();

    ArrayList<Edge> allEdges = new ArrayList<>(Arrays.asList(ab, bc, de, ef, ad, be, cf));
    ArrayList<Node> allNodes = new ArrayList<>(Arrays.asList(a, b, c, d, e, f));

    Maze kruskalsTest = new Maze(3, 2, allEdges, allNodes);

    ArrayList<Edge> manualResult = new ArrayList<>();

    manualResult.add(ef);
    manualResult.add(de);
    manualResult.add(bc);
    manualResult.add(be);
    manualResult.add(ad);

    ArrayList<Edge> result = kruskalsTest.kruskalsAlgorithm();

    for (Edge e : result) {
      t.checkExpect(manualResult.contains(e), true);
    }

    for (Edge e : manualResult) {
      t.checkExpect(result.contains(e), true);
    }

  }

  // Tests the searchPath(boolean) method
  void testSearchPath(Tester t) {

    initNodes();
    initLink();

    ArrayList<Edge> allEdges = new ArrayList<>(Arrays.asList(ab, bc, de, ef, ad, be, cf));
    ArrayList<Node> allNodes = new ArrayList<>(Arrays.asList(a, b, c, d, e, f));

    Maze mazeSearchTest = new Maze(3, 2, allEdges, allNodes);

    ArrayList<Node> manualPathResult = new ArrayList<>();

    manualPathResult.add(a);
    manualPathResult.add(d);
    manualPathResult.add(e);
    manualPathResult.add(f);

    ArrayList<Node> resultPath = mazeSearchTest.searchPath(true).get(0);

    for (Node n : manualPathResult) {
      t.checkExpect(resultPath.contains(n), true);
    }

    for (Node n : resultPath) {
      t.checkExpect(manualPathResult.contains(n), true);
    }

    ArrayList<Node> visited = mazeSearchTest.searchPath(true).get(1);

    ArrayList<Node> manualVisitedResult = new ArrayList<>();

    manualVisitedResult.add(a);
    manualVisitedResult.add(d);
    manualVisitedResult.add(e);
    manualVisitedResult.add(b);
    manualVisitedResult.add(c);

    for (Node n : manualVisitedResult) {
      t.checkExpect(visited.contains(n), true);
    }

    for (Node n : visited) {
      t.checkExpect(manualVisitedResult.contains(n), true);
    }

  }

  // Test the reconstruct(ArrayList<Node>, HashMap<Node, Edge>, Node, Node) method
  void testReconstruct(Tester t) {

    initNodes();
    initLink();
    HashMap<Node, Edge> hashmap = new HashMap<>();

    hashmap.put(f, ef);
    hashmap.put(e, de);
    hashmap.put(d, ad);

    ArrayList<Node> expectedPath = new ArrayList<>(Arrays.asList(d, e, f));

    ArrayList<Edge> allEdges = new ArrayList<>(Arrays.asList(ab, bc, de, ef, ad, be, cf));
    ArrayList<Node> allNodes = new ArrayList<>(Arrays.asList(a, b, c, d, e, f));

    Maze testMaze = new Maze(3, 2, allEdges, allNodes);

    ArrayList<Node> actualPath = testMaze.reconstruct(new ArrayList<>(), hashmap, f, a);

    t.checkExpect(actualPath.containsAll(expectedPath), true);
    t.checkExpect(expectedPath.containsAll(actualPath), true);

  }

  // Tests the canTravelBetween(Node, Node) method
  void testCanTravelBetween(Tester t) {

    initNodes();
    initLink();

    ArrayList<Edge> allEdges = new ArrayList<>(Arrays.asList(ab, bc, de, ef, ad, be, cf));
    ArrayList<Node> allNodes = new ArrayList<>(Arrays.asList(a, b, c, d, e, f));

    Maze testMaze = new Maze(3, 2, allEdges, allNodes);

    t.checkExpect(testMaze.canTravelBetween(e, f), true);
    t.checkExpect(testMaze.canTravelBetween(a, c), false);
    t.checkExpect(testMaze.canTravelBetween(b, e), true);
    t.checkExpect(testMaze.canTravelBetween(b, f), false);

  }

  // Tests the getWidth() met
  void testGetWidth(Tester t) {
    maze.makeGraph();

    t.checkExpect(maze.getWidth(), 3);
    t.checkExpect(bigMaze.getWidth(), 30);

  }

  // Tests the getHeight() method
  void testGetHeight(Tester t) {
    maze.makeGraph();

    t.checkExpect(maze.getHeight(), 2);
    t.checkExpect(bigMaze.getHeight(), 30);
  }

  // ASTRUCTURE TESTS

  // Test the addTo() method
  void testAddTo(Tester t) {
    initNodes();
    initLink();

    Stack<Node> exampleStack = new Stack<>();
    Queue<Node> exampleQueue = new Queue<>();

    t.checkExpect(exampleStack.isEmpty(), true);
    exampleStack.addTo(a);
    t.checkExpect(exampleStack.isEmpty(), false);

    t.checkExpect(exampleQueue.isEmpty(), true);
    exampleQueue.addTo(a);
    t.checkExpect(exampleQueue.isEmpty(), false);

  }

  // Tests the removeAt() method
  void testRemoveAt(Tester t) {
    initNodes();
    initLink();

    Stack<Node> exampleStack = new Stack<>();
    Queue<Node> exampleQueue = new Queue<>();

    exampleStack.addTo(a);
    t.checkExpect(exampleStack.removeAt(), a);

    exampleQueue.addTo(a);
    t.checkExpect(exampleQueue.removeAt(), a);
  }

  // tests the isEmpty() method
  void isEmpty(Tester t) {
    initNodes();
    initLink();

    Stack<Node> exampleStack = new Stack<>();
    Queue<Node> exampleQueue = new Queue<>();

    t.checkExpect(exampleStack.isEmpty(), true);
    exampleStack.addTo(a);
    t.checkExpect(exampleStack.isEmpty(), false);

    t.checkExpect(exampleQueue.isEmpty(), true);
    exampleQueue.addTo(a);
    t.checkExpect(exampleQueue.isEmpty(), false);
  }

  // EDGE COMPARATOR TESTS

  // Tests the compare(Edge, Edge) method
  void testCompare(Tester t) {
    initNodes();
    initLink();
    EdgeComparator comparator = new EdgeComparator();

    t.checkExpect(comparator.compare(ef, de), -1);
    t.checkExpect(comparator.compare(be, bc), 1);
    t.checkExpect(comparator.compare(cf, ab), -1);
    t.checkExpect(comparator.compare(ab, bc), 1);
  }

  // MAZEWORLD TESTS

  // Tests the calculateScalr() method
  void testCalculateScale(Tester t) {

    initNodes();
    initLink();
    maze.makeGraph();
    bigMaze.makeGraph();

    MazeWorld w1 = new MazeWorld(maze);
    MazeWorld w2 = new MazeWorld(bigMaze);

    t.checkExpect(w1.calculateScale(), 400);
    t.checkExpect(w2.calculateScale(), 26);
  }

  // Tests the drawInitialScene(int) method
  void testDrawInitialScene(Tester t) {
    initNodes();
    initLink();
    maze.makeGraph();

    MazeWorld w = new MazeWorld(maze);
    WorldScene manualScene = new WorldScene(maze.getWidth() * w.getScale(),
        maze.getHeight() * w.getScale());

    TextImage topLine = new TextImage("Press 'd' for Depth First Search", 16, Color.BLACK);
    TextImage middleLine = new TextImage("Press 'b' for Breadth First Search", 16, Color.BLACK);
    TextImage bottomline = new TextImage("Press the space bar to restart and build a new maze", 16,
        Color.BLACK);
    TextImage playerInstruction = new TextImage(
        "To play the maze manually, press 'u'. Use 'wasd' to move.", 16, Color.BLACK);
    AboveImage comb1 = new AboveImage(topLine, middleLine);
    AboveImage comb2 = new AboveImage(comb1, playerInstruction);
    AboveImage comb3 = new AboveImage(comb2, bottomline);
    manualScene.placeImageXY(comb3, (maze.getWidth() * w.getScale()) / 2,
        (maze.getHeight() * w.getScale()) / 2);

    t.checkExpect(w.drawInitialScene(w.getScale()), manualScene);

  }

  // UTILS TESTS

  // Tests the changePosn(Posn, String) method
  void testChangePosn(Tester t) {
    Posn testPos = new Posn(4, 7);
    t.checkExpect(new Utils().changePosn(testPos, "w"), new Posn(4, 6));
    t.checkExpect(new Utils().changePosn(testPos, "a"), new Posn(3, 7));
    t.checkExpect(new Utils().changePosn(testPos, "s"), new Posn(4, 8));
    t.checkExpect(new Utils().changePosn(testPos, "d"), new Posn(5, 7));
  }

  // Test outOfBounds(Posn, int, int) method
  void testOutOfBounds(Tester t) {
    Posn testPos = new Posn(4, 7);
    t.checkExpect(new Utils().outOfBounds(testPos, 8, 10), false);
    t.checkExpect(new Utils().outOfBounds(testPos, 10, 10), false);
    t.checkExpect(new Utils().outOfBounds(testPos, 6, 4), true);
    t.checkExpect(new Utils().outOfBounds(new Posn(-1, 0), 1, 1), true);
    t.checkExpect(new Utils().outOfBounds(new Posn(0, -1), 1, 1), true);
    t.checkExpect(new Utils().outOfBounds(new Posn(0, 0), 1, 1), false);
  }

  // Tests the assignNeighbors(Node, Node, Edge) method
  void testAssignNeighbors(Tester t) {

    initNodes();
    initLink();
    Edge ac = new Edge(a, c);
    Edge ca = new Edge(c, a);

    // before
    t.checkExpect(a.hasEdge(ac), false);
    t.checkExpect(c.hasEdge(ac), false);
    t.checkExpect(a.getEdges().size(), 2);

    new Utils().assignTwoWayNeighbor(a, c, ac);

    // after
    t.checkExpect(a.hasEdge(ac), true);
    t.checkExpect(c.hasEdge(ac), true);
    t.checkExpect(a.getEdges().size(), 3);

    // check that assigning again does not change the size of edgeList
    new Utils().assignTwoWayNeighbor(a, c, ac);
    t.checkExpect(a.getEdges().size(), 3);
    t.checkExpect(c.getEdges().size(), 3);
    new Utils().assignTwoWayNeighbor(a, c, ca);
    t.checkExpect(a.getEdges().size(), 3);
    t.checkExpect(c.getEdges().size(), 3);

  }

  // Tests the find(HashMap<Node, Node>, Node) method
  void testFind(Tester t) {

    initNodes();
    initLink();

    HashMap<Node, Node> testHashMap = new HashMap<>();
    testHashMap.put(a, a);
    testHashMap.put(b, d);
    testHashMap.put(c, a);
    testHashMap.put(d, a);
    testHashMap.put(e, a);

    t.checkExpect(new Utils().find(testHashMap, a), a);
    t.checkExpect(new Utils().find(testHashMap, c), a);
    t.checkExpect(new Utils().find(testHashMap, b), a);

    HashMap<Node, Node> testHashMap2 = new HashMap<>();

    testHashMap2.put(a, c);
    testHashMap2.put(b, c);
    testHashMap2.put(c, c);
    testHashMap2.put(d, e);
    testHashMap2.put(e, f);
    testHashMap2.put(f, f);

    t.checkExpect(new Utils().find(testHashMap2, a), c);
    t.checkExpect(new Utils().find(testHashMap2, b), c);
    t.checkExpect(new Utils().find(testHashMap2, c), c);
    t.checkExpect(new Utils().find(testHashMap2, d), f);
    t.checkExpect(new Utils().find(testHashMap2, e), f);
    t.checkExpect(new Utils().find(testHashMap2, f), f);

  }

  // Tests the union(HashMap<Node, Node>, Node, Node) method
  void testUnion(Tester t) {

    initNodes();
    initLink();

    HashMap<Node, Node> testHashMapBefore = new HashMap<>();
    testHashMapBefore.put(a, a);
    testHashMapBefore.put(b, a);
    testHashMapBefore.put(c, e);
    testHashMapBefore.put(d, e);
    testHashMapBefore.put(e, e);

    HashMap<Node, Node> testHashMapAfter = new HashMap<>();
    testHashMapAfter.put(a, e);
    testHashMapAfter.put(b, a);
    testHashMapAfter.put(c, e);
    testHashMapAfter.put(d, e);
    testHashMapAfter.put(e, e);

    new Utils().union(testHashMapBefore, a, e);

    t.checkExpect(testHashMapBefore, testHashMapAfter);

  }

  // Tests the finished(HashMap<Node, Node>) method
  void testFinished(Tester t) {
    Utils utils = new Utils();

    HashMap<Node, Node> notFinished = new HashMap<>();
    HashMap<Node, Node> finished = new HashMap<>();

    notFinished.put(a, a);
    notFinished.put(b, b);
    notFinished.put(c, b);
    notFinished.put(d, d);
    notFinished.put(e, e);
    notFinished.put(f, f);

    finished.put(a, e);
    finished.put(b, e);
    finished.put(c, e);
    finished.put(d, e);
    finished.put(e, e);
    finished.put(f, e);

    t.checkExpect(utils.finished(notFinished), false);
    t.checkExpect(utils.finished(finished), true);

  }

  // Test the edgeDirection(Posn, Posn) method
  void testEdgeDirection(Tester t) {
    Utils utils = new Utils();
    t.checkExpect(utils.edgeDirection(new Posn(0, 0), new Posn(1, 0)), "left");
    t.checkExpect(utils.edgeDirection(new Posn(1, 0), new Posn(0, 0)), "right");
    t.checkExpect(utils.edgeDirection(new Posn(0, 0), new Posn(0, 1)), "top");
    t.checkExpect(utils.edgeDirection(new Posn(0, 1), new Posn(0, 0)), "bottom");
  }

  // NODE TESTS

  // Tests the addEdge(Edge) method
  void testAddEdge(Tester t) {
    initNodes();
    ArrayList<Edge> edgeList = new ArrayList<>();
    t.checkExpect(a.getEdges(), edgeList);

    edgeList.add(new Edge(a, b, 0));
    a.addEdge(new Edge(a, b, 0));
    t.checkExpect(a.getEdges(), edgeList);

    edgeList.add(new Edge(a, c, 0));
    a.addEdge(new Edge(a, c, 0));
    t.checkExpect(a.getEdges(), edgeList);

  }

  // Tests the hasEdge(Edge) method
  void testHasEdge(Tester t) {
    initNodes();
    initLink();
    Edge ba = new Edge(b, a);
    t.checkExpect(a.hasEdge(ab), true);
    t.checkExpect(b.hasEdge(ab), true);
    t.checkExpect(a.hasEdge(ba), true);
    t.checkExpect(b.hasEdge(ba), true);
  }

  // Tests the hashCode() method in the Node class
  void testHashCodeNode(Tester t) {
    initNodes();
    initLink();
    t.checkExpect(a.hashCode(), 0);
    t.checkExpect(b.hashCode(), 100);
    t.checkExpect(c.hashCode(), 200);
    t.checkExpect(d.hashCode(), 10);
    t.checkExpect(e.hashCode(), 110);
    t.checkExpect(f.hashCode(), 210);
  }

  // Tests the equals(Object) method in the Node class
  // TODO FIX
  void testEqualsNode(Tester t) {
    initNodes();
    initLink();

    t.checkExpect(a.equals(a), true);
    t.checkExpect(b.equals(b), true);
    t.checkExpect(c.equals(c), true);
    t.checkExpect(d.equals(a), false);
    t.checkExpect(e.equals(a), false);
    t.checkExpect(f.equals(a), false);
  }

  // Tests the draw(Color, int) method in the Node class
  void testDrawNode(Tester t) {
    initNodes();
    initLink();
    a.paint(Color.WHITE);
    t.checkExpect(a.draw(10), new RectangleImage(10, 10, OutlineMode.SOLID, Color.WHITE));
    b.paint(Color.RED);
    t.checkExpect(b.draw(5), new RectangleImage(5, 5, OutlineMode.SOLID, Color.RED));
    c.paint(Color.BLUE);
    t.checkExpect(c.draw(1), new RectangleImage(1, 1, OutlineMode.SOLID, Color.BLUE));
  }

  // Tests the getPos() method
  void testGetPos(Tester t) {
    initNodes();
    initLink();

    t.checkExpect(a.getPos(), new Posn(0, 0));
    t.checkExpect(b.getPos(), new Posn(1, 0));
    t.checkExpect(c.getPos(), new Posn(2, 0));
    t.checkExpect(d.getPos(), new Posn(0, 1));
    t.checkExpect(e.getPos(), new Posn(1, 1));
    t.checkExpect(f.getPos(), new Posn(2, 1));

  }

  // Tests the getEdges() method
  void testGetEdges(Tester t) {
    initNodes();
    initLink();

    ArrayList<Edge> aEdge = new ArrayList<>();
    ArrayList<Edge> result = a.getEdges();

    aEdge.add(ab);
    aEdge.add(ad);

    for (Edge e : aEdge) {
      t.checkExpect(result.contains(e), true);
    }

    for (Edge e : result) {
      t.checkExpect(aEdge.contains(e), true);
    }

  }

  // EDGE TESTS

  // Tests the equals() method in the Edge class
  // TODO: FIX THIS
  void testEquals(Tester t) {
    initNodes();
    initLink();
    Edge ba = new Edge(b, a);
    Edge baDupe = new Edge(b, a);
    t.checkExpect(ba.equals(ab), true);
    t.checkExpect(ab.equals(ba), true);
    t.checkExpect(ba.equals(baDupe), true);
    t.checkExpect(baDupe.equals(ba), true);
    t.checkExpect(ab.equals(ad), false);
  }

  // Tests the hashCode() method in the Edge class
  void testHashCode(Tester t) {
    initNodes();
    initLink();
    t.checkExpect(ab.hashCode(), 0);
    t.checkExpect(bc.hashCode(), 20000);
    t.checkExpect(de.hashCode(), 1100);
    t.checkExpect(ef.hashCode(), 23100);
    t.checkExpect(ad.hashCode(), 0);
    t.checkExpect(be.hashCode(), 11000);
    t.checkExpect(cf.hashCode(), 42000);
  }

  // Tests the drawWall(WorldScene, int)
  void testDrawWall(Tester t) {
    initNodes();
    initLink();

    MazeWorld w = new MazeWorld(maze);
    WorldScene manualBaseAD = new WorldScene(maze.getWidth() * w.getScale(),
        maze.getHeight() * w.getScale());
    WorldScene manualBaseAB = new WorldScene(maze.getWidth() * w.getScale(),
        maze.getHeight() * w.getScale());

    manualBaseAD.placeImageXY(new RectangleImage(w.getScale(), 1, OutlineMode.SOLID, Color.BLACK),
        (a.getPos().x * w.getScale()) + w.getScale() / 2,
        (a.getPos().y * w.getScale()) + w.getScale());
    manualBaseAB.placeImageXY(new RectangleImage(1, w.getScale(), OutlineMode.SOLID, Color.BLACK),
        (a.getPos().x * w.getScale()) + w.getScale(),
        (a.getPos().y * w.getScale()) + w.getScale() / 2);

    WorldScene baseAD = new WorldScene(maze.getWidth() * w.getScale(),
        maze.getHeight() * w.getScale());
    WorldScene baseAB = new WorldScene(maze.getWidth() * w.getScale(),
        maze.getHeight() * w.getScale());

    ad.drawWall(baseAD, w.getScale());
    ab.drawWall(baseAB, w.getScale());

    t.checkExpect(baseAD, manualBaseAD);
    t.checkExpect(baseAB, manualBaseAB);

  }

  // Tests the getOther(Node) method in the Edge class
  void testGetOther(Tester t) {
    initNodes();
    initLink();
    t.checkExpect(ad.getOther(d), a);
    t.checkExpect(be.getOther(b), e);
    t.checkExpect(be.getOther(e), b);
  }

  // Tests the getFrom() method
  void testGetFrom(Tester t) {
    initNodes();
    initLink();

    t.checkExpect(ab.getFrom(), a);
    t.checkExpect(ef.getFrom(), e);
    t.checkExpect(de.getFrom(), d);
    t.checkExpect(bc.getFrom(), b);
    t.checkExpect(be.getFrom(), b);

  }

  // Tests the getTo() method
  void testGetTo(Tester t) {
    initNodes();
    initLink();

    t.checkExpect(ab.getTo(), b);
    t.checkExpect(ef.getTo(), f);
    t.checkExpect(de.getTo(), e);
    t.checkExpect(bc.getTo(), c);
    t.checkExpect(be.getTo(), e);
  }

  // Tests the getWeight() method
  void testGetWeight(Tester t) {
    initNodes();
    initLink();

    t.checkExpect(ab.getWeight(), 10.0);
    t.checkExpect(ef.getWeight(), 1.0);
    t.checkExpect(de.getWeight(), 3.0);
    t.checkExpect(bc.getWeight(), 5.0);
    t.checkExpect(be.getWeight(), 6.0);

  }

  // BIG BANG

  // Displays our Maze
  void testBigMazeBigBang(Tester t) {
    initNodes();
    initLink();
    MazeWorld w = new MazeWorld(bigMaze);
    int worldWidth = bigMaze.getWidth() * w.getScale();
    int worldHeight = bigMaze.getHeight() * w.getScale();
    double tickRate = 0.05;
    w.bigBang(worldWidth, worldHeight, tickRate);
  }
}

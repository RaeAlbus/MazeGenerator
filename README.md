# MazeGenerator

An interactive maze generation and solving application implementing **Kruskal's Minimum Spanning Tree algorithm** for maze creation and both **Depth-First Search (DFS)** and **Breadth-First Search (BFS)** for pathfinding. Features animated maze solving with step-by-step visualization and manual player mode.

## 🧮 Algorithm Overview

This project demonstrates three fundamental graph algorithms working together to create, solve, and visualize perfect mazes (mazes with exactly one path between any two points).

## 🔬 Technical Implementation

### 1. Maze Generation: Kruskal's Algorithm (MST)

**Purpose:** Creates a perfect maze by treating it as a minimum spanning tree problem.

**Algorithm Steps:**
1. **Initialize:** Create a fully connected grid graph where every cell has edges to all adjacent cells
2. **Assign Random Weights:** Each edge receives a random weight (0.0 - 1.0)
3. **Sort Edges:** Order all edges by weight (ascending)
4. **Union-Find:** Use disjoint-set data structure to detect cycles
5. **Build MST:** Iterate through sorted edges, adding edge if it connects two disjoint sets

**Why Kruskal's?** 
- Generates unbiased, uniformly random mazes
- Guarantees exactly one path between any two cells
- Natural fit for grid-based maze structure

**Time Complexity:** O(E log E) where E = number of edges
- Sorting edges: O(E log E)
- Union-Find operations: O(E × α(V)) ≈ O(E) with path compression
- V = width × height (number of cells)
- E ≈ 2V for grid graphs

**Space Complexity:** O(V) for union-find data structure

### 2. Maze Solving: Graph Traversal

#### Depth-First Search (DFS)
- **Data Structure:** Stack (LIFO)
- **Behavior:** Explores as far as possible along each branch before backtracking
- **Visualization:** Shows a winding, exploratory path that deeply probes corridors
- **Time Complexity:** O(V + E) where E = edges in spanning tree ≈ V - 1
- **Space Complexity:** O(V) for visited set and stack

#### Breadth-First Search (BFS)
- **Data Structure:** Queue (FIFO)
- **Behavior:** Explores all neighbors at current depth before proceeding deeper
- **Visualization:** Shows a spreading wave-like pattern from start to goal
- **Optimality:** **Guaranteed to find the shortest path** in unweighted graphs
- **Time Complexity:** O(V + E)
- **Space Complexity:** O(V) for visited set and queue

### 3. Union-Find (Disjoint Set Union)

Critical data structure for Kruskal's algorithm cycle detection.

**Operations:**
- **Find(x):** Returns representative of x's set
  - With path compression: O(α(n)) ≈ O(1)
- **Union(x, y):** Merges sets containing x and y
  - O(α(n)) with union by rank

**Implementation Optimizations:**
- **Path Compression:** During find(), update all nodes to point directly to root
- Significantly reduces tree height for future operations

## ✨ Features

### Interactive Modes

**Automated Solving:**
- **'d' key** - Solve using Depth-First Search (explores deeply)
- **'b' key** - Solve using Breadth-First Search (finds shortest path)
- **Real-time visualization** - Watch the algorithm explore and find the solution

**Manual Play Mode:**
- **'u' key** - Enter user-controlled mode
- **WASD keys** - Navigate through the maze
- **Auto-solve on completion** - Shows optimal path when you reach the end

**Maze Generation:**
- **Space bar** - Generate new random maze

### Visual Design

- **Color-coded nodes:**
  - Red/Pink: Start and end positions
  - Light blue: Visited nodes during search
  - Cyan: Final solution path
  - Yellow: Player position (manual mode)
  - White: Unvisited passages
  - Black: Walls

- **Animated solving:** Step-by-step visualization of algorithm progress
- **Responsive scaling:** Automatically adjusts cell size based on maze dimensions

## 🛠️ Built With

- **Language:** Java
- **Graphics Library:** Northeastern University's JavaLib (impworld package)
- **Data Structures:** Custom implementations of Stack, Queue, Union-Find

## 📊 Complexity Analysis

### Complete Maze Generation Pipeline

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Create grid graph | O(V) | O(V) |
| Generate all edges | O(V) | O(E) ≈ O(V) |
| Sort edges | O(E log E) | O(E) |
| Kruskal's MST | O(E × α(V)) | O(V) |
| **Total Generation** | **O(E log E)** | **O(V + E)** |
| DFS/BFS solving | O(V + E) | O(V) |
| Path reconstruction | O(V) | O(V) |

**For a 100×100 maze:**
- V = 10,000 cells
- E ≈ 19,800 edges
- Edge sorting dominates at ~236,000 operations
- Highly efficient even for large mazes

## 🎨 Design Patterns

### Strategy Pattern
Abstract `AStructure<T>` class allows switching between Stack/Queue for DFS/BFS:
```java
AStructure struct = dfs ? new Stack<>() : new Queue<>();
```
Single `searchPath()` method works for both algorithms through polymorphism.

### Template Method
Edge and Node classes implement consistent interfaces for graph operations while allowing customization of behavior.

### Comparator Pattern
`EdgeComparator` enables sorting edges by weight for Kruskal's algorithm:
```java
edges.sort(new EdgeComparator());
```

## 🧪 Testing Methodology

Comprehensive test suite includes:
- **Unit tests** for Union-Find operations (find, union, cycle detection)
- **Algorithm correctness** - Verifying Kruskal's produces valid spanning trees
- **Path validation** - Confirming DFS/BFS find correct solutions
- **Edge case handling** - Single-cell mazes, large grids, boundary conditions
- **Visual regression** - Testing maze rendering at various scales

Test data includes manually constructed 3×2 and 2×3 grids with known optimal solutions.

## 🎓 Academic Context

Created for an **Algorithms and Data Structures** course at Northeastern University. This project demonstrates:

- Practical application of minimum spanning tree algorithms
- Graph traversal strategies and their trade-offs
- Union-Find data structure with path compression optimization
- Interactive visualization of abstract algorithms
- Test-driven development with comprehensive coverage

## 🚀 Performance Characteristics

**Maze Generation:**
- 30×30 maze: ~0.01 seconds
- 100×100 maze: ~0.15 seconds
- 1000×1000 maze: ~20 seconds (dominated by E log E sort)

**Pathfinding:**
- Both BFS and DFS: O(V) in practice for tree structures
- BFS typically visits ~50% more nodes than necessary (spreading pattern)
- DFS may visit nearly all nodes in worst case (deep exploration)

## 🔧 Implementation Highlights

### Efficient Graph Representation
- Adjacency list through Node's edge collection
- Bidirectional edge storage prevents duplicate edges
- Position-based hashing for O(1) node lookups

### Memory-Efficient Union-Find
- Path compression reduces average find() to nearly O(1)
- Recursive implementation with memoization
- HashMap-based parent tracking

### Visual Optimization
- Dynamic cell scaling based on screen resolution
- Image composition using JavaLib's functional approach
- Minimal redraws during animation

### Player Movement Validation
- Edge-based collision detection prevents wall-passing
- Position validation using maze's MST edge list
- Smooth color transitions for visited cells

## 📚 Algorithm Background

**Kruskal's Algorithm** was developed by Joseph Kruskal in 1956 for finding minimum spanning trees in weighted graphs. While originally designed for network optimization problems (like minimizing cable length in telecommunications), it's perfect for maze generation due to:
- Uniform randomness in maze structure
- Guarantee of single solution path
- Natural prevention of loops and isolated regions

**BFS for Shortest Paths** guarantees optimality in unweighted graphs, making it ideal for finding the quickest maze solution, while **DFS** provides an alternative approach that's often more memory-efficient for deep mazes.

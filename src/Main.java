import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

  // BIG BANG
  public static void main(String[] args)
  {
      System.out.println("Main Method being ran");
      Maze bigMaze = new Maze(30, 30);
      MazeWorld w = new MazeWorld(bigMaze);
      int worldWidth = bigMaze.getWidth() * w.getScale();
      int worldHeight = bigMaze.getHeight() * w.getScale();
      double tickRate = 0.05;
      w.bigBang(worldWidth, worldHeight, tickRate);
  }

}

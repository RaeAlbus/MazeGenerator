import java.util.ArrayDeque;
import java.util.Deque;

// represents a wrapper structure for an ArrayDeque
abstract class AStructure<T> {

  Deque<T> structure;

  // constructor
  public AStructure() {
    this.structure = new ArrayDeque<>();
  }

  // adds T to this AStructure
  abstract void addTo(T elem);

  // removes an element from this AStructure
  abstract T removeAt();

  // returns true if this AStructure is empty
  boolean isEmpty() {
    return this.structure.isEmpty();
  }

}

// represents a stack
class Stack<T> extends AStructure<T> {

  // pushes an element at the top of the stack
  void addTo(T elem) {
    this.structure.push(elem);
  }

  // removes and returns the first element at the top of the stack
  T removeAt() {
    return this.structure.pop();
  }

}

// represents a queue
class Queue<T> extends AStructure<T> {

  // adds the given element to the back of this queue
  void addTo(T elem) {
    this.structure.offer(elem);
  }

  // removes and returns the first element
  // at the start of the queue
  T removeAt() {
    return this.structure.poll();
  }

}

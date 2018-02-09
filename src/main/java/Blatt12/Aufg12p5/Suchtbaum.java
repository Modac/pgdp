package Blatt12.Aufg12p5;

import java.util.Stack;

public class Suchtbaum<T extends Comparable<T>> {

  private class SuchtbaumElement {

    private T element;
    private SuchtbaumElement left = null;
    private SuchtbaumElement right = null;

    public SuchtbaumElement(T element, SuchtbaumElement left,
        SuchtbaumElement right) {
      this.element = element;
      this.left = left;
      this.right = right;
    }

    public SuchtbaumElement(T element) {
      this(element, null, null);
    }
  }

  private final RW rw;
  private SuchtbaumElement root = null;

  public Suchtbaum(SuchtbaumElement root) {
    this.rw = new RW();
    this.root = root;
  }

  public Suchtbaum() {
    this(null);
  }

  public void insert(T element) throws InterruptedException {

    rw.startWrite();
    try {

      if (root == null) {
        root = new SuchtbaumElement(element);
      } else {
        SuchtbaumElement parent = findParentToInsertInSubTree(element, root);

        int cmp = element.compareTo(parent.element);

        if (cmp == 0) {
          throw new RuntimeException("Element already in tree");
        }

        if (cmp < 0) {
          parent.left = new SuchtbaumElement(element);
        } else {
          parent.right = new SuchtbaumElement(element);
        }
      }
    } finally {
      rw.endWrite();
    }
  }

  public boolean contains(T element) throws InterruptedException {

    boolean res;

    rw.startRead();
    try {

      if (root == null) {
        return false;
      }
      SuchtbaumElement parent = findParentToInsertInSubTree(element, root);
      res = element.compareTo(parent.element) == 0;

    } finally {
      rw.endRead();
    }
    return res;

  }

  private SuchtbaumElement findParentToInsertInSubTree(T element, SuchtbaumElement subTreeRoot) {
    int cmp = element.compareTo(subTreeRoot.element);

    if (cmp == 0) {
      return subTreeRoot;
    }
    if (cmp < 0) {
      return subTreeRoot.left == null ?
          subTreeRoot :
          findParentToInsertInSubTree(element, subTreeRoot.left);
    }
    return subTreeRoot.right == null ?
        subTreeRoot :
        findParentToInsertInSubTree(element, subTreeRoot.right);
  }

  public void remove(T element) throws InterruptedException {

    //System.out.println(Thread.currentThread().getName() + ": remove(): " + element.toString());

    rw.startWrite();

    try {
      if (root == null) {
        throw new RuntimeException("Element nicht enthalten");
      }

      /*
      if (root.element.compareTo(element) == 0) {
        root = null;
        return;
      }
      */
      SuchtbaumElement parent = findParentInSubTree(element, null, root);

      /*
      if (parent == null) {
        throw new RuntimeException("Element nicht enthalten");
      }
      */

      SuchtbaumElement tElem =
          parent == null ? root : parent.left == null || parent.left.element.compareTo(element) != 0 ? parent.right
              : parent.left;

      if (tElem.left == null && tElem.right == null) {
        if(parent == null){
          root = null;
        } else if (parent.left == tElem) {
          parent.left = null;
        } else {
          parent.right = null;
        }
      } else if (tElem.left != null && tElem.right != null) {
        SuchtbaumElement maxElem = findMaxInSubTree(tElem.left);

        //System.out.println("recRemove");
        //remove(maxElem.element);

        //System.out.println("maxElem: " + maxElem.element);

        SuchtbaumElement maxElemParent = findParentInSubTree(maxElem.element, tElem, tElem.left);

        //System.out.println("maxElemParent: " + maxElemParent.element);
        //System.out.println("elem: " + element);


        if(maxElemParent.element.compareTo(element) == 0) {
          maxElemParent.left = maxElem.left;
        } else {
          maxElemParent.right = maxElem.left;
        }

        if (parent == null){
          root = new SuchtbaumElement(maxElem.element, tElem.left, tElem.right);
        } else if (parent.left == tElem) {
          parent.left = new SuchtbaumElement(maxElem.element, tElem.left, tElem.right);
        } else {
          parent.right = new SuchtbaumElement(maxElem.element, tElem.left, tElem.right);
        }
      } else {
        SuchtbaumElement child = tElem.left;
        if (tElem.right != null) {
          child = tElem.right;
        }

        if(parent == null){
          root = child;
        } else if (parent.left == tElem) {
          parent.left = child;
        } else {
          parent.right = child;
        }
      }
    } finally {
      rw.endWrite();
    }
  }

  private SuchtbaumElement findMaxInSubTree(SuchtbaumElement subTreeRoot) {
    SuchtbaumElement cur = subTreeRoot;
    SuchtbaumElement lastCur = cur;
    while (cur != null) {
      lastCur = cur;
      cur = cur.right;
    }
    return lastCur;
  }

  private SuchtbaumElement findParentInSubTree(T element, SuchtbaumElement parent,
      SuchtbaumElement subTreeRoot) {
    int cmp = element.compareTo(subTreeRoot.element);

    if (cmp == 0) {
      return parent;
    }
    if (cmp < 0) {
      if (subTreeRoot.left == null) {
        throw new RuntimeException("Element not in Tree (" + element.toString() + ")");
      }
      return findParentInSubTree(element, subTreeRoot, subTreeRoot.left);
    }

    if (subTreeRoot.right == null) {
      throw new RuntimeException("Element not in Tree (" + element.toString() + ")");
    }
    return findParentInSubTree(element, subTreeRoot, subTreeRoot.right);
  }

  public SuchtbaumElement getRoot() throws InterruptedException {
    rw.startRead();

    synchronized (rw) {
      rw.endRead();
      return root;
    }
  }

  @Override
  public String toString() {
    System.out.println(Thread.currentThread().getName() + ": toString()");
    StringBuilder res = new StringBuilder("digraph G {\n");

    try {
      rw.startRead();
    } catch (InterruptedException ignored) {
    }
    try {

      if (root != null) {

        Stack<SuchtbaumElement> elemStack = new Stack<>();
        elemStack.push(root);

        while (elemStack.size() > 0) {

          SuchtbaumElement cur = elemStack.pop();
          res.append(cur.element.toString()).append(";\n");

          if (cur.left != null) {
            res.append(cur.element.toString())
                .append(" -> ")
                .append(cur.left.element.toString())
                .append(" [label=left];\n");
          }

          if (cur.right != null) {
            res.append(cur.element.toString())
                .append(" -> ")
                .append(cur.right.element.toString())
                .append(" [label=right];\n");
            elemStack.push(cur.right);
          }

          if (cur.left != null) {
            elemStack.push(cur.left);
          }
        }
      }
      res.append("}");
    } finally {
      rw.endRead();
    }
    return res.toString();
  }

  public String subtreeToString(StringBuilder res, SuchtbaumElement root) {
    res.append(root.element.toString()).append(";\n");
    if (root.left != null) {
      res.append(root.element.toString())
          .append(" -> ")
          .append(root.left.element.toString())
          .append(" [label=left];\n")
          .append(subtreeToString(res, root.left));
    }
    if (root.right != null) {
      res.append(root.element.toString())
          .append(" -> ")
          .append(root.right.element.toString())
          .append(" [label=right];\n")
          .append(subtreeToString(res, root.right));
    }
    return res.toString();
  }
}

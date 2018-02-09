package Blatt12.Aufg12p5;

public class SuchtbaumRec<T extends Comparable<T>> {

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

  private SuchtbaumElement root = null;

  public void insert(T element) throws InterruptedException {
    if (root == null) {
      root = new SuchtbaumElement(element);
    } else {
      SuchtbaumElement parent = findParentElementInSubTree(element, root);

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
  }

  public boolean contains(T element) throws InterruptedException {
    if (root == null) {
      return false;
    }
    SuchtbaumElement parent = findParentElementInSubTree(element, root);

    return element.compareTo(parent.element) == 0;

  }

  private SuchtbaumElement findParentElementInSubTree(T element, SuchtbaumElement subTreeRoot) {
    int cmp = element.compareTo(subTreeRoot.element);

    if (cmp == 0) {
      return subTreeRoot;
    }
    if (cmp < 0) {
      return subTreeRoot.left == null ?
          subTreeRoot :
          findParentElementInSubTree(element, subTreeRoot.left);
    }
    return subTreeRoot.right == null ?
        subTreeRoot :
        findParentElementInSubTree(element, subTreeRoot.right);
  }

  public void remove(T element) throws InterruptedException {

  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder("digraph G {\n");
    res.append(subtreeToString(res, root)).append("}");
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

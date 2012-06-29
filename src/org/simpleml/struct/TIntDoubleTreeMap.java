package org.simpleml.struct;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.*;

/**
 * TODO: Unsupported Operations
 *
 * @author sitfoxfly
 */
public class TIntDoubleTreeMap implements TIntDoubleMap {

    private static class TreeNode {

        private static final String NODE_NAME = "O";
        private static final String NULL = "X";

        private static enum Color {
            RED, BLACK;

            private final String shortColor;

            private Color() {
                this.shortColor = this.toString().substring(0, 1);
            }

            public String getShortColor() {
                return shortColor;
            }
        }

        private TreeNode parent;
        private TreeNode left;
        private TreeNode right;

        private Color color;

        private double value;
        private int key;

        public TreeNode(int key, double value, Color color, TreeNode parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("( TreeNode ");
            sb.append("{").append(key).append(": ").append(value).append("} ");
            sb.append("[").append(color.getShortColor()).append("]");
            sb.append(" l: ").append(left == null ? NULL : NODE_NAME);
            sb.append(" r: ").append(right == null ? NULL : NODE_NAME);
            sb.append(" )");
            return sb.toString();
        }
    }

    private TreeNode root;

    private int size;

    public TIntDoubleTreeMap() {
        root = null;
        size = 0;
    }

    private static TreeNode getSibling(TreeNode node) {
        TreeNode parent = node.parent;
        if (parent.left == node) {
            return parent.right;
        } else {
            return parent.left;
        }
    }

    private static TreeNode getGrandparent(TreeNode node) {
        if (node != null && node.parent != null) {
            return node.parent.parent;
        }
        return null;
    }

    private static TreeNode getUncle(TreeNode node) {
        TreeNode grandparent = getGrandparent(node);
        if (grandparent == null) {
            return null;
        }

        if (grandparent.left == node.parent) {
            return grandparent.right;
        } else {
            return grandparent.left;
        }
    }

    private void insertCase1(TreeNode node) {
        if (node.parent == null) {
            node.color = TreeNode.Color.BLACK;
        } else {
            insertCase2(node);
        }
    }

    private void insertCase2(TreeNode node) {
        if (node.parent.color != TreeNode.Color.BLACK) {
            insertCase3(node);
        }
    }

    private void insertCase3(TreeNode node) {
        TreeNode uncle = getUncle(node);
        if (uncle != null && uncle.color == TreeNode.Color.RED) {
            node.parent.color = TreeNode.Color.BLACK;
            uncle.color = TreeNode.Color.BLACK;
            TreeNode grandparent = uncle.parent;
            grandparent.color = TreeNode.Color.RED;
            insertCase1(grandparent);
        } else {
            insertCase4(node);
        }
    }

    private void insertCase4(TreeNode node) {
        TreeNode grandparent = getGrandparent(node);
        TreeNode parent = node.parent;
        if (node == parent.right && parent == grandparent.left) {
            rotateLeft(parent);
            node = node.left;
        } else if (node == parent.left && parent == grandparent.right) {
            rotateRight(parent);
            node = node.right;
        }
        insertCase5(node);
    }

    private void insertCase5(TreeNode node) {
        TreeNode grandparent = getGrandparent(node);

        node.parent.color = TreeNode.Color.BLACK;
        grandparent.color = TreeNode.Color.RED;
        if (node == node.parent.left && node.parent == grandparent.left) {
            rotateRight(grandparent);
        } else {
            rotateLeft(grandparent);
        }
    }

    private void rotateLeft(TreeNode node) {
        if (node.parent != null) {
            final TreeNode parent = node.parent;
            final TreeNode rightChild = node.right;
            if (parent.right == node) {
                parent.right = rightChild;
            } else if (parent.left == node) {
                parent.left = rightChild;
            } else {
                throw new AssertionError("Node has illegal parent");
            }
            node.right = rightChild.left;
            if (rightChild.left != null) {
                rightChild.left.parent = node;
            }
            rightChild.left = node;
            rightChild.parent = parent;
            node.parent = rightChild;
        } else {
            final TreeNode rightChild = node.right;
            node.right = rightChild.left;
            if (rightChild.left != null) {
                rightChild.left.parent = node;
            }
            node.parent = rightChild;
            rightChild.left = node;
            rightChild.parent = null;
            root = rightChild;
        }
    }

    private void rotateRight(TreeNode node) {
        if (node.parent != null) {
            final TreeNode parent = node.parent;
            final TreeNode leftChild = node.left;
            if (parent.right == node) {
                parent.right = leftChild;
            } else if (parent.left == node) {
                parent.left = leftChild;
            } else {
                throw new AssertionError("Node has illegal parent");
            }
            node.left = leftChild.right;
            if (leftChild.right != null) {
                leftChild.right.parent = node;
            }
            leftChild.right = node;
            leftChild.parent = parent;
            node.parent = leftChild;
        } else {
            final TreeNode leftChild = node.left;
            node.left = leftChild.right;
            if (leftChild.right != null) {
                leftChild.right.parent = node;
            }
            node.parent = leftChild;
            leftChild.right = node;
            leftChild.parent = null;
            root = leftChild;
        }
    }

    public double put(int key, double value, boolean replaceOldValue) {
        TreeNode parent = root;
        while (parent != null) {
            if (parent.key > key) {
                if (parent.left == null) {
                    break;
                }
                parent = parent.left;
            } else if (parent.key < key) {
                if (parent.right == null) {
                    break;
                }
                parent = parent.right;
            } else {
                final double prevValue = parent.value;
                if (replaceOldValue) {
                    parent.value = value;
                }
                return prevValue;
            }
        }
        TreeNode newNode = new TreeNode(key, value, TreeNode.Color.RED, parent);
        if (parent == null) {
            root = newNode;
        } else if (parent.key > key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        insertCase1(newNode);
        size++;
        return getNoEntryValue();
    }

    private void deleteCase1(TreeNode node) {
        if (node.parent != null) {
            deleteCase2(node);
        }
    }

    private void deleteCase2(TreeNode node) {
        TreeNode sibling = getSibling(node);
        if (sibling.color == TreeNode.Color.RED) {
            node.parent.color = TreeNode.Color.RED;
            sibling.color = TreeNode.Color.BLACK;
            if (node == node.parent.left) {
                rotateLeft(node.parent);
            } else {
                rotateRight(node.parent);
            }
        }
        deleteCase3(node);
    }

    private void deleteCase3(TreeNode node) {
        TreeNode sibling = getSibling(node);

        if (node.parent.color == TreeNode.Color.BLACK && sibling.color == TreeNode.Color.BLACK
                && sibling.left.color == TreeNode.Color.BLACK && sibling.right.color == TreeNode.Color.BLACK) {
            sibling.color = TreeNode.Color.RED;
            deleteCase1(node.parent);
        } else
            deleteCase4(node);
    }

    private void deleteCase4(TreeNode node) {
        TreeNode sibling = getSibling(node);

        if (node.parent.color == TreeNode.Color.RED && sibling.color == TreeNode.Color.BLACK
                && sibling.left.color == TreeNode.Color.BLACK && sibling.right.color == TreeNode.Color.BLACK) {
            sibling.color = TreeNode.Color.RED;
            node.parent.color = TreeNode.Color.BLACK;
            return;
        }
        deleteCase5(node);
    }

    private void deleteCase5(TreeNode node) {
        TreeNode sibling = getSibling(node);

        if (sibling.color == TreeNode.Color.BLACK) {
            if ((node == node.parent.left) &&
                    (sibling.right.color == TreeNode.Color.BLACK) &&
                    (sibling.left.color == TreeNode.Color.RED)) {
                sibling.color = TreeNode.Color.RED;
                sibling.left.color = TreeNode.Color.BLACK;
                rotateRight(sibling);
            } else if ((node == node.parent.right) &&
                    (sibling.left.color == TreeNode.Color.BLACK) &&
                    (sibling.right.color == TreeNode.Color.RED)) {
                sibling.color = TreeNode.Color.RED;
                sibling.right.color = TreeNode.Color.BLACK;
                rotateLeft(sibling);
            }
        }
        deleteCase6(node);
    }

    private void deleteCase6(TreeNode node) {
        TreeNode sibling = getSibling(node);

        sibling.color = node.parent.color;
        node.parent.color = TreeNode.Color.BLACK;

        if (node == node.parent.left) {
            sibling.right.color = TreeNode.Color.BLACK;
            rotateLeft(node.parent);
        } else {
            sibling.left.color = TreeNode.Color.BLACK;
            rotateRight(node.parent);
        }
    }

    public double get(int key) {
        TreeNode node = search(key);
        if (node == null) {
            throw new IllegalArgumentException("Key: " + key + " is not presented");
        }
        return node.value;
    }

    private TreeNode search(int key) {
        TreeNode node = root;
        while (node != null) {
            if (node.key == key) {
                return node;
            } else if (node.key > key) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    @Override
    public double remove(int key) {
        TreeNode node = search(key);
        if (node == null) {
            return getNoEntryValue();
        }
        double result = node.value;
        remove(node);
        size--;
        return result;
    }

    private void remove(TreeNode node) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            if (node == root) {
                root = null;
            } else if (node.parent.left == node) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
        } else if (node.left != null && node.right != null) {
            removeNodeWithTwoChildren(node);
        } else {
            removeNodeWithOneChild(node);
        }
    }

    private void removeNodeWithOneChild(TreeNode node) {
        TreeNode child = node.left == null ? node.right : node.left;

        if (node == root) {
            root = child;
            child.parent = null;
        } else if (node.parent.left == node) {
            node.parent.left = child;
            child.parent = node.parent.parent;
        } else {
            node.parent.right = child;
            child.parent = node.parent.parent;
        }

        if (node.color == TreeNode.Color.BLACK) {
            if (child.color == TreeNode.Color.RED) {
                child.color = TreeNode.Color.BLACK;
            } else {
                deleteCase1(child);
            }
        }
    }

    private void removeNodeWithTwoChildren(TreeNode node) {
        TreeNode replaceNode = node.left;
        while (replaceNode.left != null) {
            replaceNode = replaceNode.left;
        }
        int tmpKey = replaceNode.key;
        double tmpValue = replaceNode.value;
        replaceNode.key = node.key;
        replaceNode.value = node.value;
        node.key = tmpKey;
        node.value = tmpValue;
        remove(replaceNode);
    }

    @Override
    public double putIfAbsent(int i, double v) {
        return put(i, v, false);
    }

    @Override
    public double put(int i, double v) {
        return put(i, v, true);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean increment(int i) {
        return adjustValue(i, 1d);
    }

    @Override
    public boolean adjustValue(int i, double v) {
        TreeNode node = search(i);
        if (node == null) {
            return false;
        }
        node.value += v;
        return true;
    }

    @Override
    public double adjustOrPutValue(int i, double v, double v1) {
        TreeNode node = search(i);
        if (node == null) {
            put(i, v1);
            return v1;
        }
        node.value += v;
        return node.value;
    }

    @Override
    public boolean containsKey(int i) {
        return search(i) != null;
    }

    @Override
    public TIntDoubleIterator iterator() {
        return new TreeIterator(this);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(TIntDoubleMap tIntDoubleMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public TIntSet keySet() {
        return new TIntHashSet(keys());
    }

    @Override
    public int[] keys() {
        TreeIterator iterator = new TreeIterator(this);
        int[] keys = new int[size];
        int i = 0;
        while (iterator.hasNext()) {
            iterator.advance();
            keys[i] = iterator.key();
            i++;
        }
        return keys;
    }

    @Override
    public int[] keys(int[] ints) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TDoubleCollection valueCollection() {
        return new TDoubleArrayList(values());
    }

    @Override
    public double[] values() {
        TreeIterator iterator = new TreeIterator(this);
        double[] values = new double[size];
        int i = 0;
        while (iterator.hasNext()) {
            iterator.advance();
            values[i] = iterator.value();
            i++;
        }
        return values;
    }

    @Override
    public double[] values(double[] doubles) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(double v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean forEachKey(TIntProcedure tIntProcedure) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean forEachValue(TDoubleProcedure tDoubleProcedure) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean forEachEntry(TIntDoubleProcedure tIntDoubleProcedure) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void transformValues(TDoubleFunction tDoubleFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainEntries(TIntDoubleProcedure tIntDoubleProcedure) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNoEntryKey() {
        return 0;
    }

    @Override
    public double getNoEntryValue() {
        return 0d;
    }

    public static class TreeIterator implements TIntDoubleIterator {

        private Iterator<TreeNode> innerIterator;
        private TIntDoubleTreeMap tree;

        private TreeNode currentNode = null;

        public TreeIterator(TIntDoubleTreeMap tree) {
            this.tree = tree;
            List<TreeNode> nodes = new ArrayList<TreeNode>(tree.size);
            Stack<TreeNode> stack = new Stack<TreeNode>();
            TreeNode node = tree.root;
            while (node != null) {
                while (node.left != null) {
                    stack.add(node);
                    node = node.left;
                }
                stack.add(node);
                while (!stack.isEmpty()) {
                    node = stack.pop();
                    nodes.add(node);
                    if (node.right != null) {
                        break;
                    }
                }
                node = node.right;
            }
            innerIterator = nodes.iterator();
        }

        @Override
        public int key() {
            return currentNode.key;
        }

        @Override
        public double value() {
            return currentNode.value;
        }

        @Override
        public double setValue(double v) {
            return currentNode.value = v;
        }

        @Override
        public void advance() {
            currentNode = innerIterator.next();
        }

        @Override
        public boolean hasNext() {
            return innerIterator.hasNext();
        }

        @Override
        public void remove() {
            tree.remove(currentNode);
        }
    }

    public static void main(String[] args) {
        TIntDoubleTreeMap map = new TIntDoubleTreeMap();

        for (int i = 0; i < 10; i += 2) {
            map.put(i, 2d * i);
        }

        final TIntDoubleIterator iterator = map.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            System.out.println(iterator.key() + " " + iterator.value());
        }
    }

}

package org.simpleml.struct;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;

import java.util.Map;

/**
 * TODO: Unsupported Operations
 *
 * @author sitfoxfly
 */
public class TIntDoubleTreeMap implements TIntDoubleMap {

    private static class TreeNode {

        private static enum Color {
            RED, BLACK
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

    }

    private TreeNode root;

    public TIntDoubleTreeMap() {
        root = null;
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

    private static void insertCase1(TreeNode node) {
        if (node.parent == null) {
            node.color = TreeNode.Color.BLACK;
        } else {
            insertCase2(node);
        }
    }

    private static void insertCase2(TreeNode node) {
        if (node.parent.color != TreeNode.Color.BLACK) {
            insertCase3(node);
        }
    }

    private static void insertCase3(TreeNode node) {
        TreeNode uncle = getUncle(node);
        if (uncle != null && uncle.color == TreeNode.Color.RED) {
            node.parent.color = TreeNode.Color.BLACK;
            uncle.color = TreeNode.Color.BLACK;
            TreeNode grandparent = uncle.parent;
            grandparent.color = TreeNode.Color.RED;
            insertCase1(node);
        } else {
            insertCase4(node);
        }
    }

    private static void insertCase4(TreeNode node) {
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

    private static void insertCase5(TreeNode node) {
        TreeNode grandparent = getGrandparent(node);

        node.parent.color = TreeNode.Color.BLACK;
        grandparent.color = TreeNode.Color.RED;
        if (node == node.parent.left && node.parent == grandparent.left) {
            rotateRight(grandparent);
        } else {
            rotateLeft(grandparent);
        }
    }

    private static void rotateLeft(TreeNode node) {
        final TreeNode parent = node.parent;
        final TreeNode rightChild = node.right;
        parent.left = rightChild;
        node.right = rightChild.left;
        rightChild.left = node;
        rightChild.parent = parent;
        node.parent = rightChild;
    }

    private static void rotateRight(TreeNode node) {
        final TreeNode parent = node.parent;
        final TreeNode leftChild = node.left;
        parent.left = leftChild;
        node.left = leftChild.right;
        leftChild.right = node;
        leftChild.parent = parent;
        node.parent = leftChild;
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
        insertCase1(new TreeNode(key, value, TreeNode.Color.RED, parent));
        return getNoEntryValue();
    }

    private static void deleteCase1(TreeNode node) {
        if (node.parent != null) {
            deleteCase2(node);
        }
    }

    private static void deleteCase2(TreeNode node) {
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

    private static void deleteCase3(TreeNode node) {
        TreeNode sibling = getSibling(node);

        if (node.parent.color == TreeNode.Color.BLACK && sibling.color == TreeNode.Color.BLACK
                && sibling.left.color == TreeNode.Color.BLACK && sibling.right.color == TreeNode.Color.BLACK) {
            sibling.color = TreeNode.Color.RED;
            deleteCase1(node.parent);
        } else
            deleteCase4(node);
    }

    private static void deleteCase4(TreeNode node) {
        TreeNode sibling = getSibling(node);

        if (node.parent.color == TreeNode.Color.RED && sibling.color == TreeNode.Color.BLACK
                && sibling.left.color == TreeNode.Color.BLACK && sibling.right.color == TreeNode.Color.BLACK) {
            sibling.color = TreeNode.Color.RED;
            node.parent.color = TreeNode.Color.BLACK;
            return;
        }
        deleteCase5(node);
    }

    private static void deleteCase5(TreeNode node) {
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

    private static void deleteCase6(TreeNode node) {
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
            if (root.key == key) {
                return root;
            } else if (root.key > key) {
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
        double result = node.value;
        remove(node);
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public TIntSet keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] keys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] keys(int[] ints) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TDoubleCollection valueCollection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] values() {
        throw new UnsupportedOperationException();
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

}

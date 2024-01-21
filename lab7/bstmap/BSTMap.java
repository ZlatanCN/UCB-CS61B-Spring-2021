package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;
    private Set<K> keySet;

    public BSTMap() {
        root = null;
        size = 0;
        keySet = new java.util.HashSet<>();
    }

    private class BSTNode {
        private  K key;
        private V value;
        private BSTNode leftChild;
        private BSTNode rightChild;

        private BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
        }

        private V get(K key) {
            if (this.key == null) {
                return null;
            }
            if (key.equals(this.key)) {
                return this.value;
            } else if (key.compareTo(this.key) < 0 && leftChild != null) {
                return leftChild.get(key);
            } else if (key.compareTo(this.key) > 0 && rightChild != null) {
                return rightChild.get(key);
            }
            return null;
        }

        private void put(K key, V value) {
            BSTNode newNode = new BSTNode(key, value);
            if (key.equals(this.key)) {
                this.value = value;
            } else if (key.compareTo(this.key) < 0) {
                if (this.leftChild == null) {
                    this.leftChild = newNode;
                } else {
                    this.leftChild.put(key, value);
                }
            } else {
                if (this.rightChild == null) {
                    this.rightChild = newNode;
                } else {
                    this.rightChild.put(key, value);
                }
            }
        }

        private void printInOrder(BSTNode tree) {
            BSTNode p = tree;
            if (p == null) {
                return;
            }
            printInOrder(p.leftChild);
            System.out.println(p.key);
            printInOrder(p.rightChild);
        }

        private V remove(K key, BSTNode parent) {
            V removedValue;
            if (parent == null && this.leftChild == null && this.rightChild == null) {
                removedValue = this.value;
                root = null;
            } else {
                if (key.compareTo(this.key) < 0) {
                    return leftChild.remove(key, this);
                } else if (key.compareTo(this.key) > 0) {
                    return rightChild.remove(key, this);
                } else {
                    removedValue = value;
                    if (leftChild == null && rightChild == null) {
                        clearNode(this, parent);
                    } else if (leftChild != null && rightChild == null) {
                        reset(this, this.leftChild);
                    } else if (leftChild == null && rightChild != null) {
                        reset(this, this.rightChild);
                    } else {
                        BSTNode maxInLeft = getMaxNode(this.leftChild);
                        this.key = maxInLeft.key;
                        this.value = maxInLeft.value;
                        if (maxInLeft.leftChild == null) {
                            clearNode(maxInLeft, parent);
                        } else {
                            reset(maxInLeft, maxInLeft.leftChild);
                        }
                    }
                }
            }
            return removedValue;
        }

        private void clearNode(BSTNode node, BSTNode parent) {
            if (parent.leftChild != null && parent.leftChild.equals(node)) {
                parent.leftChild = null;
            } else if (parent.rightChild != null && parent.rightChild.equals(node)) {
                parent.rightChild = null;
            }
        }

        private void reset(BSTNode srcNode, BSTNode dstNode) {
            srcNode.key = dstNode.key;
            srcNode.value = dstNode.value;
            srcNode.leftChild = dstNode.leftChild;
            srcNode.rightChild = dstNode.rightChild;
        }

        private BSTNode getMaxNode(BSTNode node) {
            if (node.rightChild == null) {
                return node;
            }
            return getMaxNode(node.rightChild);
        }
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
        this.keySet = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (this.keySet == null) {
            return false;
        }
        return this.keySet.contains(key);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        return root.get(key);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        BSTNode newNode = new BSTNode(key, value);
        if (this.root == null) {
            this.root = newNode;
        } else {
            root.put(key, value);
        }
        this.size += 1;
        this.keySet.add(key);
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (root == null || !keySet.contains(key)) {
            return null;
        }
        this.keySet.remove(key);
        this.size -= 1;
        return root.remove(key, null);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("UnsupportedOperationException");
    }

    public void printInOrder() {
        if (root == null) {
            return;
        }
        root.printInOrder(root);
    }
}

package com.example.questions.binary_tree;


/**
 * Created by singh on 3/3/18.
 */

public class BinaryTree {

    private Node root;

    public static final String POST_ORDER = "post";
    public static final String PRE_ORDER = "pre";
    public static final String IN_ORDER = "in";
    private String order;

    BinaryTree(String order) {
        this.order = order;
    }

    public void add(int value) {
        root = addNode(root, value);
    }

    private Node addNode(Node current, int data) {

        if (current == null) {
            current = new Node(data);
            return current;
        }

        if(data<current.data) current.left = addNode(current.left, data);
        else if (data>current.data) current.right = addNode(current.right,data);
        else return current;

        return current;
    }

    private void print() {


    }

    private void balanceTree() {

    }

}

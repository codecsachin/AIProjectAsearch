package com.example.aiprojectasearch;


public class Node {
    int[][] board;
    int g, h, f;
    Node parent;
    String move;

    public Node(int[][] board, int g, int h, Node parent, String move) {
        this.board = board;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
        this.move = move;
    }
}
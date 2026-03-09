package com.example.aiprojectasearch;

import java.util.*;

public class PuzzleUtils {

    static int[][] GOAL = {
            {1,2,3},
            {4,5,6},
            {7,8,0}
    };

    public static int manhattan(int[][] board) {
        int dist = 0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                int val = board[i][j];
                if(val != 0){
                    int x = (val-1)/3;
                    int y = (val-1)%3;
                    dist += Math.abs(i-x) + Math.abs(j-y);
                }
            }
        }
        return dist;
    }

    public static boolean isGoal(int[][] board) {
        return Arrays.deepEquals(board, GOAL);
    }

    public static String serialize(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for(int[] r : board)
            for(int c : r)
                sb.append(c);
        return sb.toString();
    }

    public static List<Node> neighbors(Node node) {
        List<Node> list = new ArrayList<>();
        int[][] b = node.board;
        int zx=0, zy=0;

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(b[i][j]==0){ zx=i; zy=j; }

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        String[] moves = {"U","D","L","R"};

        for(int k=0;k<4;k++){
            int nx = zx + dirs[k][0];
            int ny = zy + dirs[k][1];
            if(nx>=0 && ny>=0 && nx<3 && ny<3){
                int[][] nb = copy(b);
                nb[zx][zy] = nb[nx][ny];
                nb[nx][ny] = 0;
                list.add(new Node(nb, node.g+1,
                        manhattan(nb), node, moves[k]));
            }
        }
        return list;
    }

    private static int[][] copy(int[][] b) {
        int[][] n = new int[3][3];
        for(int i=0;i<3;i++) n[i] = b[i].clone();
        return n;
    }
}
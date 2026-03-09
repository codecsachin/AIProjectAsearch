package com.example.aiprojectasearch;

import java.util.*;

public class AStarSolver {

    public static List<String> solve(int[][] start) {

        PriorityQueue<Node> pq =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<String> visited = new HashSet<>();

        pq.add(new Node(start, 0,
                PuzzleUtils.manhattan(start), null, ""));

        while(!pq.isEmpty()){
            Node cur = pq.poll();

            if(PuzzleUtils.isGoal(cur.board)){
                return path(cur);
            }

            visited.add(PuzzleUtils.serialize(cur.board));

            for(Node nb : PuzzleUtils.neighbors(cur)){
                if(!visited.contains(
                        PuzzleUtils.serialize(nb.board))){
                    pq.add(nb);
                }
            }
        }
        return new ArrayList<>();
    }

    private static List<String> path(Node n){
        List<String> p = new ArrayList<>();
        while(n.parent != null){
            p.add(n.move);
            n = n.parent;
        }
        Collections.reverse(p);
        return p;
    }
}
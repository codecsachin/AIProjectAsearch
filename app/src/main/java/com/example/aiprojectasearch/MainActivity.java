package com.example.aiprojectasearch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    GridLayout grid;
    Button[] tiles = new Button[9];

    int[][] board = {
            {1,2,3},
            {4,5,6},
            {7,8,0}
    };

    int moves = 0;
    boolean animating = false;

    ArrayList<String> playerMoves = new ArrayList<>();

    TextView tvMoves, tvHeuristic, tvOptimal, tvEfficiency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = findViewById(R.id.grid);
        tvMoves = findViewById(R.id.tvMoves);
        tvHeuristic = findViewById(R.id.tvHeuristic);
        tvOptimal = findViewById(R.id.tvOptimal);
        tvEfficiency = findViewById(R.id.tvEfficiency);

        createGrid();

        findViewById(R.id.btnShuffle).setOnClickListener(v -> shuffle());
        findViewById(R.id.btnHint).setOnClickListener(v -> hint());
        findViewById(R.id.btnSolve).setOnClickListener(v -> solve());
    }

    void createGrid() {
        grid.removeAllViews();
        for(int i=0;i<9;i++){
            Button b = new Button(this);
            tiles[i] = b;
            b.setTextSize(24f);
            final int idx = i;
            b.setOnClickListener(v -> move(idx));
            grid.addView(b);
        }
        refresh();
    }

    void refresh() {
        int k=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                tiles[k++].setText(board[i][j]==0?"":""+board[i][j]);

        tvMoves.setText("Moves: " + moves);
        tvHeuristic.setText("Heuristic: " +
                PuzzleUtils.manhattan(board));
    }

    void move(int idx) {
        if(animating) return;

        int r = idx/3, c = idx%3;
        int[] z = zero();

        if(Math.abs(z[0]-r)+Math.abs(z[1]-c)==1){
            if(z[0]==r+1) playerMoves.add("U");
            if(z[0]==r-1) playerMoves.add("D");
            if(z[1]==c+1) playerMoves.add("L");
            if(z[1]==c-1) playerMoves.add("R");

            board[z[0]][z[1]] = board[r][c];
            board[r][c] = 0;

            moves++;
            refresh();
        }
    }

    int[] zero(){
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(board[i][j]==0) return new int[]{i,j};
        return null;
    }

    void shuffle() {
        moves = 0;
        playerMoves.clear();
        tvOptimal.setText("Optimal: - | Player: -");
        tvEfficiency.setText("Efficiency: -");

        Random r = new Random();
        for(int i=0;i<100;i++){
            List<Node> n =
                    PuzzleUtils.neighbors(
                            new Node(board,0,0,null,""));
            board = n.get(r.nextInt(n.size())).board;
        }
        refresh();
    }

    void hint() {
        List<String> sol = AStarSolver.solve(board);
        if(!sol.isEmpty())
            Toast.makeText(this,
                    "Hint: " + sol.get(0),
                    Toast.LENGTH_SHORT).show();
    }

    void applyMove(String m) {
        int[] z = zero();
        int x=z[0], y=z[1];

        if(m.equals("U")) swap(x,y,x-1,y);
        if(m.equals("D")) swap(x,y,x+1,y);
        if(m.equals("L")) swap(x,y,x,y-1);
        if(m.equals("R")) swap(x,y,x,y+1);

        refresh();
    }

    void solve() {
        List<String> sol = AStarSolver.solve(board);
        if(sol.isEmpty()) return;

        int optimal = sol.size();
        int player = moves;

        tvOptimal.setText("Optimal: " + optimal + " | Player: " + player);

        double eff = (player==0)?100:((double)optimal/player)*100;
        tvEfficiency.setText(
                "Efficiency: " + String.format("%.2f", eff) + "%"
        );

        animating = true;
        Handler h = new Handler(Looper.getMainLooper());

        for(int i=0;i<sol.size();i++){
            int idx = i;
            h.postDelayed(() -> {
                applyMove(sol.get(idx));
                if(idx == sol.size()-1) animating = false;
            }, i*500);
        }
    }

    void swap(int x1,int y1,int x2,int y2){
        int t = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = t;
    }
}
package com.example.navneetnandan.atry;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    GridAdapter gridAdapter;
    int currentX;
    int currentY;
    int currentEnergy;
    int indexfirst;
    int indexsecond;
    int indexthird;
    Node[] initialNodes;
    ArrayList<Integer> userPath;
    private static final int[][] NEIGHBOUR_COORDS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_main);
        initialNodes = new Node[64];
        userPath=new ArrayList<>();
        game(null);

    }

    public void game(View v) {
        TextView tex=(TextView)findViewById(R.id.en);
        tex.setVisibility(View.VISIBLE);
        currentX = 0;
        currentY = 0;
        final Node[] allNodes = new Node[64];
        for (int i = 0; i < allNodes.length; i++) {
            allNodes[i] = new Node(i);
        }
        allNodes[0].isCurrent = true;
        allNodes[0].traversed = true;
        currentEnergy = 5;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        gridAdapter = new GridAdapter(this, allNodes, screenWidth);
        generate(allNodes);
        initialNodes = allNodes.clone();
        for (int i = 0; i < initialNodes.length; i++) {
            initialNodes[i] = new Node(i);
            initialNodes[i].setEnergy(allNodes[i].getEnergy()
            );
        }
        /*String[] data=new String[64];
        char c='a';
        for (int i=0;i<data.length;i++){
            data[i]=new String();
            data[i]+=c++;
        }Log.e("data",data.toString());*/
        final GridView gridView = (GridView) findViewById(R.id.gridview);
        Set<Integer> allNodesSet=new HashSet<>();
        for (int i=1;i<64;i++){
            allNodesSet.add(i);
        }
        Set<Integer> energyNodes=new HashSet<>();
        energyNodes.add(indexfirst);
        energyNodes.add(indexsecond);
        energyNodes.add(indexthird);
        Log.e("index1",indexfirst+"");
        Log.e("index2",indexsecond+"");
        Log.e("index3",indexthird+"");
        Set<Integer> shortestPathNodesSet=new HashSet<>();
        shortestPathNodesSet.addAll(getMinPath(allNodes));
        allNodesSet.removeAll(shortestPathNodesSet);
        allNodesSet.removeAll(energyNodes);
        Log.e("set",allNodesSet.toString());
        ArrayList<Integer> listOfNodesForObstacles=new ArrayList<>(allNodesSet);
        Log.e("size",listOfNodesForObstacles.size()+"");
        Random random=new Random();
        int numberOfObstacles=12+random.nextInt(5);
        for (int i=0;i<numberOfObstacles;i++){
            int indexForObstacle=random.nextInt(listOfNodesForObstacles.size());
            allNodes[listOfNodesForObstacles.get(indexForObstacle)].isObstacle=true;
        }
        gridAdapter.notifyDataSetChanged();
        initialNodes = allNodes.clone();
        for (int i = 0; i < initialNodes.length; i++) {
            initialNodes[i] = new Node(i);
            initialNodes[i].setEnergy(allNodes[i].getEnergy()
            );
            initialNodes[i].isObstacle=allNodes[i].isObstacle;
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tex=(TextView)findViewById(R.id.en);
                tex.setVisibility(View.INVISIBLE);
                Node temp = allNodes[i];
                userPath.add(i);
                if (distance(temp, allNodes[XYtoIndex(currentX, currentY)]) == 1&&!temp.isObstacle) {
                    allNodes[XYtoIndex(currentX, currentY)].isCurrent = false;
                    Log.e("Energy", currentEnergy + "");
                    currentEnergy += temp.getEnergy();
                    if (currentEnergy <= 0) {
                        Toast.makeText(getApplicationContext(), "You Lost", Toast.LENGTH_LONG).show();
                        game(null);
                    } else {
                        temp.setEnergy(-1);
                        temp.traversed = true;
                        currentX = temp.getX();
                        currentY = temp.getY();
                        if (XYtoIndex(currentX, currentY) == 63) {
                            Log.e("if",getSum(allNodes,getMinPath(allNodes))+" "+getSum(allNodes,userPath));
                            if (getSum(allNodes,userPath)<=getSum(allNodes,getMinPath(allNodes))){
                                Toast.makeText(getApplicationContext(), "You Won. Excellent", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "You Won. But Path is not good", Toast.LENGTH_LONG).show();
                            }
                            game(null);
                        }
                        temp.isCurrent = true;
                        gridAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        gridView.setAdapter(gridAdapter);
    }

    public void showSolution(View v) {
        Node[] allNodes = initialNodes;
        ArrayList<Integer> minPath = getMinPath(allNodes);

        for (int i = 0; i < minPath.size(); i++) {
            allNodes[minPath.get(i)].setPath(true);
        }
        gridAdapter.setData(allNodes);
        gridAdapter.notifyDataSetChanged();
    }

    @NonNull
    private ArrayList<Integer> getMinPath(Node[] allNodes) {
        GraphNode graphNode = new GraphNode(64);
        for (int i = 0; i < 64; i++) {
            for (int[] NEIGHBOUR_COORD : NEIGHBOUR_COORDS) {
                if (i / 8 + NEIGHBOUR_COORD[1] < 8 && i / 8 + NEIGHBOUR_COORD[1] >= 0 && i % 8 + NEIGHBOUR_COORD[0] < 8 && i % 8 + NEIGHBOUR_COORD[0] >= 0) {
                    graphNode.addEdge(i, i + NEIGHBOUR_COORD[0] + 8 * NEIGHBOUR_COORD[1]);
                }
            }
        }
//        Log.e("array",graphNode.BFS(0,35).toString());
        ArrayList<Integer> path = new ArrayList<>();
        ArrayList<Integer> pathToFirst = graphNode.BFS(0, indexfirst);
        Log.e("first", indexfirst + "");
        Collections.reverse(pathToFirst);
        path.addAll(pathToFirst);
        ArrayList<Integer> pathToSecond = graphNode.BFS(indexfirst, indexsecond);
        Collections.reverse(pathToSecond);
        path.addAll(pathToSecond);
        ArrayList<Integer> pathToThird = graphNode.BFS(indexsecond, indexthird);
        Collections.reverse(pathToThird);
        path.addAll(pathToThird);
        ArrayList<Integer> pathToFinal = graphNode.BFS(indexthird, 63);
        Collections.reverse(pathToFinal);
        path.addAll(pathToFinal);
        //alternative path 1 for(1->3->f)
        ArrayList<Integer> pathToThirdFromFirst = graphNode.BFS(indexfirst, indexthird);
        Collections.reverse(pathToThirdFromFirst);
        ArrayList<Integer> path1 = new ArrayList<>();
        int dist1 = distance(initialNodes[0], initialNodes[indexfirst]) + distance(initialNodes[indexfirst], initialNodes[indexthird]);
        if (dist1 <= 5 + initialNodes[indexfirst].getEnergy()) {
            path1.addAll(pathToFirst);//if feasible
            path1.addAll(pathToThirdFromFirst);
            path1.addAll(pathToFinal);
        }
        //alternative path 2 for(1->f)
        ArrayList<Integer> pathToFinalFromFirst = graphNode.BFS(indexfirst, 63);
        Collections.reverse(pathToFinalFromFirst);
        ArrayList<Integer> path2 = new ArrayList<>();
        dist1 = distance(initialNodes[0], initialNodes[indexfirst]) + distance(initialNodes[indexfirst], initialNodes[63]);
        if (dist1 <= 5 + initialNodes[indexfirst].getEnergy()) {
            path2.addAll(pathToFirst);//if feasible
            path2.addAll(pathToFinalFromFirst);
        }
        //alternative path 3 for(1->2->f)
        ArrayList<Integer> pathToFinalFromSecond = graphNode.BFS(indexsecond, 63);
        Collections.reverse(pathToFinalFromSecond);
        ArrayList<Integer> path3 = new ArrayList<>();
        dist1 = distance(initialNodes[indexsecond], initialNodes[63]);
        if (dist1 <= initialNodes[indexsecond].getEnergy()) {
            path3.addAll(pathToFirst);//if feasible
            path3.addAll(pathToSecond);
            path3.addAll(pathToFinalFromSecond);
        }
        int sum = getSum(allNodes, path);
        int sum1 = getSum(allNodes, path1);
        int sum2 = getSum(allNodes, path2);
        int sum3 = getSum(allNodes, path3);
        int min = sum;
        ArrayList<Integer> minPath = path;
        if (sum1 < min) {
            min = sum1;
            minPath = path1;
        }
        if (sum2 < min) {
            min = sum1;
            minPath = path2;
        }
        if (sum3 < min) {
            minPath = path3;
        }
        return minPath;
    }

    private int getSum(Node[] allNodes, ArrayList<Integer> path) {
        int sum = 0;
        if (path.isEmpty())
            return 1000;
        for (int i = 1; i < path.size(); i++) {
            sum += distance(allNodes[path.get(i - 1)], allNodes[path.get(i)]);
        }
        return sum;
    }

    void generate(Node[] nodes) {
        int[] arrayFor1st = {4, 11, 18, 25, 32, 5, 12, 19, 26, 33, 40};
        Random random = new Random();
        int randomIndex = random.nextInt(arrayFor1st.length);
        int indexOfEnergyNode1 = arrayFor1st[randomIndex];
        indexfirst = indexOfEnergyNode1;
        gridAdapter.notifyDataSetChanged();
        int[] arrayForLast = {23, 30, 37, 44, 51, 58, 31, 38, 45, 52, 59};
        randomIndex = random.nextInt(arrayForLast.length);
        int indexOfEnergyNodeLast = arrayForLast[randomIndex];
        indexthird = indexOfEnergyNodeLast;
        int finalIndex = 63;
        int sum = distance(nodes[indexOfEnergyNodeLast], nodes[finalIndex]);
        nodes[indexOfEnergyNodeLast].setEnergy(sum);
        //int distN1N2=distance(nodes[indexOfEnergyNode1],nodes[indexOfEnergyNodeLast]);
        int[] arrayForMiddle = {6, 13, 20, 27, 34, 41, 48, 7, 14, 21, 28, 35, 42, 49, 56, 15, 22, 29, 36, 43, 50, 57};
        randomIndex = random.nextInt(arrayForMiddle.length);
        int indexOfEnergyNodeMiddle = arrayForMiddle[randomIndex];
        indexsecond = indexOfEnergyNodeMiddle;
        nodes[indexOfEnergyNodeMiddle].setEnergy(distance(nodes[indexOfEnergyNodeMiddle], nodes[indexOfEnergyNodeLast]));
        nodes[indexOfEnergyNode1].setEnergy(distance(nodes[indexOfEnergyNodeMiddle], nodes[indexOfEnergyNode1]));
    }

    int distance(Node first, Node second) {
        int changeInX = Math.abs(first.getX() - second.getX());
        int changeInY = Math.abs(first.getY() - second.getY());
        return changeInX + changeInY;
    }

    int XYtoIndex(int X, int Y) {
        return Y * 8 + X;
    }

    public void credits(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        AlertDialog dialog=builder.setMessage("Developed by:\nNavneet Nandan\n Prashant Maurya\n Gautam Kumar").create();
        dialog.show();

    }

}

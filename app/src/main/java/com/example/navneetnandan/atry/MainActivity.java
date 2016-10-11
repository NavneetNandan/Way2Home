package com.example.navneetnandan.atry;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    GridAdapter gridAdapter;
    int currentX;
    int currentY;
    int currentEnergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentX = 0;
        currentY = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_main);
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
        /*String[] data=new String[64];
        char c='a';
        for (int i=0;i<data.length;i++){
            data[i]=new String();
            data[i]+=c++;
        }Log.e("data",data.toString());*/
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Node temp = allNodes[i];
                if (distance(temp, allNodes[XYtoIndex(currentX, currentY)]) == 1) {
                    allNodes[XYtoIndex(currentX, currentY)].isCurrent = false;
                    Log.e("Energy",currentEnergy+"");
                    currentEnergy += temp.getEnergy();
                    if (currentEnergy<=0){
                        Toast.makeText(getApplicationContext(),"You Lost",Toast.LENGTH_LONG).show();
                    }else{
                        temp.setEnergy(-1);
                        temp.traversed = true;
                        currentX = temp.getX();
                        currentY = temp.getY();
                        if(XYtoIndex(currentX,currentY)==63)
                            Toast.makeText(getApplicationContext(),"You Won",Toast.LENGTH_LONG).show();
                        temp.isCurrent = true;
                        gridAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        gridView.setAdapter(gridAdapter);
    }

    void generate(Node[] nodes) {
        int[] arrayFor1st = {4, 11, 18, 25, 32, 5, 12, 19, 26, 33, 40};
        Random random = new Random();
        int randomIndex = random.nextInt(arrayFor1st.length);
        int indexOfEnergyNode1 = arrayFor1st[randomIndex];
        gridAdapter.notifyDataSetChanged();
        int[] arrayForLast = {23, 30, 37, 44, 51, 58, 31, 38, 45, 52, 59};
        randomIndex = random.nextInt(arrayForLast.length);
        int indexOfEnergyNodeLast = arrayForLast[randomIndex];
        int finalIndex = 63;
        int sum = distance(nodes[indexOfEnergyNodeLast], nodes[finalIndex]);
        nodes[indexOfEnergyNodeLast].setEnergy(sum);
        //int distN1N2=distance(nodes[indexOfEnergyNode1],nodes[indexOfEnergyNodeLast]);
        int[] arrayForMiddle = {6, 13, 20, 27, 34, 41, 48, 7, 14, 21, 28, 35, 42, 49, 56, 15, 22, 29, 36, 43, 50, 57};
        randomIndex = random.nextInt(arrayForMiddle.length);
        int indexOfEnergyNodeMiddle = arrayForMiddle[randomIndex];
        nodes[indexOfEnergyNodeMiddle].setEnergy(distance(nodes[indexOfEnergyNodeMiddle], nodes[indexOfEnergyNodeLast]));
        nodes[indexOfEnergyNode1].setEnergy(distance(nodes[indexOfEnergyNodeMiddle], nodes[indexOfEnergyNode1]));
    }


    void createFalse(Node first, Node second) {

    }

    int distance(Node first, Node second) {
        int changeInX = Math.abs(first.getX() - second.getX());
        int changeInY = Math.abs(first.getY() - second.getY());
        return changeInX + changeInY;
    }

    int XYtoIndex(int X, int Y) {
        return Y * 8 + X;
    }
}

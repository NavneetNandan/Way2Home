package com.example.navneetnandan.atry;

/**
 * Created by Navneet Nandan on 11-10-2016.
 */

public class Node {
    int position;
    private int  energy;
    boolean isCurrent;
    boolean traversed;
    Node(int position){
        this.position=position;
        this.energy=-1;
        traversed=false;
        isCurrent=false;
    }
    int getX(){
        return position%8;
    }
    int getY(){
        return position/8;
    }
    void setEnergy(int energy){
        this.energy=energy;
    }
    int getEnergy(){
        return this.energy;
    }
}

package me.udnek.fnafu.mechanic;

import me.udnek.fnafu.utils.Resettable;

public class Time implements Resettable {

    private int time;
    private final int maxTime;

    public Time(int maxTime){
        this.maxTime = maxTime;
    }

    public void tick(){
        time++;
    }
    public boolean isEnded(){
        return time >= maxTime;
    }

    public int get(){return time;}

    @Override
    public void reset() {
        time = 0;
    }
}

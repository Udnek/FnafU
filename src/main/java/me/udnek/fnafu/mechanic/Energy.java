package me.udnek.fnafu.mechanic;

import me.udnek.fnafu.map.FnafUMap;
import me.udnek.fnafu.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.utils.Resettable;

public class Energy implements Resettable {

    public static float MAX_ENERGY = 100f;
    public static float MIN_ENERGY = 0f;

    private float energy = MAX_ENERGY;
    private float consumption = 0f;
    private int usage = 0;

    private final FnafUMap map;

    public Energy(FnafUMap fnafUMap){
        map = fnafUMap;
    }

    public float getEnergy() {return energy;}
    public float getConsumption() {return consumption;}
    public float getUsage() {return usage;}

    public boolean isEndedUp(){return energy == MIN_ENERGY;}

    public void tick(){
        energy -= consumption;
        if (energy < MIN_ENERGY) energy = MIN_ENERGY;
        else if (energy > MAX_ENERGY) energy = MAX_ENERGY;
    }

    private void updateUsage(){
        int closedAmount = 0;
        for (DoorButtonPair doorButtonPair : map.getDoors()) {
            if (doorButtonPair.getDoor().isClosed()) closedAmount++;
        }
        usage = closedAmount;
    }

    public void updateConsumption(){
        updateUsage();
        consumption = usage/5f;
    }

    @Override
    public void reset() {
        consumption = 0f;
        energy = MAX_ENERGY;
        usage = 0;
    }
}

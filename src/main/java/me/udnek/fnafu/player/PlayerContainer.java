package me.udnek.fnafu.player;

import me.udnek.fnafu.player.type.Animatronic;
import me.udnek.fnafu.player.type.Survivor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer {

    private final List<Survivor> survivors = new ArrayList<>();
    private final List<Animatronic> animatronics = new ArrayList<>();

    public Survivor getSurvivor(Player player){
        for (Survivor survivor : survivors) {
            if (survivor.getPlayer() == player) return survivor;
        }
        return null;
    }
    public Animatronic getAnimatronic(Player player){
        for (Animatronic animatronic : animatronics) {
            if (animatronic.getPlayer() == player) return animatronic;
        }
        return null;
    }

    public void add(Survivor survivor){
        survivors.add(survivor);
    }
    public void add(Animatronic animatronic){
        animatronics.add(animatronic);
    }
    public boolean remove(Player player){
        List<Survivor> survivorList = getSurvivors(false);
        for (int i = 0; i < survivorList.size(); i++) {
            Survivor survivor = survivorList.get(i);
            if (survivor.getPlayer() == player) {
                survivorList.remove(i);
                return true;
            }
        }
        List<Animatronic> animatronicList = getAnimatronics(false);
        for (int i = 0; i < animatronicList.size(); i++) {
            Animatronic animatronic = animatronicList.get(i);
            if (animatronic.getPlayer() == player) {
                animatronicList.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Survivor> getSurvivors(boolean returnCopy) {
        if (returnCopy) return new ArrayList<>(survivors);
        return survivors;
    }
    public List<Animatronic> getAnimatronics(boolean returnCopy) {
        if (returnCopy) return new ArrayList<>(animatronics);
        return animatronics;
    }
    public List<FnafUPlayer> getAll(){
        ArrayList<FnafUPlayer> fnafUPlayers = new ArrayList<>();
        fnafUPlayers.addAll(survivors);
        fnafUPlayers.addAll(animatronics);
        return fnafUPlayers;
    }
    public boolean contains(Player player){
        return (containsSurvivor(player) || containsAnimatronic(player));

    }

    public int getAliveSurvivorsAmount(){
        int amount = 0;
        for (Survivor survivor : survivors) {
            if (survivor.getHealthState().isAlive()){
                amount++;
            }
        }
        return amount;
    }

    public boolean containsSurvivor(Player player){
        for (Survivor survivor : survivors) {
            if (survivor.getPlayer() == player) return true;
        }
        return false;
    }
    public boolean containsAnimatronic(Player player){
        for (Animatronic animatronic : animatronics) {
            if (animatronic.getPlayer() == player) return true;
        }
        return false;
    }
}

package me.udnek.fnafu.game;

public enum GameState {
    WAITING(false),
    RUNNING(true);

    private final boolean active;
    GameState(boolean active){
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}

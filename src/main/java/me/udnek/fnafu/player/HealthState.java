package me.udnek.fnafu.player;

public enum HealthState {

    HEALTHY {
        @Override
        public HealthState getNextState() {return INJURED;}
    },
    INJURED {
        @Override
        public HealthState getNextState() {return DEAD;}
    },

    DEAD {
        @Override
        public HealthState getNextState() {return DEAD;}

        @Override
        public boolean isAlive() {return false;}
    };


    public abstract HealthState getNextState();
    public boolean isAlive() {return true;}
}

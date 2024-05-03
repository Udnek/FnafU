package me.udnek.fnafu.command;

import me.udnek.fnafu.manager.GameManager;

public enum CommandType {
    START("start", 1, false) {
        @Override
        public boolean checkArgs(String[] args) {
            return GameManager.getManager().gameIdExists(args[1]);
        }
    },
    STOP("stop", 1, false) {
        @Override
        public boolean checkArgs(String[] args) {
            return START.checkArgs(args);
        }
    },
    JOIN("join", 2, true) {
        @Override
        public boolean checkArgs(String[] args) {
            if (!START.checkArgs(args)) return false;
            return (args[2].equals("survivor") || args[2].equals("animatronic"));
        }
    },
    LEAVE("leave", 0, true) {
        @Override
        public boolean checkArgs(String[] args) {
            return true;
        }
    },
    DEBUG("debug", 1, false){
        @Override
        public boolean checkArgs(String[] args) {
            return START.checkArgs(args);
        }
    },

    NULL("", 0, false) {
        @Override
        public boolean checkArgs(String[] args) {
            return false;
        }
    };


    public final String name;
    public final int extraArgsLength;
    public final boolean playerOnly;

    CommandType(String name, int extraArgsLength, boolean playerOnly){
        this.name = name;
        this.extraArgsLength = extraArgsLength;
        this.playerOnly = playerOnly;
    }

    public abstract boolean checkArgs(String[] args);

    public boolean equals(String[] args){
        return this.name.equalsIgnoreCase(args[0]);
    }

    public static CommandType getType(String[] args){
        CommandType commandType = NULL;
        for (CommandType value : CommandType.values()) {
            if (value.equals(args)){
                commandType = value;
                break;
            }
        }
        if ((args.length-1) == commandType.extraArgsLength){
            return commandType;
        }
        return NULL;
    }

    public boolean isStartOrStop(){
        return this == START || this == STOP;
    }
}

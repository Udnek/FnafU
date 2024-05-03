package me.udnek.fnafu.utils;

public interface NameId {

    default String getNameId(){
        return this.getClass().getSimpleName();
    }

}

package nikifor.tatarkin.myweatherfrarmentapp;

import java.io.Serializable;

public class CoatContainer implements Serializable {
    public int position = 0;
    public String cityName = "";
    public boolean visibilitySpeed = true;
    public boolean visibilityPressure;
    public String temp = "-";
    public String speed = "-";
    public String pressure = "-";

    private static CoatContainer instance;
    private CoatContainer(){}

    public static CoatContainer getInstance(){
        if(instance == null){
            instance = new CoatContainer();
        }
        return instance;
    }

}

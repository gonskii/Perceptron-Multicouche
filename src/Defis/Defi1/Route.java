package Defis.Defi1;

import java.io.Serializable;

public class Route implements Serializable {
    String type;
    double distance;

    public Route(String type, double distance) {
        this.type = type;
        this.distance = distance;
    }

}

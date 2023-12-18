package Defis.Defi1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VilleVoisine implements Serializable {
    private Map<Ville,Route> villesVoisines;

    public VilleVoisine() {
        this.villesVoisines = new HashMap<>();
    }

    public Map<Ville,Route> getVillesVoisines() {
        return villesVoisines;
    }

    public void setVillesVoisines(Map<Ville,Route> villesVoisines) {
        this.villesVoisines = villesVoisines;
    }

    public void addVille(Ville ville, Route route) {
        villesVoisines.put(ville, route);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Ville,Route> entry : villesVoisines.entrySet()) {
            sb.append(entry.getKey().getNom()).append(" : ").append(entry.getValue().distance).append(" km sur ").append(entry.getValue().type).append("\n");
        }
        return sb.toString();
    }
}

package Defis.Defi1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VilleVoisine implements Serializable {
    private Map<Ville,Double> villesVoisines;

    public VilleVoisine() {
        this.villesVoisines = new HashMap<>();
    }

    public Map<Ville,Double> getVillesVoisines() {
        return villesVoisines;
    }

    public void setVillesVoisines(Map<Ville,Double> villesVoisines) {
        this.villesVoisines = villesVoisines;
    }

    public void addVille(Ville ville, double distance) {
        villesVoisines.put(ville, distance);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Ville,Double> entry : villesVoisines.entrySet()) {
            sb.append(entry.getKey().getNom()).append(" : ").append(entry.getValue()).append(" km\n");
        }
        return sb.toString();
    }
}

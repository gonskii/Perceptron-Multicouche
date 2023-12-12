package Defis.Defi1;

public class VilleComparator implements java.util.Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Ville v1 = (Ville) o1;
        Ville v2 = (Ville) o2;
        return v2.getPopulation() - v1.getPopulation();
    }
}

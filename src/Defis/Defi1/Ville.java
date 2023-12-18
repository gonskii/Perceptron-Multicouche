package Defis.Defi1;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ville extends Point implements DistancePoint, Serializable {
    private String nom;
    private int population;
    private VilleVoisine villesVoisines;

    public Ville(String nom, int population, double latitude, double longitude) {
        super(latitude, longitude);
        this.nom = nom;
        this.population = population;
        this.villesVoisines = new VilleVoisine();
    }

    public Ville(String nom, int population) {
        super();
        this.nom = nom;
        this.population = population;
        this.villesVoisines = new VilleVoisine();
    }

    public String getNom() {
        return nom;
    }

    public int getPopulation() {
        return population;
    }

    public void addVille(Ville ville,String type) {
        if(!this.equals(ville))
            villesVoisines.addVille(ville, new Route(type,this.distance(ville)));
    }

    public Map<Ville,Route> getVillesVoisines() {
        return villesVoisines.getVillesVoisines();
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", villesVoisines=" + villesVoisines.toString() +
                '}';
    }

    @Override
    public double distance(Point p) {
        double a = Math.sin(Math.toRadians(p.getLatitude() - getLatitude()) / 2) * Math.sin(Math.toRadians(p.getLatitude() - getLatitude()) / 2) +
                Math.cos(Math.toRadians(getLatitude())) * Math.cos(Math.toRadians(p.getLatitude())) *
                        Math.sin(Math.toRadians(p.getLongitude() - getLongitude()) / 2) * Math.sin(Math.toRadians(p.getLongitude() - getLongitude()) / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RAYON_TERRE * c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville)) return false;
        Ville ville = (Ville) o;
        return getNom().equals(ville.getNom());
    }

    public static void serialize(List<Ville> villes) throws IOException {
        FileOutputStream fos = new FileOutputStream("src/Defis/Defi1/villes.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(villes);
        oos.close();
        fos.close();
    }

    public static List<Ville> deserialize(String fichier) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fichier);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<Ville> villes = (List<Ville>) ois.readObject();
        ois.close();
        fis.close();
        return villes;
    }

    public List<Ville> ajouterVille(List<Ville> villes) {
        villes.add(this);
        villes.stream().sorted(new VilleComparator()).forEach(ville -> {
            ((Ville) ville).addVille(this,"départementale");
        });
        return villes.stream().sorted(new VilleComparator()).toList();
    }
}

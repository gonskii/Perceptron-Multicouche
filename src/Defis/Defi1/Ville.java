package Defis.Defi1;

import java.io.*;
import java.util.List;

public class Ville extends Point implements Serializable {
    private String nom;
    private int population;

    public Ville(String nom, int population, double latitude, double longitude) {
        super(latitude, longitude);
        this.nom = nom;
        this.population = population;
    }

    public Ville(String nom, int population) {
        super();
        this.nom = nom;
        this.population = population;
    }

    public static Ville getVilleByName(List<Ville> graphe, String nomVille) {
        return graphe.stream().filter(ville -> ville.getNom().equals(nomVille)).findFirst().get();
    }

    public String getNom() {
        return nom;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "{" +
                "nom='" + nom + '\'' +
                ", pop=" + population +
                ", lat=" + getLatitude() +
                ", long=" + getLongitude() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville)) return false;
        Ville ville = (Ville) o;
        return getNom().equals(ville.getNom());
    }

    public static void serialize(List<Ville> villes,String f) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
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

}

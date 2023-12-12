package Defis.Defi1;

public class Ville extends Point implements DistancePoint{
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

    public String getNom() {
        return nom;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
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
}

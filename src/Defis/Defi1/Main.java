package Defis.Defi1;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static String api = "https://api-adresse.data.gouv.fr/search/";
    static String communes = "https://geo.api.gouv.fr/communes";
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        URL url = new URL(communes);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream()));
        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
        //liste des villes
        List villes = new ArrayList<>();
        List villes100 = new ArrayList<>();
        jsonArray.asList().forEach(ville -> {
            String nom = ville.getAsJsonObject().get("nom").getAsString();
            if(ville.getAsJsonObject() != null && ville.getAsJsonObject().get("population") != null){
                int population = ville.getAsJsonObject().get("population").getAsInt();
                villes.add(new Ville(nom, population));
            }
        });
        villes.stream().sorted(new VilleComparator()).limit(100).forEach(ville -> {
            try {
                URL url1 = new URL(api + "?q=" + ((Ville)ville).getNom().replace(" ", "+").replace("é","e") + "&limit=1");
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setRequestMethod("GET");
                connection1.connect();
                JsonReader jsonReader1 = new JsonReader(new InputStreamReader(connection1.getInputStream()));
                JsonElement jsonElement = JsonParser.parseReader(jsonReader1);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonArray features = jsonObject.getAsJsonArray("features");
                    if (features.size() > 0) {
                        JsonObject feature = features.get(0).getAsJsonObject();
                        JsonObject geometry = feature.getAsJsonObject("geometry");
                        JsonArray coordinates = geometry.getAsJsonArray("coordinates");
                        ((Ville)ville).setLongitude(coordinates.get(0).getAsDouble());
                        ((Ville)ville).setLatitude(coordinates.get(1).getAsDouble());
                    }
                } else {
                    System.out.println("Not a JSON Object for " + ((Ville)ville).getNom() + "");
                    System.out.println(jsonElement);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            villes100.add(ville);
        });
        System.out.println(villes100);
        System.out.println(Math.round(((Ville) villes100.get(0)).distance((Point) villes100.get(1))));
        System.out.println("--------------------");
        System.out.println("Villes sans longitude ni latitude :");
        villes100.stream().filter(v -> ((Ville)v).getLongitude() == 0 && ((Ville)v).getLatitude() == 0).forEach(v -> System.out.println(v));
        villes100.stream().sorted(new VilleComparator()).forEach(ville -> {
            //Pour les 50 premières villes, autoroutes
            if(villes100.indexOf(ville) < 50) {
                villes100.stream().filter(villeR -> !villeR.equals(ville)).forEach(villeR -> {
                    if(villes100.indexOf(villeR) < 50) {
                        ((Ville)ville).addVille((Ville) villeR,"autoroute");
                    } else {
                        ((Ville)ville).addVille((Ville) villeR,"voie rapide");
                    }
                });
            } else {
                //Pour les 50 dernières villes, voies rapides
                villes100.stream().filter(villeR -> !villeR.equals(ville)).forEach(villeR -> {
                        ((Ville)ville).addVille((Ville) villeR,"voie rapide");
                });
            }
        });
        System.out.println("--------------------");
        System.out.println("Villes avec leurs voisines :");
        System.out.println(villes100);
        Ville.serialize(villes100);

    }

}

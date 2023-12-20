package Defis.Defi1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Main {
    static int compteurVille = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Ville> villes10;
        List<Ville> villes100;
        List<Ville> villes1000;
        List<Ville> villes10000;


        /*
        villes10 = afficherVilles(10);
        Ville.serialize(villes10, "src/Defis/Defi1/villes10.ser");
        villes10 = Ville.deserialize("src/Defis/Defi1/villes10.ser");
        System.out.println("Pour 10 villes :");
        System.out.println("taille de la liste : " + villes10.size());
        villes10.forEach(ville -> {
            System.out.println(ville.getNom() + " - " + ville.getPopulation() + " - " + ville.getLatitude() + " - " + ville.getLongitude());
        });
        */
        /*
        villes100 = afficherVilles(100);
        Ville.serialize(villes100, "src/Defis/Defi1/villes100.ser");
        villes100 = Ville.deserialize("src/Defis/Defi1/villes100.ser");
        System.out.println("Pour 100 villes :");
        System.out.println("taille de la liste : " + villes100.size());
        villes100.forEach(v -> {
            System.out.println((v.getNom() + " - " + v.getPopulation() + " - " + v.getLatitude() + " - " + v.getLongitude()));
        });*/

        /*
        villes1000 = afficherVilles(1000);
        Ville.serialize(villes1000, "src/Defis/Defi1/villes1000.ser");
        villes1000 = Ville.deserialize("src/Defis/Defi1/villes1000.ser");
        System.out.println("Pour 1000 villes :");
        System.out.println("taille de la liste : " + villes1000.size());
        villes1000.forEach(ville -> {
            System.out.println(ville.getNom() + " - " + ville.getPopulation() + " - " + ville.getLatitude() + " - " + ville.getLongitude());
        });
        */

        /*villes10000 = afficherVilles(10000);
        Ville.serialize(villes10000, "src/Defis/Defi1/villes10000.ser");
        villes10000 = Ville.deserialize("src/Defis/Defi1/villes10000.ser");
        System.out.println("Pour 10000 villes :");
        System.out.println("taille de la liste : " + villes10000.size());
        villes10000.forEach(ville -> {
            System.out.println(ville.getNom() + " - " + ville.getPopulation() + " - " + ville.getLatitude() + " - " + ville.getLongitude());
        });*/
    }

    public static List<Ville>  afficherVilles(int nbVilles) throws IOException {

        String api = "https://api-adresse.data.gouv.fr/search/";
        String communes = "https://geo.api.gouv.fr/communes";
        URL url = new URL(communes);
        System.out.println("Recuperation des villes...");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream()));
        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
        //On récupère le nom des villes de France
        List<Ville> villes = new ArrayList<>();
        jsonArray.asList().forEach(ville -> {
            String nom = ville.getAsJsonObject().get("nom").getAsString();
            if (ville.getAsJsonObject() != null && ville.getAsJsonObject().get("population") != null) {
                int population = ville.getAsJsonObject().get("population").getAsInt();
                villes.add(new Ville(nom, population));
            }
        });
        System.out.println("Recuperation des villes terminée");
        connection.disconnect();

        List<Ville> villesFinales = new ArrayList<>(nbVilles);
        villes.stream().sorted(new VilleComparator()).limit(nbVilles).forEach(v -> {
            long startTime = System.currentTimeMillis();


                Ville ville = (Ville) v;
                String nom = ville.getNom();
                int population = ville.getPopulation();
                double latitude = ville.getLatitude();
                double longitude = ville.getLongitude();
                boolean success = false;
                int retryCount = 0;

                while (!success && retryCount < 2) {
                    try {
                    URL urlF = new URL(api + "?q=" + nom.toLowerCase().replace(" ", "+").replace("é", "e").replace("ç", "c").replace("î","i").replace("è","e") + "&type=municipality&limit=1");
                    System.out.println("URL : " + urlF.toString());
                    //Thread.sleep(100);
                    HttpURLConnection connectionF = (HttpURLConnection) urlF.openConnection();

                    connectionF.setConnectTimeout(5000);

                    connectionF.setRequestMethod("GET");


                    connectionF.connect();

                    int responseCode = connectionF.getResponseCode();
                        System.out.println("Response code : " + responseCode);

                   if (responseCode == HttpURLConnection.HTTP_OK) {
                        JsonReader jsonReaderF = new JsonReader(new InputStreamReader(connectionF.getInputStream()));
                        JsonElement jsonElement = JsonParser.parseReader(jsonReaderF);
                        if (jsonElement.isJsonObject() || jsonElement.isJsonArray()) {
                           JsonArray coordinates = jsonElement.getAsJsonObject().get("features").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray();
                            if (coordinates.size() > 0) {
                               longitude = coordinates.get(0).getAsDouble();
                                latitude = coordinates.get(1).getAsDouble();
                                ville = new Ville(nom, population,latitude,longitude);
                                villesFinales.add(ville);
                                System.out.println(compteurVille + " : " + ville.getNom() + " - Population : " + ville.getPopulation() + " - Latitude : " + ville.getLatitude() + " - Longitude : " + ville.getLongitude());
                                success = true;
                            }
                        } else {
                            System.out.println("Not a JSON Object for " + nom);
                        }
                    } else {
                        System.out.println("GET request not worked for " + nom);
                        retryCount++; // Incrémenter le compteur de tentative
                    }

                    connectionF.disconnect();

                } catch (ConnectException e) {
                        System.out.println("Connection timed out for " + nom + ". Retrying...");
                        retryCount++; // Incrémenter le compteur de tentative
                    } catch (Exception e) {
                        System.out.println("Exception for " + nom + ". Retrying...");
                        retryCount++; // Incrémenter le compteur de tentative
                    }
                }
            long endTime = System.currentTimeMillis();
            System.out.println("That took " + (endTime - startTime) + " milliseconds for the number " + compteurVille);
            compteurVille++;
        });
        return villesFinales;
    }

}

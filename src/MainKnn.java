import java.io.IOException;

public class MainKnn {

    public static void main(String[] args) throws IOException {

        Imagette[] datatrain = Imagette.chargerFichierGz("donnees/train-images-idx3-ubyte.gz", "donnees/train-labels-idx1-ubyte.gz", 1000);
        Imagette[] datatest = Imagette.chargerFichierGz("donnees/t10k-images-idx3-ubyte.gz", "donnees/t10k-labels-idx1-ubyte.gz", 100);;

        int[] nbNeurones = {datatrain[0].getLigne()*datatrain[0].getColonne(), 100, 50, 10}; // Nombre de neurones dans chaque couche
        int nbPassage = 100;

        MLP res = ReseauNeuron.entrainement(convertirImagettesEnTableauPixels(datatrain), recupererEtiquettes(datatrain), nbNeurones, 0.01, new Tanh(), nbPassage);

        testReseau(res, datatest);


    }

    public static void testReseau (MLP reseau, Imagette[] datatest) {
        double[][] donnees = convertirImagettesEnTableauPixels(datatest);
        double[][] reponsesAttendues = recupererEtiquettes(datatest);
        int nbErreur = 0;

        for (int i = 0; i < donnees.length; i++) {
            int reponse = interpreterSorties(reponsesAttendues[i]);
            int prediction = interpreterSorties(reseau.execute(donnees[i]));

            if (prediction != reponse) {
                nbErreur++;
            }

        }
        System.out.println("Test sur " + donnees.length + " imagette de test :");
        System.out.println("Total d'erreur : " + nbErreur + " -- taux de reussite : " + (100 - (nbErreur*100)/datatest.length )+ " %");

    }

    public static int interpreterSorties (double[] coucheSortie) {
        int indiceRes = -1;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < coucheSortie.length; i++) {
            double val = coucheSortie[i];
            if (val > max) {
                max = val;
                indiceRes = i;
            }
        }
        return indiceRes;
    }

    public static double[][] convertirImagettesEnTableauPixels(Imagette[] imagettes) {
        int nbImagettes = imagettes.length;
        int nbLignes = imagettes[0].getLigne();
        int nbColonnes = imagettes[0].getColonne();

        double[][] tableauPixels = new double[nbImagettes][nbLignes*nbColonnes];

        for (int i = 0; i < nbImagettes; i++) {
            int p = 0;

            for (int j = 0; j < nbLignes; j++) {
                for (int k = 0; k < nbColonnes; k++) {
                    double pixel = imagettes[i].getPixel(j, k);
                    tableauPixels[i][p] = normaliserNiveauDeGris(pixel);
                    p++;
                }
            }
        }

        return tableauPixels;
    }

    public static double normaliserNiveauDeGris(double pixel) {
        return (double) 2 / 255 * pixel -1;
    }

    public static double[][] recupererEtiquettes(Imagette[] imagettes) {
        double[][] res = new double[imagettes.length][10];
        double[] nombres = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        for (int i = 0; i < res.length; i++) {
            res[i] = nombres.clone();
            int indice = Integer.parseInt(imagettes[i].getEtiquette());
            res[i][indice] = 1;
        }

        return res;
    }
}
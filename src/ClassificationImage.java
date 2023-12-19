import MLP.MLP;
import MLP.function.Sigmoide;
import MLP.function.TransferFunction;

import java.io.IOException;

public class ClassificationImage {

    public static void main(String[] args) throws IOException {
        Imagette[] datatrain = Imagette.chargerFichierGz("donnees/train-images-idx3-ubyte.gz", "donnees/train-labels-idx1-ubyte.gz", 10000 );
        Imagette[] datatest = Imagette.chargerFichierGz("donnees/t10k-images-idx3-ubyte.gz", "donnees/t10k-labels-idx1-ubyte.gz", 10000);;

        int[] nbNeurones = {datatrain[0].getLigne()*datatrain[0].getColonne(), 100,50, 10}; // Nombre de neurones dans chaque couche
        int nbPassage = 150000;

        MLP res = entrainement(convertirImagettesEnTableauPixels(datatrain), recupererEtiquettes(datatrain), nbNeurones, 0.6, new Sigmoide(), nbPassage,true, datatest);

        testReseau(res, datatest);
    }

    /**
     * Méthode qui test le réseau avec des données de tests
     * @param reseau, le MLP.MLP à tester
     * @param datatest, les données de test à tester
     * @return retourne un double qui représente le taux d'échec
     */
    public static double testReseau (MLP reseau, Imagette[] datatest) {
        double[][] donnees = convertirImagettesEnTableauPixels(datatest);
        double[][] reponsesAttendues = recupererEtiquettes(datatest);
        int nbErreur = 0;
        int nbJuste = 0;

        ReseauNeuron.melangerDonnees(donnees, reponsesAttendues);

        for (int i = 0; i < donnees.length; i++) {
            int reponse = interpreterSorties(reponsesAttendues[i]);
            int prediction = interpreterSorties(reseau.execute(donnees[i]));

            if (prediction != reponse) {
                nbErreur++;
            }
            else {
                nbJuste++;
            }

        }

        double pourcentEchec = ((double) nbErreur / donnees.length);
        double pourcentJuste = ((double) nbJuste / donnees.length);


        System.out.println("Test sur " + donnees.length + " imagette de test :");
        System.out.println("Total d'erreur : " + nbErreur);
        System.out.println("Total de juste : " + nbJuste);
        System.out.println("Taux d'echec : " + pourcentEchec * 100 + "%");
        System.out.println("Taux de reussite : " + pourcentJuste * 100 + "%");

        return pourcentEchec;
    }

    /**
     * Méthode qui représente l'entrainement du MLP.MLP
     * @param entrees les entrées
     * @param sorties les sorties
     * @param layers les couches
     * @param learning_rates le taux d'apprentissage
     * @param fun la function
     * @param nbPassage le nombre de passe
     * @param affichage si on affiche ou non
     * @param dataTest les données de test
     * @return retourne un MLP.MLP
     */
    public static MLP entrainement(double[][] entrees, double[][] sorties, int[] layers, double learning_rates, TransferFunction fun, int nbPassage, boolean affichage, Imagette[] dataTest)
    {
        //On crée une instance
        MLP multicouche = new MLP(layers, learning_rates, fun);

        //On entraine
        int i = 1;
        double erreur = 0.0;
        double testErreur = Double.MAX_VALUE;
        double totalErreur = 0;

        int iterationCounter = 0;
        int evaluationInterval = 1000;

        // Démarrez le chronomètre
        long debut = System.currentTimeMillis();

        while(i< nbPassage && testErreur > 0.02)
        {
            int indexAleatoire = (int) (Math.random() * (entrees.length-1));
            totalErreur += multicouche.backPropagate(entrees[indexAleatoire], sorties[indexAleatoire]);

            erreur = totalErreur /i;

            if(affichage) System.out.println(testErreur);

            if(iterationCounter>0 && iterationCounter%evaluationInterval == 0)
            {
                testErreur = testReseau(multicouche, dataTest);
                iterationCounter = 0;
            }

            iterationCounter++;
            i++;
        }

        if(affichage)
        {
            long fin = System.currentTimeMillis();
            long tempsEcoule = fin - debut;
            System.out.println("\nTemps d'entraînement : " + tempsEcoule + " millisecondes\n");
        }

        return multicouche;
    }

    /**
     * Méthode qui permets de récupérer le résultat du bon neurones
     * @param coucheSortie neurones de sorties
     * @return retourne l'étiquette
     */
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

    /**
     * Méthode qui permets de convertir une image en tableau de double
     * @param imagettes image
     * @return retourne le tableau de double
     */
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

    /**
     * Méthode qui permets de récupérer les étiquettes d'une image
     * @param imagettes, image
     * @return retourne un tableau de double corréspondant au réponse
     */
    public static double[][] recupererEtiquettes(Imagette[] imagettes) {
        double[][] res = new double[imagettes.length][10];
        double[] nombres = {0,0,0,0,0,0,0,0,0,0};
        for (int i = 0; i < res.length; i++) {
            res[i] = nombres.clone();
            int indice = Integer.parseInt(imagettes[i].getEtiquette());
            res[i][indice] = 1;
        }

        return res;
    }

    /**
     * Méthode qui permets de normaliser niveau de gris pour la fonction tanH
     * @param pixel le pixel a normaliser
     * @return retourne une valeur entre -1 et 1
     */
    public static double normaliserNiveauDeGris(double pixel) {
        //return (double) 2 / 255 * pixel -1;
        return pixel / 255;
    }

}
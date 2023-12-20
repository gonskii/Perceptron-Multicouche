import MLP.MLP;
import MLP.function.Sigmoide;
import MLP.function.Tanh;
import MLP.function.TransferFunction;

import java.util.Random;

public class ReseauNeuron {
    public static void main(String[] args) {
        // On définit la structure de la couche
        int[] layers = {2,5,1};
        int nbIteration = 1000000;
        double learningRate = 0.1;

        System.out.println("Pour la fonction sigmoid : \n");
        tester(new Sigmoide(),layers,nbIteration,learningRate, false);


        System.out.println("\nPour la fonction tanH : \n");
        tester(new Tanh(), layers, nbIteration, learningRate, false );


        layers = new int[]{2, 3, 2};

        System.out.println("\nPour la fonction sigmoid avec comme sortie deux dimension: \n");
        tester(new Sigmoide(), layers, nbIteration, learningRate, true);

        System.out.println("\nPour la fonction tanH avec comme sortie deux dimension: \n");
        tester(new Tanh(), layers, nbIteration, learningRate, true);
    }

    /**
     * Méthode qui permet de tester notre réseau de neurone
     * @param func fonction
     * @param layers couche
     * @param nbIteration nombre d'itération
     * @param learningRate taux d'apprentissage
     * @param dimension dimension dans la réponse
     */
    public static void tester(TransferFunction func, int[] layers, int nbIteration, double learningRate, boolean dimension) {
        // On initialise
        double[][] entree_ET;
        double[][] entree_OU;
        double[][] entree_XOR;
        double[][] sortie_ET;
        double[][] sortie_OU;
        double[][] sortie_XOR;


        if (func instanceof Tanh) {
            entree_ET = new double[][]{{1, -1}, {-1, -1}, {-1, 1}, {1, 1}};
            entree_OU = new double[][]{{1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
            entree_XOR = new double[][]{{-1, 1}, {1, -1}, {-1, -1}, {1, 1}};

            if(!dimension)
            {
                sortie_ET = new double[][]{{-1}, {-1}, {-1}, {1}};
                sortie_OU = new double[][]{{1},{-1}, {1}, {1}};
                sortie_XOR = new double[][]{{1}, {1} , {-1}, {-1}};
            }
            else
            {
                sortie_ET = new double[][]{{-1,1}, {-1,1}, {-1,1}, {1,-1}};
                sortie_OU = new double[][]{{1,-1},{-1,1}, {1,-1}, {1,-1}};
                sortie_XOR = new double[][]{{1,-1}, {1,-1} , {-1,1}, {-1,1}};
            }
        }
        else
        {
            entree_ET = new double[][]{{1, 0}, {0,0}, {0, 1}, {1, 1}};
            entree_OU = new double[][]{{1, 1}, {0, 0}, {1, 0}, {0, 1}};
            entree_XOR = new double[][]{{0, 1}, {1, 0}, {0, 0}, {1, 1}};

            if (!dimension) {
                sortie_ET = new double[][]{{0}, {0}, {0}, {1}};
                sortie_OU = new double[][]{{1}, {0}, {1}, {1}};
                sortie_XOR = new double[][]{{1}, {1}, {0}, {0}};
            } else {
                sortie_ET = new double[][]{{0, 1}, {0, 1}, {0, 1}, {1, 0}};
                sortie_OU = new double[][]{{1, 0}, {0, 1}, {1, 0}, {1, 0}};
                sortie_XOR = new double[][]{{1, 0}, {1, 0}, {0, 1}, {0, 1}};
            }
        }

        melangerDonnees(entree_ET, sortie_ET);
        melangerDonnees(entree_OU,sortie_OU);
        melangerDonnees(entree_XOR,sortie_XOR);


        System.out.println("Résultats du ET après entraînement : ");
        MLP res1 = entrainement(entree_ET, sortie_ET, layers, learningRate, func, nbIteration, false);

        for (double[] input : entree_ET) {
            double[] output = res1.execute(input);
            System.out.println(input[0] + " ET " + input[1] + " = " + Math.round(output[0]));
        }
        System.out.println(" ");


        System.out.println("Résultats du OU après entraînement : ");
        MLP res2 = entrainement(entree_OU, sortie_OU, layers, learningRate, func, nbIteration,false);

        for (double[] input : entree_ET) {
            double[] output = res2.execute(input);
            System.out.println(input[0] + " OU " + input[1] + " = " + Math.round(output[0]));
        }
        System.out.println(" ");


        System.out.println("Résultats du XOR après entraînement : ");
        MLP res3 = entrainement(entree_XOR, sortie_XOR, layers, learningRate, func, nbIteration,false);
        for (double[] input : entree_ET) {
            double[] output = res3.execute(input);
            System.out.println(input[0] + " XOR " + input[1] + " = " + Math.round(output[0]));
        }

    }

    /**
     * Méthode qui permets de réaliser l'entrainement du MLP
     * @param entrees les données d'entrée
     * @param sorties les données de sortie
     * @param layers les différentes couches
     * @param learning_rates le taux d'apprentissage
     * @param fun la fonction
     * @param nbPassage le nombre de passages
     * @param affichage l'affichage du temps etc
     * @return retourne un MLP entrainé
     */
    public static MLP entrainement(double[][] entrees, double[][] sorties, int[] layers, double learning_rates, TransferFunction fun, int nbPassage, boolean affichage)
    {
        //On crée une instance
        MLP multicouche = new MLP(layers, learning_rates, fun);

        //On entraine
        int i = 1;
        double erreur = 1;
        double totalErreur = 0;

        // Démarrez le chronomètre
        long debut = System.currentTimeMillis();

        while(i< nbPassage && erreur > 0.02)
        {
            int indexAleatoire = (int) (Math.random() * (entrees.length-1));
            totalErreur += multicouche.backPropagate(entrees[indexAleatoire], sorties[indexAleatoire]);

            erreur = totalErreur /i;

            if(affichage) System.out.println(erreur);
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
     * Méthode qui permets de mélanger les données
     * @param entree données d'entrée
     * @param sortie données de sortie
     */
    public static void melangerDonnees(double[][] entree, double[][] sortie) {
        Random random = new Random();

        for (int i = entree.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            // Échanger les lignes dans les données d'entrée
            double[] tempEntree = entree[index];
            entree[index] = entree[i];
            entree[i] = tempEntree;

            // Échanger les lignes dans les données de sortie correspondantes
            double[] tempSortie = sortie[index];
            sortie[index] = sortie[i];
            sortie[i] = tempSortie;
        }
    }
}

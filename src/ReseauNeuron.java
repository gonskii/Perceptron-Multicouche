import java.util.Random;

public class ReseauNeuron {
    public static void main(String[] args) {
        // On définit la structure de la couche
        int[] layers = {2,3,1};
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
        MLP res1 = entrainement(entree_ET, sortie_ET, layers, learningRate, func, nbIteration);

        for (double[] input : entree_ET) {
            double[] output = res1.execute(input);
            System.out.println(input[0] + " ET " + input[1] + " = " + Math.round(output[0]));
        }
        System.out.println(" ");



        System.out.println("Résultats du OU après entraînement : ");
        MLP res2 = entrainement(entree_OU, sortie_OU, layers, learningRate, func, nbIteration);

        for (double[] input : entree_ET) {
            double[] output = res2.execute(input);
            System.out.println(input[0] + " OU " + input[1] + " = " + Math.round(output[0]));
        }
        System.out.println(" ");


        System.out.println("Résultats du XOR après entraînement ( ne fonctionne pas pour sig) : ");
        MLP res3 = entrainement(entree_XOR, sortie_XOR, layers, learningRate, func, nbIteration);
        for (double[] input : entree_ET) {
            double[] output = res3.execute(input);
            System.out.println(input[0] + " XOR " + input[1] + " = " + Math.round(output[0]));
        }

    }

    public static MLP entrainement(double[][] entrees, double[][] sorties, int[] layers, double learning_rates, TransferFunction fun, int nbPassage)
    {
        //On crée une instance
        MLP multicouche = new MLP(layers, learning_rates, fun);

        //On entraine
        int i = 0;
        double erreur = Double.MAX_VALUE;

        while(i< nbPassage && erreur > 0.001)
        {
            erreur = 0.0;
            for(int j = 0; j<entrees.length; j++)
            {
                erreur += multicouche.backPropagate(entrees[j],sorties[j]);
            }
            i++;
            System.out.println("Le taux d'erreur est de : " + erreur);
        }

        return multicouche;
    }

    // Méthode pour mélanger les données
    private static void melangerDonnees(double[][] entree, double[][] sortie) {
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

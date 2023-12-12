import java.util.Objects;

public class Main {

    public static void main (String[] args) {
        int[] nbNeurones = {2, 2, 1};
        int nbPassage = 100000;

        genererReseau("sigmoide", nbNeurones, nbPassage);
        genererReseau("tanh", nbNeurones, nbPassage);

    }


    public static void genererReseau(String fonction, int[] nbNeurones, int nbPassage) {

        TransferFunction fun;

        double[][] entree_ET;
        double[][] sortie_ET;

        double[][] entree_OU;
        double[][] sortie_OU;

        double[][] entree_XOR;
        double[][] sortie_XOR;

        if (Objects.equals(fonction, "sigmoide")) {
            fun = new Sigmoide();

            entree_ET = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
            sortie_ET = new double[][]{{0}, {0}, {0}, {1}};

            entree_OU = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
            sortie_OU = new double[][]{{0}, {1}, {1}, {1}};

            entree_XOR = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
            sortie_XOR = new double[][]{{0}, {1}, {1}, {0}};

        } else {
            fun = new Tanh();

            entree_ET = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            sortie_ET = new double[][]{{-1}, {-1}, {-1}, {1}};

            entree_OU = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            sortie_OU = new double[][]{{-1}, {1}, {1}, {1}};

            entree_XOR = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            sortie_XOR = new double[][]{{-1}, {1}, {1}, {-1}};
        }

        MLP res1 = entrainerReseau(entree_ET, sortie_ET, nbNeurones, 0.6, fun, nbPassage);
        MLP res2 = entrainerReseau(entree_OU, sortie_OU, nbNeurones, 0.6, fun, nbPassage);
        MLP res3 = entrainerReseau(entree_XOR, sortie_XOR, nbNeurones, 0.6, fun, nbPassage);

        System.out.println("Résultats du ET après entraînement : ");
        for (double[] input : entree_ET) {
            double[] output = res1.execute(input);
            System.out.println(input[0] + " ET " + input[1] + " = " + Math.round(output[0]));
        }

        System.out.println("Résultats du OU après entraînement : ");
        for (double[] input : entree_OU) {
            double[] output = res2.execute(input);
            System.out.println(input[0] + " OU " + input[1] + " = " + Math.round(output[0]));
        }

        System.out.println("Résultats du XOR après entraînement : ");
        for (double[] input : entree_XOR) {
            double[] output = res3.execute(input);
            System.out.println(input[0] + " XOR " + input[1] + " = " + output[0]);
        }

    }

    public static MLP entrainerReseau (double[][] entrees, double[][] sorties, int[] layers, double learning_rates, TransferFunction fun, int nbPassage) {
        MLP reseau = new MLP(layers, learning_rates, fun);
        Double erreur = Double.MAX_VALUE;
        int i = 0;

        while (i < nbPassage &&  erreur > 0.0001) {
            erreur = 0.0;
            for (int j = 0; j < entrees.length; j++) {
                double[] entree = entrees[j];
                double[] sortie = sorties[j];

                erreur += reseau.backPropagate(entree, sortie);
            }
            i++;

        }
        System.out.println("Taux d'erreur final : " + erreur);

        return reseau;

    }
}

import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.GZIPInputStream;

public class Imagette {

    private int[][] pixels;

    private String etiquette;

    public Imagette (int l, int c) {
        this.pixels = new int[l][c];
    }

    public int getPixel (int l, int c) {
        return pixels[l][c];
    }

    public void setPixel (int l, int c, int val) {
        this.pixels[l][c] = val;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public int getLigne() {
        return pixels.length;
    }

    public int getColonne() {
        return pixels[0].length;
    }

    public static Imagette[] chargerFichierGz (String fileDataName, String fileResName, int maxVal) throws IOException {

        // ouvrir datainputstream
        DataInputStream data = new DataInputStream(new GZIPInputStream(new FileInputStream(fileDataName)));

        // lire numero magique
        int mumMagique = data.readInt();

        // lire nb images
        int nbImage = data.readInt();
        if (maxVal < nbImage && maxVal != -1) {
            nbImage = maxVal;
        }

        // lire nb lignes
        int nbLignes = data.readInt();

        // lire nb colonnes
        int nbColonnes = data.readInt();

        //////////////////////

        // ouvrir datainputstream etiquette
        DataInputStream res = new DataInputStream(new GZIPInputStream(new FileInputStream(fileResName)));

        // id du fichier
        int idEtiquette = res.readInt();

        // nb d etiquette
        int nbetiquette = res.readInt();



        Imagette[] tableauImagettes = new Imagette[nbImage];
        int i = 0;

        while (i < nbImage){
            Imagette img = new Imagette(nbLignes, nbColonnes);

            for (int l = 0; l < nbLignes; l++) {
                for (int c = 0; c < nbColonnes; c++) {
                    int pixel = data.readUnsignedByte();
                    img.setPixel(l, c, pixel);
                }
            }
            // Lire l'étiquette correspondante
            int etiquette = res.readUnsignedByte();
            img.etiquette = String.valueOf(etiquette);
            tableauImagettes[i] = img;
            i++;
        }

        data.close();
        res.close();
        return tableauImagettes;
    }

    public static void exporterImage(Imagette imagette, String fileName) {
        int colonnes = imagette.pixels[0].length;
        int lignes = imagette.pixels.length;

        BufferedImage image = new BufferedImage(imagette.pixels[0].length, imagette.pixels.length, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < colonnes; i++) {
            for (int j = 0; j < lignes; j++) {
                int pixel = imagette.pixels[j][i];
                int rgb = (pixel << 16) | (pixel << 8) | pixel;
                image.setRGB(i, j, rgb);

            }
        }

        try {
            File fichierImage = new File("images/" + fileName);
            ImageIO.write(image, "png", fichierImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

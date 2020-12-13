import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Sortie {
    public static void main( String[] args ) throws FileNotFoundException, IOException {

        if ( args.length > 2 || args.length == 0 || (args.length == 1 && (!args[0].equals("S") && !args[0].equals("SR")))) {
            System.out.println( "Argument(s) invalide(s).\n" +
                "L'usage: java -jar Sortie.jar inputFile.json outputFile.json \n" +
                    "Afficher des statistiques: java -jar Sortie.jar -S\nReinitiliaser des statistiques: java -jar Sortie.jar -SR" );
        } else if (args.length == 1){
            faireStatistiques(args[0]);
        }
        else {
            calculerResultat(args[0], args[1]);
        }
    }


    public static void faireStatistiques(String param){
        Statistiques statistiques = new Statistiques();
        if(param.equals("S")){
            statistiques.load();
            statistiques.afficher();
        }
        else if(param.equals("SR")){
            statistiques.reinitialiser();
        }
    }


    public static void calculerResultat(String fichierInput, String fichierOutput) throws IOException{
        Activites activites = new Activites( fichierInput );
        activites.setResultat();
        JSONObject fichierJson = activites.getResultat();
        EcrireFichier.saveStringIntoFile( fichierOutput, fichierJson.toString() );
    }


}


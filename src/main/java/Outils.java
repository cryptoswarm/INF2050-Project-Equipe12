// La classe qui contient divers methodes qui peuvent etre servi dans
// les autres classes.

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class Outils {


    public static String convDoubleADevise( double valeur ) {

        String resultat;
        DecimalFormat df = new DecimalFormat( "0.00" );
        df.setRoundingMode( RoundingMode.UP );
        resultat = df.format( valeur ) + " $";
        return resultat;
    }

    public static String remplacerVirguleParPoint( String StringAvecVirgule ) {

        String StringAvecPoints = StringAvecVirgule.replace( ',', '.' );
        return StringAvecPoints;
    }

    public static ArrayList<String> remplacerVirgulesParPoints(ArrayList<String> ListeAvecVirgule ) {

        ArrayList<String> listeSansVirgule = new ArrayList<>();
        for ( int i = 0; i < ListeAvecVirgule.size(); i++ ) {
            listeSansVirgule.add( remplacerVirguleParPoint( ListeAvecVirgule.get( i ) ) );
        }
        return listeSansVirgule;
    }


    public static LocalDate convStringEnDate(String dateString, DateTimeFormatter formatDuDate) {

        LocalDate date = LocalDate.parse( dateString, formatDuDate );
        return date;
    }

    public static int calcNbSemainesEntreDeuxDates(LocalDate d1, LocalDate d2 ) {

        int nbSemaines = ( int ) ChronoUnit.WEEKS.between( d1, d2 );
        return nbSemaines;
    }

    public static int calcNbMoisEntreDeuxDates( LocalDate d1, LocalDate d2 ) {

        int nbMois = ( int ) ChronoUnit.MONTHS.between( d1, d2 );
        return nbMois;
    }


    // Trier une liste en ordre croissant.
    public static ArrayList<String> trierUneListe( ArrayList<String> liste ) {

        ArrayList<String> listeTriees = new ArrayList<>();
        listeTriees.addAll( liste );
        Collections.sort( listeTriees );
        return listeTriees;
    }


    public static boolean validerUnStringEstVide( String s ) {

        boolean estVide = false;
        if ( s.isEmpty() || s.equals( "" ) ) {
            estVide = true;
        }
        return estVide;
    }

    //
    // Valider le format d'une date en String, si le String ne peut pas etre "parsed", la date en String n'est pas
    // le format voulu.
    public static boolean validerFormatDeDate( String dateString , DateTimeFormatter formatDuDate) throws DateTimeParseException {

        boolean estValide = true;
        try {
            LocalDate date = LocalDate.parse( dateString, formatDuDate );
        } catch ( DateTimeParseException e ) {
            estValide = false;
        }
        return estValide;
    }

}

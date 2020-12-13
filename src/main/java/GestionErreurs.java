import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class GestionErreurs {

    private final int NB_ELEVES_MIN_VALIDE = 1;
    private final int NB_ELEVES_MAX_VALIDE = 32;
    private final int AGE_MIN_VALIDE = 4;
    private final int AGE_MAX_VALIDE = 17;
    private final String PATTERN_MONTNT_ARGENT_EN = "\\d+\\.\\d{2}\\ \\$";
    private final String PATTERN_MONTNT_ARGENT_FR = "\\d+\\,\\d{2}\\ \\$";
    private final int DISTANCE_MAX_VALIDE = 120;

    Activites activites;

    public GestionErreurs(Activites activites){
        this.activites = activites;
    }
    //
    // Trouver la propriete (cle) manquante dans un objet JSON, la methode retournera la premiere cle manquante, sinon elle retourne null.
    //
    public static String trouverCleManquante(JSONObject objet, ArrayList<String> clesAttendus ) {
        String cleManquante = null;
        ArrayList<String> clesAVerifier = extraireClesDeObjetJson( objet );
        for ( String cle : clesAttendus ) {
            if ( clesAVerifier.contains( cle ) == false ) {
                cleManquante = cle;
            }
        }
        return cleManquante;
    }


    //
    // Trouver la propriete (cle) manquante dans un objet Json d'un array JSON, la methode retournera la premiere cle manquante,
    // sinon elle retourne null.
    //
    public static String trouverCleManquante(JSONArray jsArrayAVerifier, ArrayList<String> clesAttendus) {
        String cleManquante = null;
        for ( int i = 0; i < jsArrayAVerifier.size() && cleManquante == null; i++ ) {
            String cleCourante = trouverCleManquante(jsArrayAVerifier.getJSONObject( i ),clesAttendus);
            if ( cleCourante != null ) {
                cleManquante = cleCourante;
            }
        }
        return cleManquante;
    }


    //
    // Creer un message de validation des proprietes JSON.
    //
    public static String creerMsgCleManquante(String proprieteManquante) {

        String message = "La propriete (" + proprieteManquante + ") est manquee ou en erreur!";
        return message;
    }


    // Obtenir une liste de toutes les cles (proprietes) d'un objet JSON
    public static ArrayList<String> extraireClesDeObjetJson(JSONObject obj ) {

        Iterator itr = obj.keys();
        ArrayList<String> cles = new ArrayList<>();
        while ( itr.hasNext() ) {
            cles.add( itr.next().toString() );
        }
        return cles;
    }






    //
    // Creer un message de validation du fichier d'entree, le message est NULL si le fichier d'entree est valide.
    // Il y a 3 niveaux d'erreur possible, soit:
    // 1. Erreur dans la propriete JSON.
    // 2.a Erreur dans le nombre ou l'age des eleves.
    // 2.b Erreur dans les elements des activites.
    // La methode retourne seulement un message pour la premiere erreur trouvee.
    //
    public String creerMsgValidationFichierEntree() {

        String messageValidation = creerMsgValidationProprietesJson();
        if ( messageValidation == null ) {
            messageValidation = creerMsgValidationEleves();
        }
        if ( messageValidation == null ) {
            for (int i = 0; i < getActivites().getListeClesActivite().size() && messageValidation == null; i++ ) {
                messageValidation = creerMsgValidationElementsActivite( getActivites().getListeClesActivite().get( i ) );
            }
        }
        return messageValidation;
    }

    //
    // Creer un message de validation des proprietes JSON.
    // Il y a 2 niveaux d'erreur possible, soit:
    // 1. Il manque "age, nombre_des_eleves, ou activite" OU
    // 2. Il manque des proprietes dans "activite", ex: 'description', 'transport'.
    //
    public String creerMsgValidationProprietesJson() {

        String message = null;
        String cleManquante1 = trouverCleManquante( getActivites().getJsR().getJsObj(),getActivites().getListeClesFichierEntree() );
        String cleManquante2 = trouverCleManquante(
                getActivites().getJsR().getJsObjArray(Activites.cles.ACTIVITES), getActivites().getListeClesActivite() );
        if ( cleManquante1 != null ) {
            message = creerMsgCleManquante(cleManquante1);
        } else if ( cleManquante2 != null ) {
            message = creerMsgCleManquante(cleManquante2);
        }
        return message;
    }


    //
    // Creer un message de validation selon le type d'element a valider.
    // Le switch est utilise puisqu'on arrete des qu'il y a une erreur.
    //
    public String creerMsgValidationElementsActivite(String cle ) {
        String message;
        switch ( cle ) {
            case Activites.cles.ACTIVITES:
                message = creerMsgValidationDescription();
                break;
            case Activites.cles.NB_PARENTS_ACCOM:
                message = creerMsgValidationNbAdulte();
                break;
            case Activites.cles.PRIX_UNIT_ENFANT:
                message = creerMsgValidationFormatArgentListePxEnfant();
                break;
            case Activites.cles.PRIX_UNIT_ADULTE:
                message = creerMsgValidationFormatArgentListePxAdulte();
                break;
            case Activites.cles.CODE_TRANSPORT:
                message = creerMsgValidationCodeTransport();
                break;
            case Activites.cles.DISTANCE_TRAJET:
                message = creerMsgValidationDistance();
                break;
            case Activites.cles.DATE:
                message = creerMsgValidationFormatDate();
                break;
            default:
                message = null;
        }
        return message;
    }


    //
    // Creer un message de validation des 2 proprietes des eleves, soit le nombre ou l'age
    // Selon la premiere erreur trouvee.
    //
    public String creerMsgValidationEleves() {

        String message = null;
        if ( creerMsgValidationNbEleves() != null ) {
            message = creerMsgValidationNbEleves();
        } else if ( creerMsgValidationAge() != null ) {
            message = creerMsgValidationAge();
        }
        return message;
    }

    //
    // Creer un message de validation du nombre des eleves.
    //
    public String creerMsgValidationNbEleves() {

        String message = null;
        if ( getActivites().getNbEleves() > NB_ELEVES_MAX_VALIDE ) {
            message = "Le nombre d'eleve est superieur a " + NB_ELEVES_MAX_VALIDE + ".";
        } else if ( getActivites().getNbEleves() < NB_ELEVES_MIN_VALIDE ) {
            message = "Le nombre d'eleve  n'est pas valide.";
        }
        return message;
    }

    //
    // Creer un message de validation de l'age des eleves.
    //
    public String creerMsgValidationAge() {

        String message = null;
        if ( getActivites().getAgeEleves() < AGE_MIN_VALIDE ) {
            message = "L'age des eleves doit etre superieur ou egale a " + AGE_MIN_VALIDE + ".";
        } else if ( getActivites().getAgeEleves() > AGE_MAX_VALIDE ) {
            message = "L'age des eleves doit etre inferieur ou egale a " + AGE_MAX_VALIDE + ".";
        }
        return message;
    }

    //
    // Creer un message de validation de la description, une description ne peut pas etre vide ni doublon
    // LSelon la premiere erreur trouvee.
    //
    public String creerMsgValidationDescription() {

        String message = null;
        if ( validerDescriptionVide() == true ) {
            message = "Une description ne peut pas etre vide.";
        } else if ( validerDescriptionDoublon() == true ) {
            message = "Une description doit etre unique.";
        }
        return message;
    }

    public boolean validerDescriptionDoublon() {

        boolean estDoublon = false;
        for ( int i = 0; i < getActivites().getListeDescriptions().size() - 1 && getActivites().getListeDescriptions().size() > 1
                && estDoublon == false; i++ ) {
            for ( int j = i + 1; j < getActivites().getListeDescriptions().size(); j++ ) {
                if ( getActivites().getListeDescriptions().get( j ).equals( getActivites().getListeDescriptions().get( i ) ) ) {
                    estDoublon = true;
                }
            }
        }
        return estDoublon;
    }

    public boolean validerDescriptionVide() {

        boolean estVide = false;
        String description;
        for ( int i = 0; i < getActivites().getListeDescriptions().size() && estVide == false; i++ ) {
            description = getActivites().getListeDescriptions().get( i );
            if ( Outils.validerUnStringEstVide( description ) ) {
                estVide = true;
            }
        }
        return estVide;
    }

    //
    // Creer un message de validation du nombre des adultes, en parcourant la liste du nombre des parents
    // Le boucle s'arrete desqu'une erreur est trouvee (donc un message est cree).
    //
    public String creerMsgValidationNbAdulte() {

        String message = null;
        for ( int i = 0; i < getActivites().getListeNbParentsAccompagne().size() && message == null; i++ ) {
            if ( validerNbAdulte( getActivites().getListeNbParentsAccompagne().get( i ) ) == false ) {
                message = "Le nombre des adultes (incluant le(s) enseignant(s) depasse le nombre d'enfants.";
            }
        }
        return message;
    }

    public boolean validerNbAdulte( String nbAdulte ) {

        boolean estValide = true;
        if ( Integer.valueOf( nbAdulte ) + getActivites().NOMBRE_DE_PROF > getActivites().getNbEleves() ) {
            estValide = false;
        }
        return estValide;
    }

    //
    // Creer un message de validation du format d'argent, en parcourant la liste des prix unitaires enfant.
    // Le boucle s'arrete desqu'une erreur est trouvee (donc un message est cree).
    //
    public String creerMsgValidationFormatArgentListePxEnfant() {

        String message = null;
        for ( int i = 0; i < getActivites().getListePrixUnitaireEnfant().size() && message == null; i++ ) {
            if ( Pattern.matches( PATTERN_MONTNT_ARGENT_EN, getActivites().getListePrixUnitaireEnfant().get( i ) ) == false ) {
                message = "Le format du montant d'argent doit contenir 2 decimales, un espace et un signe de dollar.";
            }
        }
        return message;
    }

    //
    // Creer un message de validation du format d'argent, en parcourant la liste des prix unitaires adulte.
    // Le boucle s'arrete desqu'une erreur est trouvee (donc un message est cree).
    //
    public String creerMsgValidationFormatArgentListePxAdulte() {

        String message = null;
        for ( int i = 0; i < getActivites().getListePrixUnitaireAdulte().size() && message == null; i++ ) {
            if ( validerFormatArgent( getActivites().getListePrixUnitaireAdulte().get( i ) ) == false ) {
                message = "Le format du montant d'argent doit contenir 2 decimales, un espace et un signe de dollar.";
            }
        }
        return message;
    }

    // Valider un format d'un String d'un montant d'argent a l'aide d'une expression reguilere
    public boolean validerFormatArgent( String argent ) {

        boolean estValide = true;
        if ( Pattern.matches( PATTERN_MONTNT_ARGENT_EN, argent ) == false
                && Pattern.matches( PATTERN_MONTNT_ARGENT_FR, argent ) == false ) {
            estValide = false;
        }
        return estValide;
    }

    //
    // Creer un message de validation de la distance d'une activitee, en parcourant la liste des distances.
    // Le boucle s'arrete desqu'une erreur est trouvee (donc un message est cree).
    //
    public String creerMsgValidationDistance() {

        String message = null;
        for ( int i = 0; i < getActivites().getListeDistanceTrajet().size() && message == null; i++ ) {
            if ( validerDistance( getActivites().getListeDistanceTrajet().get( i ) ) == false ) {
                message = "La distance ne peut pas etre negative et ne peut pas superieure a 120 km.";
            }
        }
        return message;
    }

    public boolean validerDistance( String distance ) {

        boolean estValide = true;
        int distanceInt = Integer.valueOf( distance );
        if ( distanceInt > DISTANCE_MAX_VALIDE || distanceInt < 0 ) {
            estValide = false;
        }
        return estValide;
    }

    //
    // Creer un message de validation du format des dates, en parcourant la liste des dates.
    // Le boucle s'arrete desqu'une erreur est trouvee (donc un message est cree).
    //
    public String creerMsgValidationFormatDate() {

        String message = null;
        for ( int i = 0; i < getActivites().getListeDates().size() && message == null; i++ ) {
            if ( Outils.validerFormatDeDate( getActivites().getListeDates().get( i ) , getActivites().FORMAT_DU_DATE) == false ) {
                message = "Le format des dates n'est pas respect (ISO 8601) ou l'une des dates n'est pas valide.";
            }
        }
        return message;
    }

    //
    // Creer un message de validation du code de transport, en parcourant la liste des codes de transport..
    // Le boucle s'arrete desqu'une erreur est trouvee (donc un message est cree).
    //
    public String creerMsgValidationCodeTransport() {

        String message = null;
        for ( int i = 0; i < getActivites().getListeCodeTransport().size() && message == null; i++ ) {
            if ( validerCodeTransport( getActivites().getListeCodeTransport().get( i ) ) == false ) {
                message = "Le type de transport doit etre 0, 1, ou 2.";
            }
        }
        return message;
    }

    public boolean validerCodeTransport( String codeTransport ) {

        boolean estValide = false;
        if ( codeTransport.equals( getActivites().CODE_TRANSPORT_MARCHE )
                || codeTransport.equals( getActivites().CODE_TRANSPORT_METRO )
                || codeTransport.equals( getActivites().CODE_TRANSPORT_BUS ) ) {
            estValide = true;
        }
        return estValide;
    }



    public Activites getActivites(){
        return this.activites;
    }


}

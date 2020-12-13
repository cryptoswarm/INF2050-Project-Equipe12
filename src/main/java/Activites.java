
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Activites {

    JsonReader jsR;

    public final String CODE_TRANSPORT_MARCHE = "0";
    public final String CODE_TRANSPORT_METRO = "1";
    public final String CODE_TRANSPORT_BUS = "2";

    public final int NOMBRE_DE_PROF = 1;

    // ISO_LOCAL_DATE correspond au format "YYYY-MM-DD" ;
    public final DateTimeFormatter FORMAT_DU_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    private final int AGE_ENFANT_PETIT = 5;
    private final double COUT_TRANSPORT_METRO_ENFANT_MOYEN = 4.50;
    private final double COUT_TRANSPORT_METRO_ADULTE = 6.50;

    private final int TRAJET_DE_BASE_POUR_BUS = 40;
    private final double COUT_INFERIEUR_TRAJET_DE_BASE = 4.00;
    private final double COUT_SUPERIEUR_TRAJET_DE_BASE = 4.95;
    private final int MAX_CAPACITE_BUS = 30;
    private final double RABAIS_ENFANT_PETIT = 0.10;
    private final int NB_PARENTS_NECESSAIRE_POUR_RABAIS = 4;
    private final double RABAIS_PARENTS = 0.10;
    private final double COUT_SUPPL_POUR_BUS_REMPLI = 30;
    private final double FRAIS_SERVICE_GARDE_PAR_ELEVE = 9.00;

    public interface cles {
        String NOMBRE_DES_ELEVES = "nombre_eleves";
        String AGE_ELEVES = "age";
        String ACTIVITES = "activites";
        String DESCRIPTIONS_ACTIVITES = "description";
        String NB_PARENTS_ACCOM = "nombre_parents_accompagnateurs";
        String PRIX_UNIT_ENFANT = "prix_unitaire_enfant";
        String PRIX_UNIT_ADULTE = "prix_unitaire_adulte";
        String CODE_TRANSPORT = "transport";
        String DISTANCE_TRAJET = "distance";
        String DATE = "date";
    }

    private final String RECOMMENDATIONS = "recommendations";
    private final String MSG_POUR_ERREUR_VALIDATION = "message";
    private final String PRIX_PAR_ELEVE = "prix_par_eleve";

    private String nbDesEleves;
    private String ageDesEleves;
    private ArrayList<String> listeDescriptions;
    private ArrayList<String> listeNbParentsAccompagne;
    private ArrayList<String> listePrixUnitaireEnfant;
    private ArrayList<String> listePrixUnitaireAdulte;
    private ArrayList<String> listeCodeTransport;
    private ArrayList<String> listeDistanceTrajet;
    private ArrayList<String> listeDates;
    private ArrayList<String> listeClesActivite;
    private ArrayList<String> listeClesFichierEntree;
    private Statistiques statistiques;

    private JSONObject resultat;

    public Activites() {
        initialiserDesChamps();
    }

    public Activites( String clientDataFileName ) throws FileNotFoundException, JSONException, IOException {

        this.jsR = new JsonReader( clientDataFileName );
        this.jsR.load();
        initialiserDesChamps();
        loadClesFichierEntree();
        loadClesDesActivites();
        if ( validerProprieteFichierEntree() == true) {
            loadDonneesActivites();
        }
    }

    public void initialiserDesChamps(){

        this.listeClesFichierEntree = new ArrayList<>();
        this.listeClesActivite = new ArrayList<>();
        this.resultat = new JSONObject();
        this.statistiques = new Statistiques();
    }
    public void loadClesFichierEntree(){
        this.listeClesFichierEntree.add( cles.NOMBRE_DES_ELEVES );
        this.listeClesFichierEntree.add( cles.AGE_ELEVES );
        this.listeClesFichierEntree.add( cles.ACTIVITES );
    }
    public void loadClesDesActivites() {

        this.listeClesActivite.add( cles.DESCRIPTIONS_ACTIVITES );
        this.listeClesActivite.add( cles.NB_PARENTS_ACCOM );
        this.listeClesActivite.add( cles.PRIX_UNIT_ENFANT );
        this.listeClesActivite.add( cles.PRIX_UNIT_ADULTE );
        this.listeClesActivite.add( cles.CODE_TRANSPORT );
        this.listeClesActivite.add( cles.DISTANCE_TRAJET );
        this.listeClesActivite.add( cles.DATE );
    }
    public void loadDonneesActivites() {

        nbDesEleves = jsR.getJsObjValue( cles.NOMBRE_DES_ELEVES );
        ageDesEleves = jsR.getJsObjValue( cles.AGE_ELEVES );
        listeDescriptions = jsR.getListJsObjValue( cles.ACTIVITES, cles.DESCRIPTIONS_ACTIVITES );
        listeNbParentsAccompagne = jsR.getListJsObjValue( cles.ACTIVITES, cles.NB_PARENTS_ACCOM );
        listeCodeTransport = jsR.getListJsObjValue( cles.ACTIVITES, cles.CODE_TRANSPORT );
        listeDistanceTrajet = jsR.getListJsObjValue( cles.ACTIVITES, cles.DISTANCE_TRAJET );
        listeDates = jsR.getListJsObjValue( cles.ACTIVITES, cles.DATE );
        listePrixUnitaireEnfant = Outils.remplacerVirgulesParPoints( jsR.getListJsObjValue( cles.ACTIVITES, cles.PRIX_UNIT_ENFANT ) );
        listePrixUnitaireAdulte = Outils.remplacerVirgulesParPoints( jsR.getListJsObjValue( cles.ACTIVITES, cles.PRIX_UNIT_ADULTE ) );
    }






    public boolean validerProprieteFichierEntree(){

        boolean estValide = true;
        if ( GestionErreurs.trouverCleManquante(jsR.getJsObj(),getListeClesFichierEntree()) != null ||
                GestionErreurs.trouverCleManquante(jsR.getJsObjArray(cles.ACTIVITES), getListeClesActivite() ) != null) {
            estValide = false;
        }
        return estValide;
    }


    //
    // Calculer le montant total pour l'ensemble des enfants pour une activite
    // Le resultat est une liste des montants totaux pour l'activite correspondante.
    //
    public ArrayList<Double> calcMontantEnsembleEnfants() {

        ArrayList<Double> listeMontantEnsembleEnfants = new ArrayList<>();
        double prixUnitEnfant, prixEntreeEnfants;

        for ( String pxUnit : getListePrixUnitaireEnfant() ) {
            prixUnitEnfant = Double.valueOf( pxUnit.substring( 0, pxUnit.length() - 2 ) );
            prixEntreeEnfants = prixUnitEnfant * getNbEleves();
            listeMontantEnsembleEnfants.add( prixEntreeEnfants );
        }
        return listeMontantEnsembleEnfants;
    }


    //
    // Calculer le montant total pour l'ensemble des adultes pour une activite
    // Le resultat est une liste des montants totaux pour l'activite correspondante.
    //
    public ArrayList<Double> calcMontantEnsembleAdultes() {

        ArrayList<Double> listeMontantEnsembleAdultes = new ArrayList<>();
        double prixUnitAdulte, prixEntreeAdulte;

        for ( int j = 0; j < getListePrixUnitaireAdulte().size(); j++ ) {
            String pxUnit = getListePrixUnitaireAdulte().get( j );
            prixUnitAdulte = Double.valueOf( pxUnit.substring( 0, pxUnit.length() - 2 ) );
            prixEntreeAdulte = prixUnitAdulte * (Integer.valueOf( getListeNbParentsAccompagne().get( j ) ) + NOMBRE_DE_PROF);
            listeMontantEnsembleAdultes.add( prixEntreeAdulte );
        }
        return listeMontantEnsembleAdultes;
    }


    //
    // Calculer le montant total de transport pour chaque activite
    // Le resultat est une liste des montants totaux de transport pour l'activite correspondante.
    //
    public ArrayList<Double> calcMontantTotalTransport() {

        ArrayList<Double> listeMontantTransport = new ArrayList<>();
        double prixTransport = 0;
        for ( int i = 0; i < getListeCodeTransport().size(); i++ ) {
            String codeTransport = getListeCodeTransport().get( i );
            int distance = Integer.valueOf( getListeDistanceTrajet().get( i ) );
            int nbParentsAccompagnateurs = Integer.valueOf( getListeNbParentsAccompagne().get( i ) );
            prixTransport = calcMontantTransportPourUneActivite( codeTransport, distance, nbParentsAccompagnateurs );
            listeMontantTransport.add( prixTransport );
        }
        return listeMontantTransport;
    }

    //
    // Calculer le montant de transport pour UNE activite, dependement du code transport, distance et nombre de parents
    //
    public double calcMontantTransportPourUneActivite( String codeTransport, int distance, int nbParentsAccompagnateurs ) {

        double prixTransport = 0;

        if ( codeTransport.equals( CODE_TRANSPORT_METRO ) ) {
            prixTransport = calcMontantTransportMetro( nbParentsAccompagnateurs, getAgeEleves(), getNbEleves());
        } else if ( codeTransport.equals( CODE_TRANSPORT_BUS ) ) {
            prixTransport = calcMontantTransportBus( distance, nbParentsAccompagnateurs, getAgeEleves(), getNbEleves() );
        }
        return prixTransport;
    }

    //
    // Calculer le montant total du transport par metro pour UNE activite.
    //
    public double calcMontantTransportMetro( int nbParentsAccompagnateurs, int ageEleves, int nbEleves ) {

        double prixTotal, prixPourEnfant = 0, prixPourAdulte;

        prixPourAdulte = (nbParentsAccompagnateurs + this.NOMBRE_DE_PROF) * COUT_TRANSPORT_METRO_ADULTE;
        if ( ageEleves > AGE_ENFANT_PETIT ) {
            prixPourEnfant = COUT_TRANSPORT_METRO_ENFANT_MOYEN * nbEleves;
        }
        prixTotal = prixPourAdulte + prixPourEnfant;
        return prixTotal;
    }

    //
    // Calculer le montant total du transport par bus (En incluant les rabais) pour UNE activite.
    //
    public double calcMontantTransportBus( int distance, int nbParentsAccompagnateurs, int ageEleves, int nbEleves ) {

        int nbPersonnes = nbEleves + nbParentsAccompagnateurs + NOMBRE_DE_PROF;
        int nbBus = ( int ) Math.ceil( (( double ) nbPersonnes / MAX_CAPACITE_BUS) );
        int nbBusPleinCapacite = nbPersonnes / MAX_CAPACITE_BUS;
        double prixDeBase = calcPrixDeBaseBus( distance );
        double rabaisEnfant = calcRabaisBusEnfants(prixDeBase,ageEleves);
        double rabaisAdulte = calcRabaisBusParents( (prixDeBase - rabaisEnfant), nbParentsAccompagnateurs, nbBus );

        return nbBus * (prixDeBase - rabaisEnfant - rabaisAdulte) + COUT_SUPPL_POUR_BUS_REMPLI * nbBusPleinCapacite;
    }

    //
    // Calculer le prix de base pour transport par bus, considerant seulement la distance.
    //
    public double calcPrixDeBaseBus( int distance ) {

        double prixDeBase = 0;
        if ( distance < TRAJET_DE_BASE_POUR_BUS ) {
            prixDeBase = COUT_INFERIEUR_TRAJET_DE_BASE * distance;
        } else {
            prixDeBase = COUT_SUPERIEUR_TRAJET_DE_BASE * distance;
        }
        return prixDeBase;
    }

    // Calculer le rabais pour les enfants pour le transport par bus.
    public double calcRabaisBusEnfants( double prixDeBase , int ageEleves) {

        double rabaisEnfants = 0;
        if ( ageEleves <= AGE_ENFANT_PETIT ) {
            rabaisEnfants = prixDeBase * RABAIS_ENFANT_PETIT;
        }
        return rabaisEnfants;
    }

    // Calculer le rabais pour les parents pour le transport par bus.
    public double calcRabaisBusParents( double prixAvecRabaisEnfants, int nbParentsAccompagnateurs, int nbBus ) {

        double rabaisParents = 0;
        if ( nbParentsAccompagnateurs >= NB_PARENTS_NECESSAIRE_POUR_RABAIS * nbBus ) {
            rabaisParents = prixAvecRabaisEnfants * RABAIS_PARENTS;
        }
        return rabaisParents;
    }

    //
    // Calculer le prix total par eleve (somme de prix pour enfant, prix pour adulte, frais de transport et service de garde.
    // Le resultat est un ArrayList contenant le prix total par eleve pour chaque activite.
    //
    public ArrayList<Double> calcPrixTotalParEleve( int nbEleves) {

        ArrayList<Double> listePrixTotal = new ArrayList<>();
        double prixTotalUneActivite, prixParEleve;

        for ( int i = 0; i < getListeDescriptions().size(); i++ ) {
            prixTotalUneActivite = calcMontantEnsembleEnfants().get( i ) + calcMontantEnsembleAdultes().get( i )
                    + calcMontantTotalTransport().get( i );
            prixParEleve = Math.ceil( prixTotalUneActivite / nbEleves * 20 ) / 20.0 + FRAIS_SERVICE_GARDE_PAR_ELEVE;
            listePrixTotal.add( prixParEleve );
        }
        return listePrixTotal;
    }

    //
    // Calculer le prix total de toutes les activites par eleve pour une annee.
    //
    public double calcPrixTotalParEleveAnnee( ArrayList<Double> listePrixTotalParEleve ) {

        double prixTotalSurAnnee = 0;

        for ( double prix : listePrixTotalParEleve ) {
            prixTotalSurAnnee = prixTotalSurAnnee + prix;
        }
        return prixTotalSurAnnee;
    }

    //
    // Creer une liste de prix par eleve  par activitee,
    //
    public ArrayList<JSONObject> creerListePrixParEleve() {

        ArrayList<JSONObject> activites = new ArrayList<>();

        for ( int i = 0; i < getListeDescriptions().size(); i++ ) {
            JSONObject activite = new JSONObject();
            activite.accumulate( cles.DESCRIPTIONS_ACTIVITES, getListeDescriptions().get( i ) );
            activite.accumulate( PRIX_PAR_ELEVE, Outils.convDoubleADevise( calcPrixTotalParEleve(getNbEleves()).get( i ) ) );
            activites.add( activite );
        }
        return activites;
    }

    //
    // Creer une liste de recommendations a inclure dans le resultat.
    //
    public JSONArray creerListeRecommendations() {

        Recommandation recommandations = new Recommandation(this);

        JSONArray recommendations = new JSONArray();
        ArrayList<String> listeRecommandations = recommandations.creerListeMsgRecommandation();

        if ( listeRecommandations.isEmpty() == false ) {
            for ( String msgRecmd : listeRecommandations ) {
                recommendations.add( msgRecmd );
            }
        }
        return recommendations;
    }

    //
    // Setter le resultat (un objet JSON), qui va contenir, soit:
    // 1. Un message de validation sur le fichier d'entree
    // 2. Une liste de prix par activite + les messages recommendations si applicable.
    //
    public void setResultat() {

        if ( creerMsgValidationFichierEntree() != null ) {
            this.resultat.accumulate( MSG_POUR_ERREUR_VALIDATION, creerMsgValidationFichierEntree() );
        } else {
            this.resultat.accumulate( cles.ACTIVITES, creerListePrixParEleve() );
            if ( creerListeRecommendations().isEmpty() == false ) {
                this.resultat.accumulate( RECOMMENDATIONS, creerListeRecommendations() );
            }
            this.statistiques.load();
            this.statistiques.ajusterStats(this);
            this.statistiques.save();
        }
    }




    //
    // Creer un message de validation du fichier d'entree, le message est NULL si le fichier d'entree est valide.
    // Il y a 3 niveaux d'erreur possible, soit:
    // 1. Erreur dans la propriete JSON.
    // 2. Erreur dans le nombre ou l'age des eleves.
    // 3. Erreur dans la propriete des activites.
    // La methode retourne seulement un message pour la premiere erreur trouvee.
    //
    public String creerMsgValidationFichierEntree() {
        GestionErreurs gestionErreurs = new GestionErreurs(this);
        String messageValidation = gestionErreurs.creerMsgValidationFichierEntree();
        return messageValidation;
    }



    //
    // Collection des getters.
    //
    public ArrayList<String> getListeDescriptions() {

        return this.listeDescriptions;
    }

    public ArrayList<String> getListePrixUnitaireEnfant() {

        return this.listePrixUnitaireEnfant;
    }

    public ArrayList<String> getListePrixUnitaireAdulte() {

        return this.listePrixUnitaireAdulte;
    }

    public ArrayList<String> getListeNbParentsAccompagne() {

        return this.listeNbParentsAccompagne;
    }

    public ArrayList<String> getListeDistanceTrajet() {

        return this.listeDistanceTrajet;
    }

    public ArrayList<String> getListeCodeTransport() {

        return this.listeCodeTransport;
    }

    public ArrayList<String> getListeDates() {

        return this.listeDates;
    }

    public ArrayList<String> getListeClesActivite() {

        return this.listeClesActivite;
    }

    public ArrayList<String> getListeClesFichierEntree(){
        return this.listeClesFichierEntree;
    }

    public int getNbEleves() {

        return Integer.valueOf( this.nbDesEleves );
    }

    public int getAgeEleves() {

        return Integer.valueOf( this.ageDesEleves );
    }

    public JSONObject getResultat() {

        return this.resultat;
    }

    public JsonReader getJsR(){
        return this.jsR;
    }
    public void setJsR(JsonReader jsR) {
        this.jsR = jsR;
    }

    public void setNbDesEleves(String nbDesEleves) {
        this.nbDesEleves = nbDesEleves;
    }

    public void setAgeDesEleves(String ageDesEleves) {
        this.ageDesEleves = ageDesEleves;
    }

    public void setListeDescriptions(ArrayList<String> listeDescriptions) {
        this.listeDescriptions = listeDescriptions;
    }

    public void setListeNbParentsAccompagne(ArrayList<String> listeNbParentsAccompagne) {
        this.listeNbParentsAccompagne = listeNbParentsAccompagne;
    }

    public void setListePrixUnitaireEnfant(ArrayList<String> listePrixUnitaireEnfant) {
        this.listePrixUnitaireEnfant = listePrixUnitaireEnfant;
    }

    public void setListePrixUnitaireAdulte(ArrayList<String> listePrixUnitaireAdulte) {
        this.listePrixUnitaireAdulte = listePrixUnitaireAdulte;
    }

    public void setListeCodeTransport(ArrayList<String> listeCodeTransport) {
        this.listeCodeTransport = listeCodeTransport;
    }

    public void setListeDistanceTrajet(ArrayList<String> listeDistanceTrajet) {
        this.listeDistanceTrajet = listeDistanceTrajet;
    }

    public void setListeDates(ArrayList<String> listeDates) {
        this.listeDates = listeDates;
    }
}


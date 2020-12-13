import java.util.ArrayList;

public class Recommandation {

    private final int DISTANCE_RECMD_BUS = 80;
    private final int DISTANCE_RECMD_METRO = 40;
    private final int DISTANCE_RECMD_MARCHE = 12;
    private final int DISTANCE_RECMD_MARCHE_ENFANT10 = 8;
    private final int DISTANCE_RECMD_MARCHE_AGE = 10;

    private final int ECART_MAX_ENTRE_ACTS_MOIS = 2;
    private final int ECART_MIN_ENTRE_ACTS_SEMAINES = 3;

    private final double PRIX_RECMD_PAR_ELEVE = 45.0;
    private final double PRIX_RECMD_PAR_ELEVE_ANNEE = 300.0;

    Activites activites;

    public Recommandation(Activites activites){
        this.activites = activites;
    }
    //
    // Creer une liste des messages de recommandations
    // Les recommandations sont divisees en 4 categories, soit:
    // Prix a payer pour les eleves, distance des activitees, ecarts entre les activites et
    //  le nombre minimal des activites exterieurs recommandes.
    //
    public ArrayList<String> creerListeMsgRecommandation() {

        ArrayList<String> listeMsgs = new ArrayList<>();
        listeMsgs.addAll( creerMsgRecmdPrixTropEleve() );
        listeMsgs.addAll( creerMsgRecmdDistance() );
        listeMsgs.addAll( creerMsgRecmdEcartMinEcartMax() );
        listeMsgs.addAll( creerMsgRecmdNbMinActivitesExterieur() );
        return listeMsgs;
    }

    //
    // Valider au moins une activitee est a l'exterieur, si toutes les activitees sont a l'ecole,
    // un message de recommendation est ajoute pour celle-ci dans la liste.
    //
    public ArrayList<String> creerMsgRecmdNbMinActivitesExterieur() {

        ArrayList<String> msgRecmd = new ArrayList<>();
        boolean auMoinsUneActiviteExterieure = false;
        for ( int i = 0; i < getActivites().getListeDistanceTrajet().size(); i++ ) {
            if ( verifierActiviteEstExterieure( getActivites().getListeDistanceTrajet().get( i ) ) == true ) {
                auMoinsUneActiviteExterieure = true;
            }
        }
        if ( auMoinsUneActiviteExterieure == false ) {
            msgRecmd.add( "Au moins une activite doit etre a l'exterieur de l'ecole." );
        }
        return msgRecmd;
    }

    public boolean verifierActiviteEstExterieure( String distance ) {

        boolean estExterieur = false;
        if ( Integer.valueOf( distance ) > 0 ) {
            estExterieur = true;
        }
        return estExterieur;
    }


    //
    // Valider l'ecart maximal et minimal entre des activites. Si les ecarts ne respectent pas les exigences.
    // un message de recommendation est ajoute pour celle-ci dans la liste.
    //
    public ArrayList<String> creerMsgRecmdEcartMinEcartMax() {

        ArrayList<String> msgRecmd = new ArrayList<>();
        if ( calcEcartMaxEnMoisEntreActivites() > ECART_MAX_ENTRE_ACTS_MOIS ) {
            msgRecmd.add( "L'ecart maximal entre 2 activites devrait etre de 2 mois." );
        }
        if ( calcEcartMinEnSemainesEntreActivites() < ECART_MIN_ENTRE_ACTS_SEMAINES ) {
            msgRecmd.add( "Il doit y avoir au moins 3 semaines d'ecart entre chaque activites." );
        }
        return msgRecmd;
    }

    public int calcEcartMaxEnMoisEntreActivites() {

        ArrayList<String> listeDatesTriee = Outils.trierUneListe( getActivites().getListeDates() );
        int ecartMax = 0, ecart = 0;
        for ( int i = 0; i < listeDatesTriee.size() - 1; i++ ) {
            ecart = Outils.calcNbMoisEntreDeuxDates( Outils.convStringEnDate( listeDatesTriee.get( i ), getActivites().FORMAT_DU_DATE),
                                                 Outils.convStringEnDate( listeDatesTriee.get( i + 1 ) ,getActivites().FORMAT_DU_DATE) );
            if ( ecart > ecartMax ) {
                ecartMax = ecart;
            }
        }
        return ecartMax;
    }

    public int calcEcartMinEnSemainesEntreActivites() {

        ArrayList<String> listeDatesTriee = Outils.trierUneListe( getActivites().getListeDates() );
        int ecartMin = 100, ecart = 0;
        for ( int i = 0; i < listeDatesTriee.size() - 1; i++ ) {
            ecart = Outils.calcNbSemainesEntreDeuxDates( Outils.convStringEnDate( listeDatesTriee.get( i ), getActivites().FORMAT_DU_DATE),
                                                Outils.convStringEnDate( listeDatesTriee.get( i + 1 ) ,getActivites().FORMAT_DU_DATE) );
            if ( ecart < ecartMin ) {
                ecartMin = ecart;
            }
        }
        return ecartMin;
    }

    //
    // Valider le prix des activites par eleve et le prix total sur l'annee complete, si le prix est trop eleve,
    // un message de recommendation est ajoute pour celle-ci dans la liste.
    //
    public ArrayList<String> creerMsgRecmdPrixTropEleve() {

        ArrayList<String> msgRecmd = new ArrayList<>();
        for ( int i = 0; i < getActivites().getListeDescriptions().size(); i++ ) {
            if ( getActivites().calcPrixTotalParEleve(getActivites().getNbEleves()).get( i ) > PRIX_RECMD_PAR_ELEVE ) {
                msgRecmd.add( "L'activite " + getActivites().getListeDescriptions().get( i ) + " est trop dispendieuse." );
            }
        }
        if ( getActivites().calcPrixTotalParEleveAnnee( getActivites().calcPrixTotalParEleve(getActivites().getNbEleves()) ) > PRIX_RECMD_PAR_ELEVE_ANNEE ) {
            msgRecmd.add( "Le prix total pour l'annee est trop dispendieuse." );
        }
        return msgRecmd;
    }


    //
    // Valider la distance des Activites selon le code de transport,
    // si la distance est trop loin pour une activite, un message de recommendation est ajoute pour celle-ci dans la liste.
    //
    public ArrayList<String> creerMsgRecmdDistance() {

        ArrayList<String> msgRecmd = new ArrayList<>();
        for ( int i = 0; i < getActivites().getListeDescriptions().size(); i++ ) {
            int distance = Integer.valueOf( getActivites().getListeDistanceTrajet().get( i ) );
            if ( distance > calcDistanceRecmd( getActivites().getListeCodeTransport().get( i ) ) ) {
                msgRecmd.add( "L'activite " + getActivites().getListeDescriptions().get( i ) + " est peut-etre trop loin.." );
            }
        }
        return msgRecmd;
    }

    //
    // Calculer la distance recommandee selon le code transport
    // La distance est initialisee avec la distance recommandee pour marche et modifiee si ce n'est pas le cas.
    //
    public int calcDistanceRecmd( String codeTransport ) {

        int distance = calcDistanceRecmdMarche( getActivites().getAgeEleves() );
        if ( codeTransport.equals( getActivites().CODE_TRANSPORT_METRO ) ) {
            distance = DISTANCE_RECMD_METRO;
        } else if ( codeTransport.equals( getActivites().CODE_TRANSPORT_BUS ) ) {
            distance = DISTANCE_RECMD_BUS;
        }
        return distance;
    }

    //
    // Calculer la distance recommandee selon l'age des eleves.
    //
    public int calcDistanceRecmdMarche( int ageDesEleves ) {

        int distance = 0;
        if ( ageDesEleves > DISTANCE_RECMD_MARCHE_AGE ) {
            distance = DISTANCE_RECMD_MARCHE;
        } else {
            distance = DISTANCE_RECMD_MARCHE_ENFANT10;
        }
        return distance;
    }

    public Activites getActivites(){
        return this.activites;
    }

}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestRecommandation {

    Recommandation recommandations, recommandations2;
    Activites activites, activites2;
    ArrayList<String> listeClesActivite;
    ArrayList<String> listeClesFichierEntree;


    @BeforeEach
    void setUp() {
        activites = new Activites();
        activites.setNbDesEleves("10");
        activites.setAgeDesEleves("8");
        activites.setListeDescriptions( new ArrayList<>(Arrays.asList("Activite1", "Activite2", "Activite3")));
        activites.setListeNbParentsAccompagne(new ArrayList<>(Arrays.asList("2","2","2")));
        activites.setListePrixUnitaireEnfant(new ArrayList<>(Arrays.asList("15 $","10.00 $","300.00 $")));
        activites.setListePrixUnitaireAdulte(new ArrayList<>(Arrays.asList("25 $","20 $","200 $")));
        activites.setListeCodeTransport(new ArrayList<>(Arrays.asList("0","1","2")));
        activites.setListeDistanceTrajet(new ArrayList<>(Arrays.asList("15","45","85")));
        activites.setListeDates(new ArrayList<>(Arrays.asList("2019-09-01","2019-12-15","2020-01-01")));
        recommandations = new Recommandation(activites);

        activites2 = new Activites();
        activites2.setListeDistanceTrajet(new ArrayList<>(Arrays.asList("0")) );
        recommandations2 = new Recommandation(activites2);
        //listeClesActivite = ;
        //listeClesFichierEntree;
    }

    /*
    @Test
    void creerListeMsgRecommandation() {
    }
*/
    @Test
    void creerMsgRecmdNbMinActivitesExterieur() {
        assertEquals("Au moins une activite doit etre a l'exterieur de l'ecole.",
            recommandations2.creerMsgRecmdNbMinActivitesExterieur().get(0) );
    }

    @Test
    void verifierActiviteEstExterieure() {
        assertEquals(true,recommandations.verifierActiviteEstExterieure("10"));
        assertEquals(false,recommandations.verifierActiviteEstExterieure("0"));
    }

    @Test
    void creerMsgRecmdEcartMinEcartMax() {
        assertEquals("L'ecart maximal entre 2 activites devrait etre de 2 mois.",
                recommandations.creerMsgRecmdEcartMinEcartMax().get(0));
        assertEquals("Il doit y avoir au moins 3 semaines d'ecart entre chaque activites.",
                recommandations.creerMsgRecmdEcartMinEcartMax().get(1));
    }

    @Test
    void calcEcartMaxEnMoisEntreActivites() {
        assertEquals(3,recommandations.calcEcartMaxEnMoisEntreActivites());
    }

    @Test
    void calcEcartMinEnSemainesEntreActivites() {
        assertEquals(2,recommandations.calcEcartMinEnSemainesEntreActivites());
    }

    @Test
    void creerMsgRecmdPrixTropEleve() {
        assertEquals("L'activite Activite3 est trop dispendieuse.",recommandations.creerMsgRecmdPrixTropEleve().get(0));
        assertEquals("Le prix total pour l'annee est trop dispendieuse.", recommandations.creerMsgRecmdPrixTropEleve().get(1));
    }

    @Test
    void creerMsgRecmdDistance() {
        assertEquals("L'activite Activite1 est peut-etre trop loin..",recommandations.creerMsgRecmdDistance().get(0));
        assertEquals("L'activite Activite2 est peut-etre trop loin..",recommandations.creerMsgRecmdDistance().get(1));
        assertEquals("L'activite Activite3 est peut-etre trop loin..",recommandations.creerMsgRecmdDistance().get(2));
    }

    @Test
    void calcDistanceRecmd() {
        assertEquals(40,recommandations.calcDistanceRecmd("1"));
        assertEquals(80,recommandations.calcDistanceRecmd("2"));
    }

    @Test
    void calcDistanceRecmdMarche() {
        assertEquals(8,recommandations.calcDistanceRecmdMarche(10));
        assertEquals(12,recommandations.calcDistanceRecmdMarche(11));
    }
}
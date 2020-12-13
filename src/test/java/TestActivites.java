import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestActivites {

    Activites activites;
    Statistiques statistiques, statistiques2;
    ArrayList<String> stats;
    ArrayList<String> statsAjustes;
    ArrayList<Double> listePrix;

    @BeforeEach
    void setUp() {
        activites = new Activites();
        activites.setNbDesEleves("10");
        activites.setAgeDesEleves("8");
        activites.setListeDescriptions(new ArrayList<>(Arrays.asList("Activite1", "Activite2", "Activite3", "Activite4")));
        activites.setListeNbParentsAccompagne(new ArrayList<>(Arrays.asList("1", "1", "1", "4")));
        activites.setListePrixUnitaireEnfant(new ArrayList<>(Arrays.asList("0.00 $", "2.00 $", "5.00 $", "10.00 $")));
        activites.setListePrixUnitaireAdulte(new ArrayList<>(Arrays.asList("0.00 $", "2.00 $", "5.00 $", "10.00 $")));
        activites.setListeCodeTransport(new ArrayList<>(Arrays.asList("0", "1", "2", "2")));
        activites.setListeDistanceTrajet(new ArrayList<>(Arrays.asList("0", "2", "5", "40")));
        activites.setListeDates(new ArrayList<>(Arrays.asList("2019-09-01", "2019-12-15", "2020-01-01", "2020-02-28")));
        activites.loadClesFichierEntree();
        activites.loadClesDesActivites();

        listePrix = new ArrayList<>();
        listePrix.add(50.0);
        listePrix.add(200.0);

        statistiques = new Statistiques();
        statistiques.initialiser();

        stats = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            stats.add("2");
        }
        statistiques.setStats(stats);

        statistiques2 = new Statistiques();
        statsAjustes = new ArrayList<>(Arrays.asList("4", "1", "1", "2", "2", "1", "1", "85", "411.1"));
    }



    @Test
    void calcMontantEnsembleEnfants1() {
        assertEquals(0,activites.calcMontantEnsembleEnfants().get(0));
        assertEquals(20,activites.calcMontantEnsembleEnfants().get(1));
    }
    @Test
    void calcMontantEnsembleEnfants2() {
        assertEquals(50,activites.calcMontantEnsembleEnfants().get(2));
        assertEquals(100,activites.calcMontantEnsembleEnfants().get(3));
    }

    @Test
    void calcMontantEnsembleAdultes1() {
        assertEquals(0,activites.calcMontantEnsembleAdultes().get(0));
        assertEquals(4,activites.calcMontantEnsembleAdultes().get(1));
    }
    @Test
    void calcMontantEnsembleAdultes2() {
        assertEquals(10,activites.calcMontantEnsembleAdultes().get(2));
        assertEquals(50,activites.calcMontantEnsembleAdultes().get(3));
    }


    @Test
    void calcMontantTotalTransport1() {
        assertEquals(0, activites.calcMontantTotalTransport().get(0));
        assertEquals(58, activites.calcMontantTotalTransport().get(1));
    }
    @Test
    void calcMontantTotalTransport2() {
        assertEquals(20, activites.calcMontantTotalTransport().get(2));
        assertEquals(178.2, activites.calcMontantTotalTransport().get(3));
    }

    @Test
    void calcMontantTransportMetro() {
        assertEquals(39,activites.calcMontantTransportMetro(5,5, 10));
        assertEquals(84,activites.calcMontantTransportMetro(5,10, 10));
    }

    @Test
    void calcMontantTransportBus() {
        assertEquals(36,activites.calcMontantTransportBus(10,3,5,10));
        assertEquals(228,activites.calcMontantTransportBus(40,2,10,27));
    }

    @Test
    void calcPrixDeBaseBus() {
        assertEquals(156,activites.calcPrixDeBaseBus(39));
        assertEquals(198,activites.calcPrixDeBaseBus(40));
    }

    @Test
    void calcRabaisBusEnfants() {
        assertEquals(10,activites.calcRabaisBusEnfants(100,5));
        assertEquals(0,activites.calcRabaisBusEnfants(100,17));
    }

    @Test
    void calcRabaisBusParents() {
        assertEquals(0,activites.calcRabaisBusParents(100,3,1));
        assertEquals(20,activites.calcRabaisBusParents(200,8,2));
    }

    @Test
    void calcPrixTotalParEleve1() {
        assertEquals(17.0,activites.calcPrixTotalParEleve(10).get(2));
        assertEquals(41.85,activites.calcPrixTotalParEleve(10).get(3));
    }
    @Test
    void calcPrixTotalParEleve2() {
        assertEquals(17.0,activites.calcPrixTotalParEleve(10).get(2));
        assertEquals(41.85,activites.calcPrixTotalParEleve(10).get(3));
    }

    @Test
    void calcPrixTotalParEleveAnnee() {
        assertEquals(250, activites.calcPrixTotalParEleveAnnee(listePrix) );

    }

    @Test
    void creerListePrixParEleve() {
        assertTrue(activites.creerListePrixParEleve().get(0) instanceof JSONObject);
        assertTrue(activites.creerListePrixParEleve().get(3) instanceof JSONObject);
    }

    @Test
    void creerListeRecommendations() {
        assertTrue(activites.creerListeRecommendations() instanceof JSONArray);
    }
    @Test
    void getListeClesActivite() {
        assertEquals(Activites.cles.DESCRIPTIONS_ACTIVITES, (activites.getListeClesActivite().get(0)));
    }
    @Test
    void getListeClesFichierEntree(){
        assertEquals(Activites.cles.NOMBRE_DES_ELEVES, activites.getListeClesFichierEntree().get(0));
    }
}
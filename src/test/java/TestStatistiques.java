import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestStatistiques {

    Activites activites;
    Statistiques statistiques, statistiques2;
    ArrayList<String> stats;
    ArrayList<String> statsAjustes;

    @BeforeEach
    void setUp() {
        activites = new Activites();
        activites.setNbDesEleves("10");
        activites.setAgeDesEleves("8");
        activites.setListeDescriptions( new ArrayList<>(Arrays.asList("Activite1", "Activite2", "Activite3","Activite4")));
        activites.setListeNbParentsAccompagne(new ArrayList<>(Arrays.asList("2","2","2","2")));
        activites.setListePrixUnitaireEnfant(new ArrayList<>(Arrays.asList("25 $","3.00 $","300.00 $", "10 $")));
        activites.setListePrixUnitaireAdulte(new ArrayList<>(Arrays.asList("25 $","5 $","200 $", "15 $")));
        activites.setListeCodeTransport(new ArrayList<>(Arrays.asList("0","1","2","0")));
        activites.setListeDistanceTrajet(new ArrayList<>(Arrays.asList("15","45","85","0")));
        activites.setListeDates(new ArrayList<>(Arrays.asList("2019-09-01","2019-12-15","2020-01-01", "2020-02-28")));
        activites.loadClesFichierEntree();
        activites.loadClesDesActivites();
        statistiques = new Statistiques();
        statistiques.initialiser();

        stats = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            stats.add("2");
        }
        statistiques.setStats(stats);

        statistiques2 = new Statistiques();
        statsAjustes = new ArrayList<>(Arrays.asList("4","1","1","2","2","1","1","85","411.1"));

    }



    @Test
    void incrementerNbActvSoumises() {
        assertEquals("6",statistiques.incrementerNbActvSoumises(activites));
    }

    @Test
    void incrementerNbActvPrixMoinsDe20() {
        assertEquals("3",statistiques.incrementerNbActvPrixMoinsDe20(activites));
    }

    @Test
    void incrementerNbActvPrixDe20a40() {
        assertEquals("3",statistiques.incrementerNbActvPrixDe20a40(activites));
    }

    @Test
    void incrementerNbActvPrixPlusDe40() {
        assertEquals("4",statistiques.incrementerNbActvPrixPlusDe40(activites));
    }

    @Test
    void incrementerNbActvMarche() {
        assertEquals("4",statistiques.incrementerNbActvMarche(activites));
    }

    @Test
    void incrementerNbActvMetro() {
        assertEquals("3",statistiques.incrementerNbActvMetro(activites));
    }

    @Test
    void incrementerNbActvBus() {
        assertEquals("3",statistiques.incrementerNbActvBus(activites));
    }

    @Test
    void updaterDistanceMaxActv() {
        assertEquals("85",statistiques.updaterDistanceMaxActv(activites));
    }

    @Test
    void updaterPrixMaxActv() {
        assertEquals("411.1",statistiques.updaterPrixMaxActv(activites));
    }

    @Test
    void ajusterStats() {
        boolean estValide = true;
        statistiques2.initialiser();
        statistiques2.ajusterStats(activites);
        for(int i = 0; i < statistiques2.getStats().size();i++){
            if(statistiques2.getStat(i).equals(statsAjustes.get(i)) == false){
                estValide = false;
            }
        }
        assertEquals(true,estValide);
    }


    @Test
    void afficher(){

        boolean reussi = true;
        try {
            System.setOut(new PrintStream(new OutputStream() {public void write(int b){ }
            }));
            statistiques.afficher();
            System.setOut(System.out);
        } catch (Exception e) {
            reussi = false;
        }
        assertEquals(true,reussi);
    }
}
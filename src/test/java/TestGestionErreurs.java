import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionErreurs {

    Activites activites1, activites2;
    GestionErreurs gsErreurs1, gsErreurs2;
    JSONObject jsobj1, jsobj2;
    JSONArray jsarray1;
    ArrayList<String> clesAttendus1, clesAttendusTest1, clesAttendus2, clesAttendusTest2;


    @BeforeEach
    void setUp() {
        activites1 = new Activites();
        activites1.setNbDesEleves("0");
        activites1.setAgeDesEleves("2");
        activites1.setListeDescriptions( new ArrayList<>(Arrays.asList("Activite1", "Activite2", "Activite2","Activite4")));
        activites1.setListeNbParentsAccompagne(new ArrayList<>(Arrays.asList("2","2","2","2")));
        activites1.setListePrixUnitaireEnfant(new ArrayList<>(Arrays.asList("25","3.00 $","$300.00", "10 $")));
        activites1.setListePrixUnitaireAdulte(new ArrayList<>(Arrays.asList("-25 $","5 $","200 $", "15 $")));
        activites1.setListeCodeTransport(new ArrayList<>(Arrays.asList("3","1","2","0")));
        activites1.setListeDistanceTrajet(new ArrayList<>(Arrays.asList("15","45","121","0")));
        activites1.setListeDates(new ArrayList<>(Arrays.asList("2019-09-01","2019-12-15","2020-01-01", "2020/02/28")));
        activites1.loadClesFichierEntree();
        activites1.loadClesDesActivites();
        gsErreurs1 = new GestionErreurs(activites1);

        activites2 = new Activites();
        activites2.setNbDesEleves("33");
        activites2.setAgeDesEleves("18");
        activites2.setListeDescriptions( new ArrayList<>(Arrays.asList("Activite1", "Activite2", "","Activite4")));
        activites2.setListeNbParentsAccompagne(new ArrayList<>(Arrays.asList("2","2","2","2")));
        activites2.setListePrixUnitaireEnfant(new ArrayList<>(Arrays.asList("-25 $","3.00 $","300.00 $", "10 $")));
        activites2.setListePrixUnitaireAdulte(new ArrayList<>(Arrays.asList("25 $","5 $","200 $", "15 $")));
        activites2.setListeCodeTransport(new ArrayList<>(Arrays.asList("0","1","2","0")));
        activites2.setListeDistanceTrajet(new ArrayList<>(Arrays.asList("15","45","85","0")));
        activites2.setListeDates(new ArrayList<>(Arrays.asList("2019-09-01","2019-12-15","2020-01-01", "2020-02-28")));
        activites2.loadClesFichierEntree();
        activites2.loadClesDesActivites();
        gsErreurs2 = new GestionErreurs(activites2);

        jsobj1 = new JSONObject();
        jsobj1.accumulate("cle1","donnee1");
        jsobj1.accumulate("cle1a","donnee1a");
        jsobj1.accumulate("cle1b","donnee1b");

        jsarray1 = new JSONArray();
        jsarray1.add(jsobj1);
        clesAttendus1 = new ArrayList<>(Arrays.asList("cle1","cle1a","cle1b"));
        clesAttendusTest1 = new ArrayList<>(Arrays.asList("cle1c","cle1b","cle1a","cle1d"));


        jsobj2 = new JSONObject();
        jsobj2.accumulate("cleA",jsobj1);
        jsobj2.accumulate("cleB", jsarray1);
        clesAttendus2 = new ArrayList<>(Arrays.asList("cleA","cleB"));
        clesAttendusTest2 = new ArrayList<>(Arrays.asList("cleA","cleC","cleB"));

    }




    @Test
    void trouverCleManquanteJsobj() {
        assertEquals(null,GestionErreurs.trouverCleManquante(jsobj2, clesAttendus2));
        assertEquals("cleC",GestionErreurs.trouverCleManquante(jsobj2, clesAttendusTest2));
    }

    @Test
    void trouverCleManquanteJsarray() {
        assertEquals(null,GestionErreurs.trouverCleManquante(jsarray1, clesAttendus1));
        assertEquals("cle1d",GestionErreurs.trouverCleManquante(jsarray1, clesAttendusTest1));
    }

    @Test
    void creerMsgCleManquante() {
        assertEquals("La propriete (age) est manquee ou en erreur!", GestionErreurs.creerMsgCleManquante("age"));
    }

    @Test
    void extraireClesDeObjetJson() {
        assertEquals("cleA",GestionErreurs.extraireClesDeObjetJson(jsobj2).get(0));
        assertEquals("cleB",GestionErreurs.extraireClesDeObjetJson(jsobj2).get(1));
    }

    @Test
    void creerMsgValidationElementsActivite1() {
        assertEquals("Une description doit etre unique.",gsErreurs1.creerMsgValidationElementsActivite(Activites.cles.ACTIVITES));
    }
    @Test
    void creerMsgValidationElementsActivite2() {
        assertEquals("Le nombre des adultes (incluant le(s) enseignant(s) depasse le nombre d'enfants."
                            ,gsErreurs1.creerMsgValidationElementsActivite( Activites.cles.NB_PARENTS_ACCOM));
    }
    @Test
    void creerMsgValidationElementsActivite3() {
        assertEquals("Le format du montant d'argent doit contenir 2 decimales, un espace et un signe de dollar."
                ,gsErreurs1.creerMsgValidationElementsActivite( Activites.cles.PRIX_UNIT_ENFANT));
    }
    @Test
    void creerMsgValidationElementsActivite4() {
        assertEquals("Le format du montant d'argent doit contenir 2 decimales, un espace et un signe de dollar."
                ,gsErreurs2.creerMsgValidationElementsActivite( Activites.cles.PRIX_UNIT_ADULTE));
    }
    @Test
    void creerMsgValidationElementsActivite5() {
        assertEquals("Le type de transport doit etre 0, 1, ou 2."
                ,gsErreurs1.creerMsgValidationElementsActivite( Activites.cles.CODE_TRANSPORT));
    }
    @Test
    void creerMsgValidationElementsActivite6() {
        assertEquals("La distance ne peut pas etre negative et ne peut pas superieure a 120 km."
                ,gsErreurs1.creerMsgValidationElementsActivite( Activites.cles.DISTANCE_TRAJET));
    }
    @Test
    void creerMsgValidationElementsActivite7() {
        assertEquals("Le format des dates n'est pas respect (ISO 8601) ou l'une des dates n'est pas valide."
                ,gsErreurs1.creerMsgValidationElementsActivite( Activites.cles.DATE));
    }
    @Test
    void creerMsgValidationElementsActivite8() {
        assertEquals(null
                ,gsErreurs1.creerMsgValidationElementsActivite( ""));
    }

    @Test
    void creerMsgValidationEleves() {
        assertEquals("Le nombre d'eleve  n'est pas valide.",gsErreurs1.creerMsgValidationEleves());
        assertEquals("Le nombre d'eleve est superieur a 32.",gsErreurs2.creerMsgValidationEleves());
    }

    @Test
    void creerMsgValidationNbEleves() {
        assertEquals("Le nombre d'eleve  n'est pas valide.",gsErreurs1.creerMsgValidationNbEleves());
        assertEquals("Le nombre d'eleve est superieur a 32.",gsErreurs2.creerMsgValidationNbEleves());
    }

    @Test
    void creerMsgValidationAge() {
        assertEquals("L'age des eleves doit etre superieur ou egale a 4.",gsErreurs1.creerMsgValidationAge());
        assertEquals("L'age des eleves doit etre inferieur ou egale a 17.",gsErreurs2.creerMsgValidationAge());
    }

    @Test
    void creerMsgValidationDescription() {
        assertEquals("Une description doit etre unique.",gsErreurs1.creerMsgValidationDescription());
        assertEquals("Une description ne peut pas etre vide.",gsErreurs2.creerMsgValidationDescription());
    }

    @Test
    void validerDescriptionDoublon() {
        assertEquals(true,gsErreurs1.validerDescriptionDoublon());
        assertEquals(false,gsErreurs2.validerDescriptionDoublon());
    }

    @Test
    void validerDescriptionVide() {
        assertEquals(false,gsErreurs1.validerDescriptionVide());
        assertEquals(true,gsErreurs2.validerDescriptionVide());
    }

    @Test
    void creerMsgValidationNbAdulte() {
        assertEquals("Le nombre des adultes (incluant le(s) enseignant(s) depasse le nombre d'enfants."
                            ,gsErreurs1.creerMsgValidationNbAdulte());
    }

    @Test
    void validerNbAdulte() {
        assertEquals(false,gsErreurs1.validerNbAdulte("1000"));
    }

    @Test
    void creerMsgValidationFormatArgentListePxEnfant() {
        assertEquals("Le format du montant d'argent doit contenir 2 decimales, un espace et un signe de dollar."
                            ,gsErreurs1.creerMsgValidationFormatArgentListePxEnfant());
    }

    @Test
    void creerMsgValidationFormatArgentListePxAdulte() {
        assertEquals("Le format du montant d'argent doit contenir 2 decimales, un espace et un signe de dollar."
                ,gsErreurs2.creerMsgValidationFormatArgentListePxAdulte());
    }


    @Test
    void validerFormatArgent() {
        assertEquals(false,gsErreurs1.validerFormatArgent("0.50"));
        assertEquals(true,gsErreurs1.validerFormatArgent("5.32 $"));
    }

    @Test
    void creerMsgValidationDistance() {
        assertEquals("La distance ne peut pas etre negative et ne peut pas superieure a 120 km.",
                gsErreurs1.creerMsgValidationDistance());
    }

    @Test
    void validerDistance() {
        assertEquals(false,gsErreurs1.validerDistance("1000"));
        assertEquals(true,gsErreurs1.validerDistance("20"));
    }


    @Test
    void creerMsgValidationFormatDate() {
        assertEquals("Le format des dates n'est pas respect (ISO 8601) ou l'une des dates n'est pas valide."
                , gsErreurs1.creerMsgValidationFormatDate());
    }

    @Test
    void creerMsgValidationCodeTransport() {
        assertEquals("Le type de transport doit etre 0, 1, ou 2.", gsErreurs1.creerMsgValidationCodeTransport());
    }

    @Test
    void validerCodeTransport() {

        assertEquals(false, gsErreurs1.validerCodeTransport("4"));
        assertEquals(true, gsErreurs1.validerCodeTransport("0"));
        assertEquals(true, gsErreurs1.validerCodeTransport("1"));
        assertEquals(true, gsErreurs1.validerCodeTransport("2"));
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOutils {

    DateTimeFormatter formatDuDateAttendu;
    LocalDate date1;
    LocalDate date2;
    LocalDate date3;
    ArrayList<String> listeAvecVirgules;
    ArrayList<String> listeNonTriee;
    ArrayList<String> listeTriee;


    @BeforeEach
    public void setup(){
        listeAvecVirgules = new ArrayList<>();
        listeAvecVirgules.add("3.00 $");
        listeAvecVirgules.add("4,50 $");

        listeNonTriee = new ArrayList<>();
        listeNonTriee.add("3"); listeNonTriee.add("1"); listeNonTriee.add("2"); listeNonTriee.add("4");
        listeTriee = new ArrayList<>();
        listeTriee.add("1");listeTriee.add("2");listeTriee.add("3");listeTriee.add("4");

        formatDuDateAttendu = DateTimeFormatter.ISO_LOCAL_DATE;
        date1 = LocalDate.parse("2019-09-28", formatDuDateAttendu);
        date2 = LocalDate.parse("2019-11-30", formatDuDateAttendu);
        date3 = LocalDate.parse("2020-01-15", formatDuDateAttendu);
    }


    @Test
    public void testRemplacerVirgule() {
        assertEquals( "3.50 $", Outils.remplacerVirguleParPoint( "3,50 $" ) );
        assertEquals( "3.00 $", Outils.remplacerVirguleParPoint( "3.00 $" ) );
        assertEquals( "3 $", Outils.remplacerVirguleParPoint( "3 $" ) );
    }

    @Test
    public void testRemplacerVirgulesParPoints() {
        assertEquals( "3.00 $", Outils.remplacerVirgulesParPoints( listeAvecVirgules ).get( 0 ) );
        assertEquals( "4.50 $", Outils.remplacerVirgulesParPoints( listeAvecVirgules ).get( 1 ) );
    }

    @Test
    public void testConvDoubleADevise(){
        assertEquals("3.50 $", Outils.convDoubleADevise(3.5));
        assertEquals("4.00 $", Outils.convDoubleADevise(4));
    }


    @Test
    public void testCalcNbSemainesEntreDeuxDates(){
        assertEquals(9,Outils.calcNbSemainesEntreDeuxDates(date1,date2));
        assertEquals(6,Outils.calcNbSemainesEntreDeuxDates(date2,date3));
    }


    @Test
    public void testCalcNbMoisEntreDeuxDates(){
        assertEquals(2,Outils.calcNbMoisEntreDeuxDates(date1,date2));
        assertEquals(1,Outils.calcNbMoisEntreDeuxDates(date2,date3));
    }


    @Test
    public void testValiderUnStringEstVide(){
        String s = new String();
        assertEquals(true,Outils.validerUnStringEstVide(""));
        assertEquals(true,Outils.validerUnStringEstVide(s));
        assertEquals(false,Outils.validerUnStringEstVide("Test"));
    }

    @Test
    public void testValiderFormatDeDate(){
        assertEquals(true,Outils.validerFormatDeDate("2019-01-01", formatDuDateAttendu));
        assertEquals(true,Outils.validerFormatDeDate("2019-05-30", formatDuDateAttendu));
        assertEquals(false,Outils.validerFormatDeDate("2019-02-29", formatDuDateAttendu));
        assertEquals(false,Outils.validerFormatDeDate("2019/01/01", formatDuDateAttendu));
        assertEquals(false,Outils.validerFormatDeDate("00000-12-20", formatDuDateAttendu));
        assertEquals(false,Outils.validerFormatDeDate("2020-30-20", formatDuDateAttendu));
        assertEquals(false,Outils.validerFormatDeDate("2020-01-40", formatDuDateAttendu));
    }


    @Test
    public void testTrierUneListe(){
        boolean egal = true;
        ArrayList<String> liste = new ArrayList<>();
        liste = Outils.trierUneListe(listeNonTriee);
        for(int i = 0; i < listeTriee.size();i++){
            if(listeTriee.get(i).equals(liste.get(i)) == false){
                egal = false;
            }
        }
        assertEquals(true,egal);
    }
}
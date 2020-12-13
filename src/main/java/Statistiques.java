import java.io.*;
import java.util.ArrayList;

public class Statistiques {

    private ArrayList<String> stats;
    private final String NOM_DU_FICHIER = "Statistiques.txt";


    public interface indexDescStats {
        int NB_ACTV_SOUMISES = 0;
        int NB_ACTV_PRIX_MOINS_DE_20 = 1;
        int NB_ACTV_PRIX_DE_20_A_40 = 2;
        int NB_ACTV_PRIX_PLUS_DE_40 = 3;
        int NB_ACTV_MARCHE = 4;
        int NB_ACTV_METRO = 5;
        int NB_ACTV_BUS = 6;
        int DISTANCE_MAX_POUR_UNE_ACTV = 7;
        int PRIX_MAX_POUR_UNE_ACTV = 8;
    }


    enum descriptionsStats{
        NB_ACTV_SOUMISES("Nombre d'activite(s) soumises : "),
        NB_ACTV_PRIX_MOINS_DE_20("Nombre d'activite(s) coutant moins de 20 $ par eleve : "),
        NB_ACTV_PRIX_DE_20_A_40("Nombre d'activite(s) coutant 20 $ a 40 $ par eleve : "),
        NB_ACTV_PRIX_PLUS_DE_40("Nombre d'activite(s) coutant plus de 40 $ par eleve : "),
        NB_ACTV_MARCHE("Nombre d'activite(s) par marche : "),
        NB_ACTV_METRO("Nombre d'activite(s) par metro : "),
        NB_ACTV_BUS("Nombre d'activite(s) par bus : "),
        DISTANCE_MAX_POUR_UNE_ACTV("Distance maximale pour une activite : "),
        PRIX_MAX_POUR_UNE_ACTV("Prix maximal pour une activite : ");

        private String desc;

        descriptionsStats(String statsDesc) {
            this.desc = statsDesc;
        }

        public String getDesc() {
            return desc;
        }
    }


    public Statistiques(){
        this.stats = new ArrayList<String>();
    }




    public void ajusterStats(Activites activites){
        setStat(indexDescStats.NB_ACTV_SOUMISES, incrementerNbActvSoumises(activites));
        setStat(indexDescStats.NB_ACTV_PRIX_MOINS_DE_20, incrementerNbActvPrixMoinsDe20(activites));
        setStat(indexDescStats.NB_ACTV_PRIX_DE_20_A_40, incrementerNbActvPrixDe20a40(activites));
        setStat(indexDescStats.NB_ACTV_PRIX_PLUS_DE_40, incrementerNbActvPrixPlusDe40(activites));
        setStat(indexDescStats.NB_ACTV_MARCHE, incrementerNbActvMarche(activites));
        setStat(indexDescStats.NB_ACTV_METRO, incrementerNbActvMetro(activites));
        setStat(indexDescStats.NB_ACTV_BUS, incrementerNbActvBus(activites));
        setStat(indexDescStats.DISTANCE_MAX_POUR_UNE_ACTV, updaterDistanceMaxActv(activites));
        setStat(indexDescStats.PRIX_MAX_POUR_UNE_ACTV, updaterPrixMaxActv(activites));
    }

    public String incrementerNbActvSoumises(Activites activites){
        return String.valueOf(Integer.parseInt(getStat(indexDescStats.NB_ACTV_SOUMISES)) + activites.getListeDescriptions().size() );
    }
    public String incrementerNbActvPrixMoinsDe20(Activites activites){
        int nbActvPrixMoinsDe20 = Integer.parseInt(getStat(indexDescStats.NB_ACTV_PRIX_MOINS_DE_20) );
        for(double prix: activites.calcPrixTotalParEleve(activites.getNbEleves())){
            if(prix < 20){
                nbActvPrixMoinsDe20 = nbActvPrixMoinsDe20 + 1;
            }
        }
        return String.valueOf(nbActvPrixMoinsDe20);
    }
    public String incrementerNbActvPrixDe20a40(Activites activites){
        int nbActvPrixDe20a40 = Integer.parseInt(getStat(indexDescStats.NB_ACTV_PRIX_DE_20_A_40) );
        for(double prix: activites.calcPrixTotalParEleve(activites.getNbEleves())){
            if(prix >= 20 && prix <= 40){
                nbActvPrixDe20a40 = nbActvPrixDe20a40 + 1;
            }
        }
        return String.valueOf(nbActvPrixDe20a40);
    }
    public String incrementerNbActvPrixPlusDe40(Activites activites){
        int nbActvPrixPlusDe40 = Integer.parseInt(getStat(indexDescStats.NB_ACTV_PRIX_PLUS_DE_40) );
        for(double prix: activites.calcPrixTotalParEleve(activites.getNbEleves())){
            if(prix > 40){
                nbActvPrixPlusDe40 = nbActvPrixPlusDe40 + 1;
            }
        }
        return String.valueOf(nbActvPrixPlusDe40 );
    }
    public String incrementerNbActvMarche(Activites activites){
        int nbActvMarche = Integer.parseInt(getStat(indexDescStats.NB_ACTV_MARCHE) );
        for(String codeTransport: activites.getListeCodeTransport()){
            if(codeTransport.equals(activites.CODE_TRANSPORT_MARCHE)){
                nbActvMarche = nbActvMarche + 1;
            }
        }
        return String.valueOf(nbActvMarche);
    }
    public String incrementerNbActvMetro(Activites activites){
        int nbActvMetro = Integer.parseInt(getStat(indexDescStats.NB_ACTV_METRO) );
        for(String codeTransport: activites.getListeCodeTransport()){
            if(codeTransport.equals(activites.CODE_TRANSPORT_METRO)){
                nbActvMetro = nbActvMetro + 1;
            }
        }
        return String.valueOf(nbActvMetro);
    }
    public String incrementerNbActvBus(Activites activites){
        int nbActvBus = Integer.parseInt(getStat(indexDescStats.NB_ACTV_BUS) );
        for(String codeTransport: activites.getListeCodeTransport()){
            if(codeTransport.equals(activites.CODE_TRANSPORT_BUS)){
                nbActvBus = nbActvBus + 1;
            }
        }
        return String.valueOf(nbActvBus );
    }
    public String updaterDistanceMaxActv(Activites activites){
        int distanceMax = Integer.parseInt(getStat(indexDescStats.DISTANCE_MAX_POUR_UNE_ACTV) );
        for(String distance: activites.getListeDistanceTrajet()){
            if(Integer.valueOf(distance) > distanceMax){
                distanceMax = Integer.parseInt(distance);
            }
        }
        return String.valueOf(distanceMax);
    }
    public String updaterPrixMaxActv(Activites activites) {
        double prixMax = Double.parseDouble(getStat(indexDescStats.PRIX_MAX_POUR_UNE_ACTV) );
        for(double prix: activites.calcPrixTotalParEleve(activites.getNbEleves())){
            if(prix > prixMax){
                prixMax = prix;
            }
        }
        return String.valueOf(prixMax);
    }




    public void load(){

        try{
            FileReader fileReader = new FileReader(NOM_DU_FICHIER);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while(bufferedReader.ready() == true){
                this.stats.add(bufferedReader.readLine());
            }
            bufferedReader.close(); fileReader.close();
        }
        catch(FileNotFoundException e){
            initialiser();
        }
        catch(IOException e){
            System.err.println("Erreur dans la lecture du fichier!");
        }
    }


    public void save(){

        try{
            PrintWriter printWriter = new PrintWriter(NOM_DU_FICHIER);
            for(int i = 0; i < this.stats.size(); i++){
                printWriter.println(this.stats.get(i));
            }
            printWriter.close();
        }
        catch(IOException e){
            System.err.println("Erreur dans l'ecriture du fichier!");
        }
    }

    public void initialiser(){
        for(int i = 0; i < descriptionsStats.values().length; i++){
            this.stats.add("0");
        }
    }

    public void reinitialiser(){

        initialiser();
        save();
        System.out.println("Les statistiques ont ete reinitialisees.");
    }

    public void afficher(){
        int i = 0;
        System.out.println();
        for(descriptionsStats desc: descriptionsStats.values()){
            System.out.printf("%s%s\n",desc.getDesc(),this.stats.get(i) );
            i++;
        }
    }
    public ArrayList<String> getStats() {
        return this.stats;
    }
    public void setStats(ArrayList<String> stats) {
        this.stats = stats;
    }
    public String getStat(int index){
        return this.stats.get(index);
    }

    public void setStat(int index, String stat){
        this.stats.set(index, stat);
    }



}

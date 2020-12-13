# Style suivie pour écrire le code de notre logiciel: 
Le style du code  de notre logiciel est inspiré du ([1TBS](http://en.wikipedia.org/wiki/Indent_style#Variant:_1TBS)).
Il faut noter que le français est la langue de nos  variables, méthodes et classes. 
Aussi, les commentaires dans le code ont été rédigé en français.

L'indentation est de 2 colonnes.
La continuation de l'indentation est de 4 colonnes. 
les continuations imbriqués peut ajouter 4 colonnes ou 2 a chaque niveau. 
L'initialisation est faite sur une seule ligne comme suit : 

    :::java
    private final int NB_ELEVES_MIN_VALIDE = 1;
    
La declaration des methodes est faite en sorte qu'il ya une ligne qui sépare la signatuire du corps de la methode et une autre ligne avant la fin du corps comme l'exemple ci-bas: 
 
    public void loadNomDesCles() {
 
        JSONArray activites = this.jsR.getJsObjArray(ACTIVITES);
        JSONObject uneActivite = activites.getJSONObject(0);
        Iterator itr = uneActivite.keys();
        while (itr.hasNext()) {
            this.listeNomDesClesActivite.add(itr.next().toString());
            
        }
    }
   
 
 # Nomenclature des classes, methodes, variables et constantes. 
 
Le style de declaration des constantes est SNAKE_CASE. 
Exemple de notre code :
    
        CODE_TRANSPORT_MARCHE
 
Le style de declaration des variables et methode est camelCase 

        listeDates
        
        public double calcPrixDeBaseBus(int distance) {...}
 
Le style de declaration des classe est PascaleCase . 

          public class Activites {...}
    
Tout appel de methode est fait comme est montré par l'exemple suivant : 

    public ArrayList<String> validerAuMoinsUneActiviteeExterieur() {
        ArrayList<String> msgRecmd = new ArrayList<>();
        boolean estExterieur = false;
        for (int i = 0; i < getListeDistanceTrajet().size(); i++) {
            if (Integer.valueOf(getListeDistanceTrajet().get(i)) > 0) {
            estExterieur = true;
        }

# Declaration des variables et constantes 


# les importations
Il n'a pas de  ligne qui sépare chaque importation de l'autre: 

    
    import java.time.temporal.ChronoUnit;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Iterator;

# la declaration des packages:
On a met une ligne avant et apres la declaration du package. 
      
      package com.intellij.activies
      
      import java.util.Iterator;
      

Il ya un esapce avant et apres chaque:

 - Operateur(+, =, -, / )
 - Condition 
 - Methodes
 - Variables
 - Constantes
 - Paraentheses
 - Les boucles 
 - Apres chaque accolade
 
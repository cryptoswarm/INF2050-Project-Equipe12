# equipe12

TP 3 - INF2050

L'application à développer est un logiciel qui calculera les frais de sorties pour les classes d'une
école primaire et fera des recommandations

Le logiciel ne possèdera pas d'interface utilisateur car il est destiné à être invoqué à partir d'une
application web. Le contrat ne consiste donc qu'au développement du moteur de validation de
l'application.

Le logiciel contient 4 classes : 
# 1) JsonReader: 
Classe qui charge un objet JSON et possède des méthodes pour extraire des informations de ce dernier.
# 2) EcrireFichier: 
Classe qui est responsable à sauvegarder un STRING dans un fichier. (Code fourni par M. Berger)
# 3) Activites : 
Classe qui contient des informations pour des activités, à l'aide de la classe JsonReader. De plus, elle contient des méthodes qui permet de faire des calculs par rapports aux activités.
# 4) Sortie : 
Classe avec la méthode Main, qui s'en occupe à faire la coordination des 3 autres classes précédentes.

Le logiciel nécessite des librairie de JSON qui peut être obtenu à partir du site suivante : <br />
https://github.com/jacquesberger/exemplesINF2050/tree/master/ateliers/json-lib/librairies

Le logiciel doit être lancé avec 2 paramètres : 
java -jar Sortie.jar nomDuFichierEntree.json nomDuFichierSortie.json

Où nomDuFichierEntree.json est le nom du fichier contenant des informations des activités en format JSON <br />
et nomDuFichierSortie.json est le nom du fichier qu'on sauvegarde le résultat.

Le logiciel vérifie si le fichier d'entrée contient tous les champs nécessaires et aussi s'il y a des erreurs dans des données. 
Dans le cas des erreurs, le logiciel sauvegardera seulement un message d'erreur dans le fichier resultant. Si tout est valide, il effectuera les calculs, puis sauvegardera le résultat dans le fichier sous le nom donné ci-dessus, avec des messages de recommandations si applicable.

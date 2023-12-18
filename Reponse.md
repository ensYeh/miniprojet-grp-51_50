Manuel utilisateur

* Comment l’utilisateur peut savoir quelle commande taper pour un élément du répertoire désigné ?  
l’utilisateur peut lancer n’importe quelle commande avec n’importe quel NER. Toutefois il se peut que la fonction n’effectue rien. 
Exemple si l’utilisateur souhaite supprimer une note pour un NER qui n’a pas de notes alors il pourra le faire mais cela ne fera rien.

* Quelles sont les mises à jours du fichier des annotations à effectuer en fonction des types de commandes  
En fonction de la commande lancée il faut prendre en compte les choses suivantes : 
Pour la commande Mkdir : 
- lorsque le repertoire B est crée dans un répertoire A, un NER lui est attribué. Il faut donc mettre à jour le NER (NER = NER +1) de tous les éléments de B qui ont un NER plus grand que A. 

Pour la commande + : 
- Si le fichier n’a pas de notes alors une note lui est crée dans le fichier JSON
- Si le fichier a déjà une note alors la note doit être ajouté au tableau des notes du NER en question.

Pour la commande - : 
- Si le fichier n’a pas de notes il ne se passe rien 
- Si le fichier à une note alors il faut supprimer la note dans le fichier JSON.
- Si la note supprimée était la dernière du fichier alors le fichier JSON est supprimé.

Pour la commande past :
- Si la commande est précédé d’un cut alors il faut verifier que lorsque le fichier est supprimé alors les NER qui était plus grand que le fichier supprimé baissent de 1. Il faut donc aussi adapter le JSON et baisser aussi l’attribut number des notes correspondantes dans le JSON.
- Quand les fichiers sont collés, c’est comme pour un mkdir, il faut augmenter de 1 les NER des fichiers qui ont un NER supérieur au fichier ajouté ( A faire pour chaque element ajouté).

* Y a t il des bibliothèques Java qui permettront de prendre en charge la visualisation d’une image png si l’utilisateur veut l’afficher ? 

- Ca n’a pas été implémenté dans notre projet mais c’est quelque chose qui pourrait être ajouté.

* Quelles sont les commandes qui seraient utiles de rajouter ?  
- Ajouter une commande qui permet de créer des fichiers txt
- Ajouter un commande qui permet de supprimer des elements du repertoire ( fichier/repertoire )
- Ajouter une commande qui permet de créer un dossier compressé
- Ajouter une commande help qui afficher l’intégralité des commandes prises en charge par l’application. (Comme le man sur linux)
- Ajouter une commande qui montre les droits qui nous sont accordés à l’utilisateur pour un element de NER (ou tous)
- Permettre de visualiser des elements sur page plutôt que dans le terminal (txt ou autre)
- Ajouter un commande Grep qui permet si un element est un fichier, de lire le contenu du fichier et de retourner toutes les lignes qui contiennent le motif recherché. 

* Quelles améliorations peut on envisager pour rendre l’usage de l’interface clavier plus souples/efficaces pour l’utilisateur ?  
- Permettre un affichage en sur plusieurs lines des notes pour un fichier (Si la note est trop longue il fait déplacer sur le coté pour tout voir)
- Ajouter une saisie des commandes plus permissive à l’utilisateur car si la commande est précédée d’un espace elle ne se lance pas car le regex ne la reconnait pas.  
- Ajouter une auto-complétion des commandes lors de la saisie.
- Permettre à l’utilisateur de pouvoir lancer des commandes sur des elements de répertoire qui ne sont pas du répertoire courant.
- Mettre en sur-brillance le NER sélectionné plutôt  que de faire un print de l’élément
- Et par consequent utiliser la librairie Jansi pour faire un affichage plus propre que l’affichage actuel.
- Ajouter un système de vérification de copy cut pour ne pas déplacer ou couper des fichiers sensibles

* Quelles évolutions peut-on envisager ?  
- Lorsque le past à lieu nous aimerions pouvoir écraser le fichier possédant le même nom au lieu de ne pouvoir que rajouter un l’élément après l’avoir renommé. 

Quel est le rôle des différentes classes ? 

App.java : 
- Crée un terminal
- Construit un object de la classe Directory à partir du répertoire courant
- Donne l’information sur le NER actuel
- Récupère la saisie de l’utilisateur
- Vérifie qu’elle correspond bien à un commande réalisable 
- Execute la fonction associée à la saisie de l’utilisateur 
- Elle représente la boucle principale du code qui permet de relancer des commandes tant que l’utilisateur le souhaite.

Directory.java : 
- Permet de créer un répertoire
- Permet de se déplacer d’un répertoire à l’autre
- Permet de récupérer le path du répertoire courant
- Permet la gestion des NER et par conséquent permet l’association entre un NER et un élément du répertoire
- Un répertoire est composé d’un chemin et d’une Hashmap
    - La Hashmap a pour clé les NER et pour valeur les éléments du répertoire
- Ainsi il est possible de récupérer un NER associé à un fichier

Afficheur.java :
- Le seul but de cette classe est de récupérer le répertoire courant (sa Hashmap ) et d’afficher sur le terminal le contenu de la Hashmap. Les notes associées à chaque éléments s’il en existe seront affichées. Un répertoire est affiché en bleu et un fichier est affiché en rouge

NoteManager : 
- Gère l’intégralité des opération sur le fichier JSON du répertoire. 
- Création du Json
- Ajout d’une note
- Suppression d’une note
- Mettre à jour le NER d’une note lorsqu’un fichier avec un NER plus petit  (ou plus grand) est ajouté
- - SI une note est supprimée et que le JSON est vide alors le JSON est supprimé
- Trie les notes (permet un accès séquentiel des notes plus rapide si le NER est petit) La recherche reste tout de même en O(n) 

NoteEntry : 
- Chaque note est un objet ainsi chaque opération présenté dans le NoteManager vont justement traiter le JSON à partir des objets NoteEntry qui composent le fichier JSON

CommandManager : 
- Contient l’intégralité des commandes de demandé dans le sujet : 
    - Mkdir crée un répertoire
    - Cut/Copy enregistre un fichier dans le press papier
    - Past colle le fichier/repertoire qui se trouve dans le press papier
        - Si un cut est fait auparavant alors l’élément source est supprimé
        - Propose systématiquement de renommer le fichier (au lieu de mettre -copy par défaut)
    - Find affiche le path de tous les éléments qui ont le même nom que le fichier demandé
    - Visu affichele contenu si c’est un txt sinon, affiche la taille
- Des fonctions intermédiaires sont utilisées pour faire fonctionnels certaines fonctions présentées juste au dessus : 
    - getExtension qui récupère l’extension d’un fichier pour le Visu
    - getDirectorysize qui récupère la taille du répertoire pour Visu
    - generateUniqueElementNumber pour construire la Hashmap de la fonction copyCut 
    - copyDirectoryContents qui récupère l’élément dans le presse papier pour les copier dans le répertoire destination 

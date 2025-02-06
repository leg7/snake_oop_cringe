# Compilation

Pour compiler le projet, lancez la commande suivante à la racine du projet :
`./build.sh && ./run.sh leonardgomez/Test`

# Ce que j'ai fais

Tout jusqu'a la 5

# Ce que j'ai ajouté pour la 5

J'ai ajouté deux types de jeux différents : SnakeGameAI et SnakeGamePVP. Deux
instances sont fournies dans le contrôleurs, il vous suffit de décommenter
celle de votre choix pour l'utiliser.

Pour implémenter ces deux types de jeux, j'ai étendu le type Agent avec une
sous-interface AgentUserControlled. J'ai également modifié la fabrique afin de
pouvoir créer divers agents différents, avec des paramètres par défaut
intéressants.

Enfin, j'ai utilisé le pattern Strategy pour les agents SnakeAI.

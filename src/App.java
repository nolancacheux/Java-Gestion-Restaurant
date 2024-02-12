
import java.text.ParseException;
import java.util.*;

public class App {

    public static void main(String[] args) throws ParseException {
    	
    	
    	System.out.println("\nBienvenue dans l'application de gestion du restaurant.\n");
    	
        // Initialisation du stock, du menu et de la liste d'employés
        Stock stock = Stock.initialiserStock();
        Menu carte = Menu.initialiserMenu();
        List<Employe> employes = Employe.initialiserEmployes();

        // Création d'une instance de restaurant
        Restaurant restaurant = new Restaurant("Le Gourmet", "123 Rue de la Gastronomie", 50, stock, carte, employes);
        
        Restaurant.afficherInformationsRestaurant(restaurant);
        
     // Création d'un scanner pour la saisie utilisateur
        Scanner scanner = new Scanner(System.in);

        // Ouverture du restaurant
        restaurant.ouvrirRestaurant(scanner);

        // Affichage des informations du restaurant
        Restaurant.afficherInformationsRestaurant(restaurant);
        


        int choixEcran;
        try {
        	do {
            // Affichage du menu principal
            afficherMenuPrincipal();
            choixEcran = lireChoixUtilisateur(scanner);

            // Gestion des choix de l'utilisateur
            switch (choixEcran) {
                case 1:
                    // Gestion de la prise de commande
                    Gestion.gérerPriseDeCommande(scanner, restaurant);
                    break;
                case 2:
                    // Gestion de l'écran de cuisine
                    Gestion.gérerEcranCuisine(scanner, restaurant);
                    break;
                case 3:
                    // Gestion de l'écran de bar
                    Gestion.gérerEcranBar(scanner, restaurant);
                    break;
                case 4:
                    // Gestion de l'écran de monitoring
                    Gestion.gérerEcranMonitoring(scanner, restaurant);
                    break;
                case 5:
                    // Gestion des employés
                    Gestion.gérerGestionEmployés(scanner, restaurant);
                    break;
                case 6:
                    // Gestion des stocks
                    Gestion.gérerGestionStock(scanner, restaurant);
                    break;
                case 7:
                    // Planification des employés pour la soirée
                    Gestion.gérerPlanificationEmployés(scanner, restaurant);
                    break;
                case 8:
                    // Gestion de l'addition
                    Gestion.gererAddition(scanner, restaurant);
                    break;
                case 9:
                    // Gestion de l'addition
                    Gestion.EcranGestionFacture(scanner);
                    break;
                case 0:
                    // Après toutes les opérations quotidiennes
                    Restaurant.nettoyerRestaurant(restaurant);
                    System.out.println("\nFermeture du programme.");
                    scanner.close();
                    return; // Ajout de cette ligne pour quitter le programme

                default:
                    System.out.println("\nChoix non valide. Veuillez réessayer.");
            }
        	} while (choixEcran != 0);  // 0 pour quitter
        } catch (NoSuchElementException e) {
            System.err.println("Erreur de lecture de l'entrée : " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Méthode pour afficher le menu principal
    private static void afficherMenuPrincipal() {
        System.out.println("\n\n\nQuel écran souhaitez-vous afficher ?");
        System.out.println("\n1 - Ecran prise de commande");
        System.out.println("\n2 - Ecran cuisine");
        System.out.println("\n3 - Ecran bar");
        System.out.println("\n4 - Ecran Monitoring");
        System.out.println("\n5 - Gestion des employés");
        System.out.println("\n6 - Gestion des stocks");
        System.out.println("\n7 - Planification des employés pour la soirée");
        System.out.println("\n8 - Gestion de l'addition");
        System.out.println("\n9 - Ecran d'affichage des factures");
        System.out.println("\n0 - Quitter");
    }

 // Méthode pour lire le choix de l'utilisateur
    public static int lireChoixUtilisateur(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();  // Retourne l'entier si l'entrée est valide
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next();  // Élimine l'entrée non entière pour éviter une boucle infinie
            }
        }
    }

}

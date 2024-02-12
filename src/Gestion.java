import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Gestion {
	
	public static void gérerPriseDeCommande(Scanner scanner, Restaurant restaurant) {
	    // Sélection du serveur
	    Serveur serveur = Serveur.choisirServeur(scanner, restaurant);

	    // Informations sur le client
	    int nombreClients;
	    do {
	        System.out.println("\nEntrer le nombre de clients (1-20):");
	        nombreClients = App.lireChoixUtilisateur(scanner);
	        if (nombreClients < 1 || nombreClients > 20) {
	            System.out.println("Nombre de clients invalide. Veuillez entrer un nombre entre 1 et 20.");
	        }
	    } while (nombreClients < 1 || nombreClients > 20);

	    scanner.nextLine(); // Nettoyer le tampon

	 // Informations sur le souhait du client
	    String souhaits;
	    int choixSouhait;
	    do {
	        System.out.println("\nChoisissez les souhaits pour le placement (entrez le numéro correspondant) :");
	        System.out.println("1 - Proximité Fenêtre");
	        System.out.println("2 - Vue sur le jardin");
	        System.out.println("3 - Coin tranquille");
	        System.out.println("4 - Près du bar");
	        System.out.println("5 - Aucun souhait spécifique");

	        choixSouhait = App.lireChoixUtilisateur(scanner);

	        switch (choixSouhait) {
	            case 1:
	                souhaits = "Proximité Fenêtre";
	                break;
	            case 2:
	                souhaits = "Vue sur le jardin";
	                break;
	            case 3:
	                souhaits = "Coin tranquille";
	                break;
	            case 4:
	                souhaits = "Près du bar";
	                break;
	            case 5:
	                souhaits = "Aucun souhait spécifique";
	                break;
	            default:
	                System.out.println("Veuillez choisir une option valide.");
	                souhaits = "";
	        }
	    } while (souhaits.isEmpty());

	    Client client = new Client(nombreClients, souhaits);

	    
	    // Sélection de la table
	    int numTable;
	    do {
	        System.out.println("\nNuméro de la table (1-50):");
	        numTable = App.lireChoixUtilisateur(scanner);
	        if (numTable < 1 || numTable > 50) {
	            System.out.println("Numéro de table invalide. Veuillez entrer un numéro entre 1 et 50.");
	        }
	    } while (numTable < 1 || numTable > 50);

	    // Création de la commande
	    Commande commande = new Commande();
	    Table table = new Table(numTable, nombreClients, true, serveur, commande);

	    // Ajout des plats et des boissons
	    Serveur.prendreCommande(scanner, commande, restaurant);
	    
	    // Vérifier si la commande est vide
	    if (commande.getPlats().isEmpty() && commande.getBoissons().isEmpty()) {
	        System.out.println("\nAucune commande n'a été faite.");
	        return; // Sortir de la méthode si aucune commande n'est passée
	    }
	    
	    // Associer la commande à la table et au serveur
	    serveur.getTablesAssignees().add(table);

	    // Récapitulatif de la commande et mise à jour du stock
	    System.out.println("\n\n\nRécapitulatif de la commande:\n\n");
	    serveur.accueillirClient(client, numTable);
	    System.out.println(commande.resumerCommande());
	    System.out.println("\nTotal à payer: " + commande.calculerTotal() + "€");
	    Stock.diminuerStock(commande, restaurant); // Appel à la méthode de mise à jour du stock

	    commande.setEnPreparation(true);
	    // Envoyer la commande au bar ou en cuisine
	    if (!commande.getPlats().isEmpty()) {
	        System.out.println("\nCommande envoyée à la cuisine.");
	    }
	    if (!commande.getBoissons().isEmpty()) {
	        System.out.println("\nCommande envoyée au bar.");
	    }
	}

    
    public static void gérerEcranCuisine(Scanner scanner, Restaurant restaurant) {
    	System.out.println("\nListe des commandes en cours:");

        Map<Integer, Commande> commandesEnCours = new HashMap<>();
        int compteur = 1;
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                Serveur serveur = (Serveur) employe;
                for (Table table : serveur.getTablesAssignees()) {
                    Commande commande = table.getCommande();
                    if (commande != null && !commande.estPretee() && commande.estEnPreparation() && !commande.getPlats().isEmpty()) {
                        commandesEnCours.put(compteur, commande);
                        System.out.println("\nCommande en cours: " + compteur + " (Table " + table.getNumero() + "): " + commande.getPlats());
                        compteur++;
                    }
                }
            }
        }
        if (commandesEnCours.isEmpty()) {
            System.out.println("\nAucune commande en cours de préparation.");
            return;
        }

        System.out.println("\nEntrer le numéro de la commande en cours pour vérifier l'état ou passer à une autre commande:");
        int choixCommande = App.lireChoixUtilisateur(scanner);

        if (!commandesEnCours.containsKey(choixCommande)) {
            System.out.println("\nNuméro de commande invalide.");
            return;
        }

        // Vérifier si un cuisinier est déjà occupé avec cette commande
        boolean commandeDejaPrise = false;
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Cuisinier) {
                Cuisinier cuisinier = (Cuisinier) employe;
                if (cuisinier.estOccupeAvecCommande(commandesEnCours.get(choixCommande))) {
                    System.out.println("\nLe cuisinier " + cuisinier.getNom() + " est déjà en train de traiter cette commande. Veuillez préparer une autre commande.");
                    commandeDejaPrise = true;
                    break;
                }
            }
        }

        // Suite de la méthode gérerEcranCuisine
        if (!commandeDejaPrise) {
        	// Trouver un cuisinier libre
            final Cuisinier[] cuisinierLibre = new Cuisinier[1];
            for (Employe employe : restaurant.getEmployes()) {
                if (employe instanceof Cuisinier && !((Cuisinier) employe).estOccupe()) {
                    cuisinierLibre[0] = (Cuisinier) employe;
                    break;
                }
            }

            if (cuisinierLibre[0] == null) {
                System.out.println("\nAucun cuisinier libre pour le moment. Veuillez réessayer plus tard.");
                return;
            }

            // Préparation de la commande
            Commande commandeAPreparer = commandesEnCours.get(choixCommande);
            if (commandeAPreparer.EstPretePlats()) {
                System.out.println("\nLes plats de cette commande ont déjà été préparés.");
                return; // Sortir de la méthode si les plats ont déjà été préparés
            }
            cuisinierLibre[0].setOccupe(true);
            cuisinierLibre[0].setCommandeEnCours(commandeAPreparer); // Supposant qu'il y a une méthode pour attribuer une commande au cuisinier
            System.out.println("\nLe cuisinier " + cuisinierLibre[0].getNom() + " commence la préparation de la commande de la table " + choixCommande + ". (Attente estimée : 5 secondes)");
            
            // Simulation du temps de préparation
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    cuisinierLibre[0].setOccupe(false);
                    cuisinierLibre[0].setCommandeEnCours(null); 
                    commandeAPreparer.setPretePlats(true);
                    System.out.println("\nLa commande de la table " + choixCommande + " est maintenant prête en cuisine;");
                    
                 // Vérification si la commande de boissons est également prête
                    if (commandeAPreparer.EstPreteBoissons() || commandeAPreparer.getBoissons().isEmpty()) {
                        Serveur serveur = commandeAPreparer.getServeur(restaurant); 
                        System.out.println("Le serveur assigné " + serveur.getNom() + " va servir la commande à la table " + choixCommande);
                        commandeAPreparer.marquerCommePrete();
                        System.out.println("\nLa commande est maintenant disponible dans l'écran de gestion des additions");
                     
                    }
                }
            }, 5000); // 5 secondes pour la préparation
        
        }
    }

    public static void gérerEcranBar(Scanner scanner, Restaurant restaurant) {
        System.out.println("\nEcran de gestion du bar");

        // Affichage des commandes de boissons en attente
        Map<Integer, Commande> commandesEnAttente = new HashMap<>();
        int compteur = 1;
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                for (Table table : ((Serveur) employe).getTablesAssignees()) {
                    Commande commande = table.getCommande();
                    if (commande != null && !commande.estPretee() && commande.estEnPreparation() && !commande.getBoissons().isEmpty()) {
                        commandesEnAttente.put(compteur, commande);
                        System.out.println("\nCommande " + compteur + " (Table " + table.getNumero() + "): Boissons: " + commande.getBoissons());
                        compteur++;
                    }
                }
            }
        }

        if (commandesEnAttente.isEmpty()) {
            System.out.println("\nAucune commande en attente.");
            return;
        }

        // Sélection de la commande à préparer
        System.out.println("\nEntrer le numéro de la commande à préparer:");
        int choixCommande = App.lireChoixUtilisateur(scanner);
        if (!commandesEnAttente.containsKey(choixCommande)) {
            System.out.println("\nNuméro de commande invalide.");
            return;
        }

        // Vérifier si un barman est déjà occupé avec cette commande
        boolean commandeDejaPrise = false;
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Barman) {
                Barman barman = (Barman) employe;
                if (barman.estOccupeAvecCommande(commandesEnAttente.get(choixCommande))) {
                    System.out.println("\nLe barman " + barman.getNom() + " est déjà en train de traiter cette commande. Veuillez préparer une autre commande.");
                    commandeDejaPrise = true;
                    break;
                }
            }
        }

        if (!commandeDejaPrise) {
            // Trouver un barman libre
            final Barman[] barmanLibre = new Barman[1];
            for (Employe employe : restaurant.getEmployes()) {
                if (employe instanceof Barman && !((Barman) employe).EstOccupe()) {
                    barmanLibre[0] = (Barman) employe;
                    break;
                }
            }

            if (barmanLibre[0] == null) {
                System.out.println("\nAucun barman libre pour le moment. Veuillez réessayer plus tard.");
                return;
            }

            // Préparation de la commande
            Commande commandeAPreparer = commandesEnAttente.get(choixCommande);
            
            if (commandeAPreparer.EstPreteBoissons()) {
                System.out.println("\nLes boissons de cette commande ont déjà été préparées.");
                return; // Sortir de la méthode si les boissons ont déjà été préparées
            }
            
            barmanLibre[0].setOccupe(true);
            barmanLibre[0].setCommandeEnCours(commandeAPreparer); // Supposant qu'il y a une méthode pour attribuer une commande au barman
            System.out.println("\nLe barman " + barmanLibre[0].getNom() + " commence la préparation de la commande de la table " + choixCommande + ". (Attente estimée : 5 secondes)");

            // Simulation du temps de préparation
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    barmanLibre[0].setOccupe(false);
                    barmanLibre[0].setCommandeEnCours(null); // La commande est terminée
                    commandeAPreparer.setPreteBoissons(true);
                    System.out.println("\nLa commande de boissons pour la table " + choixCommande + " est maintenant prête au bar;");
                    
                 // Vérification si la commande de boissons est également prête
                    if (commandeAPreparer.EstPretePlats()|| commandeAPreparer.getPlats().isEmpty()) {
                        Serveur serveur = commandeAPreparer.getServeur(restaurant); 
                        System.out.println("Le serveur assigné " + serveur.getNom() + " va servir la commande à la table " + choixCommande);
                        commandeAPreparer.marquerCommePrete(); // Marquer la commande comme servie
                        System.out.println("\nLa commande est maintenant disponible dans l'écran de gestion des additions");
                        
                        
                    }
                }
            }, 5000); // 5 secondes pour la préparation
        }
    }

    public static void gérerEcranMonitoring(Scanner scanner, Restaurant restaurant) throws ParseException {
        // Affichage et sélection du manager
        System.out.println("Veuillez choisir le manager qui s'occupe de cet écran :");
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Manager) {
                System.out.println(employe.getNom());
            }
        }
        Manager manager = null;
        scanner.nextLine().trim();
        do {
        	
        
        // Lecture du choix du manager
        System.out.println("\nEntrez le nom du manager:");
        String nomManager = scanner.nextLine().trim();
        
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Manager && employe.getNom().equalsIgnoreCase(nomManager)) {
                manager = (Manager) employe;
                break;
            }
        }
        if (manager == null) {
            System.out.println("Manager non trouvé. Veuillez réessayer.");
        }
    }while(manager == null) ;

        System.out.println("\nLe manager "+manager.getPrenom()+" "+manager.getNom()+" gère l'écran de Monitoring.") ;
 
        // Options pour le manager
        System.out.println("\nQue souhaitez-vous faire ?");
        System.out.println("1 - Surveiller les performances");
        System.out.println("2 - Imprimer la liste des courses");

        int choix = App.lireChoixUtilisateur(scanner);

        switch (choix) {
            case 1:
                // Surveillance des performances
                manager.surveillerPerformance(restaurant);
                break;
            case 2:
                // Impression de la liste des courses
                Map<String, Integer> ventesParPlat = collecterVentesParPlat(restaurant);
                Map<String, Integer> ventesParBoissons = collecterVentesParBoissons(restaurant);
                manager.imprimerListeDeCourses(restaurant, ventesParPlat,ventesParBoissons);
                break;
            default:
                System.out.println("Choix non valide.");
        }
    }

    private static Map<String, Integer> collecterVentesParPlat(Restaurant restaurant) {
        Map<String, Integer> ventesParPlat = new HashMap<>();

        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                Serveur serveur = (Serveur) employe;
                for (Table table : serveur.getTablesAssignees()) {
                    Commande commande = table.getCommande();
                    if (commande != null) {
                        for (Plat plat : commande.getPlats()) {
                            ventesParPlat.put(plat.getNom(), ventesParPlat.getOrDefault(plat.getNom(), 0) + 1);
                        }
                    }
                }
            }
        }
        return ventesParPlat;
    }
    
    private static Map<String, Integer> collecterVentesParBoissons(Restaurant restaurant) {
        Map<String, Integer> ventesParBoissons = new HashMap<>();

        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                Serveur serveur = (Serveur) employe;
                for (Table table : serveur.getTablesAssignees()) {
                    Commande commande = table.getCommande();
                    if (commande != null) {
                        for (Boisson plat : commande.getBoissons()) {
                            ventesParBoissons.put(plat.getNom(), ventesParBoissons.getOrDefault(plat.getNom(), 0) + 1);
                        }
                    }
                }
            }
        }
        return ventesParBoissons;
    }
    
    
    public static void gérerGestionEmployés(Scanner scanner, Restaurant restaurant) {
        System.out.println("\nGestion des employés:");
        System.out.println("\n1 - Ajouter un employé");
        System.out.println("\n2 - Supprimer un employé");
        System.out.println("\n3 - Afficher les employés");
        int choix = App.lireChoixUtilisateur(scanner);
        scanner.nextLine();  // Nettoyer le tampon

        switch (choix) {
            case 1:
                Employe.ajouterEmploye(scanner, restaurant);
                break;
            case 2:
                Employe.supprimerEmploye(scanner, restaurant);
                break;
            case 3:
                Employe.afficherEmployes(restaurant);
                break;
            default:
                System.out.println("\nChoix non valide.");
        }
    }

    public static void gérerGestionStock(Scanner scanner, Restaurant restaurant) {
        System.out.println("\nGestion des stocks:");
        System.out.println("1 - Afficher l'inventaire");
        System.out.println("2 - Ajouter du stock à un aliment existant");
        System.out.println("3 - Ajouter un nouvel aliment");
        System.out.println("4 - Supprimer un aliment");
        int choix = App.lireChoixUtilisateur(scanner);

        switch (choix) {
            case 1:
                Stock.afficherInventaire(restaurant);
                break;
            case 2:
                Stock.ajouterStockAlimentExistant(scanner, restaurant);
                break;
            case 3:
                Aliment.creerNouvelAliment(scanner, restaurant);
                break;
            case 4:
                Aliment.supprimerAliment(scanner, restaurant);
                break;
            default:
                System.out.println("\nChoix non valide.");
        }
    }
    
    public static void gérerPlanificationEmployés(Scanner scanner, Restaurant restaurant) throws ParseException {
        System.out.println("\nPlanification des employés pour la soirée (De 18h à 23h30):");
        System.out.println("\n1 - Ajouter un shift pour un employé");
        System.out.println("\n2 - Voir les shifts d'un employé");
        int choix = App.lireChoixUtilisateur(scanner);
        scanner.nextLine(); // Nettoyer le tampon

        switch (choix) {
            case 1:
                Horaires.ajouterShift(scanner, restaurant);
                break;
            case 2:
            	Horaires.voirShifts(scanner, restaurant);
                break;
            default:
                System.out.println("\nChoix non valide.");
        }
    }

    public static void gererAddition(Scanner scanner, Restaurant restaurant) {
	    System.out.println("\nGestion des additions pour les tables");
	
	    // Affichage des informations de toutes les tables
	    boolean tableDisponible = false;
	    for (Employe employe : restaurant.getEmployes()) {
	        if (employe instanceof Serveur) {
	            for (Table table : ((Serveur) employe).getTablesAssignees()) {
	                Commande commande = table.getCommande();
	                if (commande != null && !commande.estEntierementPayee() && 
	                	    (commande.estPretee() || 
	                	     (commande.EstPretePlats() && commande.getBoissons().isEmpty()) || 
	                	     (commande.EstPreteBoissons() && commande.getPlats().isEmpty()))) {
	                	    System.out.println("\nTable " + table.getNumero() + " - " + commande.resumerCommande());
	                	    tableDisponible = true;
	                	}

	                }

	            }
	        }
	
	    if (!tableDisponible) {
	        System.out.println("\nAucune table avec une commande en cours ou non payée.");
	        return;
	    }
	
	    // Sélection et gestion de l'addition pour une table spécifique
	    System.out.println("\nEntrez le numéro de la table pour laquelle l'addition doit être présentée:");
	    int numeroTable = App.lireChoixUtilisateur(scanner);
	    Table table = Table.trouverTableParNumero(numeroTable, restaurant);
	
	    if (table == null || table.getCommande() == null || table.getCommande().estEntierementPayee()) {
	        System.out.println("\nTable non trouvée ou aucune commande en cours pour cette table.");
	        return;
	    }
	
	    // Affichage de l'addition
	    Commande commande = table.getCommande();
	    System.out.println("\nAddition pour la table " + numeroTable + ":\n" + commande.resumerCommande());
	    System.out.println("\nTotal à payer: " + commande.calculerTotal() + "€");
	
	    // Gestion du paiement
	    Transaction transaction = new Transaction(table, table.getServeurAssigne(), commande);
	    int choixPaiement;
	    do {
	        System.out.println("\nMontant à payer : " + commande.calculerTotal());
	        System.out.println("\nPayer par :\n     1) Carte bancaire\n     2) Espèces");
	        choixPaiement = App.lireChoixUtilisateur(scanner);
	
	        if (choixPaiement == 1) {
	            System.out.println("\nMontant payé par carte bancaire.");
	            break;
	        } else if (choixPaiement == 2) {
	            System.out.println("\nMontant payé en espèces.");
	            break;
	        } else {
	            System.out.println("\nVeuillez choisir entre 1 et 2.");
	        }
	    } while (choixPaiement != 1 && choixPaiement != 2);
	
	    try {
	        transaction.effectuerPaiement(commande.calculerTotal());
	        System.out.println("\nFacture générée.");
	    } catch (IllegalArgumentException e) {
	        System.out.println(e.getMessage());
	    }
	}


    public static void EcranGestionFacture(Scanner scanner) {
        System.out.println("Quelles factures voulez-vous afficher ?");
        System.out.println("1) Facture des listes de courses");
        System.out.println("2) Factures des additions");

        int choixType = App.lireChoixUtilisateur(scanner);
        String fichier = choixType == 1 ? "listeDeCourses.ser" : "factures.ser";

        if (choixType == 1) {
            // Désérialisation de la liste de courses
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
                @SuppressWarnings("unchecked")
				HashMap<String, Integer> listeDeCourses = (HashMap<String, Integer>) ois.readObject();
                listeDeCourses.forEach((aliment, quantite) -> System.out.println(aliment + " : " + quantite + " unités"));
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la lecture de la liste de courses: " + e.getMessage());
            }
        } else if (choixType == 2) {
        	ArrayList<Facture> factures = chargerFactures();
            
            if (factures.isEmpty()) {
                System.out.println("Aucune facture à afficher.");
                return;
            }

            // Affichage des factures
            for (int i = 0; i < factures.size(); i++) {
                System.out.println((i + 1) + " - Facture n°" + (i + 1));
            }

            System.out.println("\nEntrez le numéro de la facture à afficher:");
            int choixFacture = App.lireChoixUtilisateur(new Scanner(System.in));
            if (choixFacture < 1 || choixFacture > factures.size()) {
                System.out.println("Choix invalide, veuillez réessayer.");
                return;
            }

            Facture factureChoisie = factures.get(choixFacture - 1);

            // Vérifier si factureChoisie est null avant d'appeler genererFacture
            if (factureChoisie != null) {
                System.out.println("Facture choisie:\n" + factureChoisie.genererFacture());
            } else {
                System.out.println("La facture choisie est null.");
            }
        } else {
            System.out.println("Choix invalide, veuillez réessayer.");
        }
    }

    public static ArrayList<Facture> chargerFactures() {
        ArrayList<Facture> factures = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("factures.ser"))) {
            // Désérialiser la liste existante de factures
            Object obj = ois.readObject();

            // Vérifier si l'objet est bien une ArrayList de Facture
            if (obj instanceof ArrayList) {
                factures = (ArrayList<Facture>) obj;
            } else {
                System.err.println("Le fichier ne contient pas une liste de factures.");
            }

        } catch (FileNotFoundException e) {
            // Si le fichier n'existe pas, créez une nouvelle liste de factures
            System.out.println("Aucune facture existante. Le fichier n'a pas été trouvé.");
        } catch (EOFException e) {
            // Si le fichier est vide, créez une nouvelle liste de factures
            System.out.println("Aucune facture existante. Le fichier est vide.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la lecture des factures: " + e.getMessage());
            e.printStackTrace();
        }

        return factures;
    }




}

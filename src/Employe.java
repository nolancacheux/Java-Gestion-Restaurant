import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Employe {
    protected String nom;
    protected String prenom;
    protected double salaire;
    protected Contrat contrat;
    protected List<Shift> shifts; // Ajout de la gestion des shifts

    public Employe(String nom, String prenom, double salaire) {
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.shifts = new ArrayList<>(); // Initialiser la liste des shifts
    }
    
 // Ajout du constructeur avec contrat
    public Employe(String nom, String prenom, double salaire, Contrat contrat) {
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.contrat = contrat; // Assignation du contrat
    }
    

    // Ajout du getter et setter pour contrat
    public Contrat getContrat() {
        return contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public void enregistrerHeuresTravail(Shift shift) {
        this.shifts.add(shift);
    }
    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public double getSalaire() {
		return salaire;
	}

	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}

    public static List<Employe> initialiserEmployes() { // liste de tous les employés*
    	List<Employe> employes = new ArrayList<>();
        employes.add(new Serveur("Pitt", "Brad", 2500.0)); // Serveur 1
        employes.add(new Serveur("Jolie", "Angelina", 2400.0)); // Serveur 2
        employes.add(new Serveur("Clooney", "George", 2450.0)); // Serveur 3
        employes.add(new Serveur("Roberts", "Julia", 2550.0)); // Serveur 4
        employes.add(new Serveur("Damon", "Matt", 2480.0)); // Serveur 5
        employes.add(new Serveur("Portman", "Natalie", 2430.0)); // Serveur 6
        employes.add(new Serveur("Doe", "John", 2500.0)); // Serveur 7
	    employes.add(new Serveur("Stone", "Emma", 2400.0)); // Serveur 8
        
        
        employes.add(new Cuisinier("DiCaprio", "Leonardo", 2700.0)); // Cuisinier 1
        employes.add(new Cuisinier("Hanks", "Tom", 2600.0)); // Cuisinier 2
        employes.add(new Cuisinier("Streep", "Meryl", 2600.0)); // Cuisinier 3
        employes.add(new Cuisinier("Depp", "Johnny", 2700.0)); // Cuisinier 4
        employes.add(new Cuisinier("Roe", "Alice", 2700.0)); // Cuisinier 5
	    employes.add(new Cuisinier("Smith", "Bob", 2600.0)); // Cuisinier 6
	    employes.add(new Cuisinier("Brown", "Charlie", 2600.0)); // Cuisinier 7
	    employes.add(new Cuisinier("Craig", "Daniel", 2700.0)); // Cuisinier 8
        
        employes.add(new Barman("Scarlett", "Johansson", 2200.0)); // Barman
        employes.add(new Barman("Stone", "Emma", 2250.0)); // Barman 2
        employes.add(new Barman("Lynn", "Eve", 2200.0)); // Barman
        
        employes.add(new Manager("Washington", "Denzel", 3200.0)); // Manager
        employes.add(new Manager("Jackson", "Samuel", 3300.0)); // Manager 2
        employes.add(new Manager("Theron", "Charlize", 3350.0)); // Manager 3
        employes.add(new Manager("Dond", "James", 3200.0)); // Manager
        
        employes.add(new Etudiant("Lawrence", "Jennifer", 1500.0)); // Employé étudiant
        employes.add(new Etudiant("Evans", "Chris", 1550.0)); // Employé étudiant 2
        employes.add(new Etudiant("Hathaway", "Anne", 1580.0)); // Employé étudiant 3
	    employes.add(new Etudiant("Dupont", "Robert", 1500.0)); // Employé étudiant


    	return employes;

    }
    
    public static String formatEmploye(Employe employe) {
        return employe.getNom() + " (" + employe.getClass().getSimpleName() + ")";
    }
    
    public static List<Employe> EmployesParDefaut() {
        List<Employe> employes = new ArrayList<>();
        employes.add(new Serveur("Doe", "John", 2500.0)); // Serveur 1
        employes.add(new Serveur("Stone", "Emma", 2400.0)); // Serveur 2
        employes.add(new Cuisinier("Roe", "Alice", 2700.0)); // Cuisinier 1
        employes.add(new Cuisinier("Smith", "Bob", 2600.0)); // Cuisinier 2
        employes.add(new Cuisinier("Brown", "Charlie", 2600.0)); // Cuisinier 3
        employes.add(new Cuisinier("Craig", "Daniel", 2700.0)); // Cuisinier 4
        employes.add(new Barman("Lynn", "Eve", 2200.0)); // Barman
        employes.add(new Manager("Bond", "James", 3200.0)); // Manager
        employes.add(new Manager("Jackson", "Samuel", 3300.0)); // Manager 2
        employes.add(new Etudiant("Dupont", "Robert", 1500.0)); // Employé étudiant
        return employes; 
    }

	
    public static List<Employe> selectionnerEmployes(Scanner scanner, List<Employe> tousLesEmployes) {
        List<Employe> employesSelectionnes = new ArrayList<>();
        Map<String, Integer> compteEmployesParCategorie = new HashMap<>();
        compteEmployesParCategorie.put("Cuisinier", 4);
        compteEmployesParCategorie.put("Serveur", 2);
        compteEmployesParCategorie.put("Manager", 1);
        compteEmployesParCategorie.put("Barman", 1);
        scanner.nextLine().trim();
        do {
            afficherEmployesParCategorie(tousLesEmployes, compteEmployesParCategorie);
            
            System.out.println("\nEntrez le nom de famille de l'employé à sélectionner:");
            String nomFamille = scanner.nextLine().trim();

            Employe employeChoisi = tousLesEmployes.stream()
                .filter(e -> e.getNom().equalsIgnoreCase(nomFamille))
                .findFirst()
                .orElse(null);

            if (employeChoisi == null || employesSelectionnes.contains(employeChoisi)) {
                System.out.println("Employé non trouvé ou déjà sélectionné.");
                continue;
            }

            String categorie = employeChoisi.getClass().getSimpleName();
            if (compteEmployesParCategorie.get(categorie) > 0) {
                employesSelectionnes.add(employeChoisi);
                compteEmployesParCategorie.put(categorie, compteEmployesParCategorie.get(categorie) - 1);
            } else {
                System.out.println("Aucun besoin supplémentaire en " + categorie);
            }
        }while (compteEmployesParCategorie.values().stream().anyMatch(count -> count > 0));

        return employesSelectionnes;
    }
    
    public static void afficherEmployesParCategorie(List<Employe> employes, Map<String, Integer> compteEmployesParCategorie) {
        System.out.println("\nEmployés disponibles par catégorie:");
        String formatLigne = "| %-15s | %-15s | %-15s | %-15s |\n";
        System.out.format(formatLigne, "Cuisinier", "Serveur", "Manager", "Barman");

        // Obtenez les listes séparées pour chaque catégorie
        Map<String, List<Employe>> employesParCategorie = employes.stream()
            .collect(Collectors.groupingBy(e -> e.getClass().getSimpleName()));

        // Déterminez la plus longue liste pour savoir combien de lignes afficher
        int maxTaille = employesParCategorie.values().stream()
            .mapToInt(List::size)
            .max()
            .orElse(0);

        for (int i = 0; i < maxTaille; i++) {
            String cuisinier = employesParCategorie.getOrDefault("Cuisinier", List.of()).size() > i ? employesParCategorie.get("Cuisinier").get(i).getNom() : "";
            String serveur = employesParCategorie.getOrDefault("Serveur", List.of()).size() > i ? employesParCategorie.get("Serveur").get(i).getNom() : "";
            String manager = employesParCategorie.getOrDefault("Manager", List.of()).size() > i ? employesParCategorie.get("Manager").get(i).getNom() : "";
            String barman = employesParCategorie.getOrDefault("Barman", List.of()).size() > i ? employesParCategorie.get("Barman").get(i).getNom() : "";
            System.out.format(formatLigne, cuisinier, serveur, manager, barman);
        }

        // Afficher les besoins restants en employés
        System.out.println("\nBesoin en employés:");
        compteEmployesParCategorie.forEach((categorie, compte) -> {
            if (compte > 0) {
                System.out.println(categorie + ": " + compte + " restant(s)");
            }
        });
    }
    
    public static boolean verifierPersonnelMinimum(List<Employe> employes) {
        int nombreCuisiniers = 0;
        int nombreServeurs = 0;
        int nombreManagers = 0;
        int nombreBarmans = 0;

        for (Employe employe : employes) {
            if (employe instanceof Cuisinier) {
                nombreCuisiniers++;
            } else if (employe instanceof Serveur) {
                nombreServeurs++;
            } else if (employe instanceof Manager) {
                nombreManagers++;
            } else if (employe instanceof Barman) {
                nombreBarmans++;
            }
        }

        return nombreCuisiniers >= 4 && nombreServeurs >= 2 && nombreManagers >= 1 && nombreBarmans >= 1;
    }
    
    public static void ajouterEmploye(Scanner scanner, Restaurant restaurant) {
        String type = "";
        while (!type.equalsIgnoreCase("serveur") && !type.equalsIgnoreCase("cuisinier") && 
               !type.equalsIgnoreCase("barman") && !type.equalsIgnoreCase("manager")&& !type.equalsIgnoreCase("etudiant")) {
            System.out.println("\nEntrez le type d'employé (Serveur, Cuisinier, Barman, Manager, Etudiant):");
            type = scanner.nextLine();

            if (!type.equalsIgnoreCase("serveur") && !type.equalsIgnoreCase("cuisinier") &&
                !type.equalsIgnoreCase("barman") && !type.equalsIgnoreCase("manager")&& !type.equalsIgnoreCase("etudiant")) {
                System.out.println("\nType d'employé non valide. Veuillez réessayer.");
            }
        }

        System.out.println("\nEntrez le nom:");
        String nom = scanner.nextLine();

        System.out.println("\nEntrez le prénom:");
        String prenom = scanner.nextLine();

        double salaire = 0.0;
        boolean salaireValide = false;
        while (!salaireValide) {
            try {
                System.out.println("\nEntrez le salaire:");
                salaire = Double.parseDouble(scanner.nextLine());
                salaireValide = true;
            } catch (NumberFormatException e) {
                System.out.println("\nSalaire invalide. Veuillez saisir un nombre.");
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date debutContrat = null;
        Date finContrat = null;
        boolean datesValides = false;
        while (!datesValides) {
            try {
                System.out.println("\nEntrez la date de début du contrat (format dd/MM/yyyy):");
                debutContrat = sdf.parse(scanner.nextLine());

                System.out.println("\nEntrez la date de fin du contrat (format dd/MM/yyyy):");
                finContrat = sdf.parse(scanner.nextLine());
                
                if (finContrat.before(debutContrat)) {
                    System.out.println("\nLa date de fin doit être postérieure à la date de début.");
                } else {
                    datesValides = true;
                }
            } catch (ParseException e) {
                System.out.println("\nFormat de date invalide. Veuillez réessayer.");
            }
        }

        Contrat contrat = new Contrat(null, debutContrat, finContrat);
        Employe employe = null;

        switch (type.toLowerCase()) {
            case "serveur":
            	contrat.setType("Standard");
                employe = new Serveur(nom, prenom, salaire);
                break;
            case "cuisinier":
            	contrat.setType("Standard");
                employe = new Cuisinier(nom, prenom, salaire);
                break;
            case "barman":
            	contrat.setType("Standard");
                employe = new Barman(nom, prenom, salaire);
                break;
            case "manager":
            	contrat.setType("Standard");
                employe = new Manager(nom, prenom, salaire);
                break;
            case "etudiant":
            	contrat.setType("Etudiant");
                employe = new Etudiant(nom, prenom, salaire);
                break;
                          
        }

        if (employe != null) {
            employe.setContrat(contrat);
            restaurant.getEmployes().add(employe);
            System.out.println("\nEmployé " + nom + " ajouté avec succès.");
        }
    }
    
    public static void supprimerEmploye(Scanner scanner, Restaurant restaurant) {
        boolean fini = false;
        while (!fini) {
            System.out.println("\nEntrez le nom de l'employé à supprimer (ou tapez 'annuler' pour revenir au menu précédent):");
            String nomASupprimer = scanner.nextLine();

            if ("annuler".equalsIgnoreCase(nomASupprimer)) {
                System.out.println("\nAnnulation de la suppression de l'employé.");
                fini = true;
            } else {
                boolean trouve = restaurant.getEmployes().removeIf(employe -> employe.getNom().equalsIgnoreCase(nomASupprimer));
                if (trouve) {
                    System.out.println("\nEmployé supprimé avec succès.");
                    if (!verifierPersonnelMinimum(restaurant.getEmployes())) {
                        System.out.println("Le personnel minimum n'est plus satisfait. Le restaurant doit fermer.");
                        System.exit(0); // Ferme le programme
                    }
                    fini = true;
                } else {
                    System.out.println("\nEmployé non trouvé. Veuillez réessayer.");
                }
            }
        }
    }
    
    public static void afficherEmployes(Restaurant restaurant) {
        System.out.println("\nListe des employés:");
        for (Employe employe : restaurant.getEmployes()) {
            System.out.println(employe.getPrenom()+ " "+employe.getNom() + " (" + employe.getClass().getSimpleName() + "), Salaire: " + employe.getSalaire());
        }
    }
    
    
    
    public static boolean peutTravailler(Employe employe, Restaurant restaurant) {
        if (employe instanceof Manager) {
            return true; // Les managers peuvent travailler sans restriction
        }

        List<Shift> shifts = restaurant.getHoraires().get(employe);
        if (shifts == null || shifts.size() < 2) {
            return true; // Pas assez de shifts précédents pour poser problème
        }

        // Vérifiez les deux derniers shifts
        Shift dernierShift = shifts.get(shifts.size() - 1);
        Shift avantDernierShift = shifts.get(shifts.size() - 2);

        // Vérifiez si les deux derniers shifts étaient consécutifs et incluent hier
        return !sontConsecutifs(avantDernierShift, dernierShift);
    }
    
    public static boolean sontConsecutifs(Shift shift1, Shift shift2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(shift1.getHeureDebut());
        cal1.add(Calendar.DATE, -1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(shift2.getHeureDebut());

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    
    public static Employe trouverEmploye(String nomEmploye, Restaurant restaurant) {
        for (Employe employe : restaurant.getEmployes()) {
            if (employe.getNom().equalsIgnoreCase(nomEmploye)) {
                return employe;
            }
        }
        return null;
    }
}

class Serveur extends Employe implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Table> tablesAssignees;

    public Serveur(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
        this.setTablesAssignees(new ArrayList<>());
    }

    public void accueillirClient(Client client, int numTable) {
        System.out.println("Le serveur " + this.getNom() + " " + this.getPrenom() + " accueille " + client.getNombre() + " clients à la table n°"+ numTable +"\nSouhaits exprimés par le client : " + client.getSouhaits() + "\nPrise de commande : ");
    }
    
    public static Serveur choisirServeur(Scanner scanner, Restaurant restaurant) {
        System.out.println("\nVeuillez choisir un serveur parmi les suivants:");
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                System.out.println(employe.getNom());
            }
        }

        Serveur serveur = null;
        scanner.nextLine().trim();
        while (serveur == null) {
            System.out.println("\nNom du serveur:");
            String nomServeur = scanner.nextLine().trim();

            for (Employe employe : restaurant.getEmployes()) {
                if (employe instanceof Serveur && employe.getNom().equalsIgnoreCase(nomServeur)) {
                    serveur = (Serveur) employe;
                    break;
                }
            }

            if (serveur == null) {
                System.out.println("\nServeur non existant. Veuillez donner un nom valide.");
            }
        }

        return serveur;
    }
    
    public static void prendreCommande(Scanner scanner, Commande commande, Restaurant restaurant) {
        boolean ajouterPlus = true;
        do {
            System.out.println("\nAjouter un plat (1), une boisson (2), terminer la commande (0):");
            int choix = App.lireChoixUtilisateur(scanner);
            switch (choix) {
                case 1:
                    ajouterPlatCommande(scanner, commande, restaurant);
                    break;
                case 2:
                    ajouterBoissonCommande(scanner, commande, restaurant);
                    break;
                default:
                    ajouterPlus = false;
            }
        } while (ajouterPlus);
    }

    public static void ajouterPlatCommande(Scanner scanner, Commande commande, Restaurant restaurant) {
        restaurant.getCarte().afficherPlats();
        System.out.println("\nEntrer le nom du plat:");
        scanner.nextLine(); // Consommer la ligne restante
        String nomPlat = scanner.nextLine();
        Plat plat = restaurant.getCarte().trouverPlatParNom(nomPlat);
        if (plat != null && plat.estDisponible()) {
            commande.ajouterPlat(plat);
        } else {
            System.out.println("\nPlat non disponible.");
        }
    }
    

    private static void ajouterBoissonCommande(Scanner scanner, Commande commande, Restaurant restaurant) {
        restaurant.getCarte().afficherBoissons();
        System.out.println("\nEntrer le nom de la boisson:");
        scanner.nextLine(); // Consommer la ligne restante
        String nomBoisson = scanner.nextLine();
        Boisson boisson = restaurant.getCarte().trouverBoissonParNom(nomBoisson);
        if (boisson != null && boisson.estDisponible()) {
            commande.ajouterBoisson(boisson);
        } else {
            System.out.println("\nBoisson non disponible.");
        }
    }


	public List<Table> getTablesAssignees() {
		return tablesAssignees;
	}

	public void setTablesAssignees(List<Table> tablesAssignees) {
		this.tablesAssignees = tablesAssignees;
	}
	
	public Commande prendreCommande() {
        return new Commande();  // Crée une nouvelle instance de commande
    }
}

class Cuisinier extends Employe implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean estOccupe;
    private Commande commandeEnCours;

    public Cuisinier(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
        this.estOccupe = false; // Par défaut, le cuisinier n'est pas occupé
        this.commandeEnCours = null; // Au départ, le cuisinier n'a pas de commande en cours
    }
    
 // Dans Cuisinier et Barman
    public void notifierPreparationTerminee(Commande commande) {
        commande.marquerCommePrete();
    }

    public void preparerPlat(Commande commande) {
        if (estOccupe) {
            System.out.println("Le cuisinier " + this.getNom() + " est déjà occupé à préparer une autre commande.");
            return;
        }

        this.setOccupe(true);
        this.setCommandeEnCours(commande);
        System.out.println("Le cuisinier " + this.getNom() + " commence à préparer la commande.");

        for (Plat plat : commande.getPlats()) {
            System.out.println("Préparation du plat: " + plat.getNom());
            // Simuler un temps de préparation
            try {
                Thread.sleep(2000); // Simule 2 secondes pour la préparation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.setOccupe(false);
        this.setCommandeEnCours(null);
        System.out.println("Tous les plats sont préparés.");
    }

    public boolean estOccupe() {
        return estOccupe;
    }

    public void setOccupe(boolean estOccupe) {
        this.estOccupe = estOccupe;
    }

    public Commande getCommandeEnCours() {
        return commandeEnCours;
    }

    public void setCommandeEnCours(Commande commandeEnCours) {
        this.commandeEnCours = commandeEnCours;
    }

    public boolean estOccupeAvecCommande(Commande commande) {
        return estOccupe && commandeEnCours != null && commandeEnCours.equals(commande);
    }
}


class Barman extends Employe implements Serializable {
    private static final long serialVersionUID = 1L;
	private boolean estOccupe;
    private Commande commandeEnCours;


    public void setCommandeEnCours(Commande commande) {
        this.commandeEnCours = commande;
    }

    public Commande getCommandeEnCours() {
        return commandeEnCours;
    }
    
    public boolean estOccupeAvecCommande(Commande commande) {
        return commandeEnCours != null && commandeEnCours.equals(commande);
    }

    public Barman(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
        this.estOccupe = false;
        this.commandeEnCours = null;
    }

    public void preparerBoisson(Commande commande) {
        System.out.println("Le barman " + this.getNom() + " prépare les boissons de la commande.");
        for (Boisson boisson : commande.getBoissons()) {
            System.out.println("Préparation de la boisson: " + boisson.getNom());
            // Simuler un temps de préparation
            try {
                Thread.sleep(1000); // Simule 1 seconde pour la préparation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Toutes les boissons sont prêtes.");
    }

	public boolean EstOccupe() {
		return estOccupe;
	}

	public void setOccupe(boolean estOccupe) {
		this.estOccupe = estOccupe;
	}
   }

class Etudiant extends Employe implements Serializable {
    private static final long serialVersionUID = 1L;
    public Etudiant(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
    }
}


class Manager extends Employe implements Serializable {
    private static final long serialVersionUID = 1L;
    public Manager(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
    }
    public void prevoirEmployes(Restaurant restaurant) {
    	System.out.println("\n       - Prévision des employés qui travailleront aujourd'hui:");
        System.out.println("             1 - Par défaut");
        System.out.println("             2 - Choisir les employés");
        
        int choixPreparation;
        Scanner scanner = new Scanner(System.in);
		do {
            choixPreparation = App.lireChoixUtilisateur(scanner);

            if (choixPreparation == 1) {
                restaurant.setEmployes(Employe.EmployesParDefaut());
                break;
            } else if (choixPreparation == 2) {
            	restaurant.setEmployes(Employe.selectionnerEmployes(scanner, restaurant.getEmployes()));
                break;
            } else {
                System.out.println("Veuillez choisir soit 1 ou 2");
            }
        } while (choixPreparation != 1 && choixPreparation != 2);

        
        // Vérifier si le personnel minimum est satisfait
        if (!Employe.verifierPersonnelMinimum(restaurant.getEmployes())) {
            System.out.println("Le restaurant ne peut pas ouvrir, personnel insuffisant.");
            return;
        }


        // Création du restaurant
        System.out.println("\nLe personnel nécessaire est présent (4 cuisiniers, 2 serveurs, 1 manager et un barman).");
        System.out.println("\n\nOuverture du restaurant.");
    }

    public void reconstituerStock(Restaurant restaurant) {
    	// Ajouter les plats et boissons au stock
        for (Plat plat : restaurant.getCarte().getPlats()) {
        	restaurant.getStock().ajouterAuStock(plat, plat.getQuantite());
        }
        for (Boisson boisson : restaurant.getCarte().getBoissons()) {
        	restaurant.getStock().ajouterAuStock(boisson, boisson.getQuantite());
        }
        
        System.out.println("\n       - Reconstitution de l'état des stocks effectuée\n");
    }


    public void imprimerListeDeCourses(Restaurant restaurant, Map<String, Integer> ventesParPlat, Map<String, Integer> ventesParBoisson) {
        System.out.println("\nListe de courses basée sur les ventes actuelles:");

        Map<String, Integer> listeDeCourses = new HashMap<>();

        // Traitement des plats
        for (Map.Entry<String, Integer> vente : ventesParPlat.entrySet()) {
            String platNom = vente.getKey();
            int quantiteVendue = vente.getValue();

            Plat plat = restaurant.getCarte().trouverPlatParNom(platNom);
            if (plat != null) {
                for (Aliment ingredient : plat.getIngredients()) {
                    int quantiteNecessaire = ingredient.getQuantite() * quantiteVendue;
                    listeDeCourses.put(ingredient.getNom(), listeDeCourses.getOrDefault(ingredient.getNom(), 0) + quantiteNecessaire);
                }
            }
        }

        // Traitement des boissons
        for (Map.Entry<String, Integer> vente : ventesParBoisson.entrySet()) {
            String boissonNom = vente.getKey();
            int quantiteVendue = vente.getValue();

            Boisson boisson = restaurant.getCarte().trouverBoissonParNom(boissonNom);
            if (boisson != null) {
                String nomBoisson = boisson.getNom();
                listeDeCourses.put(nomBoisson, listeDeCourses.getOrDefault(nomBoisson, 0) + quantiteVendue);
            }
        }

        // Affichage de la liste de courses
        if (listeDeCourses.isEmpty()) {
            System.out.println("Aucun article nécessaire pour la liste de courses.");
        } else {
            System.out.println("Articles nécessaires pour la liste de courses:");
            for (Map.Entry<String, Integer> course : listeDeCourses.entrySet()) {
                System.out.println(course.getKey() + " : " + course.getValue() + " unités");
            }
        }

        // Sérialisation de la liste de courses
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("listeDeCourses.ser"))) {
            oos.writeObject(listeDeCourses);
            System.out.println("\nListe de courses sérialisée avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sérialisation de la liste de courses: " + e.getMessage());
        }
    }


    public void surveillerPerformance(Restaurant restaurant) {
    	System.out.println("\nRapport des performances du jour:");

        double totalVentes = 0.0;
        int nombreClientsServis = 0;
        Map<String, Integer> ventesParPlat = new HashMap<>();
        Map<String, Double> ventesParServeur = new HashMap<>();
        Map<String, Integer> compteurPlats = new HashMap<>();

        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                Serveur serveur = (Serveur) employe;
                double totalServeur = 0.0;

                for (Table table : serveur.getTablesAssignees()) {
                    Commande commande = table.getCommande();
                    if (commande != null) {
                        totalVentes += commande.getTotal();
                        totalServeur += commande.getTotal();
                        nombreClientsServis += table.getCapacite();

                        for (Plat plat : commande.getPlats()) {
                            ventesParPlat.put(plat.getNom(), ventesParPlat.getOrDefault(plat.getNom(), 0) + 1);
                            compteurPlats.put(plat.getNom(), compteurPlats.getOrDefault(plat.getNom(), 0) + 1);
                        }

                        for (Boisson boisson : commande.getBoissons()) {
                            ventesParPlat.put(boisson.getNom(), ventesParPlat.getOrDefault(boisson.getNom(), 0) + 1);
                        }
                    }
                }

                ventesParServeur.put(serveur.getNom(), totalServeur);
            }
        }

        System.out.println("\nTotal des ventes : " + totalVentes + "€");
        System.out.println("\nNombre total de clients servis : " + nombreClientsServis);
        System.out.println("\nVentes par plat/boisson : " + ventesParPlat);
        System.out.println("\nPerformances des serveurs : " + ventesParServeur);
        System.out.println("\nPlats les plus populaires : " + compteurPlats);
    }
}




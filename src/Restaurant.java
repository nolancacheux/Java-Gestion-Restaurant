import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Restaurant {
    private String nom;
    private String adresse;
    private int capacite;
    private Stock stock;
    private Menu carte;
    private List<Employe> employes;
    private Map<Employe, List<Shift>> horaires;
    
    public Restaurant(String nom, String adresse, int capacite, Stock stock, Menu carte, List<Employe> employes) {
        this.nom = nom;
        this.adresse = adresse;
        this.capacite = capacite;
        this.stock = stock;
        this.carte = carte; 
        this.employes = employes; 
        this.horaires = new HashMap<>();
    }

    
 // Méthode pour ajouter un Shift
    public void ajouterShift(Employe employe, Shift shift) {
        if (!horaires.containsKey(employe)) {
            horaires.put(employe, new ArrayList<>());
        }
        horaires.get(employe).add(shift);
    }

    // Getter pour les horaires
    public Map<Employe, List<Shift>> getHoraires() {
        return horaires;
    }

    public void ouvrirRestaurant(Scanner scanner) {
    	
			Manager manager = null;
			do {
    // Affichage et sélection du manager
			System.out.println("\nVeuillez choisir le manager qui organise le restaurant pour aujourd'hui : :");
			for (Employe employe : this.getEmployes()) {
			    if (employe instanceof Manager) {
			        System.out.println(employe.getNom());
			    }
			}

			// Lecture du choix du manager
			System.out.println("\nEntrez le nom du manager:");
			String nomManager = scanner.nextLine().trim();
			for (Employe employe : this.getEmployes()) {
			    if (employe instanceof Manager && employe.getNom().equalsIgnoreCase(nomManager)) {
			        manager = (Manager) employe;
			        break;
			    }
			}
			}while (manager == null);
			
			System.out.println("\n"+ manager.getPrenom()+ " "+manager.getNom()+" organise le restaurant :\n");
			
			manager.reconstituerStock(this);
			manager.prevoirEmployes(this);
		}

    
    public static void afficherInformationsRestaurant(Restaurant restaurant) {
        System.out.println("\nInformations du Restaurant:");

        // Filtre et création d'une liste des aliments dans l'inventaire
        List<Map.Entry<Object, Integer>> alimentEntries = restaurant.getStock().getInventaire().entrySet().stream()
            .filter(entry -> entry.getKey() instanceof Aliment)
            .collect(Collectors.toList());

        int maxColWidth = 25;
        String headerFormat = "| %-" + maxColWidth + "s | %-" + maxColWidth + "s | %-" + maxColWidth + "s | %-" + maxColWidth + "s |\n";
        System.out.format(headerFormat, "Employés", "Aliments", "Plats", "Boissons");
        System.out.println(new String(new char[headerFormat.length()]).replace("\0", "-"));

        int maxLines = Math.max(
            Math.max(restaurant.getEmployes().size(), alimentEntries.size()),
            Math.max(restaurant.getCarte().getPlats().size(), restaurant.getCarte().getBoissons().size())
        );

        for (int i = 0; i < maxLines; i++) {
            String employeData = i < restaurant.getEmployes().size() ? Employe.formatEmploye(restaurant.getEmployes().get(i)) : "";
            String alimentData = i < alimentEntries.size() ? Aliment.formatAliment(alimentEntries.get(i)) : "";
            String platData = i < restaurant.getCarte().getPlats().size() ? Plat.formatPlat(restaurant.getCarte().getPlats().get(i)) : "";
            String boissonData = i < restaurant.getCarte().getBoissons().size() ? Boisson.formatBoisson(restaurant.getCarte().getBoissons().get(i)) : "";

            System.out.format(headerFormat, employeData, alimentData, platData, boissonData);
        }
    }
    
    public static void nettoyerRestaurant(Restaurant restaurant) {
        System.out.println("\nDébut du nettoyage du restaurant par tous les employés.");
        for (Employe employe : restaurant.getEmployes()) {
            System.out.println(employe.getPrenom()+" "+ employe.getNom() + " (" + employe.getClass().getSimpleName() + ") participe au nettoyage.");
        }
        System.out.println("Le restaurant a été nettoyé avec succès.");
    }

	public Menu getCarte() {
		return carte;
	}

	public void setCarte(Menu carte) {
		this.carte = carte;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public List<Employe> getEmployes() {
		return employes;
	}

	public void setEmployes(List<Employe> employes) {
		this.employes = employes;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

}

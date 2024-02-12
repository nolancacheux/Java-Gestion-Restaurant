import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Object, Integer> inventaire;

    public Stock() {
        this.inventaire = new HashMap<>();
    }
    
    public static Stock initialiserStock() {
    	// Initialisation du stock
        Stock stock = new Stock();
        stock.ajouterAuStock(new Aliment("Salade", 100), 200);
        stock.ajouterAuStock(new Aliment("Tomate", 100), 150);
        stock.ajouterAuStock(new Aliment("Noix", 100), 50);
        stock.ajouterAuStock(new Aliment("Champignon", 100), 100); 
        stock.ajouterAuStock(new Aliment("Pain", 100), 300);
        stock.ajouterAuStock(new Aliment("Viande", 100), 100);
        stock.ajouterAuStock(new Aliment("Fromage", 100), 120);
        stock.ajouterAuStock(new Aliment("Pâte à pizza", 100), 80);
        stock.ajouterAuStock(new Aliment("Piment", 100), 30);
        return stock; 
    }

    public void ajouterAuStock(Object item, int quantite) {
        // Ajoute ou met à jour la quantité d'un item dans le stock
        this.inventaire.put(item, this.inventaire.getOrDefault(item, 0) + quantite);
    }

    public boolean verifierDisponibilite(Object item) {
        // Vérifie si l'item est disponible en stock
        return this.inventaire.getOrDefault(item, 0) > 0;
    }
    
    public static void diminuerStock(Commande commande, Restaurant restaurant) {
        Stock stock = restaurant.getStock();

        for (Plat plat : commande.getPlats()) {
            stock.reduireStock(plat, 1); // Diminuer le stock du plat
        }

        for (Boisson boisson : commande.getBoissons()) {
            stock.reduireStock(boisson, 1); // Diminuer le stock de la boisson
        }
    }

    /**
     * Réduit le stock d'un objet spécifique.
     * 
     * @param item L'objet dont le stock doit être réduit.
     * @param quantite La quantité à soustraire du stock.
     */
    public void reduireStock(Object item, int quantite) {
        // Recherche de l'objet dans l'inventaire. Utilise un flux (stream) sur l'ensemble des clés (keySet)
        // de la carte de l'inventaire. Le filtre vérifie si chaque clé est égale à l'objet recherché.
        Object itemEnStock = this.inventaire.keySet().stream()
            .filter(key -> key.equals(item))
            .findFirst() // Obtient le premier élément correspondant au filtre, s'il existe.
            .orElse(null); // Si aucun élément correspondant n'est trouvé, renvoie null.

        if (itemEnStock != null) {
            // Si l'objet est trouvé, récupère sa quantité actuelle dans l'inventaire.
            int nouvelleQuantite = this.inventaire.get(itemEnStock) - quantite;
            // Assure que la nouvelle quantité n'est pas négative.
            if (nouvelleQuantite < 0) {
                nouvelleQuantite = 0;
            }
            // Met à jour l'inventaire avec la nouvelle quantité.
            this.inventaire.put(itemEnStock, nouvelleQuantite);
            // Affiche une confirmation de la mise à jour du stock.
            System.out.println("Stock mis à jour pour " + itemEnStock.toString() + ": Quantité actuelle = " + nouvelleQuantite);
        } else {
            // Si l'objet n'est pas trouvé dans l'inventaire, affiche un message d'erreur.
            System.out.println("Item non trouvé dans le stock : " + item.toString());
        }
    }


    
    public static void ajouterStockAlimentExistant(Scanner scanner, Restaurant restaurant) {
        afficherInventaire(restaurant);
        String nom;
        do {
            System.out.println("\nNom de l'aliment à augmenter en stock:");
            nom = scanner.nextLine().trim();
        } while (!restaurant.getStock().getInventaire().containsKey(new Aliment(nom, 0)));

        int quantiteAjoutee = 0;
        do {
            System.out.println("\nQuantité à ajouter:");
            quantiteAjoutee = App.lireChoixUtilisateur(scanner);
            if (quantiteAjoutee < 0) {
                System.out.println("La quantité ne peut pas être négative. Veuillez réessayer.");
            }
        } while (quantiteAjoutee < 0);

        Aliment aliment = new Aliment(nom, 0);
        int nouvelleQuantite = restaurant.getStock().getInventaire().get(aliment) + quantiteAjoutee;
        restaurant.getStock().getInventaire().put(aliment, nouvelleQuantite);
        System.out.println("\nStock mis à jour pour " + nom + " - Nouvelle quantité: " + nouvelleQuantite);
    }
    
    public static void afficherInventaire(Restaurant restaurant) {
        System.out.println("\nInventaire actuel:");

        // Création de listes pour aliments, plats et boissons
        List<Map.Entry<Object, Integer>> alimentEntries = restaurant.getStock().getInventaire().entrySet().stream()
            .filter(entry -> entry.getKey() instanceof Aliment)
            .collect(Collectors.toList());

        List<Map.Entry<Object, Integer>> platEntries = restaurant.getStock().getInventaire().entrySet().stream()
            .filter(entry -> entry.getKey() instanceof Plat)
            .collect(Collectors.toList());

        List<Map.Entry<Object, Integer>> boissonEntries = restaurant.getStock().getInventaire().entrySet().stream()
            .filter(entry -> entry.getKey() instanceof Boisson)
            .collect(Collectors.toList());

        int maxColWidth = 30;
        String headerFormat = "| %-" + maxColWidth + "s | %-" + maxColWidth + "s | %-" + maxColWidth + "s |\n";
        System.out.format(headerFormat, "Aliments", "Plats", "Boissons");
        System.out.println(new String(new char[headerFormat.length()]).replace("\0", "-"));

        int maxLines = Math.max(
            Math.max(alimentEntries.size(), platEntries.size()),
            boissonEntries.size()
        );

        for (int i = 0; i < maxLines; i++) {
            String alimentData = i < alimentEntries.size() ? Aliment.formatAliment(alimentEntries.get(i)) : "";
            String platData = i < platEntries.size() ? Plat.formatPlat((Plat)platEntries.get(i).getKey()) + " - " + platEntries.get(i).getValue() : "";
            String boissonData = i < boissonEntries.size() ? Boisson.formatBoisson((Boisson)boissonEntries.get(i).getKey()) + " - " + boissonEntries.get(i).getValue() : "";

            System.out.format(headerFormat, alimentData, platData, boissonData);
        }
    }



    public Map<Object, Integer> getInventaire() {
        return inventaire;
    }

    public void setInventaire(Map<Object, Integer> inventaire) {
        this.inventaire = inventaire;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Inventaire du Stock:\n");
        for (Map.Entry<Object, Integer> entry : inventaire.entrySet()) {
            sb.append(entry.getKey().toString()).append(" - Quantité: ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}


class Aliment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private int quantite;
    
    public Aliment(String nom, int quantite) {
        this.nom = nom;
        this.quantite = quantite;
    }
    
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getQuantite() {
		return quantite;
	}
	
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
    public static String formatAliment(Map.Entry<Object, Integer> entry) {
        Aliment aliment = (Aliment) entry.getKey();
        return aliment.getNom() + " (" + entry.getValue() + ")";
    }
    
    public static void creerNouvelAliment(Scanner scanner, Restaurant restaurant) {
        String nom;
        do {
            System.out.println("\nNom du nouvel aliment:");
            nom = scanner.nextLine().trim();
        } while (nom.isEmpty() || !nom.matches("[a-zA-Z ]+"));

        int quantite = -1;
        while (quantite < 0) {
            System.out.println("\nQuantité initiale:");
            quantite = App.lireChoixUtilisateur(scanner);
            if (quantite < 0) {
                System.out.println("La quantité ne peut pas être négative. Veuillez entrer une quantité valide.");
            }
        }

        Aliment nouvelAliment = new Aliment(nom, quantite);
        restaurant.getStock().getInventaire().put(nouvelAliment, quantite);
        System.out.println("\nNouvel aliment ajouté avec succès: " + nom + " - Quantité: " + quantite);
    }
    
    public static void supprimerAliment(Scanner scanner, Restaurant restaurant) {
        String nom;
        scanner.nextLine().trim();
        do {
            System.out.println("\nNom de l'aliment à supprimer:");
            nom = scanner.nextLine().trim();
        } while (!restaurant.getStock().getInventaire().containsKey(new Aliment(nom, 0)));

        restaurant.getStock().getInventaire().remove(new Aliment(nom, 0));
        System.out.println("\nAliment supprimé avec succès.");
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Aliment aliment = (Aliment) obj;
        return nom.equals(aliment.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
    @Override
    public String toString() {
        return "Plat{" +
                "nom='" + nom + '\'' +
                ", quantite=" + quantite +'}';
    }
}

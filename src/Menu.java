import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Menu {
    private List<Plat> plats;
    private List<Boisson> boissons;

    public Menu(List<Plat> plats, List<Boisson> boissons) {
        this.plats = plats;
        this.boissons = boissons;
    }
    
    public static Menu initialiserMenu() {
    	// Initialisation de la carte des plats
        List<Plat> plats = new ArrayList<>();
        List<Aliment> ingredientsSaladeSimple = Arrays.asList(
                new Aliment("Salade", 1)
            );
        List<Aliment> ingredientsSaladeTomate = Arrays.asList(
        		new Aliment("Salade", 1),
                new Aliment("Tomate", 1)
            );
        List<Aliment> ingredientsPotageNoix = Arrays.asList(
                new Aliment("Noix", 3)
            );
        List<Aliment> ingredientsPotageTomate = Arrays.asList(
                new Aliment("Tomate", 3)
            );

        List<Aliment> ingredientsPotageChampignons = Arrays.asList(
                new Aliment("Champignon", 3)
            );

        List<Aliment> ingredientsBurgerComplet = Arrays.asList(
                new Aliment("Pain", 1),
                new Aliment("Salade", 1),
                new Aliment("Tomate", 1),
                new Aliment("Viande", 1)
            );

        List<Aliment> ingredientsBurgerSimple = Arrays.asList(
        		new Aliment("Pain", 1),
                new Aliment("Salade", 1),
                new Aliment("Viande", 1)
            );
        List<Aliment> ingredientsBurgerViande = Arrays.asList(
        		new Aliment("Pain", 1),
                new Aliment("Viande", 1)
            );

        List<Aliment> ingredientPizzaFromage = Arrays.asList(
                new Aliment("Pâte à pizza", 1),
                new Aliment("Tomate", 1),
                new Aliment("Fromage", 1)
            );
        List<Aliment> ingredientsPizzaChampignons = Arrays.asList(
        		new Aliment("Pâte à pizza", 1),
                new Aliment("Tomate", 1),
                new Aliment("Fromage", 1),
                new Aliment("Champignon", 1)
            );

        List<Aliment> ingredientsPizzaPiment = Arrays.asList(
        		new Aliment("Pâte à pizza", 1),
                new Aliment("Tomate", 1),
                new Aliment("Fromage", 1),
                new Aliment("Piment", 1)
            );
        plats.add(new Plat("Salade Simple", 9.0, 20, ingredientsSaladeSimple));
        plats.add(new Plat("Salade Tomate", 9.5, 15,ingredientsSaladeTomate));
        plats.add(new Plat("Potage Noix", 8.0, 10,ingredientsPotageNoix));
        plats.add(new Plat("Potage Tomate", 8.5, 10,ingredientsPotageTomate));
        plats.add(new Plat("Potage Champignons", 8.5, 10,ingredientsPotageChampignons));
        plats.add(new Plat("Burger Complet", 15.0, 20,ingredientsBurgerComplet));
        plats.add(new Plat("Burger Simple", 14.0, 25,ingredientsBurgerSimple));
        plats.add(new Plat("Burger Viande", 13.0, 30,ingredientsBurgerViande));
        plats.add(new Plat("Pizza Fromage", 12.0, 15,ingredientPizzaFromage));
        plats.add(new Plat("Pizza Champignons", 13.0, 15,ingredientsPizzaChampignons));
        plats.add(new Plat("Pizza Piment", 13.0, 15,ingredientsPizzaPiment));
        
        // Initialisation de la carte des boissons
        List<Boisson> boissons = new ArrayList<>();
        boissons.add(new Boisson("Limonade", 4.0, 50));
        boissons.add(new Boisson("Cidre doux", 5.0, 40));
        boissons.add(new Boisson("Bière sans alcool", 5.0, 60));
        boissons.add(new Boisson("Jus de fruit", 1.0, 70));
        boissons.add(new Boisson("Eau", 0.0, 100)); // Eau est gratuite
        
        Menu menu =new Menu(plats, boissons);
        
        return menu;
    }

    public void ajouterPlat(Plat plat) {
        if (!plats.contains(plat)) {
            plats.add(plat);
        } else {
            System.out.println("Le plat " + plat.getNom() + " existe déjà dans le menu.");
        }
    }

    public void ajouterBoisson(Boisson boisson) {
        if (!boissons.contains(boisson)) {
            boissons.add(boisson);
        } else {
            System.out.println("La boisson " + boisson.getNom() + " existe déjà dans le menu.");
        }
    }

    public List<Plat> getPlats() {
        return new ArrayList<>(plats);
    }

    public void setPlats(List<Plat> plats) {
        this.plats = new ArrayList<>(plats);
    }

    public List<Boisson> getBoissons() {
        return new ArrayList<>(boissons);
    }

    public void setBoissons(List<Boisson> boissons) {
        this.boissons = new ArrayList<>(boissons);
    }
    
    public void afficherPlats() {
        System.out.println("Liste des plats disponibles:");
        for (Plat plat : this.getPlats()) {
            System.out.println(plat.getNom() + " - Prix: " + plat.getPrix() + "€, quantite: " + plat.getQuantite());
        }
    }

    public void afficherBoissons() {
        System.out.println("Liste des boissons disponibles:");
        for (Boisson boisson : this.getBoissons()) {
            System.out.println(boisson.getNom() + " - Prix: " + boisson.getPrix() + "€, quantite: " + boisson.getQuantite());
        }
    }

    public Plat trouverPlatParNom(String nom) {
        for (Plat plat : this.getPlats()) {
            if (plat.getNom().equalsIgnoreCase(nom)) {
                return plat;
            }
        }
        System.out.println("Plat non trouvé.");
        return null;
    }

    public Boisson trouverBoissonParNom(String nom) {
        for (Boisson boisson : this.getBoissons()) {
            if (boisson.getNom().equalsIgnoreCase(nom)) {
                return boisson;
            }
        }
        System.out.println("Boisson non trouvée.");
        return null;
    }
}

class Plat implements Serializable {
    private static final long serialVersionUID = 1L;
	private String nom;
    private double prix;
    private int quantite;
    private List<Aliment> ingredients;  // Liste des ingrédients du plat

    // Constructeur modifié pour inclure les ingrédients
    public Plat(String nom, double prix, int quantite, List<Aliment> ingredients) {
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.ingredients = ingredients;  // Initialiser la liste des ingrédients
    }

    public List<Aliment> getIngredients() {
        return ingredients;
    }
    
    public static String formatPlat(Plat plat) {
        return plat.getNom() + " (" + plat.getPrix() + "€)";
    }
    
    @Override
    public String toString() {
        return "Plat{" +
                "nom='" + nom + '\'' +
                ", prix=" + prix +
                ", stock=" + quantite +
                '}';
    }
    public boolean estDisponible() {
        // Un plat est disponible si le quantite est supérieur à zéro
        return this.quantite > 0;
    }

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	@Override
	public boolean equals(Object o) {
	    // Vérifie si l'objet actuel (this) est le même que l'objet passé en paramètre (o).
	    // Si oui, ils sont évidemment égaux.
	    if (this == o) return true;

	    // Si l'objet passé est null ou si les classes des deux objets ne sont pas les mêmes,
	    // alors ils ne peuvent pas être égaux.
	    if (o == null || getClass() != o.getClass()) return false;

	    // Cast l'objet passé en paramètre en Boisson pour pouvoir le comparer.
	    Plat plat = (Plat) o;

	    // Compare le nom de l'objet actuel avec celui de l'objet passé.
	    // Si les noms sont égaux, la méthode retourne true, sinon false.
	    return nom.equals(plat.nom);
	}


	@Override
	public int hashCode() {
	    // Utilise la méthode hash de la classe Objects pour générer un code de hachage.
	    // Ce code est basé sur le nom du plat.
	    return Objects.hash(nom);
	}
	
    // Constructeur, getters, et setters
}
class Boisson implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private double prix;
    private int quantite;
    
    public Boisson(String nom, double prix, int quantite) {
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }
    public boolean estDisponible() {
        // Une boisson est disponible si le quantite est supérieur à zéro
        return this.quantite > 0;
    }
    
    public static String formatBoisson(Boisson boisson) {
        return boisson.getNom() + " (" + boisson.getPrix() + "€)";
    }
    
    @Override
        public String toString() {
            return "Boisson{" +
                    "nom='" + nom + '\'' +
                    ", prix=" + prix +
                    ", stock=" + quantite +
                    '}';
        }
    


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	@Override
	public boolean equals(Object o) {
	    // Vérifie si l'objet actuel (this) est le même que l'objet passé en paramètre (o).
	    // Si oui, ils sont évidemment égaux.
	    if (this == o) return true;

	    // Si l'objet passé est null ou si les classes des deux objets ne sont pas les mêmes,
	    // alors ils ne peuvent pas être égaux.
	    if (o == null || getClass() != o.getClass()) return false;

	    // Cast l'objet passé en paramètre en Boisson pour pouvoir le comparer.
	    Boisson boisson = (Boisson) o;

	    // Compare le nom de l'objet actuel avec celui de l'objet passé.
	    // Si les noms sont égaux, la méthode retourne true, sinon false.
	    return nom.equals(boisson.nom);
	}


	@Override
	public int hashCode() {
	    // Utilise la méthode hash de la classe Objects pour générer un code de hachage.
	    // Ce code est basé sur le nom de la boisson.
	    return Objects.hash(nom);
	}


    // Constructeur, getters, et setters
}

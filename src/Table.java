import java.io.Serializable;

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    private int numero;
    private int capacite;
    private Serveur serveurAssigne;
    private boolean estOccupee;
    private Commande commande;
    
    public Table(int numero, int capacite) {
        this.numero = numero;
        this.capacite = capacite;
        this.estOccupee = false; // Initialement, la table n'est pas occupée
        this.serveurAssigne = null; // Aucun serveur assigné initialement
        this.commande = null; // Aucune commande initialement
    }
    
 // Deuxième constructeur
    public Table(int numero, int capacite, boolean estOccupee, Serveur serveurAssigné, Commande commande) {
        this.numero = numero;
        this.capacite = capacite;
        this.estOccupee = estOccupee;
        this.serveurAssigne = serveurAssigné;
        this.commande = commande;
    }
    
    public void setCommande(Commande commande) {
        this.commande = commande;
    }

	public int getNumero() {
		return numero;
	}
	
	public Commande getCommande() {
		return this.commande;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public Serveur getServeurAssigne() {
		return serveurAssigne;
	}
	
	public void setServeurAssigne(Serveur serveurAssigne) {
		this.serveurAssigne = serveurAssigne;
	}


	public boolean isEstOccupee() {
		return estOccupee;
	}

	public void setEstOccupee(boolean estOccupee) {
		this.estOccupee = estOccupee;
	}
	
    public static Table trouverTableParNumero(int numeroTable, Restaurant restaurant) {
        for (Employe employe : restaurant.getEmployes()) {
            if (employe instanceof Serveur) {
                for (Table table : ((Serveur) employe).getTablesAssignees()) {
                    if (table.getNumero() == numeroTable) {
                        return table;
                    }
                }
            }
        }
        return null;
    }
	
}

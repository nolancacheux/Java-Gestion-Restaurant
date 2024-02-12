import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

	import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
	import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
	import java.util.List;
import java.util.stream.Collectors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
	
	public class Transaction implements Serializable {
	    private static final long serialVersionUID = 1L;
	
	    private Table table;
	    private Serveur serveur;
	    private Commande commande;
	    private Facture facture;
	    //private ArrayList<Facture> listeFactures;


	
	    public Transaction(Table table, Serveur serveur, Commande commande) {
	        this.table = table;
	        this.serveur = serveur;
	        this.commande = commande;
	    }
	
	    public void initierTransaction(Client client) {
	        System.out.println("Transaction initiée pour " + client.getNombre() + " clients.");
	    }
	
	    public void effectuerPaiement(double montant) {
	        commande.effectuerPaiement(montant);
	        if (commande.estEntierementPayee()) {
	            terminerTransaction();
	        } else {
	            System.out.println("Paiement partiel reçu. Reste à payer : " + (commande.calculerTotal() - commande.getMontantPaye()) + "€.");
	        }
	    }
	
	    public void terminerTransaction() {
	        if (!commande.estEntierementPayee()) {
	            throw new IllegalStateException("La transaction ne peut pas être terminée tant que le paiement total n'est pas reçu.");
	        }
	        this.facture = new Facture(this);

	        System.out.println("Transaction terminée.");
	        enregistrerFacture();
	    }

	    public void enregistrerFacture() {
	        // Créer la nouvelle facture
	        Date dateFacture = new Date(); // Assurez-vous que dateFacture n'est pas null
	        Facture nouvelleFacture = new Facture(
	                dateFacture,
	                this.serveur.getNom(),
	                this.table.getNumero(),
	                this.commande.getPlats().stream().map(Plat::getNom).collect(Collectors.toList()),
	                this.commande.getBoissons().stream().map(Boisson::getNom).collect(Collectors.toList()),
	                this.commande.calculerTotal(),
	                this.commande.getMontantPaye()
	        );

	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("factures.ser"))) {
	            // Désérialiser la liste existante de factures
	            ArrayList<Facture> factures = (ArrayList<Facture>) ois.readObject();

	            // Initialiser la liste si elle est null
	            if (factures == null) {
	                factures = new ArrayList<>();
	            }

	            // Ajouter la nouvelle facture à la liste
	            factures.add(nouvelleFacture);

	            // Écrire la liste mise à jour dans le fichier
	            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("factures.ser"))) {
	                oos.writeObject(factures);
	                System.out.println("Facture enregistrée avec succès.");
	            } catch (IOException e) {
	                System.err.println("Erreur lors de l'écriture des factures: " + e.getMessage());
	                e.printStackTrace();
	            }
	        } catch (IOException | ClassNotFoundException e) {
	            // Si le fichier est vide ou n'existe pas, créez une nouvelle liste de factures
	            ArrayList<Facture> factures = new ArrayList<>();
	            factures.add(nouvelleFacture);

	            // Écrire la liste dans le fichier
	            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("factures.ser"))) {
	                oos.writeObject(factures);
	                System.out.println("Facture enregistrée avec succès.");
	            } catch (IOException ex) {
	                System.err.println("Erreur lors de l'écriture des factures: " + ex.getMessage());
	                ex.printStackTrace();
	            }
	        }
	    }
	
	    public double calculerTotal() {
	        return commande.calculerTotal();
	    }
	
		public Table getTable() {
			return table;
		}
	
		public void setTable(Table table) {
			this.table = table;
		}
	
		public Serveur getServeur() {
			return serveur;
		}
	
		public void setServeur(Serveur serveur) {
			this.serveur = serveur;
		}
	
		public Commande getCommande() {
			return commande;
		}
	
		public void setCommande(Commande commande) {
			this.commande = commande;
		}
	
		public Facture getFacture() {
			return facture;
		}
	
		public void setFacture(Facture facture) {
			this.facture = facture;
		}
	}
	
	class Commande implements Serializable {
	    private static final long serialVersionUID = 1L;
		private List<Plat> plats;
	    private List<Boisson> boissons;
	    private double total;
	    private boolean estPreteBoissons; // Nouvel attribut
	    private boolean estPretePlats;
	    private boolean estPrete;
	    private boolean enPreparation;
	    private double montantPaye;
	
	
	    public Commande() {
	        this.plats = new ArrayList<>();
	        this.boissons = new ArrayList<>();
	        this.total = 0.0;
	        this.estPrete = false; // Initialisé à false
	        this.enPreparation = false; // Par défaut, une commande n'est pas en préparation
	        this.montantPaye = 0.0;
	        
	    }
	    
	 // Méthode pour effectuer un paiement
	    public void effectuerPaiement(double montant) {
	        if (montant + montantPaye <= calculerTotal()) {
	            montantPaye += montant;
	        } else {
	            throw new IllegalArgumentException("Le montant excède le total de la commande.");
	        }
	    }
	    
	 // Méthode pour récupérer le serveur assigné à la commande
	    public Serveur getServeur(Restaurant restaurant) {
	        for (Employe employe : restaurant.getEmployes()) {
	            if (employe instanceof Serveur) {
	                Serveur serveur = (Serveur) employe;
	                for (Table table : serveur.getTablesAssignees()) {
	                    if (table.getCommande() == this) {
	                        return serveur;
	                    }
	                }
	            }
	        }
	        return null; // Retourne null si aucun serveur n'est trouvé
	    }
	
	    // Méthode pour vérifier si la commande est entièrement payée
	    public boolean estEntierementPayee() {
	        return montantPaye >= calculerTotal();
	    }
	
	    // Getter pour le montant payé
	    public double getMontantPaye() {
	        return montantPaye;
	    }
	
	    // Nouvelle méthode pour marquer la commande comme prête
	    public void marquerCommePrete() {
	        this.estPrete = true;
	    }
	
	    // Vérifier si la commande est prête
	    public boolean estPretee() {
	        return estPrete;
	    }
	
	    // Résumé de la commande
	    public String resumerCommande() {
	        StringBuilder resume = new StringBuilder();
	        resume.append("Commande: ");
	        for (Plat plat : plats) {
	            resume.append(plat.getNom()).append(", ");
	        }
	        for (Boisson boisson : boissons) {
	            resume.append(boisson.getNom()).append(", ");
	        }
	        resume.append("Total: ").append(calculerTotal()).append("€");
	        return resume.toString();
	    }
	
	    // Méthode pour calculer le total de la commande
	    public double calculerTotal() {
	        total = 0.0;
	        for (Plat plat : plats) {
	            total += plat.getPrix();
	        }
	        for (Boisson boisson : boissons) {
	            total += boisson.getPrix();
	        }
	        return total;
	    }
	    
	    // Méthodes pour ajouter un plat ou une boisson à la commande
	    public void ajouterPlat(Plat plat) {
	        if (plat != null && plat.estDisponible()) {
	            plats.add(plat);
	            total += plat.getPrix();
	        }
	    }
	
	    public void ajouterBoisson(Boisson boisson) {
	        if (boisson != null && boisson.estDisponible()) {
	            boissons.add(boisson);
	            total += boisson.getPrix();
	        }
	    }
	
		public List<Plat> getPlats() {
			return plats;
		}
	
		public void setPlats(List<Plat> plats) {
			this.plats = plats;
		}
	
		public List<Boisson> getBoissons() {
			return boissons;
		}
	
		public void setBoissons(List<Boisson> boissons) {
			this.boissons = boissons;
		}
	
		public double getTotal() {
			return total;
		}
	
		public void setTotal(double total) {
			this.total = total;
		}
		
		public boolean estEnPreparation() {
	        return enPreparation;
	    }
	
	    public void setEnPreparation(boolean enPreparation) {
	        this.enPreparation = enPreparation;
	    }
	
		public boolean EstPreteBoissons() {
			return estPreteBoissons;
		}
	
		public void setPreteBoissons(boolean estPreteBoissons) {
			this.estPreteBoissons = estPreteBoissons;
		}
	
		public boolean EstPretePlats() {
			return estPretePlats;
		}
	
		public void setPretePlats(boolean estPretePlats) {
			this.estPretePlats = estPretePlats;
		}
	}
	
	
	
	class Facture implements Serializable {
	    private static final long serialVersionUID = 1L;

	    private String serveurNom;
	    private int tableNumero;
	    private double total;
	    private List<String> plats;
	    private List<String> boissons;
	    private double montantPaye;
	    private Date dateFacture;
	    
	    

	    public Facture(Transaction transaction) {
	        this.serveurNom = transaction.getServeur().getNom();
	        this.tableNumero = transaction.getTable().getNumero();
	        this.total = transaction.getCommande().calculerTotal();
	        this.montantPaye = transaction.getCommande().getMontantPaye();
	        this.dateFacture = new Date(); // Date de la facture

	        this.plats = transaction.getCommande().getPlats().stream()
	            .map(Plat::getNom)
	            .collect(Collectors.toList());
	        this.boissons = transaction.getCommande().getBoissons().stream()
	            .map(Boisson::getNom)
	            .collect(Collectors.toList());
	    }

	    public Facture(Date date, String nom, int numero, List<String> collect, List<String> collect2,
	            double calculerTotal, double montantPaye2) {
	        this.serveurNom = nom;
	        this.tableNumero = numero;
	        this.total = calculerTotal;
	        this.montantPaye = montantPaye2;

	        if (date != null) {
	            this.dateFacture = date;
	        } else {
	            this.dateFacture = new Date(); // Utilise la date actuelle si la date est null
	        }

	        // Collecter les plats et boissons
	        this.plats = new ArrayList<>(collect);
	        this.boissons = new ArrayList<>(collect2);
	    }

		public String getTotal() {
			// TODO Auto-generated method stub
			return null;
		}

	    public String genererFacture() {
	        DateFormat formatDeDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        StringBuilder sb = new StringBuilder();
	        sb.append("Date: ").append(formatDeDate.format(this.dateFacture)).append("\n");
	        sb.append("Serveur: ").append(this.serveurNom).append("\n");
	        sb.append("Table: ").append(this.tableNumero).append("\n");
	        sb.append("Commande:\n");
	        
	        // Plats
	        for (String plat : this.plats) {
	            sb.append("   Plat: ").append(plat).append("\n");
	        }
	        
	        // Boissons
	        for (String boisson : this.boissons) {
	            sb.append("   Boisson: ").append(boisson).append("\n");
	        }
	        
	        sb.append("Total: ").append(this.total).append("€\n");
	        sb.append("Montant payé: ").append(this.montantPaye).append("€\n");
	        
	        if (this.montantPaye < this.total) {
	            sb.append("Reste à payer: ").append(this.total - this.montantPaye).append("€\n");
	        } else {
	            sb.append("Paiement complet.\n");
	        }
	        
	        return sb.toString();
	    }

	    // Getters et Setters pour tous les attributs
	    // ...
	}


	
	
	//et ensuite NOLAN :  une méthode EcranGestionFacture dans gestion.java qui permet de désérialiser toutes les facctures, la méthode commencera par lister toutes les factures : exemple : 1 - Facture du 06/12/2023 à 15h53 
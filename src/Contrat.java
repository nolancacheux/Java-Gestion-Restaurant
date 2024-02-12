import java.util.Date;

public class Contrat {
    private String type;     // Le type de contrat
    private Date debut;      // La date de début du contrat
    private Date fin;        // La date de fin du contrat

    // Constructeur de la classe Contrat
    public Contrat(String type, Date debut, Date fin) {
        this.type = type;   // Initialisation du type de contrat
        this.debut = debut; // Initialisation de la date de début
        this.fin = fin;     // Initialisation de la date de fin
    }

    // Vérifie si le contrat est valide à la date actuelle
    public boolean estValide(Date dateActuelle) {
        // Un contrat est considéré valide si la date actuelle se situe entre la date de début et la date de fin
        return !dateActuelle.before(debut) && !dateActuelle.after(fin);
    }

    // Getter pour obtenir le type de contrat
    public String getType() {
        return type;
    }

    // Setter pour définir le type de contrat
    public void setType(String type) {
        this.type = type;
    }

    // Getter pour obtenir la date de début du contrat
    public Date getDebut() {
        return debut;
    }

    // Setter pour définir la date de début du contrat
    public void setDebut(Date debut) {
        this.debut = debut;
    }

    // Getter pour obtenir la date de fin du contrat
    public Date getFin() {
        return fin;
    }

    // Setter pour définir la date de fin du contrat
    public void setFin(Date fin) {
        this.fin = fin;
    }

    // Méthode pour prolonger le contrat avec une nouvelle date de fin
    public void prolongerContrat(Date nouvelleFin) {
        if (nouvelleFin.after(this.fin)) {
            this.fin = nouvelleFin;
            System.out.println("Le contrat a été prolongé jusqu'au " + nouvelleFin);
        } else {
            System.out.println("La nouvelle date de fin doit être postérieure à la date de fin actuelle.");
        }
    }

    // Méthode pour terminer le contrat à la date actuelle
    public void terminerContrat() {
        this.fin = new Date(); // Met fin au contrat à la date actuelle
        System.out.println("Le contrat a été terminé.");
    }

    // Méthode pour obtenir une représentation textuelle du contrat
    @Override
    public String toString() {
        return "Contrat{" +
                "type='" + type + '\'' +
                ", debut=" + debut +
                ", fin=" + fin +
                '}';
    }
}

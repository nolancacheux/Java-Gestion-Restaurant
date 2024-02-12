import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Horaires {
    private Map<Employe, List<Shift>> horaireEmployes;

    public Horaires() {
        this.horaireEmployes = new HashMap<>();
    }
    
    public static void voirShifts(Scanner scanner, Restaurant restaurant) {
        System.out.println("\nListe des employés disponibles pour voir leurs shifts:");
        for (Employe employe : restaurant.getEmployes()) {
            System.out.println(employe.getNom());
        }

        System.out.println("\nEntrez le nom de l'employé pour voir ses shifts (ou tapez 'annuler' pour revenir):");
        String nomEmploye = scanner.nextLine();

        if ("annuler".equalsIgnoreCase(nomEmploye)) {
            System.out.println("\nRetour au menu principal.");
            return;
        }

        Employe employe = Employe.trouverEmploye(nomEmploye, restaurant);

        if (employe == null) {
            System.out.println("\nEmployé non trouvé. Veuillez réessayer.");
            return;
        }

        List<Shift> shifts = restaurant.getHoraires().get(employe);
        if (shifts == null || shifts.isEmpty()) {
            System.out.println("\nAucun shift programmé pour cet employé.");
        } else {
            for (Shift shift : shifts) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String heureDebut = formatter.format(shift.getHeureDebut());
                String heureFin = formatter.format(shift.getHeureFin());
                System.out.println("\nShift: Debut - " + heureDebut + ", Fin - " + heureFin + ", Employé - " + shift.getEmploye().getNom());
            }
        }
    }

    public static void ajouterShift(Scanner scanner, Restaurant restaurant) throws ParseException {
        System.out.println("\nListe des employés disponibles pour ajouter un shift:");
        for (Employe employe : restaurant.getEmployes()) {
            System.out.println(employe.getNom());
        }

        System.out.println("\nEntrez le nom de l'employé pour le shift (ou tapez 'annuler' pour revenir):");
        String nomEmploye = scanner.nextLine();

        if ("annuler".equalsIgnoreCase(nomEmploye)) {
            System.out.println("\nRetour au menu principal.");
            return;
        }

        Employe employe = Employe.trouverEmploye(nomEmploye, restaurant);
        if (employe == null) {
            System.out.println("\nEmployé non trouvé. Veuillez réessayer.");
            return;
        }
        
        if (!Employe.peutTravailler(employe, restaurant)) {
            System.out.println("\n" + employe.getNom() + " ne peut pas travailler trois soirs de suite.");
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date heureDebut = null, heureFin = null;

        while (heureDebut == null) {
            System.out.println("\nEntrez l'heure de début du shift (format HH:mm):");
            try {
                heureDebut = formatter.parse(scanner.nextLine());
                if (heureDebut.before(formatter.parse("18:00")) || heureDebut.after(formatter.parse("23:30"))) {
                    System.out.println("\nL'heure de début doit être entre 18h00 et 23h30.");
                    heureDebut = null;
                }
            } catch (ParseException e) {
                System.out.println("\nFormat de l'heure invalide. Veuillez réessayer.");
            }
        }

        while (heureFin == null) {
            System.out.println("\nEntrez l'heure de fin du shift (format HH:mm):");
            try {
                heureFin = formatter.parse(scanner.nextLine());
                if (heureFin.before(heureDebut) || heureFin.after(formatter.parse("23:30"))) {
                    System.out.println("\nL'heure de fin doit être après l'heure de début et avant 23h30.");
                    heureFin = null;
                }
            } catch (ParseException e) {
                System.out.println("\nFormat de l'heure invalide. Veuillez réessayer.");
            }
        }

        Shift shift = new Shift(heureDebut, heureFin, employe);
        restaurant.ajouterShift(employe, shift);
        System.out.println("\nShift ajouté avec succès pour " + employe.getNom() + " (Soir: 18h00 à 23h30).");
    }
    
    public void planifierEmployes(Employe employe, Shift shift) {
        // Vérifier si l'employé existe déjà dans l'horaire
        List<Shift> shifts = horaireEmployes.getOrDefault(employe, new ArrayList<>());

        // Ajouter le shift à la liste des shifts de l'employé
        shifts.add(shift);

        // Mettre à jour la map avec la nouvelle liste de shifts
        horaireEmployes.put(employe, shifts);

        System.out.println("Shift ajouté pour " + employe.getNom() + " : " + shift);
    }

    public boolean verifierDisponibilite(Employe employe, Date dateDebutShift) {
        // Vérifier si l'employé est déjà planifié pour des shifts
        if (!horaireEmployes.containsKey(employe)) {
            return true; // Aucun shift planifié, donc disponible
        }

        // Parcourir les shifts de l'employé pour vérifier la disponibilité
        for (Shift shift : horaireEmployes.get(employe)) {
            if (shift.getHeureDebut().compareTo(dateDebutShift) == 0) {
                return false; // Employé déjà assigné à un shift à cette date
            }
        }

        return true; // Employé disponible pour ce shift
    }

	public Map<Employe, List<Shift>> getHoraireEmployes() {
		return horaireEmployes;
	}

	public void setHoraireEmployes(Map<Employe, List<Shift>> horaireEmployes) {
		this.horaireEmployes = horaireEmployes;
	}
}


class Shift {
    private Date heureDebut;
    private Date heureFin;
    private Employe employe;

    // Constructeur avec les trois arguments
    public Shift(Date heureDebut, Date heureFin, Employe employe) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.employe = employe;
    }

    // Getters et Setters
    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
}

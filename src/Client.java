public class Client {
    private int nombre;       // Le nombre de clients dans le groupe
    private String souhaits;  // Les souhaits spécifiques du groupe de clients

    // Constructeur de la classe Client
    public Client(int nombre, String souhaits) {
        this.nombre = nombre;     // Initialisation du nombre de clients
        this.souhaits = souhaits; // Initialisation des souhaits
    }

    // Getter pour obtenir le nombre de clients
    public int getNombre() {
        return nombre;
    }

    // Setter pour définir le nombre de clients
    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    // Getter pour obtenir les souhaits
    public String getSouhaits() {
        return souhaits;
    }

    // Setter pour définir les souhaits
    public void setSouhaits(String souhaits) {
        this.souhaits = souhaits;
    }
}

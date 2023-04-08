package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * la classe 'Server' est la classe qui permet d'initiliser un serveur;
 * d'accepter les connexions des clients;
 * d'analyser leur requêtes et de leur envoyer la reponse après le traitement de leur requête
 */
public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * le constructeur initialise un socket serveur avec le numéro de port passé en paramètre,
     * initialise une liste d'interface foncitonnelle
     * et définit la méthode à exécuter par l'interface fonctionnelle.
     * @param port détermine le numéro de port par lequel la communication entre clients et serveur sera effectuée.
     * @throws IOException exception permettant de capturer toute erreur liée à la création du socket serveur.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * ajoute l'interface dont la méthode à été définit dans le constructeur dans le vecteur d'interfece fonctionnel 'handler'.
     * @param h détermine l'interface qui va être ajouté dans la liste d'interface.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * exécute la méthode contenue dans chaque interface de la liste d'interface 'handler'.
     * @param cmd détermine l'action que l'utilisateur souhaite effectuer(Inscription ou Demande de la liste des cours).
     * @param arg détermine l'argument qui représente la session dans le cas ou il s'agit d'une requête d'obtention de la liste des cours
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * la méthode 'run' permet de lancer une attende de connexion d'un client et d'initialiser
     * les objets qui permettrons d'écouter les diférentes requêtes qui
     * provenant de ce client, d'y répondre et de le déconnecté.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * la méthode 'listen' lance une attente jusqu'à la reception de la requête du client puis analyse cette requête
     * afin de fournir une reponse qui conviendra au attentes du client.
     * @throws IOException cette exception capture toute erreur de flux lié à la lecture de la requête client.
     * @throws ClassNotFoundException capture les erreurs lié à l'invalidité de la requête envoyée par le client.
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }


    /**
     * c'est la fonction chargé d'analyser la requête reçu par le serveur en provenance d'un client.
     * @param line détermine l'objet ou encore la requête envoyée par le client.
     * @return et cette méthode renvoi une paire d'objet (la commande 'qui détermine ce que le client souhaite faire' et l'argument 'ce qu'il fourni en complément pour ce qu'il souhaite faire').
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * c'est la méthode chargé de déconnecter le client une fois que le serveur a répondu à sa requête
     * @throws IOException capture toute erreur qui empêche la fermeture des flux d'entrée/sortie ou du socket client
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * c'est la méthode à implémenter dans l'interface elle une fois que la requête du client à été analysée
     * cette méthode détermine si le client souhaite s'enregitrer ou s'il souhaite tout simplement obtenir la liste des cours.
     * @param cmd c'est l'action que le client souhaite effectuer (enregistrement ou lister les cours).
     * @param arg c'est la session correspondante à la liste des cours à charger par le serveur au client.
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        File file = new File("src/main/java/server/data/cours.txt");
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<String> Liste = new ArrayList<>();

        try {
            //Lecture du fichier
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String Ligne = "";

            //Transformation en liste de cours
            while ((Ligne = bufferedReader.readLine()) != null)
            {
                courses.add(new Course(Ligne.split("\t")[1], Ligne.split("\t")[0], Ligne.split("\t")[2]));
            }
        } catch (IOException e) {
            try {
                objectOutputStream.writeObject(new Object[]{false, "Une erreur est survenue lors du traitement de votre requête"});
                return;
            } catch (IOException ex) {
                System.out.println("Impossible de répondre à la demande du client\n Erreur : " + e);
                return;
            }
        }

        //Filtrage en fonction de la session passée en argument

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).getSession().equals(arg))
            {
                Liste.add(courses.get(i).getCode() + "\t" + courses.get(i).getName());
            }
        }

        try {
            if(courses.size() > 0)
            {
                objectOutputStream.writeObject(new Object[]{true, Liste});
            }
            else
            {
                objectOutputStream.writeObject(new Object[]{false, "Aucun cours n'a été trouvé pour cette session"});
            }
        } catch (IOException ex) {
            System.out.println("Impossible de répondre à la demande du client\n Erreur : " + ex);
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {
            System.out.println("Erreur : ");
            //Récupération de l'objet RegistrationForm
            String RegistForm = (String) objectInputStream.readObject();


            //Enregistrement dans un fichier

            File file = new File("src/main/java/server/data/inscription.txt");
            FileWriter fileWriter = new FileWriter(file, true);

            //Ecriture du fichier
            fileWriter.write(RegistForm);

            fileWriter.close();


            objectOutputStream.writeObject("Opération réussie : L'étudiant "+ RegistForm.split("\t")[3] +" " + RegistForm.split("\t")[4] + " a été enregistré");
        } catch (IOException | ClassNotFoundException e) {
            try {
                objectOutputStream.writeObject("Erreur : L'incriptiont de l'étudiant a échouée");
            } catch (IOException ex) {
                System.out.println("Erreur : Impossible de répondre au client un problème est suvenu lors de l'inscription : " + e);
            }
        }
    }
}


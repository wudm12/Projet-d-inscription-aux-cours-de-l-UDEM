package com.example.client_fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;

/**
 * classe qui permet de controler les actions d'un utilisateur sur l'interface graphique
 */
public class HelloController implements Initializable {
    @FXML
    private TableView<Course> ListeDesCours;
    @FXML
    private TableColumn<Course, String> Code;
    @FXML
    private TableColumn<Course, String> NomCours;

    @FXML
    private TextField Prenom;
    @FXML
    private TextField Nom;
    @FXML
    private TextField Email;
    @FXML
    private TextField Matricule;
    @FXML
    private ComboBox<String> Session;

    private Alert Erreur = new Alert(Alert.AlertType.ERROR);
    private Alert Information = new Alert(Alert.AlertType.INFORMATION);


    private ObservableList<String> SessionListe = FXCollections.observableArrayList();
    private ObservableList<Course> CoursListe = FXCollections.observableArrayList();


    /**
     * fonction réécrite qui permet d'initialiser certaine valeur dans l'interface tel que
     * la liste de choix pour les différente session
     * et formater le tableau de la liste des cours à la classe 'Course'.
     * @param url ...
     * @param resourceBundle ...
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionListe.add("Automne");
        SessionListe.add("Ete");
        SessionListe.add("Hiver");
        Session.setItems(SessionListe);

        Code.setCellValueFactory(new PropertyValueFactory<>("code"));
        NomCours.setCellValueFactory(new PropertyValueFactory<>("name"));
    }


    /**
     * COMMANDE d'inscription
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * Commande de chargement de la lite des cours pour une session
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private Socket serveur;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final static int PORT = 1337;
    private Course course;
    private RegistrationForm registrationForm;


    /**
     * fonction permettant d'établir une connexion au serveur
     * et initialiser les différents flux d'entré 'objectInpoutStream' et de sortie 'objectOutputStream'
     * permettant de communiquer avec le serveur.
     *
     * @return retourne 'true' si le client parvient a se connecter et 'false' dans le cas contraire
     */
    @FXML
    public boolean ConnexionAuServeur()
    {
        try {
            serveur = new Socket("localhost", PORT);
            objectOutputStream = new ObjectOutputStream(serveur.getOutputStream());
            objectInputStream = new ObjectInputStream(serveur.getInputStream());

            return true;
        } catch (IOException e) {
            Erreur.setTitle("Erreur Client");
            Erreur.setContentText("Impossible de se connecter au serveur");
            Erreur.show();

            return false;
        }
    }

    /**
     * cette fonction se déclanche lorsque l'utilisateur appui sur le bouton 'Charger' de l'interface
     * et permet de récupérer la liste des cours correspondant à la session choisie après avoir établie
     * une connexion avec le serveur
     */
    @FXML
    protected void Charger() {
        if (ConnexionAuServeur())
        {
            if(Session.getValue() == null)
            {
                Erreur.setTitle("Erreur Client");
                Erreur.setContentText("Veuillez sélectionner une Session avant de charger la liste des cours");
                Erreur.show();
                return;
            }
            else if(Session.getValue() == "Session")
            {
                Erreur.setTitle("Erreur Client");
                Erreur.setContentText("Veuillez sélectionner une Session avant de charger la liste des cours");
                Erreur.show();
                return;
            }

            try {
                objectOutputStream.writeObject(LOAD_COMMAND + " " + Session.getValue());

                Object[] objects = (Object[]) objectInputStream.readObject();

                boolean statut = (boolean) objects[0];

                if(statut)
                {
                    ArrayList<String> Reponse = (ArrayList<String>) objects[1];
                    CoursListe.clear();
                    for (int i = 0; i < Reponse.size(); i++)
                    {
                        CoursListe.add(new Course(Reponse.get(i).split("\t")[1], Reponse.get(i).split("\t")[0], Session.getValue()));
                    }

                    ListeDesCours.setItems(CoursListe);
                }
                else
                {
                    Erreur.setTitle("Erreur Serveur");
                    Erreur.setContentText(objects[1].toString());
                Erreur.show();}


            } catch (IOException | ClassNotFoundException e) {
                Erreur.setTitle("Erreur Serveur");
                Erreur.setContentText("Un problème vous a empêché de joindre le serveur : " + e);
            Erreur.show();}

        }
        else
        {
            return;
        }
    }

    /**
     * cette fonction est déclanchée lorque l'utilisateur appuie sur sur le bouton 'Envoyer' de l'interface
     * elle commence par effectuer un controle de saisi des différents et se rassure que l'utilisateur a
     * bien choisi un cours pour son inscription puis elle établi une connexion au serveur à l'aide de la fonction  'ConnexionAuServeur'
     * pour lui transmettre les informations d'inscription.
     */
    @FXML
    protected void Envoyer() {

        if (ControlerSaisie())
        {
            if(ConnexionAuServeur())
            {
                try {
                    objectOutputStream.writeObject(REGISTER_COMMAND);

                    objectOutputStream.writeObject(registrationForm.getCourse().getSession() + "\t" + registrationForm.getCourse().getCode() + "\t" + registrationForm.getMatricule() + "\t" + registrationForm.getPrenom() + "\t" + registrationForm.getNom() + "\t" + registrationForm.getEmail() + "\n");

                    Object Reponse =  objectInputStream.readObject();

                    Information.setTitle("Information Serveur");
                    Information.setContentText(Reponse.toString());
                    Information.show();

                }
                catch (IOException | ClassNotFoundException e) {
                    Erreur.setTitle("Erreur Serveur");
                    Erreur.setContentText("Un problème vous a empêché de compléter l'inscription : " + e);
                    Erreur.show();
                    return;
                }
            }
            else
            {
                Erreur.setTitle("Erreur Serveur");
                Erreur.setContentText("Un problème vous a empêché de joindre le serveur ");
                Erreur.show();
                return;
            }
        }
        else
        {
            return;
        }

    }

    /**
     * effectuer un controle de saisir pour vérifier la validité des champs du formulaire d'inscription
     * @return retourne 'true' si tout est correcte et false s'il y a au moins un champ incorrect
     */
    public boolean ControlerSaisie()
    {
        String ErrorMessage = "";

        if(Nom.getText().equals(""))
        {
            ErrorMessage = ErrorMessage + "Veuillez indiquer Nom\n";
            Nom.setBorder(Border.stroke(RED));
        }
        else
        {
            Nom.setBorder(new TextField().getBorder());
        }

        if(Prenom.getText().equals(""))
        {
            ErrorMessage = ErrorMessage + "Veuillez indiquer Prenom\n";
            Prenom.setBorder(Border.stroke(RED));
        }
        else
        {
            Prenom.setBorder(new TextField().getBorder());
        }

        if(!Email.getText().contains("@") || !Email.getText().contains("."))
        {
            ErrorMessage = ErrorMessage + "L'email saisi est invalide \n";
            Email.setBorder(Border.stroke(RED));
        }
        else
        {
            Email.setBorder(new TextField().getBorder());
        }

        if(Matricule.getText().length() > 6 || Matricule.getText().length() < 6 || ContainString(Matricule.getText()))
        {
            ErrorMessage = ErrorMessage + "Le Matricule que vous avez saisi n'est pas valide \n";
            Matricule.setBorder(Border.stroke(RED));
        }
        else
        {
            Matricule.setBorder(new TextField().getBorder());
        }

        if(ListeDesCours.getSelectionModel().getSelectedItem() == null)
        {
            ErrorMessage = ErrorMessage + "Veuillez selectionner l'un des cours SVP \n";
        }
        else
        {
            course = ListeDesCours.getSelectionModel().getSelectedItem();
            registrationForm = new RegistrationForm(Prenom.getText(), Nom.getText(), Email.getText(), Matricule.getText(), course);
        }

        if (ErrorMessage.equals(""))
        {
            return true;
        }
        else
        {
            Erreur.setTitle("Erreur Client");
            Erreur.setContentText(ErrorMessage);
            Erreur.show();
            return false;
        }

    }

    /**
     * pour vérifier si le matricule ne contient que des chiffres
     *
     * @param Matricule le matricule à tester
     * @return retroune 'true' si le matricule contient au moin un caractère différend d'un entier et 'false' s'il ne contient que des nombres
     */
    public boolean ContainString(String Matricule)
    {
        for (int i = 0; i < Matricule.length(); i++)
        {

            if(!(Matricule.charAt(i) >= '0' && Matricule.charAt(i) <= '9'))
            {
                return true;
            }
        }
        return false;
    }
}
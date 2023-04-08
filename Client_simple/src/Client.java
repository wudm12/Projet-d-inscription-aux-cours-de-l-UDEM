import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private Socket serveur;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    public final static int PORT = 1337;

    public Client(String COMMAND, String AGS) {
        try {
            serveur = new Socket("localhost", PORT);
            objectOutputStream = new ObjectOutputStream(serveur.getOutputStream());
            objectInputStream = new ObjectInputStream(serveur.getInputStream());


            if (COMMAND.equals(LOAD_COMMAND))
            {
                F1(AGS);
            }
            else
            {
                F2(AGS);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Client()
    {

    }

    public void F1(String Session)
    {

        try {
            objectOutputStream.writeObject(LOAD_COMMAND+" "+Session);

            Object[] objects = (Object[]) objectInputStream.readObject();

            boolean statut = (boolean) objects[0];

            if(statut)
            {
                Affichage((ArrayList<String>) objects[1], Session);
            }
            else
            {
                System.out.println(objects[1]);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Un problème vous a empêché de joindre le serveur : " + e);

        }
    }

    public void F2(String Informations)
    {
        try {
            objectOutputStream.writeObject(REGISTER_COMMAND);

            objectOutputStream.writeObject(Informations);

            Object Reponse =  objectInputStream.readObject();

            System.out.println(Reponse);

        }
         catch (IOException | ClassNotFoundException e) {
            System.out.println("Un problème vous a empêché de compléter l'inscription : " + e);
        }
    }


    public void ChoixSession()
    {
        System.out.println();
        System.out.println();
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println("|++++++++++\tBienvenue sur la plateforme d'inscription des cours de l'UDEM\t++++++++++|");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println();

        //Options
        System.out.println("---> Veuillez choisir une Session SVP|");
        System.out.println("1--> Automne.........................|");
        System.out.println("2--> Ete.............................|");
        System.out.println("3--> Hiver...........................|");
        System.out.println();

        //Choix
        String Choix = "";
        do {
            if(!Choix.equals(""))
            {
                System.out.println("Erreur : Choisissez l'option une(1), deux(2) ou trois(3)");
            }

            System.out.print("---> Saisissez un numéro de session : ");
            Choix = new Scanner(System.in).nextLine();
        }
        while (!Choix.equals("1") && !Choix.equals("2") && !Choix.equals("3"));

        if(Choix.equals("1"))
        {
            new Client("CHARGER", "Automne");
        }
        else if(Choix.equals("2"))
        {
            new Client("CHARGER", "Ete");
        }
        else {
            new Client("CHARGER", "Hiver");
        }
    }
    public void Affichage(ArrayList<String> Cours, String Session)
    {
        System.out.println();
        System.out.println();
        System.out.println("Les cours offerts pendant la session d'"+ Session +" Sont : ");
        System.out.println();

        for (int i = 0; i < Cours.size(); i++)
        {
            System.out.println((i + 1)+".\t" + Cours.get(i));
        }
        System.out.println();

        //Options
        System.out.println("1-> Changer de session......................");
        System.out.println("2-> S'inscrire a un cours pour cette session");

        System.out.println();

        //Choix
        String Choix = "";
        do {
            if(!Choix.equals(""))
            {
                System.out.println("Erreur : Choisissez l'option une(1) ou deux(2)");
            }
                System.out.print("---> Que souhaitez vous faire ? : ");
            Choix = new Scanner(System.in).nextLine();
        }
        while (!Choix.equals("1") && !Choix.equals("2"));

        if(Choix.equals("1"))
        {
            ChoixSession();
        }
        else {
            Inscription(Session, Cours);
        }
    }

    public void Inscription(String Session, ArrayList<String> Cours)
    {
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println("|++++++++++\t                         Inscription                         \t++++++++++|");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");

        String Nom, Prenom, Email, Matricule, CodeCours;

        System.out.print("---> Veuillez Saisir votre Prenom     : ");
        Nom = new Scanner(System.in).nextLine();

        System.out.print("---> Veuillez Saisir votre Nom        : ");
        Prenom = new Scanner(System.in).nextLine();

        System.out.print("---> Veuillez Saisir votre Email      : ");
        Email = new Scanner(System.in).nextLine();

        System.out.print("---> Veuillez Saisir votre Matricule  : ");
        Matricule = new Scanner(System.in).nextLine();

        System.out.print("---> Veuillez Saisir le Code du Cours : ");
        CodeCours = new Scanner(System.in).nextLine();

        //Vérification de la validité du code du cours
        for (int i = 0; i < Cours.size(); i++)
        {
            if(Cours.get(i).contains(CodeCours))
            {
                String Formulaire = Session+ "\t" + CodeCours + "\t" + Matricule + "\t" + Prenom + "\t" + Nom + "\t" + Email;
                new Client("INSCRIRE", Formulaire);
                ChoixSession();
                return;
            }
        }

        System.out.println();
        System.out.println("Le code du cours que vous avez entré n'est pas valide😤");
        ChoixSession();
    }
}

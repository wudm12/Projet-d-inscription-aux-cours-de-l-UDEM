package com.example.client_fx;

import com.example.client_fx.Course;

import java.io.Serializable;

/**
 * la classe 'Course' permet de creer une instance contenant des information
 * du client a enregistrer
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * contructeur de la classe "RegistrationForm" permet d'instancier (créer) un objet contenant
     * les valeurs d'un formulaire.
     * @param prenom Prenom du client
     * @param nom Nom du client
     * @param email email du client
     * @param matricule matricule du client
     * @param course cours choisi
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * c'est un accesseur qui permet d'acceder à la valeur du prénom
     * @return renvoie le prenom du client
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * pour modifier le matricule d'un étudiant
     * @param prenom nouvelle valeur pour le prénom d'un étudiant
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * c'est un accesseur qui permet d'acceder à la valeur du nom
     * @return renvoie le nom du client
     */
    public String getNom() {
        return nom;
    }

    /**
     * pour modifier le Nom d'un étudiant.
     * @param nom nouvelle valeur pour le nom de l'étudiant;
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * c'est un accesseur qui permet d'acceder à la valeur de l'email
     * @return renvoie l'email du client
     */
    public String getEmail() {
        return email;
    }

    /**
     * pour modifier l'email d'un étudiant.
     * @param email nouvelle valeur pour l'email d'un étudiant.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * c'est un accesseur qui permet d'acceder à la valeur du matricule.
     * @return renvoie le matricule du client.
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * pour modifier le matricule d'un étudiant.
     * @param matricule nouvelle valeur pour le matricule d'un étudiant.
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * c'est un accesseur qui permet d'acceder à la valeur de l'objet cours .
     * @return renvoie les information du cours choisi par le client.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * pour modifier le choix du cour d'un utilisateur.
     * @param course nouvelle valeur pour le cours choisi.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * converti l'objet 'RegistrationForm' en chaine de carractère qui décrit le décrit au mieux.
     * @return chaine de caractère formaté comportant les information de l'objet.
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}

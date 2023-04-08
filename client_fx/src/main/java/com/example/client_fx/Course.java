package com.example.client_fx;

import java.io.Serializable;

/**
 * la classe 'Course' permet de creer des intances de cours
 */
public class Course implements Serializable {

    private String name;
    private String code;
    private String session;


    /**
     * contructeur de la classe "Course" permet d'instancier (créer) des objets cours
     * @param name nom du cours
     * @param code code du cours
     * @param session session pendant laquelle a lieu le cours
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * c'est un accesseur qui permet d'acceder au nom du cours
     * @return renvoie le nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     *c'est un modificateur qui permet de modifier ou changer le nom du cours
     * @param name nouvelle valeur de la session
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * c'est un accesseur qui permet d'acceder au code du cours
     * @return renvoie le code du cours
     */
    public String getCode() {
        return code;
    }

    /**
     *c'est un modificateur qui permet de modifier ou changer le code du cours
     * @param code nouvelle valeur du code du cours
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * c'est un accesseur qui permet d'acceder à la session pendant laquel le cours a lieu
     * @return renvoie le nom du cours
     */
    public String getSession() {
        return session;
    }

    /**
     *c'est un modificateur qui permet de modifier ou changer la session du cours
     * @param session nouvelle valeur de la session
     */
    public void setSession(String session) {
        this.session = session;
    }


    /**
     * converti l'objet 'Course' en chaine de carractère qui décrit le décrit au mieux
     * @return chaine de caractère formaté comportant les information de l'objet
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}

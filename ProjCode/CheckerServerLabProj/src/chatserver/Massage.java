/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatserver;

/**
 *
 * @author Laptop
 */
public class Massage {
    private String Subject;

    public Massage(String Subject) {
        this.Subject = Subject;
    }

    public Massage() {
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

    @Override
    public String toString() {
        return "Massage{" + "Subject=" + Subject + '}';
    }

    
}

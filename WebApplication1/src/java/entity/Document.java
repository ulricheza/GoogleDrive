/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Documents")
public class Document implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @ManyToOne
    private User owner;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;    
    
    @Lob
    private String content;

    public Document() {}
    
    public Document(String title){
        this.title = title;
    }
    
    public Document(String title, User owner, Date lastModified, String content) {
        this.title = title;
        this.owner = owner;
        this.lastModified = lastModified;
        this.content = content;
    }
    
  /*  public void addToCollaborators(User user) {
        this.collaborators.add(user);
    }    
    public void removeFromCollaborators(User user){
        this.collaborators.remove(user);
    } */

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getLastModified() {
        return lastModified;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }    

  /*  public List<User> getCollaborators() {
        return collaborators;
    }*/
}
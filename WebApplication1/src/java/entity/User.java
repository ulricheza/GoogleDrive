/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "User.findByLogin",
                query = "SELECT u FROM User u WHERE u.login=:login")
})
public class User implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true,length = 10)
    private String login;
    
    private String password;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)        
    private byte[] profileImage;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Document> documents;
    
    @OneToMany(mappedBy = "owner")
    private List<Starred> starreds;
       
    public User() {}
    
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public void addToDocuments(Document document) {
        this.documents.add(document);
    }    
    public void removeFromDocuments(Document document){
        this.documents.remove(document);
    }
    
    public void addToStarreds(Starred starred) {
        this.starreds.add(starred);
    }    
    public void removeFromStarreds(Starred starred){
        this.starreds.remove(starred);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.id, other.id);
    }
    
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Document> getDocuments() {
        return documents;
    }  
    public List<Starred> getStarreds() {
        return starreds;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
    
    /*@OneToMany
    private List<Document> starred;
    
    @OneToMany
    private List<User> friends;*/
}
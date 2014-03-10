/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import service.EncryptionService;

@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name  = "User.findByLogin",
                query = "SELECT u FROM User u WHERE u.login=:login"),
    @NamedQuery(name  = "User.findByLoginLike",
                query = "SELECT u FROM User u WHERE u.login LIKE :login")
})
public class User implements Serializable{    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true,length = 10)
    private String login;
    
    private String password;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Remember remember;
        
    public User() {}
    
    public User(String login, String password) {
        this.login = login;
        this.password = password;
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
    
    public Remember getRemember(){
        return remember;
    }
    
    @Override
    public String toString(){
        return login;
    }

    public void encodePassword() {
        password = EncryptionService.encrypt(password);
    }
}
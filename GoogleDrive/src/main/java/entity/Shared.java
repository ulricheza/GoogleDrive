/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name  = "Shared.findUsersByDocument",
                query = "SELECT s.user FROM Shared s WHERE s.document.id=:document_id"),
    @NamedQuery(name  = "Shared.findDocumentsByUser",
                query = "SELECT s.document FROM Shared s WHERE s.user.id=:user_id"),
    @NamedQuery(name  = "Shared.findByUserAndDocument",
                query = "SELECT s FROM Shared s "
                      + "WHERE s.user.id=:user_id AND s.document.id=:document_id"),
    @NamedQuery(name  = "Shared.deleteByDocument",
                query = "DELETE FROM Shared s WHERE s.document.id=:document_id"),
    @NamedQuery(name  = "Shared.deleteByUserAndDocument",
                query = "DELETE FROM Shared s "
                      + "WHERE s.user.id=:user_id AND s.document.id=:document_id")
})
public class Shared implements Serializable {    
    @EmbeddedId
    private SharedId id;   
    
    @ManyToOne
    @MapsId("userId")
    private User user;
    
    @ManyToOne
    @MapsId("documentId")
    private Document document;
    
    public Shared() {}
    
    public Shared(User user, Document document){
        this.user = user;
        this.document = document;
    } 
    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Shared other = (Shared) obj;
        return Objects.equals(this.document.getId(), other.document.getId()) &&
               Objects.equals(this.user.getId(), other.user.getId());
    }
    
    @Override
    public String toString() {
        return "entity.Shared[ id=" + id + " ]";
    }
}
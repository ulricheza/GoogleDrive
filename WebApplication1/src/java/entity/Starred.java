/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Starred.findByOwnerAndDocument",
                query = "SELECT s "
                      + "FROM Starred s "
                      + "WHERE s.owner.id=:owner_id AND s.document.id=:document_id")
})
public class Starred implements Serializable{
    @EmbeddedId
    private StarredId id;  

    @MapsId("owner_id")
    @ManyToOne
    private User owner;
    
    @MapsId("document_id")
    @ManyToOne
    private Document document;
    
    public Starred() {}
    
    public Starred(User owner, Document document){
        this.owner = owner;
        this.document = document;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Starred other = (Starred) obj;
        return Objects.equals(this.document.getId(), other.document.getId()) &&
               Objects.equals(this.owner.getId(), other.owner.getId());
    }    
}

@Embeddable
class StarredId implements Serializable {
    Long owner_id;
    Long document_id;
}
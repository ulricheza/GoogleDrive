/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
    @NamedQuery(name  = "Remember.DeleteByUser",
                query = "DELETE FROM Remember r WHERE r.user.id = :user_id")
})
public class Remember implements Serializable {    
    @Id
    private String uuid;
    
    @OneToOne
    private User user;

    public Remember() {}
    
    public Remember(String uuid, User user){
        this.uuid = uuid;
        this.user = user;
    }
    
    public String getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Remember)) {
            return false;
        }
        Remember other = (Remember) object;
        return (this.uuid != null || other.uuid == null) && (this.uuid == null || this.uuid.equals(other.uuid));
    }

    @Override
    public String toString() {
        return "entity.Remember[ uuid=" + uuid + " ]";
    }
}
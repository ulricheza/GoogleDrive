/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Remember.DeleteByUser",
                query = "DELETE FROM Remember WHERE userId = :userId")
})
public class Remember implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private String uuid;
    
    @Column(name = "USER_ID")
    private Long userId;

    public Remember() {}
    
    public Remember(String uuid, Long userId){
        this.uuid = uuid;
        this.userId = userId;
    }
    
    public String getUuid() {
        return uuid;
    }

    public Long getUserId() {
        return userId;
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
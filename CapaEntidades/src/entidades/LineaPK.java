/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author LomitoFrito
 */
@Embeddable
public class LineaPK implements Serializable
{

    @Basic(optional = false)
    @Column(name = "ID")
    private BigInteger id;
    @Basic(optional = false)
    @Column(name = "PRESTAMONUMERO")
    private BigInteger prestamonumero;

    public LineaPK()
    {
    }

    public LineaPK(BigInteger id, BigInteger prestamonumero)
    {
        this.id = id;
        this.prestamonumero = prestamonumero;
    }

    public BigInteger getId()
    {
        return id;
    }

    public void setId(BigInteger id)
    {
        this.id = id;
    }

    public BigInteger getPrestamonumero()
    {
        return prestamonumero;
    }

    public void setPrestamonumero(BigInteger prestamonumero)
    {
        this.prestamonumero = prestamonumero;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (prestamonumero != null ? prestamonumero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof LineaPK))
        {
            return false;
        }
        LineaPK other = (LineaPK) object;
        if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        if((this.prestamonumero == null && other.prestamonumero != null) || (this.prestamonumero != null && !this.prestamonumero.equals(other.prestamonumero)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "LineaPK{" + "id=" + id + ", prestamonumero=" + prestamonumero + '}';
    }


    
}

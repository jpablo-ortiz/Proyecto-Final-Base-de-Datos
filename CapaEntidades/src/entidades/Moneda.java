/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author LomitoFrito
 */
@Entity
@Table(name = "MONEDA")
@NamedQueries(
{
    @NamedQuery(name = "Moneda.findAll", query = "SELECT m FROM Moneda m")
})
public class Moneda implements Serializable
{

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "DENOMINACIONMONEDA", referencedColumnName = "DENOMINACION")
    private Denominacion denominacionmoneda;
    @JoinColumn(name = "PRESTAMONUMERO", referencedColumnName = "NUMERO")
    @ManyToOne
    private Prestamo prestamonumero;

    public Moneda()
    {
    }

    public Moneda(BigDecimal id)
    {
        this.id = id;
    }

    public BigDecimal getId()
    {
        return id;
    }

    public void setId(BigDecimal id)
    {
        this.id = id;
    }

    public Denominacion getDenominacionmoneda()
    {
        return denominacionmoneda;
    }

    public void setDenominacionmoneda(Denominacion denominacionmoneda)
    {
        this.denominacionmoneda = denominacionmoneda;
    }

    public Prestamo getPrestamonumero()
    {
        return prestamonumero;
    }

    public void setPrestamonumero(Prestamo prestamonumero)
    {
        this.prestamonumero = prestamonumero;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof Moneda))
        {
            return false;
        }
        Moneda other = (Moneda) object;
        if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Moneda{" + "id=" + id + ", denominacionmoneda=" + denominacionmoneda + ", prestamonumero=" + prestamonumero + '}';
    }


    
}

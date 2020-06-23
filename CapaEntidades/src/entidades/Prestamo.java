/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author LomitoFrito
 */
@Entity
@Table(name = "PRESTAMO")
@NamedQueries(
{
    @NamedQuery(name = "Prestamo.findAll", query = "SELECT p FROM Prestamo p")
})
public class Prestamo implements Serializable
{

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private BigInteger numero;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToMany(mappedBy = "prestamonumero")
    private List<Moneda> monedaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestamo")
    private List<Linea> lineaList;

    public Prestamo()
    {
    }

    public Prestamo(BigInteger numero)
    {
        this.numero = numero;
    }

    public BigInteger getNumero()
    {
        return numero;
    }

    public void setNumero(BigInteger numero)
    {
        this.numero = numero;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public List<Moneda> getMonedaList()
    {
        return monedaList;
    }

    public void setMonedaList(List<Moneda> monedaList)
    {
        this.monedaList = monedaList;
    }

    public List<Linea> getLineaList()
    {
        return lineaList;
    }

    public void setLineaList(List<Linea> lineaList)
    {
        this.lineaList = lineaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof Prestamo))
        {
            return false;
        }
        Prestamo other = (Prestamo) object;
        if((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Prestamo{" + "numero=" + numero + ", fecha=" + fecha + ", monedaList=" + monedaList + ", lineaList=" + lineaList + '}';
    }


}

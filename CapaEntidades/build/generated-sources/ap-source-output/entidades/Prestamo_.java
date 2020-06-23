package entidades;

import entidades.Linea;
import entidades.Moneda;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-06-03T18:55:33")
@StaticMetamodel(Prestamo.class)
public class Prestamo_ { 

    public static volatile SingularAttribute<Prestamo, Date> fecha;
    public static volatile ListAttribute<Prestamo, Moneda> monedaList;
    public static volatile SingularAttribute<Prestamo, BigInteger> numero;
    public static volatile ListAttribute<Prestamo, Linea> lineaList;

}
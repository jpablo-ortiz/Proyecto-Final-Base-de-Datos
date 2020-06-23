package entidades;

import entidades.Libro;
import entidades.LineaPK;
import entidades.Prestamo;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-06-03T18:55:33")
@StaticMetamodel(Linea.class)
public class Linea_ { 

    public static volatile SingularAttribute<Linea, Prestamo> prestamo;
    public static volatile SingularAttribute<Linea, LineaPK> lineaPK;
    public static volatile SingularAttribute<Linea, Double> subtotal;
    public static volatile SingularAttribute<Linea, BigInteger> cantidad;
    public static volatile SingularAttribute<Linea, Libro> libroisbn;

}
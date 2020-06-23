package integracion;

import entidades.Linea;
import entidades.Prestamo;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface IGestionPrestamo
{
    public boolean crearPrestamo(Prestamo prestamo);
    
    public boolean crearLinea(Linea linea);
    
    public Prestamo buscarPrestamo(int numeroPrestamo);
    
    public BigInteger ultimoNumeroPrestamo();
    public BigDecimal ultimoIdLinea();
    public BigDecimal ultimoIdMoneda();
}

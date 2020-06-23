package entidades;

import java.util.ArrayList;
import java.util.List;

public class DtoResumen
{
    //Un atributo ‘mensaje’ de tipo cadena con mensajes de error 
    private String mensaje;
    /*La colección de objetos de Líneas conteniendo:
      -  Objeto Libro //se obtiene con el isbn
      -  cantidad
      -  El valor total del libro (precio)
      -  subtotal de la línea*/
    private List<Linea> lineas;
    //Un atributo de tipo booleano que indica si se pudo agregar la línea al préstamo
    private boolean lineaAgregada;
    //El total de todo el préstamo
    private double totalPrestamo;
    //El saldo de las monedas ingresadas
    private double saldoMonedasIngresadas;
    //La cantidad de vueltos del préstamo actual
    private double cantidadVueltosPrestamoActual;    

    //Auxiliares
    private Prestamo prestamoConsultado;
    
    public DtoResumen()
    {
        this.lineas = new ArrayList<>();
    }
    

    public String getMensaje()
    {
        return mensaje;
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    public List<Linea> getLineas()
    {
        return lineas;
    }

    public void setLineas(List<Linea> lineas)
    {
        this.lineas = lineas;
    }

    public boolean isLineaAgregada()
    {
        return lineaAgregada;
    }

    public void setLineaAgregada(boolean lineaAgregada)
    {
        this.lineaAgregada = lineaAgregada;
    }

    public double getTotalPrestamo()
    {
        return totalPrestamo;
    }

    public void setTotalPrestamo(double totalPrestamo)
    {
        this.totalPrestamo = totalPrestamo;
    }

    public double getSaldoMonedasIngresadas()
    {
        return saldoMonedasIngresadas;
    }

    public void setSaldoMonedasIngresadas(double saldoMonedasIngresadas)
    {
        this.saldoMonedasIngresadas = saldoMonedasIngresadas;
    }

    public double getCantidadVueltosPrestamoActual()
    {
        return cantidadVueltosPrestamoActual;
    }

    public void setCantidadVueltosPrestamoActual(double cantidadVueltosPrestamoActual)
    {
        this.cantidadVueltosPrestamoActual = cantidadVueltosPrestamoActual;
    }

    public Prestamo getPrestamoConsultado()
    {
        return prestamoConsultado;
    }

    public void setPrestamoConsultado(Prestamo prestamoConsultado)
    {
        this.prestamoConsultado = prestamoConsultado;
    }
    
    
    
}

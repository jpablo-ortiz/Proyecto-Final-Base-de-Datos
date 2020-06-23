/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entidades.Denominacion;
import entidades.DtoResumen;
import entidades.Libro;
import entidades.Linea;
import entidades.LineaPK;
import entidades.Moneda;
import entidades.Prestamo;
import integracion.IGestionLibro;
import integracion.IGestionPrestamo;
import integracion.RepositorioLibro;
import integracion.RepositorioPrestamo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author LomitoFrito
 */
public class FacadeLibreria implements IFacadeLibreria
{

    private EntityManagerFactory emf;

    //Relacion con CapaAccesoDatos (Persistencia de los datos) (Negocio a AccesoDatos)
    private IGestionLibro gestionLibro;
    private IGestionPrestamo gestionPrestamo;

    //Relaciones con CapaEntidades
    private List<Libro> catalogo;
    private List<Prestamo> prestamos;
    private Prestamo prestamoActual;
    private DtoResumen dtoResumenActual;

    //Constantes
    static final double VALOR_IMAGEN = 5.0;
    static final double VALOR_VIDEO = 5.0;
    static final String RUTA_ARCHIVO_LIBROS = "C/:";
    static final String RUTA_REPORTE = "C/:";

    //Contadores
    private int contadorNumeroPrestamo;
    private int contadorIdLinea;
    private int contadorIdMoneda;

    public FacadeLibreria()
    {
        this.contadorNumeroPrestamo = 0;
        this.contadorIdLinea = 0;
        this.contadorIdMoneda = 0;
        this.emf = Persistence.createEntityManagerFactory("CapaEntidadesPU");
        this.gestionPrestamo = new RepositorioPrestamo(emf);
        this.gestionLibro = new RepositorioLibro(emf);
        this.prestamos = new ArrayList<>();
        this.catalogo = crearCatalogo();
        this.prestamoActual = new Prestamo();
        this.dtoResumenActual = new DtoResumen();

        //OBTENER ULTIMOS ID
        if(gestionPrestamo.ultimoNumeroPrestamo() != null)
        {
            contadorNumeroPrestamo = gestionPrestamo.ultimoNumeroPrestamo().intValue();
        }
        if(gestionPrestamo.ultimoIdLinea() != null)
        {
            contadorIdLinea = gestionPrestamo.ultimoIdLinea().intValue();
        }
        if(gestionPrestamo.ultimoIdMoneda() != null)
        {
            contadorIdMoneda = gestionPrestamo.ultimoIdMoneda().intValue();
        }
    }

    @Override
    public List<Libro> crearCatalogo()
    {
        return gestionLibro.cargarLibros();
    }

    @Override
    public boolean crearNuevoPrestamo()
    {
        if(disponibilidadDeLibros())
        {
            //Genera un numero unico interno con @GeneratedValue(strategy=GenerationType.AUTO)
            contadorNumeroPrestamo++;
            prestamoActual = new Prestamo(BigInteger.valueOf(contadorNumeroPrestamo));
            dtoResumenActual = new DtoResumen();
            //Fecha actual
            prestamoActual.setFecha(java.sql.Date.valueOf(LocalDate.now()));
            List<Linea> lineaList = new ArrayList<>();
            List<Moneda> monedaList = new ArrayList<>();
            prestamoActual.setLineaList(lineaList);
            prestamoActual.setMonedaList(monedaList);

            boolean creadoCorrectamente = gestionPrestamo.crearPrestamo(prestamoActual);

            if(creadoCorrectamente)
            {
                prestamos.add(prestamoActual);
                dtoResumenActual.setMensaje("Prestamo creado correctamente");
                dtoResumenActual.setLineaAgregada(true);
                return true;
            }
            //Mensaje de error (No pudo crearse el prestamo)
            dtoResumenActual.setMensaje("Hubo un error en la BD. No pudo crearse el prestamo");
            dtoResumenActual.setLineaAgregada(false);
            return false;
        }
        //Mensaje de error (No hay libros disponibles en este momento)
        dtoResumenActual.setMensaje("No hay libros disponibles en este momento");
        dtoResumenActual.setLineaAgregada(false);
        return false;
    }

    public boolean disponibilidadDeLibros()
    {
        for(Libro libro : catalogo)
        {
            if(libro.getUnidadesdisponibles().intValue() > 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Libro> getCatalogo()
    {
        return catalogo;
    }

    @Override
    public String getMensajePrestamo()
    {
        return dtoResumenActual.getMensaje();
    }

    @Override
    public DtoResumen agregarLinea(Libro libro, int cantidad)
    {
        //Si existe el libro
        if(verificarLibroEnCatalogo(libro))
        {
            //Si hay unidades disponobles
            if(verificacionExistenciaLibro(libro, cantidad))
            {
                //Recorrer todas las lineas actuales
                for(Linea linea : dtoResumenActual.getLineas())
                {
                    //Si ya esta este libro en alguna linea sumarlo
                    if(linea.getLibroisbn().getIsbn().equals(libro.getIsbn()))
                    {
                        int cantidadNueva = linea.getCantidad().intValue() + cantidad;
                        linea.setCantidad(BigInteger.valueOf(cantidadNueva));
                        linea.setSubtotal(calcularSubtotalLinea(linea));
                        terminarLlenadoDto();
                        dtoResumenActual.setLineaAgregada(true);
                        return dtoResumenActual;
                    }
                }
                //Si no cree la nueva linea
                crearLinea(libro, cantidad);
                terminarLlenadoDto();
                return dtoResumenActual;
            }
            else
            {
                //Mensaje de error
                dtoResumenActual.setMensaje("No hay disponibilidad de más de la cantidad ingresada del libro");
                dtoResumenActual.setLineaAgregada(false);
            }
        }
        else
        {
            //Mensaje de error
            dtoResumenActual.setMensaje("El libro ingresado no está en el catálogo");
            dtoResumenActual.setLineaAgregada(false);
        }
        return dtoResumenActual;
    }

    private void terminarLlenadoDto()
    {
        //Los otros Atributos DTO ya han sido llenados con anterioridad
        //Total del prestamo actual
        dtoResumenActual.setTotalPrestamo(totalDelPrestamo());

        //dtoResumenActual.setCantidadVueltosPrestamoActual();
        //dtoResumenActual.setSaldoMonedasIngresadas();
    }

    private boolean verificarLibroEnCatalogo(Libro libro)
    {
        for(Libro libroActual : catalogo)
        {
            if(libroActual.equals(libro))
            {
                return true;
            }
        }
        return false;
    }

    private boolean verificacionExistenciaLibro(Libro libro, int cantidad)
    {
        return libro.getUnidadesdisponibles().intValue() >= cantidad;
    }

    private void crearLinea(Libro libro, int cantidad)
    {
        contadorIdLinea++;
        Linea lineaTemp = new Linea(new LineaPK(BigInteger.valueOf(contadorIdLinea), prestamoActual.getNumero()));
        lineaTemp.setCantidad(BigInteger.valueOf(cantidad));
        lineaTemp.setLibroisbn(libro);
        lineaTemp.setSubtotal(calcularSubtotalLinea(lineaTemp));

        boolean creadoCorrectamente = gestionPrestamo.crearLinea(lineaTemp);
        if(creadoCorrectamente)
        {
            //PARA METER LA NUEVA LINEA AL DTO
            dtoResumenActual.getLineas().add(lineaTemp);
            prestamoActual.getLineaList().add(lineaTemp);
            //Dice si se pudo o no agregar la nueva linea
            dtoResumenActual.setLineaAgregada(true);
            //Mensaje de exito
            dtoResumenActual.setMensaje("La linea se agrego correctamente al prestamo");
        }
        else
        {
            //Mensaje de error (No pudo crearse el prestamo)
            dtoResumenActual.setMensaje("Hubo un error en la BD. No pudo crearse la linea");
            dtoResumenActual.setLineaAgregada(false);
        }
    }

    private double calcularValorLibro(Libro libro)
    {
        double result = libro.getPreciobase();
        result += (libro.getNumeroimagenes().doubleValue() * VALOR_IMAGEN);
        result += (libro.getNumerovideos().doubleValue() * VALOR_VIDEO);
        return result;
    }

    private double calcularSubtotalLinea(Linea linea)
    {
        return calcularValorLibro(linea.getLibroisbn()) * linea.getCantidad().doubleValue();
    }

    private double totalDelPrestamo()
    {
        double total = 0;
        System.out.println(prestamoActual.getLineaList().size() + "HOLLLLLLLLLL");
        for(Linea linea : prestamoActual.getLineaList())
        {
            total += calcularSubtotalLinea(linea);
        }
        return total;
    }

    @Override
    public List<Denominacion> getDenominaciones()
    {
        return Arrays.asList(Denominacion.values());
    }

    @Override
    public Prestamo getPrestamoActual()
    {
        return prestamoActual;
    }

    @Override
    public DtoResumen eliminarLinea(Linea linea)
    {
        //Si no es nula
        if(verificarLinea(linea))
        {
            //Buscar la línea y quitar de la coleccion de lineas de prestamo actual
            prestamoActual.getLineaList().remove(linea);

            //Crear el dto que se va a retornar
            dtoResumenActual.getLineas().remove(linea);
            dtoResumenActual.setLineaAgregada(true);
            terminarLlenadoDto();
            //Mensaje de exito
            dtoResumenActual.setMensaje("La linea se eliminó correctamente del prestamo");
            return dtoResumenActual;
        }
        else
        {
            dtoResumenActual.setLineaAgregada(false);
            return dtoResumenActual;
        }
    }

    private boolean verificarLinea(Linea linea)
    {
        if(linea == null)
        {
            dtoResumenActual.setMensaje("Linea recibida nula");
            return false;
        }
        return true;
    }

    @Override
    public DtoResumen introducirMoneda(Denominacion denominacion, int cantiMonedas)
    {
        //Validar si existe el enumerado
        if(validarDenominacion(denominacion))
        {
            //Agregar la moneda creada a la colección ‘pagoMonedas’ del préstamo
            for(int i = 0; i < cantiMonedas; i++)
            {
                //Crear una nueva ‘Moneda’, vinculando el enumerado que llega como parámetro
                Moneda monedaTemp = new Moneda(BigDecimal.valueOf(contadorIdMoneda));
                monedaTemp.setDenominacionmoneda(denominacion);
                prestamoActual.getMonedaList().add(monedaTemp);
            }

            //Crear el ‘DtoResumen’ que va a retornar.
            //’saldo de monedas ingresadas’ ya diligenciado con el total de monedas de ‘pagoMonedas’ del préstamo.
            double cuenta = 0;
            for(Moneda moneda : prestamoActual.getMonedaList())
            {
                cuenta += moneda.getDenominacionmoneda().retornarNumero();
            }
            dtoResumenActual.setSaldoMonedasIngresadas(cuenta);
            dtoResumenActual.setMensaje("Moneda Ingresada Correctamente");
            //para asumir que se ingreso correctamente la moneda
            dtoResumenActual.setLineaAgregada(true);

            return dtoResumenActual;
        }
        else
        {
            //Mensaje de error
            dtoResumenActual.setMensaje("Denominacion no existente");
            dtoResumenActual.setLineaAgregada(false);
            return dtoResumenActual;
        }
    }

    private boolean validarDenominacion(Denominacion denominacion)
    {
        for(Denominacion value : Denominacion.values())
        {
            if(value.equals(denominacion))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public DtoResumen terminarPrestamo()
    {
        if(verificarSaldo())
        {
            double vueltas = dtoResumenActual.getSaldoMonedasIngresadas() - dtoResumenActual.getTotalPrestamo();
            //Usado para verificar que se pudo hacer el prestamo
            dtoResumenActual.setLineaAgregada(true);
            dtoResumenActual.setCantidadVueltosPrestamoActual(vueltas);
            dtoResumenActual.setMensaje("Transaccion realizada exitosamente");
            return dtoResumenActual;
        }
        else
        {
            dtoResumenActual.setLineaAgregada(false);
            dtoResumenActual.setMensaje("El saldo ingresado no es suficiente para realizar el prestamo");
            return dtoResumenActual;
        }
    }

    private boolean verificarSaldo()
    {
        if(dtoResumenActual.getSaldoMonedasIngresadas() > dtoResumenActual.getTotalPrestamo())
        {
            for(Linea linea : dtoResumenActual.getLineas())
            {
                //Actualizar existencias
                int disponibles = linea.getLibroisbn().getUnidadesdisponibles().intValue() - linea.getCantidad().intValue();
                linea.getLibroisbn().setUnidadesdisponibles(BigInteger.valueOf(disponibles));
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public DtoResumen consultarPrestamo(int numeroPrestamo)
    {
        Prestamo buscarPrestamo = gestionPrestamo.buscarPrestamo(numeroPrestamo);
        if(buscarPrestamo != null)
        {
            dtoResumenActual.setPrestamoConsultado(buscarPrestamo);
            dtoResumenActual.setLineas(buscarPrestamo.getLineaList());
            dtoResumenActual.setTotalPrestamo(totalDelPrestamo());
            dtoResumenActual.setMensaje("Se encontro el prestamo con este número");
            dtoResumenActual.setLineaAgregada(true);
            return dtoResumenActual;
        }
        else
        {
            dtoResumenActual.setMensaje("Hubo un error encontrando el prestamo con el número indicado");
            dtoResumenActual.setLineaAgregada(false);
            return dtoResumenActual;
        }
    }
}

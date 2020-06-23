package capapresentacion;

import entidades.Denominacion;
import entidades.DtoResumen;
import entidades.Libro;
import entidades.Linea;
import interfaces.FacadeLibreria;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import interfaces.IFacadeLibreria;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PantallaKioscoController implements Initializable
{

    //Relacion FacadeLibreria (Presentacion a Negocio)
    IFacadeLibreria interfazLibreria = new FacadeLibreria();

    @FXML
    private Button botonNuevoPrestamo;
    @FXML
    private Text editFechaHoraPrestamo;
    @FXML
    private Text editNumeroPrestamo;
    @FXML
    private TextField cantidadLibro;
    @FXML
    private Button botonAgregarLinea;
    @FXML
    private TextField cantidadMonedas;
    @FXML
    private ComboBox<Denominacion> comboBoxDenominacion;
    @FXML
    private Button botonAgregarMonedas;
    @FXML
    private Text editSaldoDisponible;
    @FXML
    private Button botonTerminarPrestamo;
    @FXML
    private Text editVueltos;
    @FXML
    private Text editTotalPrestamo;
    @FXML
    private TableView<Linea> tablaLineasDePrestamo;
    @FXML
    private TableColumn<Linea, String> columnaLibro;
    @FXML
    private TableColumn<Linea, Integer> columnaCantidad;
    @FXML
    private TableColumn<Linea, Double> columnaPrecioLibro;
    @FXML
    private TableColumn<Linea, Double> columnaSubTotal;
    @FXML
    private ComboBox<Libro> comboBoxLibros;
    @FXML
    private Button botonEliminarLinea;
    @FXML
    private Button botonConsultarPrestamo;
    @FXML
    private TextField numeroPrestamoAConsultar;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editFechaHoraPrestamo.setVisible(false);
        editNumeroPrestamo.setVisible(false);
        cantidadLibro.setText("0");
        editTotalPrestamo.setVisible(false);

        cantidadMonedas.setText("0");
        editSaldoDisponible.setVisible(false);
        editVueltos.setVisible(false);

        //ComboBox de Libros
        comboBoxLibros.setPromptText("Catálogo de Libros");
        comboBoxLibros.setItems(FXCollections.observableArrayList(interfazLibreria.getCatalogo()));

        //ComboBox de Libros
        comboBoxDenominacion.setPromptText("Denominación");
        comboBoxDenominacion.setItems(FXCollections.observableArrayList(interfazLibreria.getDenominaciones()));

        interfazLibreria.crearNuevoPrestamo();

        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Por favor Cree un nuevo Prestamo con el botón 'Nuevo Prestamo'");
        alert.showAndWait();
    }

    @FXML
    private void clicNuevoPrestamo(ActionEvent event)
    {
        boolean creadoCorrectamente = interfazLibreria.crearNuevoPrestamo();

        cantidadLibro.setText("0");
        cantidadMonedas.setText("0");

        Alert alert;
        if(creadoCorrectamente)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);
            editFechaHoraPrestamo.setVisible(true);
            editNumeroPrestamo.setVisible(true);

            DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
            editFechaHoraPrestamo.setText(hourdateFormat.format(interfazLibreria.getPrestamoActual().getFecha()));
            editNumeroPrestamo.setText(interfazLibreria.getPrestamoActual().getNumero().toString());
            tablaLineasDePrestamo.setItems(FXCollections.observableArrayList());
            editTotalPrestamo.setText("$ 0");

        }
        else
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(interfazLibreria.getMensajePrestamo());
        alert.showAndWait();

    }

    @FXML
    private void clicAgregarLinea(ActionEvent event)
    {
        DtoResumen dtoRecibido = interfazLibreria.agregarLinea(comboBoxLibros.getValue(), Integer.valueOf(cantidadLibro.getText()));

        editTotalPrestamo.setVisible(true);
        editSaldoDisponible.setVisible(true);
        editVueltos.setVisible(true);

        boolean creadoCorrectamente = dtoRecibido.isLineaAgregada();
        Alert alert;
        if(creadoCorrectamente)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);

            editVueltos.setText(Double.toString(dtoRecibido.getCantidadVueltosPrestamoActual()));
            editSaldoDisponible.setText(Double.toString(dtoRecibido.getSaldoMonedasIngresadas()));
            editTotalPrestamo.setText("$ " + Double.toString(dtoRecibido.getTotalPrestamo()));
            tablaLineasDePrestamo.setItems(FXCollections.observableArrayList());
        }
        else
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(dtoRecibido.getMensaje());
        alert.showAndWait();

        //Tabla de libros
        tablaLineasDePrestamo.setItems(FXCollections.observableArrayList(dtoRecibido.getLineas()));
        columnaLibro.setCellValueFactory((CellDataFeatures<Linea, String> p) -> new SimpleStringProperty(p.getValue().getLibroisbn().toString()));
        columnaPrecioLibro.setCellValueFactory((CellDataFeatures<Linea, Double> p) -> new SimpleObjectProperty<>(p.getValue().getLibroisbn().getPreciobase()));
        columnaCantidad.setCellValueFactory((CellDataFeatures<Linea, Integer> p) -> new SimpleObjectProperty<>(p.getValue().getCantidad().intValue()));
        columnaSubTotal.setCellValueFactory((CellDataFeatures<Linea, Double> p) -> new SimpleObjectProperty<>((p.getValue().getSubtotal())));
    }

    @FXML
    private void clicEliminarLinea(ActionEvent event)
    {
        Linea lineaSeleccionada = tablaLineasDePrestamo.getSelectionModel().getSelectedItem();
        DtoResumen dtoRecibido = interfazLibreria.eliminarLinea(lineaSeleccionada);

        editTotalPrestamo.setVisible(true);
        editSaldoDisponible.setVisible(true);
        editVueltos.setVisible(true);

        boolean eliminadoCorrectamente = dtoRecibido.isLineaAgregada();
        Alert alert;
        if(eliminadoCorrectamente)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);

            editVueltos.setText(Double.toString(dtoRecibido.getCantidadVueltosPrestamoActual()));
            editSaldoDisponible.setText(Double.toString(dtoRecibido.getSaldoMonedasIngresadas()));
            editTotalPrestamo.setText("$ " + Double.toString(dtoRecibido.getTotalPrestamo()));
        }
        else
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(dtoRecibido.getMensaje());
        alert.showAndWait();

        //Tabla de libros
        tablaLineasDePrestamo.setItems(FXCollections.observableArrayList(dtoRecibido.getLineas()));
        columnaLibro.setCellValueFactory((CellDataFeatures<Linea, String> p) -> new SimpleStringProperty(p.getValue().getLibroisbn().toString()));
        columnaPrecioLibro.setCellValueFactory((CellDataFeatures<Linea, Double> p) -> new SimpleObjectProperty<>(p.getValue().getLibroisbn().getPreciobase()));
        columnaCantidad.setCellValueFactory((CellDataFeatures<Linea, Integer> p) -> new SimpleObjectProperty<>(p.getValue().getCantidad().intValue()));
        columnaSubTotal.setCellValueFactory((CellDataFeatures<Linea, Double> p) -> new SimpleObjectProperty<>((p.getValue().getSubtotal())));

    }

    @FXML
    private void clicAgregarMonedas(ActionEvent event)
    {
        DtoResumen dtoRecibido = interfazLibreria.introducirMoneda(comboBoxDenominacion.getValue(), Integer.valueOf(cantidadMonedas.getText()));

        editTotalPrestamo.setVisible(true);
        editSaldoDisponible.setVisible(true);
        editVueltos.setVisible(true);

        boolean creadoCorrectamente = dtoRecibido.isLineaAgregada();
        Alert alert;
        if(creadoCorrectamente)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);

            editVueltos.setText(Double.toString(dtoRecibido.getCantidadVueltosPrestamoActual()));
            editSaldoDisponible.setText(Double.toString(dtoRecibido.getSaldoMonedasIngresadas()));
            editTotalPrestamo.setText("$ " + Double.toString(dtoRecibido.getTotalPrestamo()));
        }
        else
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(dtoRecibido.getMensaje());
        alert.showAndWait();
    }

    @FXML
    private void clicTerminarPrestamo(ActionEvent event)
    {
        DtoResumen dtoRecibido = interfazLibreria.terminarPrestamo();

        editTotalPrestamo.setVisible(true);
        editSaldoDisponible.setVisible(true);
        editVueltos.setVisible(true);

        boolean creadoCorrectamente = dtoRecibido.isLineaAgregada();
        Alert alert;
        if(creadoCorrectamente)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);

            editVueltos.setText(Double.toString(dtoRecibido.getCantidadVueltosPrestamoActual()));
            editSaldoDisponible.setText(Double.toString(dtoRecibido.getSaldoMonedasIngresadas()));
            editTotalPrestamo.setText("$ " + Double.toString(dtoRecibido.getTotalPrestamo()));
        }
        else
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(dtoRecibido.getMensaje());
        alert.showAndWait();
    }

    @FXML
    public void clicConsultarPrestamo(ActionEvent event)
    {
        DtoResumen dtoRecibido = interfazLibreria.consultarPrestamo(Integer.valueOf(numeroPrestamoAConsultar.getText()));

        editTotalPrestamo.setVisible(true);
        editSaldoDisponible.setVisible(true);
        editVueltos.setVisible(true);

        boolean creadoCorrectamente = dtoRecibido.isLineaAgregada();
        Alert alert;
        if(creadoCorrectamente)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);

            editVueltos.setText(Double.toString(dtoRecibido.getCantidadVueltosPrestamoActual()));
            editSaldoDisponible.setText(Double.toString(dtoRecibido.getSaldoMonedasIngresadas()));
            editTotalPrestamo.setText("$ " + Double.toString(dtoRecibido.getTotalPrestamo()));
            tablaLineasDePrestamo.setItems(FXCollections.observableArrayList());
            editFechaHoraPrestamo.setText(dtoRecibido.getPrestamoConsultado().getFecha().toString());
            editNumeroPrestamo.setText(dtoRecibido.getPrestamoConsultado().getNumero().toString());
            
        }
        else
        {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(dtoRecibido.getMensaje());
        alert.showAndWait();

        //Tabla de libros
        tablaLineasDePrestamo.setItems(FXCollections.observableArrayList(dtoRecibido.getLineas()));
        columnaLibro.setCellValueFactory((CellDataFeatures<Linea, String> p) -> new SimpleStringProperty(p.getValue().getLibroisbn().toString()));
        columnaPrecioLibro.setCellValueFactory((CellDataFeatures<Linea, Double> p) -> new SimpleObjectProperty<>(p.getValue().getLibroisbn().getPreciobase()));
        columnaCantidad.setCellValueFactory((CellDataFeatures<Linea, Integer> p) -> new SimpleObjectProperty<>(p.getValue().getCantidad().intValue()));
        columnaSubTotal.setCellValueFactory((CellDataFeatures<Linea, Double> p) -> new SimpleObjectProperty<>((p.getValue().getSubtotal())));

    }

}

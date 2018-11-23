package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Controller {

    @FXML
    private TextField placa;
    @FXML
    private TextField modelo;
    @FXML
    private TextField odometro;
    @FXML
    private TextField capacidade;
    @FXML
    private TextField ano;
    @FXML
    private TextField fabricante;

    public Controller() {

    }

    @FXML
    private void initialize(){

    }

    @FXML
    private void salvarAutomovel() throws IOException {
        Automovel a = new Automovel();
        a.setPlaca(placa.getText());
        a.setModelo(modelo.getText());
        a.setAno(Integer.parseInt(ano.getText()));
        a.setCapacidade(Double.parseDouble(capacidade.getText()));
        a.setOdometro(Double.parseDouble(odometro.getText()));
        a.setMarca(fabricante.getText());
        a.saveToTxt();
    }

}

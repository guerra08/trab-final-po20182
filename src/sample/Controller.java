package sample;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Controller {

    Automovel a;

    @FXML
    public ChoiceBox<String> listaAutomoveis;
    @FXML
    private Text returnMessage;
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
        a = new Automovel();
    }

    @FXML
    private void initialize(){
        returnMessage.setText("");
    }

    @FXML
    private void salvarAutomovel(){
        if(placa.getText() == null || placa.getText().equals("")){
            returnMessage.setText("");
            returnMessage.setText("Valor da placa informado inválido! ");
            throw new IllegalArgumentException("Valor da placa informado inválido! ");
        }
        a.setPlaca(placa.getText());
        a.setModelo(modelo.getText());
        a.setAno(Integer.parseInt(ano.getText()));
        a.setCapacidade(capacidade.getText());
        a.setOdometro(odometro.getText());
        a.setMarca(fabricante.getText());
        try {
            a.saveToTxt();
            placa.clear();
            modelo.clear();
            ano.clear();
            capacidade.clear();
            odometro.clear();
            fabricante.clear();
        }catch (Exception e){
            System.out.println("Erro ao salvar carro no arquivo!");
        }
        listaAutomoveis.getItems().add(a.getPlaca());
        returnMessage.setText("");
        returnMessage.setText("Carro cadastrado com sucesso! ");
    }

}

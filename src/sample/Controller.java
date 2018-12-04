package sample;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Controller {

    Automovel a;
    HashMap<String, Automovel> carros;

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
    @FXML
    private ChoiceBox<String> listaCombustiveis;
    @FXML
    private DatePicker dataAbas;
    @FXML
    private TextField qtdAbas;
    @FXML
    private TextField odomAbas;

    public Controller() {
        a = new Automovel();
    }

    @FXML
    private void initialize(){
        returnMessage.setText("");
        populateCombustiveis();
        populateCarros();
    }

    @FXML
    private void updateCarList(Automovel a){
        carros.put(a.getPlaca(),a);
        listaAutomoveis.getItems().add(a.getPlaca());
    }

    @FXML
    private void saveCar(){
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
        }catch (Exception e) {
            System.out.println("Erro ao salvar carro no arquivo!");
        }
        this.updateCarList(a);
        returnMessage.setText("");
        returnMessage.setText("Carro cadastrado com sucesso! ");
    }

    @FXML
    private void saveAbastecimento(){

        if(listaAutomoveis.getSelectionModel().getSelectedIndex() >= 0){

        }

    }

    @FXML
    private void populateCombustiveis(){
        listaCombustiveis.getItems().add("Gasolina");
        listaCombustiveis.getItems().add("Diesel");
        listaCombustiveis.getItems().add("Álcool");
    }

    @FXML
    private void populateCarros(){
        try {
            carros = a.readTxt();
            carros.forEach((k,v)->{
                listaAutomoveis.getItems().add(k);
            });
        }catch(Exception e){
            System.out.println(e);
        }
    }

}

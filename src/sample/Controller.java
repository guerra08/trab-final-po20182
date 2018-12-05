package sample;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;

public class Controller {

    HashMap<String, Automovel> cars;

    @FXML
    private TextArea txArea;
    @FXML
    private ChoiceBox<String> listaAutomoveis;
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
    @FXML
    private TextField porLitro;
    @FXML
    private ChoiceBox<String> listaCarrosReport;
    @FXML
    private ChoiceBox<String> reportSelection;

    public Controller() {
    }

    @FXML
    private void initialize(){
        returnMessage.setText("");
        populateCombustiveis();
        populateCarros();
        populateReports();
    }

    @FXML
    private void updateCarList(Automovel a){
        cars.put(a.getPlaca(),a);
        listaAutomoveis.getItems().add(a.getPlaca());
    }

    @FXML
    private void saveCar(){
        Automovel a = new Automovel();
        String foto = "";
        if(placa.getText() == null || placa.getText().equals("")){
            returnMessage.setText("");
            returnMessage.setText("Valor da placa informado inválido! ");
            throw new IllegalArgumentException("Valor da placa informado inválido! ");
        }
        a.setPlaca(placa.getText());
        a.setModelo(modelo.getText());
        a.setAno(Integer.parseInt(ano.getText()));
        a.setCapacidade(capacidade.getText());
        a.setOdometro(Double.parseDouble(odometro.getText()));
        a.setMarca(fabricante.getText());
        Automovel inserted = cars.get(a.getPlaca());
        if(inserted != null){
            System.out.println("null");
            try {
                a.updateSelfOnTxt();
            }catch (Exception e){
                System.out.println(e);
            }
        }else {
            try {
                a.saveToTxt();
            } catch (Exception e) {
                System.out.println("Erro ao salvar carro no arquivo!");
            }
        }
        placa.clear();
        modelo.clear();
        ano.clear();
        capacidade.clear();
        odometro.clear();
        fabricante.clear();
        this.updateCarList(a);
        saveSuccessMsg();
    }

    @FXML
    private void saveAbastecimento(){
        Automovel a = new Automovel();
        if(listaAutomoveis.getSelectionModel().getSelectedIndex() >= 0 && listaCombustiveis.getSelectionModel().getSelectedIndex() >= 0){
            try {
                Automovel byPlaca = cars.get(listaAutomoveis.getSelectionModel().getSelectedItem());
                if(byPlaca.getPlaca()!=null){
                    Abastecimento ab = new Abastecimento(byPlaca, Double.parseDouble(odomAbas.getText()), Double.parseDouble(qtdAbas.getText()), listaCombustiveis.getSelectionModel().getSelectedIndex(),Double.parseDouble(porLitro.getText()),dataAbas.getValue());
                    ab.saveToTxt();

                    saveSuccessMsg();
                }
            }catch (Exception e ){

            }
        }
        else{
            throw new IllegalArgumentException("Dados obrigatórios em branco!");
        }

    }

    private void populateCombustiveis(){
        listaCombustiveis.getItems().add("Gasolina");
        listaCombustiveis.getItems().add("Diesel");
        listaCombustiveis.getItems().add("Álcool");
    }

    private void populateCarros(){
        Automovel a = new Automovel();
        try {
            cars = a.readTxt();
            cars.forEach((k,v)->{
                listaCarrosReport.getItems().add(k);
                listaAutomoveis.getItems().add(k);
            });
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void carReport(String placa){
        txArea.setText("");
        Automovel r = cars.get(placa);
        txArea.setText(r.getPlaca() + " - " + r.getMarca() + " - " + r.getModelo() + " - " + r.getAno() + " - Capacidade: "+ r.getCapacidade() + " - Odometro: " + r.getOdometro() + "\n");
    }

    private void fuelReport(String placa){
        txArea.setText("");
        try {
            HashMap<String, List<Abastecimento>> results = Abastecimento.getAllOfSpecific(placa);
            StringBuilder report = new StringBuilder();
            if(results != null){
                report.append("Relatório de abastecimentos de "+placa+" : \n\n ----------------\n\n");
                report.append(Abastecimento.orderAbastecimentosByMonth(results.get(placa)));
            }
            txArea.setText(report.toString());
        }catch (Exception e){
            /*returnMessage.setText("");
            returnMessage.setText(e.getMessage());*/
            System.out.println(e);
        }
    }

    private void populateReports(){
        reportSelection.getItems().add("Infos. do carro");
        reportSelection.getItems().add("Abastecimentos");
        reportSelection.getItems().add("Consumo");
    }

    @FXML
    private void showReport(){
        if(reportSelection.getSelectionModel().getSelectedIndex() >= 0 && listaCarrosReport.getSelectionModel().getSelectedIndex() >= 0 ){
            String placa = listaCarrosReport.getSelectionModel().getSelectedItem();
            switch(reportSelection.getSelectionModel().getSelectedIndex()){
                case 0:
                    carReport(placa);
                    break;
                case 1:
                    fuelReport(placa);
            }
        }
    }

    private void updateCarFields(String p){
        Automovel c = cars.get(p);
        if(c != null){
            placa.setDisable(true);
            odometro.setDisable(true);
            placa.setText(c.getPlaca());
            modelo.setText(c.getModelo());
            ano.setText(Integer.toString(c.getAno()));
            fabricante.setText(c.getMarca());
            capacidade.setText(c.getCapacidade());
            odometro.setText(Double.toString(c.getOdometro()));
        }
    }

    @FXML
    private void updateCar(){
        if(listaCarrosReport.getSelectionModel().getSelectedIndex() >= 0 ){
            String p = listaCarrosReport.getSelectionModel().getSelectedItem();
            updateCarFields(p);
        }
    }

    private void saveSuccessMsg(){
        returnMessage.setText("");
        returnMessage.setText("Cadastro efetuado com sucesso!");
    }
}
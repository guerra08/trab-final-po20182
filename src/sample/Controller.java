package sample;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
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
    @FXML
    private TextField hiddenCheck;

    public Controller() {
    }

    @FXML
    private void initialize(){
        hiddenCheck.setVisible(false);
        hiddenCheck.setText("false");
        returnMessage.setText("");
        populateCombustiveis();
        populateCarros();
        populateReports();
    }

    @FXML
    private void updateCarList(Automovel a){
        cars.put(a.getPlaca(),a);
        listaAutomoveis.getItems().add(a.getPlaca());
        listaCarrosReport.getItems().add(a.getPlaca());
    }

    @FXML
    private void saveCar(){
        Automovel a = new Automovel();
        String foto = "";
        if(placa.getText() == null || placa.getText().equals("") || modelo.getText() == null || modelo.getText().equals("") || ano.getText() == null || ano.getText().equals("")
            || capacidade.getText() == null || capacidade.getText().equals("") || odometro.getText() == null || odometro.getText().equals("") || fabricante.getText() == null || fabricante.getText().equals("")
        ){
            errorMessagge("Dados obrigatórios em branco! ");
            return;
        }

        Automovel check = cars.get(placa.getText());

        if(check != null && hiddenCheck.getText().equals("false")){
            errorMessagge("Automóvel já cadastrado! ");
            return;
        }
        try {
            a.setPlaca(placa.getText());
            a.setModelo(modelo.getText());
            a.setAno(Integer.parseInt(ano.getText()));
            a.setCapacidade(Double.parseDouble(capacidade.getText()));
            a.setOdometro(Double.parseDouble(odometro.getText()));
            a.setMarca(fabricante.getText());
        }catch (Exception e){
            errorMessagge("Dado(os) no formato inválido! ");
            return;
        }

        if(hiddenCheck.getText().equals("true")){
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
        hiddenCheck.setText("false");
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
        if(listaAutomoveis.getSelectionModel().getSelectedIndex() >= 0 && listaCombustiveis.getSelectionModel().getSelectedIndex() >= 0 && !porLitro.getText().equals("")
                && !qtdAbas.getText().equals("") && !odomAbas.getText().equals("") || dataAbas.getValue()!=null){
            try {
                Automovel byPlaca = cars.get(listaAutomoveis.getSelectionModel().getSelectedItem());
                if(byPlaca.getPlaca()!=null){
                    Abastecimento ab = new Abastecimento(byPlaca, Double.parseDouble(odomAbas.getText()), Double.parseDouble(qtdAbas.getText()), listaCombustiveis.getSelectionModel().getSelectedIndex(),Double.parseDouble(porLitro.getText()),dataAbas.getValue());
                    ab.saveToTxt();
                    saveSuccessMsg();
                }
            }catch (Exception e){
                if(e instanceof IllegalArgumentException){
                    errorMessagge( e.getMessage());
                }
                errorMessagge("Dado(os) no formato inválido! ");
            }
        }
        else{
            errorMessagge("Dados obrigatórios em branco! ");
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
        try {
            HashMap<String, List<Abastecimento>> results = Abastecimento.getAllOfSpecific(placa);
            System.out.println(Abastecimento.avgConsume(results.get(placa)));
            txArea.setText("");
            Automovel r = cars.get(placa);
            txArea.setText(r.getPlaca() + " - " + r.getMarca() + " - " + r.getModelo() + " - " + r.getAno() + " - Capacidade: "+ r.getCapacidade() + " - Odometro: " + r.getOdometro() +
                    "\n\nGastos neste mês: "+Abastecimento.costThisMonth(results.get(placa))+"\nGastos no mês anterior: "+Abastecimento.costMonthBefore(results.get(placa))+"\n");
        }catch(Exception e){
            errorMessagge("Nenhum abastecimento encontrado!");
        }
    }

    private void fuelReport(String placa){
        txArea.setText("");
        try {
            HashMap<String, List<Abastecimento>> results = Abastecimento.getAllOfSpecific(placa);
            System.out.println(Abastecimento.avgConsume(results.get(placa)));
            StringBuilder report = new StringBuilder();
            if(results != null){
                report.append("Relatório de abastecimentos de "+placa+" : \n\n ----------------\n\n");
                report.append(Abastecimento.orderAbastecimentosByMonth(results.get(placa)));
            }
            txArea.setText(report.toString());
        }catch (Exception e){
            /*returnMessage.setText("");
            returnMessage.setText(e.getMessage());*/
            System.out.println(e.getMessage());
        }
    }

    private void consumeReport(String placa){
        txArea.setText("");
        try {
            HashMap<String, List<Abastecimento>> results = Abastecimento.getAllOfSpecific(placa);

            DecimalFormat df = new DecimalFormat("#.00");

            double avgVolume = Abastecimento.volumeAvg(results.get(placa));
            double priceAvg = Abastecimento.priceAvg(results.get(placa));
            double costAvg = Abastecimento.avgCost(results.get(placa));
            double consumeAvg = Abastecimento.avgConsume(results.get(placa));

            String avgV = df.format(avgVolume);
            String pAvg = df.format(priceAvg);
            String cAvg = df.format(costAvg);
            String conAvg = df.format(consumeAvg);

            txArea.setText("------- Relatório de consumo -------\n\nMédia de volume abastecido: "+avgV+"\n"+"Média de valor pago: "+pAvg+"\n"+"Custo médio por KM: "+cAvg+"\n"+"Rendimento médio (KM/L): "+conAvg);
        }catch (Exception e){
            System.out.println(e.getMessage());
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
                    break;
                case 2:
                    consumeReport(placa);
                    break;
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
            capacidade.setText(Double.toString(c.getCapacidade()));
            odometro.setText(Double.toString(c.getOdometro()));
        }
    }

    @FXML
    private void updateCar(){
        if(listaCarrosReport.getSelectionModel().getSelectedIndex() >= 0 ){
            hiddenCheck.setText("true");
            String p = listaCarrosReport.getSelectionModel().getSelectedItem();
            updateCarFields(p);
        }
    }

    private void saveSuccessMsg(){
        returnMessage.setText("");
        returnMessage.setText("Cadastro efetuado com sucesso!");
    }

    private void errorMessagge(String t){
        returnMessage.setText("");
        returnMessage.setText(t);
    }
}
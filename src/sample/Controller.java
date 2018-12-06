package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    @FXML
    private ImageView carImage;

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

    private boolean checkFieldsIfEmpty(){
        return(placa.getText() == null || placa.getText().equals("") || modelo.getText() == null || modelo.getText().equals("") || ano.getText() == null || ano.getText().equals("")
                || capacidade.getText() == null || capacidade.getText().equals("") || odometro.getText() == null || odometro.getText().equals("") || fabricante.getText() == null || fabricante.getText().equals("")
        );
    }

    @FXML
    private void saveCar(){
        Automovel a = new Automovel();
        String foto = "";
        if(checkFieldsIfEmpty()){
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
            try {
                a.updateSelfOnTxt();
                returnToActive();
            }catch (Exception e){
                errorMessagge("Erro ao atualizar carro!");
            }
        }else {
            try {
                a.saveToTxt();
            } catch (Exception e) {
                errorMessagge("Erro ao cadastrar carro!");
            }
        }
        clearCarFields();
        this.updateCarList(a);
        saveSuccessMsg();
    }

    private void clearCarFields(){
        hiddenCheck.setText("false");
        placa.clear();
        modelo.clear();
        ano.clear();
        capacidade.clear();
        odometro.clear();
        fabricante.clear();
    }

    private void clearAbastecimentoFields(){
        porLitro.clear();
        qtdAbas.clear();
        odomAbas.clear();
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
                    clearAbastecimentoFields();
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
            errorMessagge("Nenhum veículo cadastrado.");
        }
    }

    private void carReport(String placa){
        try {
            HashMap<String, List<Abastecimento>> results = Abastecimento.getAllOfSpecific(placa);
            txArea.setText("");
            Automovel r = cars.get(placa);
            txArea.setText(r.getPlaca() + " - " + r.getMarca() + " - " + r.getModelo() + " - " + r.getAno() + " - Capacidade: "+ r.getCapacidade() + " - Odometro: " + r.getOdometro() +
                    "\n\nGastos neste mês: R$"+Abastecimento.costThisMonth(results.get(placa))+"\nGastos no mês anterior: R$"+Abastecimento.costMonthBefore(results.get(placa))+"\n");
            File arq = new File(placa+".png");
            showImage(placa,arq);
        }catch(Exception e){
            errorMessagge("Nenhum abastecimento encontrado!");
        }
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
            File arq = new File(placa+".png");
            showImage(placa,arq);
        }catch (Exception e){
            errorMessagge(e.getMessage());
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

            txArea.setText("------- Relatório de consumo -------\n\nMédia de volume abastecido: "+avgV+" litros\n"+"Média de valor pago: R$"+pAvg+"\n"+"Custo médio por KM: R$"+cAvg+"\n"+"Rendimento médio (KM/L): "+conAvg);
            File arq = new File(placa+".png");
            showImage(placa,arq);
        }catch (Exception e){

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

    @FXML
    private void addImage(){
        if(checkFieldsIfEmpty()) {return;}
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            carImage.setImage(image);
            File outputfile = new File(placa.getText() + ".png");
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (Exception ex) {

        }
    }

    private void showImage(String m, File f){
        if (f.exists() && !f.isDirectory()) {
            BufferedImage toShow = null;
            try {
                toShow = ImageIO.read(new File(m + ".png"));
            } catch (IOException e) {
                errorMessagge("Erro ao abrir a foto do carro.");
            }
            Image image = SwingFXUtils.toFXImage(toShow, null);
            carImage.setImage(image);
            carImage.setVisible(true);
        } else carImage.setVisible(false);
    }

    private void saveSuccessMsg(){
        returnMessage.setText("");
        returnMessage.setText("Cadastro efetuado com sucesso!");
    }

    private void errorMessagge(String t){
        returnMessage.setText("");
        returnMessage.setText(t);
    }

    @FXML
    private void clearSaveUp(){
        String p = placa.getText();

        clearCarFields();
        placa.setDisable(false);
        odometro.setDisable(false);

        if(!placa.equals("")){
            File arq = new File(placa+".png");
            if(arq.exists() && !arq.isDirectory()){
                arq.delete();
            }
        }

    }

    private void returnToActive(){
        placa.setDisable(false);
        odometro.setDisable(false);
    }
}
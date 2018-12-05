package sample;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Abastecimento {

    private Automovel automovel;
    private double odometroAtual;
    private double odometroAnterior;
    private double distanciaDelta;
    private double quantidade;
    private double total;
    private double porLitro;


    private String combustivel;

    private LocalDate data;

    private int combType;

    public Abastecimento(Automovel automovel, double odometro, double quantidade, int combType, double porLitro, LocalDate d) { //Construtor utilizado no cadastro de abastecimento
        this.automovel = automovel;
        if(quantidade > (this.automovel.getCapacidade())){
            throw new IllegalArgumentException("Quantidade de combustível maior do que a capacidade do veículo");
        }
        if(odometro < 0){
            throw new IllegalArgumentException("Valor do odometro menor do que o atual");
        }
        this.quantidade = quantidade;
        this.combType = combType;
        this.odometroAnterior = automovel.getOdometro();
        double novoOdometro = (automovel.getOdometro() + odometro);
        System.out.println(novoOdometro);
        this.odometroAtual = novoOdometro;
        this.distanciaDelta = novoOdometro - getOdometroAnterior();

        automovel.setOdometro(novoOdometro); //Atualizar o odometro do objeto (atulizando o HashMap simultaneamente)

        try{
            automovel.updateSelfOnTxt(); //Atualiza o valor do odometro do veículo no arquivo automoveis.txt
        }catch (Exception e){
            System.out.println(e);
        }

        setPorLitro(porLitro);

        this.data = d;
        switch(getCombType()){
            case 0:
                setCombustivel("Gasolina");
                setTotal(getQuantidade()*this.porLitro);
                break;
            case 1:
                setCombustivel("Diesel");
                setTotal(getQuantidade()*this.porLitro);
                break;
            case 2:
                setCombustivel("Álcool");
                setTotal(getQuantidade()*this.porLitro);
                break;
        }
    }

    private Abastecimento(Automovel a, double odometroAnterior, double odometroAtual, double distanciaDelta, String combustivel, double quantidade, double porLitro, double total, LocalDate data){ //Construtor utilizado apenas para o método getAllOfSpecific
        this.automovel = a;
        this.odometroAnterior = odometroAnterior;
        this.odometroAtual = odometroAtual;
        this.distanciaDelta = distanciaDelta;
        this.combustivel = combustivel;
        this.quantidade = quantidade;
        this.porLitro = porLitro;
        this.total = total;
        this.data = data;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCombType() {
        return combType;
    }

    public void setCombType(int combType) {
        this.combType = combType;
    }

    public double getPorLitro() {
        return porLitro;
    }

    public void setPorLitro(double porLitro) {
        this.porLitro = porLitro;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getOdometroAtual() {
        return odometroAtual;
    }

    public void setOdometroAtual(double odometroAtual) {
        this.odometroAtual = odometroAtual;
    }

    public double getOdometroAnterior() {
        return odometroAnterior;
    }

    public void setOdometroAnterior(double odometroAnterior) {
        this.odometroAnterior = odometroAnterior;
    }

    public double getDistanciaDelta() {
        return distanciaDelta;
    }

    public void setDistanciaDelta(double distanciaDelta) {
        this.distanciaDelta = distanciaDelta;
    }

    public String toString(){
        return getAutomovel().getPlaca() + "|" + getOdometroAnterior()+"|"+ getOdometroAtual()+"|" +getDistanciaDelta()+"|"+getCombustivel()+"|"+getQuantidade()+"|"+getPorLitro()+"|"+getTotal()+"|"+getData()+"\n";
    }

    public void saveToTxt() throws IOException { //Salvar um abastecimento no arquivo texto
        BufferedWriter bw = new BufferedWriter(new FileWriter("abastecimentos.txt",true));
        bw.write(this.toString());
        bw.close();
    }

    public static HashMap<String, List<Abastecimento>> getAllOfSpecific(String placa) throws IOException { //Retornar todos os abastecimentos de um determinado veículo
        File f = new File("abastecimentos.txt");
        if(f.exists() && !f.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader("abastecimentos.txt"));
            String linha;
            HashMap<String, List<Abastecimento>> retorno = new HashMap<>();
            List<Abastecimento> list = new ArrayList<>();
            while ((linha = br.readLine()) != null) {
                String[] parts = linha.split("\\|");
                if(parts[0].equals(placa)){
                    Abastecimento toAdd = new Abastecimento(Automovel.searchForCarOnTxt(parts[0]),Double.parseDouble(parts[1]),Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),parts[4],Double.parseDouble(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]),LocalDate.parse(parts[8]));
                    list.add(toAdd);
                }
            }
            retorno.put(placa,list);
            br.close();
            return retorno;
        }
        return null;
    }

    public static String orderAbastecimentosByMonth(List<Abastecimento> lista){ //Ordenar uma lista de abastecimentos de um determinado veículo por mês

        StringBuilder jan = new StringBuilder(); jan.append("Janeiro: \n\n");
        StringBuilder fev = new StringBuilder(); fev.append("Fevereiro: \n\n");
        StringBuilder mar = new StringBuilder(); mar.append("Março: \n\n");
        StringBuilder abr = new StringBuilder(); abr.append("Abril: \n\n");
        StringBuilder mai = new StringBuilder(); mai.append("Maio: \n\n");
        StringBuilder jun = new StringBuilder(); jun.append("Junho: \n\n");
        StringBuilder jul = new StringBuilder(); jul.append("Julho: \n\n");
        StringBuilder ago = new StringBuilder(); ago.append("Agosto: \n\n");
        StringBuilder set = new StringBuilder(); set.append("Setempo: \n\n");
        StringBuilder out = new StringBuilder(); out.append("Outubro: \n\n");
        StringBuilder nov = new StringBuilder(); nov.append("Novembro: \n\n");
        StringBuilder dez = new StringBuilder(); dez.append("Dezembro: \n\n");

        lista.forEach(e->{
            switch(e.getData().getMonthValue()){
                case 1:
                    jan.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 2:
                    fev.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 3:
                    mar.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 4:
                    abr.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 5:
                    mai.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 6:
                    jun.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 7:
                    jul.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 8:
                    ago.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 9:
                    set.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 10:
                    out.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 11:
                    nov.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
                case 12:
                    dez.append(e.getData() + " - " +e.getCombustivel() + " - " + e.getQuantidade() + " litros - R$" + e.getPorLitro() + " por litro - Total: " + e.getTotal() + " R$ \n");
                    break;
            }
        });

        String retorno = jan.toString() + fev.toString() + mar.toString() + abr.toString() + mai.toString() + jun.toString() + jul.toString() + ago.toString() + set.toString() + out.toString() + nov.toString() + dez.toString();

    return retorno;
    }

    public static double volumeAvg(List<Abastecimento> l){
        int size = l.size();
        double sumVol;
        sumVol = l.stream().mapToDouble(e -> e.getQuantidade()).sum();
        return(sumVol/size);
    }

    public static double priceAvg(List<Abastecimento> l){
        int size = l.size();
        double sumPrice;
        sumPrice = l.stream().mapToDouble(e -> e.getTotal()).sum();
        return(sumPrice/size);
    }

    public static double avgCost(List<Abastecimento> l){
        int size = l.size();
        double sum;
        sum = l.stream().mapToDouble(e -> (e.getTotal()/e.getDistanciaDelta())).sum();
        return sum/size;
    }

    public static double avgConsume(List<Abastecimento> l){
        int size = l.size();
        double sum;
        sum = l.stream().mapToDouble(e -> (e.getDistanciaDelta()/e.getQuantidade())).sum();
        return sum/size;
    }

    public static double costThisMonth(List<Abastecimento> l){
        double sum;
        sum = l.stream().filter(e -> e.getData().getMonthValue() == LocalDateTime.now().getMonthValue() && e.getData().getYear() == LocalDateTime.now().getYear()).mapToDouble(e-> e.getTotal()).sum();
        return sum;
    }

    public static double costMonthBefore(List<Abastecimento> l){
        double sum;
        sum = l.stream().filter(e -> e.getData().getMonthValue() == LocalDateTime.now().getMonthValue()-1 && e.getData().getYear() == LocalDateTime.now().getYear()).mapToDouble(e-> e.getTotal()).sum();
        return sum;
    }

}
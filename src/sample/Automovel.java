package sample;

import java.io.*;

public class Automovel {

    //private
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private String capacidade;
    private String odometro;

    public Automovel(String placa, String modelo, String marca, int ano, String capacidade, String odometro) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.capacidade = capacidade;
        this.odometro = odometro;
    }

    public Automovel(){

    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(String capacidade) {
        this.capacidade = capacidade;
    }

    public String getOdometro() {
        return odometro;
    }

    public void setOdometro(String odometro) {
        this.odometro = odometro;
    }

    public String toString(){
        return getPlaca() + "|" + getModelo()+"|"+getMarca()+"|"+getAno()+"|"+getCapacidade()+"|"+getOdometro()+"\n";
    }

    public void saveToTxt() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("automoveis.txt",true));
        bw.write(this.toString());
        bw.close();
    }

    public void readTxt() throws IOException{
        File f = new File("automoveis.txt");;
        if(f.exists() && !f.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader("automoveis.txt"));
        }
    }

}

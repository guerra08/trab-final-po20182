package sample;

import java.io.*;
import java.util.HashMap;

public class Automovel {

    //private
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private String capacidade;
    private String odometro;

    private String foto;

    public Automovel(String placa, String modelo, String marca, int ano, String capacidade, String odometro, String foto) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.capacidade = capacidade;
        this.odometro = odometro;
        this.foto = foto;
    }

    public Automovel(String placa, String modelo, String marca, int ano, String capacidade, String odometro) { //Criando automovel sem foto
        this(placa, modelo, marca, ano, capacidade, odometro, "");

    }

    public Automovel(){};

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public HashMap<String, Automovel> readTxt() throws IOException{
        File f = new File("automoveis.txt");
        if(f.exists() && !f.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader("automoveis.txt"));
            String linha;
            HashMap<String, Automovel> retorno = new HashMap<>();
            while ((linha = br.readLine()) != null) {
                String[] parts = linha.split("\\|");
                if(parts.length == 6){ //Carro sem foto
                    Automovel cur = new Automovel(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4], parts[5]);
                    retorno.put(cur.getPlaca(),cur);
                }else{
                    Automovel cur = new Automovel(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4], parts[5],parts[6]);
                    retorno.put(cur.getPlaca(),cur);
                }
            }
            return retorno;
        }else{
            return null;
        }
    }

    public Automovel searchForCar(String placa) throws IOException{
        File f = new File("automoveis.txt");
        if(f.exists() && !f.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader("automoveis.txt"));
            String linha;
            Automovel ret = new Automovel();
            while ((linha = br.readLine()) != null) {
                String[] parts = linha.split("\\|");
                if(parts[0].equals(placa)) {
                    if (parts.length == 6) { //Carro sem foto
                        ret = new Automovel(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5]);
                    }else {
                        ret = new Automovel(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6]);
                    }
                }
            }
            return ret;
        }else{
            return null;
        }
    }

}

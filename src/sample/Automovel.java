package sample;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Automovel {

    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private String capacidade;
    private double odometro;
    private String foto;

    public Automovel(String placa, String modelo, String marca, int ano, String capacidade, double odometro, String foto) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.capacidade = capacidade;
        this.odometro = odometro;
        this.foto = foto;
    }

    public Automovel(String placa, String modelo, String marca, int ano, String capacidade, double odometro) { //Criando automovel sem foto
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

    public double getOdometro() {
        return odometro;
    }

    public void setOdometro(double odometro) {
        this.odometro = odometro;
    }

    public String toString(){
        return getPlaca() + "|" + getModelo()+"|"+getMarca()+"|"+getAno()+"|"+getCapacidade()+"|"+getOdometro()+"\n";
    }

    public String toStringOnUpdate(){
        return getPlaca() + "|" + getModelo()+"|"+getMarca()+"|"+getAno()+"|"+getCapacidade()+"|"+getOdometro();
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
                    Automovel cur = new Automovel(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4], Double.parseDouble(parts[5]));
                    retorno.put(cur.getPlaca(),cur);
                }else{
                    Automovel cur = new Automovel(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4], Double.parseDouble(parts[5]),parts[6]);
                    retorno.put(cur.getPlaca(),cur);
                }
            }
            br.close();
            return retorno;
        }else{
            return null;
        }
    }

    public static Automovel searchForCarOnTxt(String placa) throws IOException{
        File f = new File("automoveis.txt");
        if(f.exists() && !f.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader("automoveis.txt"));
            String linha;
            Automovel ret = new Automovel();
            while ((linha = br.readLine()) != null) {
                String[] parts = linha.split("\\|");
                if(parts[0].equals(placa)) {
                    if (parts.length == 6) { //Carro sem foto
                        ret = new Automovel(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], Double.parseDouble(parts[5]));
                    }else {
                        ret = new Automovel(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], Double.parseDouble(parts[5]), parts[6]);
                    }
                }
            }
            br.close();
            return ret;
        }else{
            throw new FileNotFoundException("Arquivo de dados não encontrado");
        }
    }

    public void updateSelfOnTxt() throws IOException {

        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("automoveis.txt"), StandardCharsets.UTF_8));

        for (int i = 0; i < fileContent.size(); i++) {
            String[] parts = fileContent.get(i).split("\\|");
            if(parts[0].equals(this.getPlaca())){
                fileContent.set(i, this.toStringOnUpdate());
            }
        }

        Files.write(Paths.get("automoveis.txt"), fileContent, StandardCharsets.UTF_8);
    }

}
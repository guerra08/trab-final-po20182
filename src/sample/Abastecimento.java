package sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Abastecimento {

    private Automovel automovel;
    private double odometro;
    private double quantidade;
    private double total;
    private double porLitro;

    //Valores gasolina

    private final double litroGaso = 4.40;
    private final double litroDiesel = 3.30;
    private final double precoAlc = 2.50;

    public Abastecimento(Automovel automovel, double odometro, double quantidade, double total, double porLitro) {
        this.automovel = automovel;
        this.odometro = odometro;
        this.quantidade = quantidade;
        this.total = total;
        this.porLitro = porLitro;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public double getOdometro() {
        return odometro;
    }

    public void setOdometro(double odometro) {
        this.odometro = odometro;
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

    public double getPorLitro() {
        return porLitro;
    }

    public void setPorLitro(double porLitro) {
        this.porLitro = porLitro;
    }

    public String toString(){
        return getAutomovel().getPlaca() + "|" + getOdometro()+"|"+getQuantidade()+"|"+getPorLitro()+"|"+getTotal()+"\n";
    }

    public void saveToTxt() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("abastecimentos.txt",true));
        bw.write(this.toString());
        bw.close();
    }

}

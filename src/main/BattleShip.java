package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

public class BattleShip {
    private static final int wielkoscPlanszy = 4;
    private static final String wspolrzedneStatku = "1B 2C,2D 4D";
    private static final String wspolrzedneTrafien = "2B 2D 3D 4D 4A";

    public static void main(String[] args) {
        BattleShip result = new BattleShip();
        System.out.println(result.solution(wielkoscPlanszy, wspolrzedneStatku, wspolrzedneTrafien));
    }

    public String solution(int N, String S, String T) {
        List<Statek> punktyNaKtorychJestStatek = statki(S);

        List<Trafienia> listaPunktowTrafien = listaTrafien(T);

        Integer liczbaTrafien = 0;
        Integer liczbaZatopien = 0;
        for (Statek statek : punktyNaKtorychJestStatek){
            statek.trafieniaAnaliza(listaPunktowTrafien);
            if (statek.toniecie()){
                liczbaZatopien++;
            } else if (statek.zostalTrafiony()) {
                liczbaTrafien++;
            }
        }
        return liczbaZatopien + "," + liczbaTrafien;
    }

    private List<Statek> statki(String S) {
        String [] tablicaWspolrzednychStatku = S.split(",");
        List<Statek> punktyNaKtorychJestStatek = new ArrayList<>();
        for (String wspolrzednePuntkow : tablicaWspolrzednychStatku) {
            punktyNaKtorychJestStatek.add(new Statek(wspolrzednePuntkow));
        }
        return punktyNaKtorychJestStatek;
    }
    private List<Trafienia> listaTrafien(String T) {
        String [] tablicaWspolrzednych = T.split(" ");
        List<Trafienia> punktyNaKtorychZanotowanoTrafienia = new ArrayList<>();
        for (String wspolrzedne : tablicaWspolrzednych) {
            punktyNaKtorychZanotowanoTrafienia.add(new Trafienia(wspolrzedne));
        }
        return punktyNaKtorychZanotowanoTrafienia;
    }
}

class Punkt {
    private Integer x;
    private Integer y;



    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    public Punkt(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Punkt(String coordinate) {
        char [] chars = coordinate.toCharArray();
        if (chars.length > 2) {
            x = Integer.valueOf(coordinate.substring(0, coordinate.length() - 1)) -1;
            y = Character.getNumericValue(coordinate.substring(coordinate.length() - 1).charAt(0)) - 10;
        } else {
            x = Character.getNumericValue(chars[0]) - 1;
            y = Character.getNumericValue(chars[1]) - 10;
        }
    }



    @Override
    public int hashCode() {
        int result = 1;
        result = 5 * result + x.hashCode();
        result = 5 * result + y.hashCode();
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        Punkt jakisPunkt = (Punkt) obj;
        return this.x == jakisPunkt.x && this.y == jakisPunkt.y;
    }

}

 class Statek {
    private Set<Punkt> wspolrzedne;
    private Punkt start;
    private Punkt koniec;
    private boolean czySieTopi;
    private boolean zostalTrafiony;

    public Statek(String wspolrzednePunktu) {
        String[] punkty = wspolrzednePunktu.split(" ");
        this.start = new Punkt(punkty[0]);
        this.koniec = new Punkt(punkty[1]);
        this.wspolrzedne = new HashSet<>();
    }

    public Set<Punkt> wszystkieWspolrzednePunktowStatku()
    {
        if (start.equals(koniec))
        {

            this.wspolrzedne.add(start);
        }
        else if (start.getX() != koniec.getX() && start.getY() == koniec.getY())
        {
            for (int i = start.getX(); i <= koniec.getX(); i++)
            {
                this.wspolrzedne.add(new Punkt(i, start.getY()));
            }
        }
        else if (start.getX() == koniec.getX() && start.getY() != koniec.getY())
        {
            for (int i = start.getY(); i <= koniec.getY(); i++)
            {
                this.wspolrzedne.add(new Punkt(start.getX(), i));
            }
        }

        else
        {
            for (int x = start.getX(); x <= koniec.getX(); x++)
            {
                for (int y = start.getY(); y <= koniec.getY(); y++)
                {
                    this.wspolrzedne.add(new Punkt(x, y));
                }
            }
        }

        return this.wspolrzedne;
    }
    public void trafieniaAnaliza(List<Trafienia> punktyTrafien) {
        int liczbaTrafien = 0;
        this.wszystkieWspolrzednePunktowStatku();
        for (Trafienia trafienia : punktyTrafien) {
            if (this.wspolrzedne.contains(trafienia.getPunktTrafienia())) {
                liczbaTrafien++;
            }
        }
        this.czySieTopi = liczbaTrafien == this.wspolrzedne.size();
        if (!this.czySieTopi) {
            this.zostalTrafiony = liczbaTrafien > 0;
        }
    }
    public boolean toniecie() {
        return czySieTopi;
    }


    public boolean zostalTrafiony() {
        return zostalTrafiony;
    }}

 class Trafienia {
    private Punkt punktTrafienia;

    public Trafienia(String coordinate) {
        this.punktTrafienia = new Punkt(coordinate);
    }

    public Punkt getPunktTrafienia() {
        return this.punktTrafienia;
    }

    @Override
    public boolean equals(Object obj) {
        Trafienia otherObj = (Trafienia) obj;
        try {
            return otherObj.getPunktTrafienia().equals(this.getPunktTrafienia());
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }}
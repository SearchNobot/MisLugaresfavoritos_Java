package com.example.mislugares.datos;

import com.example.mislugares.modelo.Lugar;
import com.example.mislugares.modelo.TipoLugar;

import java.util.ArrayList;
import java.util.List;

public class LugaresLista implements RepositorioLugares {
    protected List<Lugar> listaLugares ;

    public LugaresLista() {

        listaLugares = new ArrayList<>();

    }

    public Lugar elemento(int id) {
        return listaLugares.get(id);
    }

    public void añade(Lugar lugar) {
        listaLugares.add(lugar);
    }

    public int nuevo() {
        Lugar lugar = new Lugar();
        listaLugares.add(lugar);
        return listaLugares.size()-1;
    }

    public void borrar(int id) {
        listaLugares.remove(id);
    }

    public int tamaño() {
        return listaLugares.size();
    }
    public void actualiza(int id, Lugar lugar) {
        listaLugares.set(id, lugar);
    }




}

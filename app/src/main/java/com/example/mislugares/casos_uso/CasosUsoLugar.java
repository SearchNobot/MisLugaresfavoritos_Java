package com.example.mislugares.casos_uso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mislugares.Aplicacion;
import com.example.mislugares.R;
import com.example.mislugares.datos.LugaresBD;
import com.example.mislugares.modelo.GeoPunto;
import com.example.mislugares.modelo.Lugar;
import com.example.mislugares.presentacion.AdaptadorLugaresBD;
import com.example.mislugares.presentacion.EdicionLugarActivity;
import com.example.mislugares.presentacion.VistaLugarActivity;
import com.example.mislugares.presentacion.VistaLugarFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class CasosUsoLugar {

   protected FragmentActivity actividad;
   protected Fragment fragment;
   protected LugaresBD lugares;
   protected AdaptadorLugaresBD adaptador;

   public CasosUsoLugar(FragmentActivity actividad, Fragment fragment,
                        LugaresBD lugares,
                        AdaptadorLugaresBD adaptador) {
      this.actividad = actividad;
      this.fragment = fragment;
      this.lugares = lugares;
      this.adaptador = adaptador;
   }

   // OPERACIONES BÁSICAS

   public void actualizaPosLugar(int pos, Lugar lugar) {
      int id = adaptador.idPosicion(pos);
      guardar(id, lugar);
   }

   public void guardar(int id, Lugar nuevoLugar) {
      lugares.actualiza(id, nuevoLugar);
      adaptador.setCursor(lugares.extraeCursor());
      adaptador.notifyDataSetChanged();
   }

   public void mostrar(int pos) {
      VistaLugarFragment fragmentVista = obtenerFragmentVista();
      if (fragmentVista != null) {
         fragmentVista.pos = pos;
         fragmentVista._id = adaptador.idPosicion(pos);
         fragmentVista.actualizaVistas();
      } else {
         Intent i = new Intent(actividad, VistaLugarActivity.class);
         i.putExtra("pos", pos);
         actividad.startActivity(i);
      }
   }

   public VistaLugarFragment obtenerFragmentVista() {
      FragmentManager manejador = actividad.getSupportFragmentManager();
      return (VistaLugarFragment)
              manejador.findFragmentById(R.id.vista_lugar_fragment);
   }

   public void editar(int pos, int codidoSolicitud) {
      Intent i = new Intent(actividad, EdicionLugarActivity.class);
      i.putExtra("pos", pos);
      if (fragment != null)
           fragment.startActivityForResult(i, codidoSolicitud);
      else actividad.startActivityForResult(i, codidoSolicitud);

   }

   public void borrar(int id) {
      lugares.borrar(id);
      adaptador.setCursor(lugares.extraeCursor());
      adaptador.notifyDataSetChanged();

      FragmentManager manejador = actividad.getSupportFragmentManager();
      if (manejador.findFragmentById(R.id.selector_fragment) == null) {
         actividad.finish();
      } else {
         mostrar(0);
      }
   }

   public void nuevo() {
      int id = lugares.nuevo();
      GeoPunto posicion = ((Aplicacion) actividad.getApplication())
              .posicionActual;
      if (!posicion.equals(GeoPunto.SIN_POSICION)) {
         Lugar lugar = lugares.elemento(id);
         lugar.setPosicion(posicion);
         lugares.actualiza(id, lugar);
      }
      Intent i = new Intent(actividad, EdicionLugarActivity.class);
      i.putExtra("_id", id);
      actividad.startActivity(i);
   }


   // INTENCIONES
   public void compartir(Lugar lugar) {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("text/plain");
      i.putExtra(Intent.EXTRA_TEXT,
              lugar.getNombre() + " - " + lugar.getUrl());
      actividad.startActivity(i);
   }

   public void llamarTelefono(Lugar lugar) {
      actividad.startActivity(new Intent(Intent.ACTION_DIAL,
              Uri.parse("tel:" + lugar.getTelefono())));
   }

   public void verPgWeb(Lugar lugar) {
      actividad.startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse(lugar.getUrl())));
   }

   public final void verMapa(Lugar lugar) {
      double lat = lugar.getPosicion().getLatitud();
      double lon = lugar.getPosicion().getLongitud();
      Uri uri = lugar.getPosicion() != GeoPunto.SIN_POSICION
              ? Uri.parse("geo:" + lat + ',' + lon)
              : Uri.parse("geo:0,0?q=" + lugar.getDireccion());
      actividad.startActivity(new Intent("android.intent.action.VIEW", uri));
   }

}
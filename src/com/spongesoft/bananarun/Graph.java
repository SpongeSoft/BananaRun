package com.spongesoft.bananarun;


import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;
import com.spongesoft.dietapp.R;
 
import java.text.DecimalFormat;
import java.util.Arrays;
 
public class Graph extends Fragment
{
 
    private XYPlot mySimpleXYPlot;
    
    public Graph() {
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
    	super.onCreate(savedInstanceState);
		final View HomeView = inflater.inflate(R.layout.graph, container, false);
    	
    
 
        // Inicializamos el objeto XYPlot búscandolo desde el layout:
        mySimpleXYPlot = (XYPlot) HomeView.findViewById(R.id.mySimpleXYPlot);
        mySimpleXYPlot.setRangeLabel("Velocidad"); 
        mySimpleXYPlot.setDomainLabel("Kilómetros");
        mySimpleXYPlot.setTitle("Progresión");
 
        // Creamos dos arrays de prueba. En el caso real debemos reemplazar
        // estos datos por los que realmente queremos mostrar
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
 
        // Añadimos Línea Número UNO:
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),  // Array de datos
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Sólo valores verticales
                "Series1"); // Nombre de la primera serie
 
        // Repetimos para la segunda serie
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers
), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
 
        // Modificamos los colores de la primera serie
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // Color de la línea
                Color.rgb(0, 100, 0),                   // Color del punto
                Color.rgb(150, 190, 150)
                
        		
        		
        		);              // Relleno
 
        // Una vez definida la serie (datos y estilo), la añadimos al panel
        mySimpleXYPlot.addSeries(series1, series1Format);
 
        LineAndPointFormatter series2Format = new LineAndPointFormatter(
        		Color.rgb(0, 0, 200),                   // Color de la línea
        		Color.rgb(0, 0, 100),                   // Color del punto
        		Color.rgb(150, 150, 190)
        		);              // Relleno
        // Repetimos para la segunda serie
        
        mySimpleXYPlot.addSeries(series2, series2Format);
        
        return HomeView;
    }
}
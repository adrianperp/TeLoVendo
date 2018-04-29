package com.example.adrian.telovendo.utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FechasUtils {

	public static String getFechaActual() {
		
		Date curDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(curDate);
		return fechaActual;
	}
	
	public static String getHoraActual() {
		
		Date curHour = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String horaActual = dateFormat.format(curHour);
		return horaActual;
	}
}

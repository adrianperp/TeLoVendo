package com.example.adrian.telovendo.utilidades;

import java.text.ParseException;
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

	// Devuelve la diferencia de fechas
	public static String getDateDif(Date d1, Date d2) {
            String dif = null;
            try {
                long result = d1.getTime() - d2.getTime();
                if (result < 60000)
                    dif = "Ahora";
                else if (result < 3600000)
                    dif = String.valueOf(result / 60000) + "m";
                else if (result < 86400000)
                    dif = String.valueOf(result / 3600000) + "h";
                else
                    dif = String.valueOf(result / 86400000) + "d";
                return dif;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Devuelve un date a partir de fecha y hora
        public static Date getDate (String fecha, String hora) throws ParseException {
            String dateString = fecha + " " + hora;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            return dateFormat.parse(dateString);
    }
}

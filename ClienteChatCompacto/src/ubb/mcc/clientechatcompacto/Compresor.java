package ubb.mcc.clientechatcompacto;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Compresor {

	 public static String codificar(Hashtable<String, String> codificador, String mensaje) {

	        //inicio codificar mensaje
	        StringTokenizer st = new StringTokenizer(mensaje.trim(), " ");
	        String mensajeCodificado = "";
	        while (st.hasMoreTokens()) {

	            String palabra = st.nextToken().trim();
	            mensajeCodificado += codificador.get(palabra);

	            //System.out.println(palabra + " "+ );
	        }
	        //fin codificar mensaje

	        return mensajeCodificado;
	    }

	    public static String decodificar(Hashtable<String, String> decodificador, String mensajeCodificado) {

	        String mensaje = "";
	        String codigoTemp = "";

	        for (int i = 0; i < mensajeCodificado.length(); i++) {
	            if (mensajeCodificado.charAt(i) < 128) {
	                codigoTemp += mensajeCodificado.charAt(i);
	            } else {
	                codigoTemp += mensajeCodificado.charAt(i);
	                mensaje += decodificador.get(codigoTemp)+" ";
	                codigoTemp = "";
	            }
	        }
	        return mensaje;
	    }

	    public static Hashtable<String, String> creaCodificador(LinkedList<String> palabras) {
	        // TODO Auto-generated method stub

	        Hashtable<String, String> diccionario = new Hashtable<String, String>();

	        for (int i = 0; i < palabras.size(); i++) {
	            int aux = i / 128;
	            //System.out.printf("%d = ", i);
	            String clave = "";
	            if (aux > 0) { //tiene 2 bloques
	                //System.out.printf(String.format("%8s:", Integer.toBinaryString(aux)).replaceAll(" ", "0"));
	                clave += (char) aux;
	            }

	            aux = (i % 128) + 128;
	            clave += (char) aux;
	            diccionario.put(palabras.get(i), clave);

	            //System.out.printf(String.format("%8s\n", Integer.toBinaryString(aux)).replaceAll(" ", "0"));
	        }
	        return diccionario;

	    }

	    public static Hashtable<String, String> creaDecodificador(LinkedList<String> palabras) {
	        // TODO Auto-generated method stub
	        Hashtable<String, String> diccionario = new Hashtable<String, String>();

	        for (int i = 0; i < palabras.size(); i++) {
	            int aux = i / 128;
	            //System.out.printf("%d = ", i);
	            String clave = "";
	            if (aux > 0) { //tiene 2 bloques
	                //System.out.printf(String.format("%8s:", Integer.toBinaryString(aux)).replaceAll(" ", "0"));
	                clave += (char) aux;
	            }

	            aux = (i % 128) + 128;
	            clave += (char) aux;
	            diccionario.put(clave, palabras.get(i));

	            //System.out.printf(String.format("%8s\n", Integer.toBinaryString(aux)).replaceAll(" ", "0"));
	        }
	        return diccionario;
	    }
}

package ubb.mcc.servidorchatcompacto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.LinkedList;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Chat extends Activity {
	
	private ServerSocket serverSocket;
	private TextView visor;
	private BufferedReader entrada;
	private PrintWriter salida;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		visor = (TextView) findViewById(R.id.textView1);
		
		WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        
        visor.setText("Servidor\nIP:" + Formatter.formatIpAddress(ip) + "\npuerto: " + getIntent().getStringExtra("puerto") + "\n");
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.
		           Builder().permitNetwork().build());
        
        servidor server = new servidor();
        
        server.start();
        
        
        
        //iniciaServidor();
		
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class servidor extends Thread{
		
		public String mensaje;
		public void run(){
			int puerto = Integer.parseInt(getIntent().getStringExtra("puerto"));
			
			try {
				File archivo = null;
		        FileReader fr = null;
		        BufferedReader br = null;
		        archivo = new File("palabras.txt");
	            fr = new FileReader(archivo);
	            br = new BufferedReader(new InputStreamReader(getAssets().open("palabras.txt")));
		     // Lectura del fichero
	            String linea;
	            int i = 0;
	            LinkedList<String> palabras = new LinkedList<String>();

	            while ((linea = br.readLine()) != null) {
	                //System.out.println(linea);
	                i++;
	                palabras.add(linea);
	            }

	            Hashtable<String, String> codificador = Compresor.creaCodificador(palabras);
	            Hashtable<String, String> decodificador = Compresor.creaDecodificador(palabras);
		        
				serverSocket = new ServerSocket(puerto);
				
				Socket socket = serverSocket.accept();
				
				entrada = new BufferedReader( 
						new InputStreamReader(socket.getInputStream()));

				salida = new PrintWriter(
						new OutputStreamWriter(socket.getOutputStream()),true);
				
				while( true ){		
					
					mensaje = entrada.readLine();
					
					//Log.e("mensaje", mensaje);
					
					mensaje = Compresor.decodificar(decodificador, mensaje);
					
					runOnUiThread(new Runnable() {

					     @Override
					     public void run() {
					    	 try {
					    		 visor.append("cliente: " + mensaje+"\n");
								//visor.append("cliente: \n");
					    		 Log.e("visor",  visor.getText().toString());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					     }
					    });
					
					//mensaje = "recibido";
					
					String mensajeRespuesta = "recibido";
					
					mensajeRespuesta = Compresor.codificar(codificador, mensaje);
					
					salida.println(mensajeRespuesta+"\n");
					
//					runOnUiThread(new Runnable() {
//
//					     @Override
//					     public void run() {
//					    	 try {
//					    		 visor.append("servidor: " + mensaje+"\n");
//								//visor.append("cliente: \n");
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//					     }
//					    });
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
}



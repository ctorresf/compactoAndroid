package ubb.mcc.clientechatcompacto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends Activity {

	//atributos
	private TextView visor;
	private EditText mensaje;
	private Button botonEnviar;
	
	private Socket socket;
	private String ip;
	private int puerto;
	private BufferedReader entrada;
	private PrintWriter salida;
	
	private Hashtable<String, String> codificador;
	private Hashtable<String, String> decodificador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		visor = (TextView)findViewById(R.id.textView1);
		mensaje = (EditText)findViewById(R.id.editText1);
		botonEnviar = (Button)findViewById(R.id.button1);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.
		           Builder().permitNetwork().build());
		
		ip = getIntent().getStringExtra("ip");
		puerto = Integer.parseInt(getIntent().getStringExtra("puerto"));
		
		visor.setText(visor.getText() + "\nConectado a " + ip + "\npuerto: " + puerto);
		
		try {
			socket = new Socket(ip,puerto);
			
			entrada = new BufferedReader( 
							new InputStreamReader(socket.getInputStream()));

			salida = new PrintWriter(
							new OutputStreamWriter(socket.getOutputStream()),true);
			
			File archivo = null;
	        FileReader fr = null;
	        BufferedReader br = null;
			
	        //archivo = getResources().getAssets().open("palabras.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(new InputStreamReader(getAssets().open("palabras.txt")));
	     // Lectura del fichero
            String linea;
            int i = 0;
            LinkedList<String> palabras = new LinkedList<String>();
            Log.e("D:","D:");
            while ((linea = br.readLine()) != null) {
                //System.out.println(linea);
                i++;
                Log.e("linea",linea);
                palabras.add(linea);
            }

            codificador = Compresor.creaCodificador(palabras);
            decodificador = Compresor.creaDecodificador(palabras);
			
			if(entrada == null){
				Log.e("D:","D:");
				//System.exit(-1);
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		botonEnviar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				visor.append( "\nYo: " + mensaje.getText());
				String mensajeCodificado = Compresor.codificar(codificador, mensaje.getText().toString());
				
				salida.println(mensajeCodificado);
				
				mensaje.setText("");
				
				try {
					String aux =entrada.readLine(); 
					
					String mensajeDecodificado = Compresor.decodificar(decodificador, aux);
					
					visor.append("\nServidor: "+mensajeDecodificado);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("error", e.getMessage());
					//e.printStackTrace();
				}
				
			}
		});
		
		
//		LectorServer lector = new LectorServer(ip, puerto);
//		
//		lector.start();
		
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
	
	class LectorServer extends Thread{
		
		Socket sk;
		BufferedReader en;
		
		public LectorServer(String ip, int puerto){
			try {
				sk = new Socket(ip,puerto);
				
				en = new BufferedReader( 
						new InputStreamReader(sk.getInputStream()));
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void run(){

			while(true){
				
				try {
					
					
					String linea=null;
					
					//if(en != null){
						linea = en.readLine();
					//}else{
						//Log.e("error", "entrada es nula");
					//}
					
					
					if(linea != null){
						visor.append(linea);
					}
					
					
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("error en hilo", e.getMessage());
					//e.printStackTrace();
				}
			}
			
		}
	}
	
}

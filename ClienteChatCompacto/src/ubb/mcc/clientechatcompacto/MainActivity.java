package ubb.mcc.clientechatcompacto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

	//atributos
	private EditText ip;
	private EditText puerto;
	private Button botonConectar;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ip = (EditText)findViewById(R.id.editTextIP);
        puerto = (EditText)findViewById(R.id.editTextPort);
        botonConectar = (Button)findViewById(R.id.button1);
        
        botonConectar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent( getApplicationContext(), ChatActivity.class);
				
				//envio parametros a la siguiente actividad
				i.putExtra("ip", ip.getText().toString());
				i.putExtra("puerto", puerto.getText().toString());
				
				startActivity(i);
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}

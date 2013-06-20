package de.shop.ui.artikel;

import static de.shop.util.Constants.ARTIKELS_KEY;
import static de.shop.util.Constants.ARTIKEL_KEY;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.service.ArtikelService;
import de.shop.service.ArtikelService.ArtikelServiceBinder;


public class ArtikelsListe extends Activity {
	private ArtikelServiceBinder artikelServiceBinder;
	
	// ServiceConnection ist ein Interface: anonyme Klasse verwenden, um ein Objekt davon zu erzeugen
	private ServiceConnection artikelServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			artikelServiceBinder = (ArtikelServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			artikelServiceBinder = null;
		}
	};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO anpassen
        setContentView(R.layout.kunden_liste);
        
        final Fragment details = new ArtikelDetails();
		final Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	@SuppressWarnings("unchecked")
			final List<Artikel> artikels = (List<Artikel>) extras.get(ARTIKELS_KEY);
        	if (artikels != null && !artikels.isEmpty()) {
        		final Bundle args = new Bundle(1);
        		args.putSerializable(ARTIKEL_KEY, artikels.get(0));
        		details.setArguments(args);
        	}
        }
		
        getFragmentManager().beginTransaction()
                            .add(R.id.details, details)
                            .commit();
        
//      Entfaellt seit API 16 durch <activity android:parentActivityName="..."> in AndroidManifest.xml
//		final ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
    }

  
//    Entfaellt seit API 16 durch <activity android:parentActivityName="..."> in AndroidManifest.xml
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // App Icon in der Actionbar wurde angeklickt: Main aufrufen
//                final Intent intent = new Intent(this, Main.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;  // Ereignis ist verbraucht: nicht weiterreichen
//                
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
	
    @Override
	public void onStart() {
		super.onStart();
		
		Intent intent = new Intent(this, ArtikelService.class);
		bindService(intent, artikelServiceConnection, Context.BIND_AUTO_CREATE);
		
    }
    
	@Override
	public void onStop() {
		super.onStop();
		
		unbindService(artikelServiceConnection);
	}

	public ArtikelServiceBinder getKundeServiceBinder() {
		return artikelServiceBinder;
	}

}

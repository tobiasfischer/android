package de.shop.ui.main;

import static android.widget.Toast.LENGTH_LONG;
import static de.shop.ui.main.Prefs.mock;
import static de.shop.util.Constants.KUNDE_KEY;
import static de.shop.util.Constants.ARTIKEL_KEY;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.shop.R;
import de.shop.service.ArtikelService;
import de.shop.service.ArtikelService.ArtikelServiceBinder;
import de.shop.data.Kunde;
import de.shop.data.Artikel;
import de.shop.service.BestellungService;
import de.shop.service.KategorieService;
import de.shop.service.KategorieService.KategorieServiceBinder;
import de.shop.service.KundeService;
import de.shop.service.BestellungService.BestellungServiceBinder;
import de.shop.service.KundeService.KundeServiceBinder;
import de.shop.ui.kunde.KundeDetails;
import de.shop.ui.artikel.ArtikelDetails;

public class Main extends Activity {
	private static final String LOG_TAG = Main.class.getSimpleName();
	
	private KundeServiceBinder kundeServiceBinder;
	private BestellungServiceBinder bestellungServiceBinder;
	private ArtikelServiceBinder artikelServiceBinder;
	private KategorieServiceBinder kategorieServiceBinder;
	
	// ServiceConnection ist ein Interface: anonyme Klasse verwenden, um ein Objekt davon zu erzeugen
	private ServiceConnection kundeServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer KundeServiceBinder");
			kundeServiceBinder = (KundeServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			kundeServiceBinder = null;
		}
	};
	
	private ServiceConnection bestellungServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer BestellungServiceBinder");
			bestellungServiceBinder = (BestellungServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			bestellungServiceBinder = null;
		}
	};
	
	private ServiceConnection artikelServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer ArtikelServiceBinder");
			artikelServiceBinder = (ArtikelServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			artikelServiceBinder = null;
		}
	};
	
	private ServiceConnection kategorieServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer KategorieServiceBinder");
			kategorieServiceBinder = (KategorieServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			kategorieServiceBinder = null;
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Gibt es Suchergebnisse durch SearchView in der ActionBar, z.B. Kunde ?
        
        Fragment detailsFragment = null;
        final Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	// Keine Suchergebnisse o.ae. vorhanden
        	
        	detailsFragment = new Startseite();
        	
          // Preferences laden
          Prefs.init(this);
        }
        //TODO Abgrenzung f�r Artikel?
        else {
	        final Kunde kunde = (Kunde) extras.get(KUNDE_KEY);
	        final Artikel artikel = (Artikel) extras.get(ARTIKEL_KEY);
	        if (kunde != null) {
	        	Log.d(LOG_TAG, kunde.toString());
	        	
	    		final Bundle args = new Bundle(1);
	    		args.putSerializable(KUNDE_KEY, kunde);
	    		
	        	detailsFragment = new KundeDetails();
	        	detailsFragment.setArguments(args);
	        }
	        if (artikel != null) {
	        	Log.d(LOG_TAG, artikel.toString());
	        	
	    		final Bundle args = new Bundle(1);
	    		args.putSerializable(ARTIKEL_KEY, artikel);
	    		
	        	detailsFragment = new ArtikelDetails();
	        	detailsFragment.setArguments(args);
	        }
        }
        
        getFragmentManager().beginTransaction()
                            .add(R.id.details, detailsFragment)
                            .commit();
        
    	if (mock) {
    		Toast.makeText(this, R.string.s_mock, LENGTH_LONG).show();
    	}
    }
    
    @Override
	public void onStart() {
		super.onStart();
		
		Intent intent = new Intent(this, KundeService.class);
		bindService(intent, kundeServiceConnection, Context.BIND_AUTO_CREATE);
		
		intent = new Intent(this, BestellungService.class);
		bindService(intent, bestellungServiceConnection, Context.BIND_AUTO_CREATE);
		
		intent = new Intent(this, ArtikelService.class);
		bindService(intent, artikelServiceConnection, Context.BIND_AUTO_CREATE);
		
		intent = new Intent(this, KategorieService.class);
		bindService(intent, kategorieServiceConnection, Context.BIND_AUTO_CREATE);
    }
    
	@Override
	public void onStop() {
		super.onStop();
		
		unbindService(kundeServiceConnection);
		unbindService(bestellungServiceConnection);
		unbindService(artikelServiceConnection);
		unbindService(kategorieServiceConnection);
	}

	public KundeServiceBinder getKundeServiceBinder() {
		return kundeServiceBinder;
	}

	public BestellungServiceBinder getBestellungServiceBinder() {
		return bestellungServiceBinder;
	}
	
	public ArtikelServiceBinder getArtikelServiceBinder() {
		return artikelServiceBinder;
	}
	
	public KategorieServiceBinder getKategorieServiceBinder() {
		return kategorieServiceBinder;
	}
}

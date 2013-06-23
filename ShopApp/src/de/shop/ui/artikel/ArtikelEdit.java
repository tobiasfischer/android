package de.shop.ui.artikel;

import static de.shop.util.Constants.ARTIKEL_KEY;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import java.math.BigDecimal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.service.ArtikelService.ArtikelServiceBinder;
import de.shop.service.HttpResponse;
import de.shop.ui.main.Main;
import de.shop.ui.main.Prefs;

public class ArtikelEdit extends Fragment {
	private static final String LOG_TAG = ArtikelEdit.class.getSimpleName();
	
	private Artikel artikel;
	private EditText edtName;
	private EditText edtBeschreibung;
	private EditText edtPreis;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		artikel = (Artikel) getArguments().get(ARTIKEL_KEY);
		Log.d(LOG_TAG, artikel.toString());
        
		// Voraussetzung fuer onOptionsItemSelected()
		setHasOptionsMenu(true);
		
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.artikel_edit, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
    	final TextView txtId = (TextView) view.findViewById(R.id.artikel_id_edt);
    	txtId.setText(String.valueOf(artikel.id));

    	edtName = (EditText) view.findViewById(R.id.artikel_name_edt);
    	edtName.setText(artikel.name);
    	
    	edtBeschreibung = (EditText) view.findViewById(R.id.artikel_beschreibung_edt);
    	edtBeschreibung.setText(artikel.beschreibung);
    	
    	edtPreis = (EditText) view.findViewById(R.id.artikel_preis_edt);
    	edtPreis.setText(artikel.preis.toString());
    	
    	final TextView txtKategorie = (TextView) view.findViewById(R.id.artikel_kategorie_bezeichnung_edt);
    	txtKategorie.setText(String.valueOf(artikel.kategorie.bezeichnung));
    }
    
	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.kunde_edit_options, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.speichern:
				setArtikel();

				final Activity activity = getActivity();
				
				// Das Fragment KundeEdit kann von Main und von KundeListe aus aufgerufen werden
				ArtikelServiceBinder artikelServiceBinder;
				if (Main.class.equals(activity.getClass())) {
					final Main main = (Main) activity;
					artikelServiceBinder = main.getArtikelServiceBinder();
				}
				else if (ArtikelsListe.class.equals(activity.getClass())) {
					final ArtikelsListe artikelListe = (ArtikelsListe) activity;
					artikelServiceBinder = artikelListe.getArtikelServiceBinder();
				}
				else {
					return true;
				}
				
				final HttpResponse<Artikel> result = artikelServiceBinder.updateArtikel(artikel, activity);
				final int statuscode = result.responseCode;
				if (statuscode != HTTP_NO_CONTENT && statuscode != HTTP_OK) {
					String msg = null;
					switch (statuscode) {
						case HTTP_CONFLICT:
							msg = result.content;
							break;
						case HTTP_UNAUTHORIZED:
							msg = getString(R.string.s_error_prefs_login, artikel.id);
							break;
						case HTTP_FORBIDDEN:
							msg = getString(R.string.s_error_forbidden, artikel.id);
							break;
					}
					
		    		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		    		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
						public void onClick(DialogInterface dialog, int which) {}
                    };
		    		builder.setMessage(msg)
		    		       .setNeutralButton(getString(R.string.s_ok), listener)
		    		       .create()
		    		       .show();
		    		return true;
				}
				
				artikel = result.resultObject;  // ggf. erhoehte Versionsnr. bzgl. konkurrierender Updates
				
				// Gibt es in der Navigationsleiste eine KundenListe? Wenn ja: Refresh mit geaendertem Kunde-Objekt
				final Fragment fragment = getFragmentManager().findFragmentById(R.id.kunden_liste_nav);
				if (fragment != null) {
					final ArtikelsListeNav artikelsListeFragment = (ArtikelsListeNav) fragment;
					artikelsListeFragment.refresh(artikel);
				}
				
				final Bundle args = new Bundle(1);
				args.putSerializable(ARTIKEL_KEY, artikel);
				
				final Fragment neuesFragment = new ArtikelDetails();
				neuesFragment.setArguments(args);
				
				// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
				getFragmentManager().beginTransaction()
				                    .replace(R.id.details, neuesFragment)
				                    .addToBackStack(null)  
				                    .commit();
				return true;
				
			case R.id.einstellungen:
				getFragmentManager().beginTransaction()
                                    .replace(R.id.details, new Prefs())
                                    .addToBackStack(null)
                                    .commit();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void setArtikel() {
		artikel.name = edtName.getText().toString();
		artikel.beschreibung = edtBeschreibung.getText().toString();
		artikel.preis = new BigDecimal(edtPreis.getText().toString());

		Log.d(LOG_TAG, artikel.toString());
	}
}

package de.shop.ui.artikel;

import static de.shop.util.Constants.ARTIKEL_KEY;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.data.Kategorie;
import de.shop.service.ArtikelService.ArtikelServiceBinder;
import de.shop.service.HttpResponse;
import de.shop.service.KategorieService.KategorieServiceBinder;
import de.shop.ui.main.Main;
import de.shop.ui.main.Prefs;

public class ArtikelAnlegen extends Fragment {
	
	private static final String LOG_TAG = ArtikelAnlegen.class.getSimpleName();

	private Artikel artikel;
	private EditText crtName;
	private EditText crtBeschreibung;
	private EditText crtPreis;
	private List<Kategorie> kategorien;
	
	private Spinner spKategorie;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.artikel_anlegen, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

	    // Evtl. vorhandene Tabs der ACTIVITY loeschen
    	final ActionBar actionBar = getActivity().getActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    	actionBar.setDisplayShowTitleEnabled(true);
    	actionBar.removeAllTabs();
    	
    	
    	crtName = (EditText) view.findViewById(R.id.artikel_name_crt);
    	crtBeschreibung = (EditText) view.findViewById(R.id.artikel_beschreibung_crt);
    	crtPreis = (EditText) view.findViewById(R.id.artikel_preis_crt);
    	
    	
		final Activity activity = getActivity();
		KategorieServiceBinder kategorieServiceBinder;
		final Main main = (Main) activity;
		kategorieServiceBinder = main.getKategorieServiceBinder();
		

    	//Spinner für Kategorie
		spKategorie = (Spinner) view.findViewById(R.id.artikel_kategorie_spinner);
    	
    	List<String> kategorienBez = new ArrayList<String>();
    	
    	kategorien = kategorieServiceBinder.getAllKategorien(getActivity()).resultList;
    	
    	for (Kategorie k : kategorien) {
			kategorienBez.add(k.bezeichnung);
		}
    	kategorienBez.add("test");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, kategorienBez);
    	
        spKategorie.setAdapter(adapter);

    	
    }
	
	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.artikel_anlegen_options, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.speichern:
			setArtikel();

			final Activity activity = getActivity();
			
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
			
			final HttpResponse<Artikel> result = artikelServiceBinder.createArtikel(artikel, activity);
			final int statuscode = result.responseCode;
			Log.i("ArtikelAnlegen", "Statuscode: " + statuscode);
			if (statuscode != HTTP_NO_CONTENT && statuscode != HTTP_OK && statuscode != HTTP_CREATED) {
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
			
			//Artikel ID mit der von Hibernate erzeugten ID füllen
			artikel.id = Long.valueOf(result.content);
 
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
		artikel = new Artikel();
		Kategorie kategorie = new Kategorie();
		kategorie.id = (long) 0;
		kategorie.bezeichnung = "Leere Kategorie";

		artikel.name = crtName.getText().toString();
		artikel.beschreibung = crtBeschreibung.getText().toString();
		artikel.preis = new BigDecimal(crtPreis.getText().toString());
		artikel.id = (long) 0;

		
		for (Kategorie k : kategorien) {
			if (k.bezeichnung == spKategorie.getSelectedItem()) {
				kategorie = k;
			}
		}
		
		artikel.kategorie = kategorie;
//
		Log.d(LOG_TAG, artikel.toString());
	}
	
	
	
}

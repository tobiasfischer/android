package de.shop.ui.artikel;

import static android.view.inputmethod.EditorInfo.IME_NULL;
import static de.shop.util.Constants.ARTIKEL_KEY;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.service.HttpResponse;
import de.shop.ui.main.Main;
import de.shop.ui.main.Prefs;

public class ArtikelAnlegen extends Fragment {
	
	private static final String LOG_TAG = ArtikelAnlegen.class.getSimpleName();

	private Artikel artikel;
	private EditText crtName;
	private EditText crtBeschreibung;
	private EditText crtPreis;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		//TODO Texte anpassen
		return inflater.inflate(R.layout.artikel_anlegen, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	
    	
	    // Evtl. vorhandene Tabs der ACTIVITY loeschen
    	final ActionBar actionBar = getActivity().getActionBar();
    	actionBar.setDisplayShowTitleEnabled(true);
    	actionBar.removeAllTabs();
    }
	
	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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
		artikel.name = crtName.getText().toString();
		artikel.beschreibung = crtBeschreibung.getText().toString();
		artikel.preis = new BigDecimal(crtPreis.getText().toString());

		Log.d(LOG_TAG, artikel.toString());
	}
	
	
}

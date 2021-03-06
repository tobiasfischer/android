package de.shop.ui.kunde;

import static de.shop.util.Constants.KUNDE_KEY;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import de.shop.R;
import de.shop.data.Kunde;
import de.shop.ui.main.Prefs;
import de.shop.util.WischenListener;

public class KundeStammdaten extends Fragment implements OnTouchListener {
	private static final String LOG_TAG = KundeStammdaten.class.getSimpleName();
	
	private Kunde kunde;
	private GestureDetector gestureDetector;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        kunde = (Kunde) getArguments().get(KUNDE_KEY);
        Log.d(LOG_TAG, kunde.toString());

        // Voraussetzung fuer onOptionsItemSelected()
        setHasOptionsMenu(true);
        
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.kunde_stammdaten, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		fillValues(view);
    	
    	final Activity activity = getActivity();
	    final OnGestureListener onGestureListener = new WischenListener(activity);
	    gestureDetector = new GestureDetector(activity, onGestureListener);  // Context und OnGestureListener als Argumente
	    view.setOnTouchListener(this);
    }
	
	private void fillValues(View view) {
		final TextView txtId = (TextView) view.findViewById(R.id.kunde_id);
    	txtId.setText(kunde.id.toString());
    	
    	final TextView txtNachname = (TextView) view.findViewById(R.id.nachname_txt);
    	txtNachname.setText(kunde.nachname);
    	
    	final TextView txtVorname = (TextView) view.findViewById(R.id.vorname);
    	txtVorname.setText(kunde.vorname);
    	
    	final TextView txtEmail = (TextView) view.findViewById(R.id.email);
    	txtEmail.setText(kunde.email);
    	
    	final TextView txtLPlz = (TextView) view.findViewById(R.id.L_plz);
    	txtLPlz.setText(kunde.lieferadresse.plz);
    	
    	final TextView txtLOrt = (TextView) view.findViewById(R.id.L_ort);
    	txtLOrt.setText(kunde.lieferadresse.ort);
    	
    	final TextView txtLStrasse = (TextView) view.findViewById(R.id.L_strasse);
    	txtLStrasse.setText(kunde.lieferadresse.strasse);
    	
    	final TextView txtRPlz = (TextView) view.findViewById(R.id.R_plz);
    	txtRPlz.setText(kunde.rechnungsadresse.plz);
    	
    	final TextView txtROrt = (TextView) view.findViewById(R.id.R_ort);
    	txtROrt.setText(kunde.rechnungsadresse.ort);
    	
    	final TextView txtRStrasse = (TextView) view.findViewById(R.id.R_strasse);
    	txtRStrasse.setText(kunde.rechnungsadresse.strasse);
    	
    	
//    	final TextView txtSeit = (TextView) view.findViewById(R.id.seit);
//		final String seitStr = DateFormat.getDateFormat(view.getContext()).format(kunde.seit);
//    	txtSeit.setText(seitStr);
    	
    	
    	final RadioButton rbMaennlich = (RadioButton) view.findViewById(R.id.maennlich);
    	final RadioButton rbWeiblich = (RadioButton) view.findViewById(R.id.weiblich);
	

    		
	    	if (kunde.geschlecht != null) {
		    	switch (kunde.geschlecht) {
			    	case M:
			        	rbMaennlich.setChecked(true);
				    	break;
				    	
			    	case W:
			        	rbWeiblich.setChecked(true);
				    	break;
				    	
				    default:
		    	}
	    	}
	    	

	}

	@Override
	// http://developer.android.com/guide/topics/ui/actionbar.html#ChoosingActionItems :
	// "As a general rule, all items in the options menu (let alone action items) should have a global impact on the app,
	//  rather than affect only a small portion of the interface."
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.kunde_stammdaten_options, menu);
		
		// "Searchable Configuration" in res\xml\searchable.xml wird der SearchView zugeordnet
//		final Activity activity = getActivity();
//	    final SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
//	    final SearchView searchView = (SearchView) menu.findItem(R.id.suchen).getActionView();
//	    searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//			case R.id.edit:
//				// Evtl. vorhandene Tabs der ACTIVITY loeschen
//		    	getActivity().getActionBar().removeAllTabs();
//		    	
//				final Bundle args = new Bundle(1);
//				args.putSerializable(KUNDE_KEY, kunde);
//				
//				final Fragment neuesFragment = new KundeEdit();
//				neuesFragment.setArguments(args);
//				
//				// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
//				getFragmentManager().beginTransaction()
//				                    .replace(R.id.details, neuesFragment)
//				                    .addToBackStack(null)  
//				                    .commit();
//				return true;
				
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
	
	@Override
	// OnTouchListener
	public boolean onTouch(View view, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
}

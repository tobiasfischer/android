package de.shop.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.shop.ui.main.Prefs.timeout;
import static de.shop.util.Constants.KATEGORIEN_PATH;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.shop.R;
import de.shop.data.Kategorie;
import de.shop.util.InternalShopError;

public class KategorieService extends Service {
	private static final String LOG_TAG = KategorieService.class.getSimpleName();
	
	private KategorieServiceBinder binder = new KategorieServiceBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class KategorieServiceBinder extends Binder {
		
		public KategorieService getService() {
			return KategorieService.this;
		}
		
		private ProgressDialog progressDialog;
		private ProgressDialog showProgressDialog(Context ctx) {
			progressDialog = new ProgressDialog(ctx);
			progressDialog.setProgressStyle(STYLE_SPINNER);  // Kreis (oder horizontale Linie)
			progressDialog.setMessage(getString(R.string.s_bitte_warten));
			progressDialog.setCancelable(true);      // Abbruch durch Zuruecktaste
			progressDialog.setIndeterminate(true);   // Unbekannte Anzahl an Bytes werden vom Web Service geliefert
			progressDialog.show();
			return progressDialog;
		}
		

		public HttpResponse<Kategorie> getAllKategorien(final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "String", Resultat vom Typ "List<Kategorie>"
			final AsyncTask<String, Void, HttpResponse<Kategorie>> getAllKategorienTask = new AsyncTask<String, Void, HttpResponse<Kategorie>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kategorie> doInBackground(String... strings) {
					final String path = KATEGORIEN_PATH;
					Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Kategorie> result = WebServiceClient.getJsonList(path, Kategorie.class);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kategorie> unused) {
					progressDialog.dismiss();
	    		}
			};	
			
			getAllKategorienTask.execute();
			HttpResponse<Kategorie> result = null;
			try {
				result = getAllKategorienTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}

	    	if (result.responseCode != HTTP_OK) {
	    		return result;
	    	}
	    	
			return result;
	    }

	}
}

package de.shop.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.shop.ui.main.Prefs.mock;
import static de.shop.ui.main.Prefs.timeout;
import static de.shop.util.Constants.ARTIKEL_PATH;
import static de.shop.util.Constants.ARTIKEL_ID_PREFIX_PATH;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.util.InternalShopError;

public class ArtikelService extends Service {
	private static final String LOG_TAG = ArtikelService.class.getSimpleName();

	private ArtikelServiceBinder binder = new ArtikelServiceBinder();
	

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class ArtikelServiceBinder extends Binder {
		
		public ArtikelService getService() {
			return ArtikelService.this;
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
		
		/**
		 */
		public HttpResponse<Artikel> sucheArtikelById(Long id, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Artikel"s
			final AsyncTask<Long, Void, HttpResponse<Artikel>> sucheArtikelByIdTask = new AsyncTask<Long, Void, HttpResponse<Artikel>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Artikel> doInBackground(Long... ids) {
					final Long id = ids[0];
		    		final String path = ARTIKEL_PATH + "/" + id;
		    		Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Artikel> result = WebServiceClient.getJsonSingle(path, Artikel.class);

					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
					progressDialog.dismiss();
	    		}
			};

    		sucheArtikelByIdTask.execute(id);
    		HttpResponse<Artikel> result = null;
	    	try {
	    		result = sucheArtikelByIdTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	    	
    		if (result.responseCode != HTTP_OK) {
	    		return result;
		    }
    		
		    return result;
		}
		public List<Long> sucheIds(String prefix) {
			final String path = ARTIKEL_ID_PREFIX_PATH + "/" + prefix;
		    Log.v(LOG_TAG, "sucheIds: path = " + path);

    		final List<Long> ids = mock
   				                   ? Mock.sucheKundeIdsByPrefix(prefix)
   				                   : WebServiceClient.getJsonLongList(path);

			Log.d(LOG_TAG, "sucheIds: " + ids.toString());
			return ids;
		}
	}
}

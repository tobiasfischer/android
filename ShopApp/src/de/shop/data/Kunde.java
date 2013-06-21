package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Kunde implements JsonMappable, Serializable {
	private static final long serialVersionUID = -7505776004556360014L;
	//private static final String DATE_FORMAT = "yyyy-MM-dd";

	public Long id;
	public int version;
	public String nachname;
	public String vorname;
	public String email;
	public String telefonnummer;
	public Adresse lieferadresse;
	public Adresse rechnungsadresse;
	
	
	protected JsonObjectBuilder getJsonObjectBuilder() {
		return jsonBuilderFactory.createObjectBuilder()
				                 .add("kundeId", id)
			                     .add("version", version)
			                     .add("nachname", nachname)
			                     .add("vorname", vorname)
			                     .add("email", email)
			                     .add("lieferadresse", lieferadresse.getJsonBuilderFactory())
			                     .add("rechnungsadresse", rechnungsadresse.getJsonBuilderFactory());
			                     //.add("seit", new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(seit))
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonObjectBuilder().build();
	}

	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("kundeId").longValue());
	    version = jsonObject.getInt("version");
		nachname = jsonObject.getString("nachname");
		vorname = jsonObject.getString("vorname");
		email = jsonObject.getString("email");
		lieferadresse = new Adresse();
		lieferadresse.fromJsonObject(jsonObject.getJsonObject("lieferadresse"));
		rechnungsadresse = new Adresse();
		rechnungsadresse.fromJsonObject(jsonObject.getJsonObject("rechnungsadresse"));
		
		
		/*try {
			seit = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(jsonObject.getString("seit"));
		}
		catch (ParseException e) {
			throw new InternalShopError(e.getMessage(), e);
		}; */
		
		//bestellungenUri = jsonObject.getString("bestellungenUri");
	}
	
	@Override
	public void updateVersion() {
		version++;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kunde other = (Kunde) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kunde [id=" + id + ", version=" + version
				+ ", nachname=" + nachname + ", vorname=" + vorname
				+ ", email=" + email + ", telefonnummer=" + telefonnummer + "]";
	}

	
}

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
	public GeschlechtType geschlecht;
	
	
	protected JsonObjectBuilder getJsonObjectBuilder() {
		return jsonBuilderFactory.createObjectBuilder()
				                 .add("kundeId", id)
			                     .add("version", version)
			                     .add("nachname", nachname)
			                     .add("vorname", vorname)
			                     .add("email", email)
			                     .add("lieferadresse", lieferadresse.getJsonBuilderFactory())
			                     .add("rechnungsadresse", rechnungsadresse.getJsonBuilderFactory())
								 .add("geschlecht", geschlecht.toString());
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
		geschlecht = GeschlechtType.valueOf(jsonObject.getString("geschlecht"));
		
		
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
		result = prime * result
				+ ((geschlecht == null) ? 0 : geschlecht.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lieferadresse == null) ? 0 : lieferadresse.hashCode());
		result = prime * result
				+ ((nachname == null) ? 0 : nachname.hashCode());
		result = prime
				* result
				+ ((rechnungsadresse == null) ? 0 : rechnungsadresse.hashCode());
		result = prime * result
				+ ((telefonnummer == null) ? 0 : telefonnummer.hashCode());
		result = prime * result + version;
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
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
		if (geschlecht == null) {
			if (other.geschlecht != null)
				return false;
		} else if (!geschlecht.equals(other.geschlecht))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lieferadresse == null) {
			if (other.lieferadresse != null)
				return false;
		} else if (!lieferadresse.equals(other.lieferadresse))
			return false;
		if (nachname == null) {
			if (other.nachname != null)
				return false;
		} else if (!nachname.equals(other.nachname))
			return false;
		if (rechnungsadresse == null) {
			if (other.rechnungsadresse != null)
				return false;
		} else if (!rechnungsadresse.equals(other.rechnungsadresse))
			return false;
		if (telefonnummer == null) {
			if (other.telefonnummer != null)
				return false;
		} else if (!telefonnummer.equals(other.telefonnummer))
			return false;
		if (version != other.version)
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kunde [id=" + id + ", version=" + version + ", nachname="
				+ nachname + ", vorname=" + vorname + ", email=" + email
				+ ", telefonnummer=" + telefonnummer + ", lieferadresse="
				+ lieferadresse + ", rechnungsadresse=" + rechnungsadresse
				+ ", geschlecht=" + geschlecht + "]";
	}

	
}

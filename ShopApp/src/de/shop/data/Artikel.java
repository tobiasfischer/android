package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import de.shop.util.InternalShopError;

public class Artikel implements JsonMappable, Serializable {
	private static final long serialVersionUID = -7505776004556360014L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	public Long id;
	public String name;
	public String beschreibung;
	public BigDecimal preis;
	public Kategorie kategorie;
	public int version;


	
	protected JsonObjectBuilder getJsonObjectBuilder() {
		return jsonBuilderFactory.createObjectBuilder()
				                 .add("artikelId", id)
			                     .add("name", name)
			                     .add("beschreibung", beschreibung)
			                     .add("preis", preis)
			                     .add("kategorie", kategorie.getJsonBuilderFactory());
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonObjectBuilder().build();
	}

	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("artikelId").longValue());
		name = jsonObject.getString("name");
		beschreibung = jsonObject.getString("beschreibung");
		preis = jsonObject.getJsonNumber("preis").bigDecimalValue();
		kategorie = new Kategorie();
		kategorie.fromJsonObject(jsonObject.getJsonObject("kategorie"));
	}
	
	@Override
	public void updateVersion() {
		version++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beschreibung == null) ? 0 : beschreibung.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((kategorie == null) ? 0 : kategorie.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((preis == null) ? 0 : preis.hashCode());
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
		Artikel other = (Artikel) obj;
		if (beschreibung == null) {
			if (other.beschreibung != null)
				return false;
		} else if (!beschreibung.equals(other.beschreibung))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (kategorie == null) {
			if (other.kategorie != null)
				return false;
		} else if (!kategorie.equals(other.kategorie))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (preis == null) {
			if (other.preis != null)
				return false;
		} else if (!preis.equals(other.preis))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Artikel [id=" + id + ", name=" + name + ", beschreibung="
				+ beschreibung + ", preis=" + preis + ", kategorie="
				+ kategorie + ", version=" + version + "]";
	}
	
	
}

package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class Kategorie implements JsonMappable, Serializable {
	private static final long serialVersionUID = -4218760204888865981L;
	
	public Long id;
	public String bezeichnung;
	public int version;

	public Kategorie() {
		super();
	}
	
	public Kategorie(String bezeichnung) {
		super();
		this.bezeichnung = bezeichnung;
	}
	
	// fuer AbstractKunde.toJsonObject()
	JsonObjectBuilder getJsonBuilderFactory() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("kategorieId", id)
		                         .add("bezeichnung", bezeichnung);
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonBuilderFactory().build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {		
		id = Long.valueOf(jsonObject.getJsonNumber("kategorieId").longValue());
		bezeichnung = jsonObject.getString("bezeichnung");
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
				+ ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + id.intValue();
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
		Kategorie other = (Kategorie) obj;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kategorie [id=" + id + ", bezeichnung=" + bezeichnung + "]";
	}

	
}

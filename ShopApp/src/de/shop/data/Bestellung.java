package de.shop.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;


public class Bestellung implements JsonMappable, Serializable {
	private static final long serialVersionUID = -3227854872557641281L;
	
	public Long id;
	public String status;
	public List<Bestellposition> bestellpositionen;

	public Bestellung() {
		super();
	}


	public Bestellung(Long id, String status,
			List<Bestellposition> bestellpositionen) {
		super();
		this.id = id;
		this.status = status;
		this.bestellpositionen = bestellpositionen;
	}


	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		status = jsonObject.getString("status");
				
		final JsonArray jsonArray = jsonObject.getJsonArray("bestellpositionen");

		bestellpositionen = new ArrayList<Bestellposition>();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			bestellpositionen.add(new Bestellposition(jsonArray.getJsonObject(i).getJsonNumber("id").longValue(),jsonArray.getJsonObject(0).getString("status")));
		}
	}
	
	@Override
	public void updateVersion() {
	}

	@Override
	public String toString() {
		return "Bestellung [id=" + id + ", status=" + status
				+ ", bestellpositionen=" + bestellpositionen + "]";
	}


	@Override
	public JsonObject toJsonObject() {
		// TODO Auto-generated method stub
		return null;
	}
}

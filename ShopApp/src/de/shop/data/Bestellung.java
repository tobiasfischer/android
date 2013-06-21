package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;
import java.util.Date;

import javax.json.JsonObject;


public class Bestellung implements JsonMappable, Serializable {
	private static final long serialVersionUID = -3227854872557641281L;
	
	public Long id;
	public String status;

	public Bestellung() {
		super();
	}

	public Bestellung(long id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	@Override
	public JsonObject toJsonObject() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("id", id)
		                         .add("status", status)
		                         .build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		status = jsonObject.getString("status");
	}
	
	@Override
	public void updateVersion() {
	}

	@Override
	public String toString() {
		return "Bestellung [id=" + id + ", status=" + status + "]";
	}
}

package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;


public class Bestellposition implements JsonMappable, Serializable {

	private static final long serialVersionUID = 5143724706766689202L;
	public Long id;
	public String status;

	public Bestellposition(Long id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	public Bestellposition() {
		super();
		// TODO Auto-generated constructor stub
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
		return "Bestellposition [id=" + id + ", status=" + status + "]";
	}
}

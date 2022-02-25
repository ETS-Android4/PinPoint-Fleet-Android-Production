package json_parsing;

public class MapDetailsData {

	String id, lat, longi, address, speed, date, engine_status, icon_name;

	public MapDetailsData(String DeviceId, String lat, String longi, String address, String speed, String date,String engineStatus,String icon) {
		this.address = address;
		this.date = date;
		this.id = DeviceId;
		this.lat = lat;
		this.longi = longi;
		this.speed = speed;
		this.engine_status = engineStatus;
		this.icon_name = icon;
	}

	public String getIcon_name() {
		return icon_name;
	}

	public void setIcon_name(String icon_name) {
		this.icon_name = icon_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLongi() {
		return longi;
	}

	public void setLongi(String longi) {
		this.longi = longi;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEngine_status() {
		return engine_status;
	}

	public void setEngine_status(String engine_status) {
		this.engine_status = engine_status;
	}

}

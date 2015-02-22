package org.pelayo.dev.flickr.model;

import javax.measure.unit.SI;

import org.jscience.geography.coordinates.LatLong;
import org.jscience.geography.coordinates.UTM;
import org.jscience.geography.coordinates.crs.CoordinatesConverter;
import org.springframework.util.StringUtils;

public class GeoDataModel {

	private double lat;
	private double lng;

	public static GeoDataModel mk() {
		return new GeoDataModel();
	}

	public static GeoDataModel fromUTM(String UTMLiteral) {
		if (StringUtils.isEmpty(UTMLiteral)) {
			return null;
		}
		if ("-".equals(UTMLiteral)) {
			return null;
		}
		if (UTMLiteral.length() < 6) {
			return null;
		}
		String numericPart = UTMLiteral.substring(2);

		String eastingString = UTMLiteral.substring(0, 1).toUpperCase().equals("X") ? "06" : "05";
		eastingString += numericPart.substring(0, 2) + "000";

		String northingString = "46";
		northingString += numericPart.substring(2, 4) + "000";

		// valueOf(int longitudeZone, char latitudeZone, double easting,
		// double northing, Unit<Length> unit)
		UTM utm = UTM.valueOf(30, 'T', Float.parseFloat(eastingString), Float.parseFloat(northingString), SI.METRE);
		CoordinatesConverter<UTM, LatLong> utmToLatLong = UTM.CRS.getConverterTo(LatLong.CRS);
		LatLong latLong = utmToLatLong.convert(utm);

		GeoDataModel geoDataModel = new GeoDataModel();
		geoDataModel.setLat(latLong.getCoordinates()[0]);
		geoDataModel.setLng(latLong.getCoordinates()[1]);

		return geoDataModel;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public GeoDataModel withLat(double lat) {
		this.lat = lat;
		return this;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public GeoDataModel withLng(double lng) {
		this.lng = lng;
		return this;
	}

}

package demo;

import java.util.List;

import javax.measure.unit.SI;

import org.jscience.geography.coordinates.LatLong;
import org.jscience.geography.coordinates.UTM;
import org.jscience.geography.coordinates.crs.CoordinatesConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pelayo.BotanicaBackendApplication;
import org.pelayo.dao.CitasRepository;
import org.pelayo.model.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BotanicaBackendApplication.class)
@WebAppConfiguration
public class BotanicaBackendApplicationTests {

	@Autowired
	private CitasRepository citasRepository;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testCitas() {
		List<Cita> citas = citasRepository.findAll();
		Assert.assertNotNull(citas);
	}

	@Test
	public void testUTM() {
		//'XM1223'
		//x -> 06
		// easting -> 0612000m
		//m-> 46
		// northing -> 4623000m
		
		String UTMLiteral = "XM1223";
		int numericPart = Integer.parseInt(UTMLiteral.substring(2));
		
		int easting = numericPart / 100;
		System.out.println(easting);
		int northing = numericPart % 100;
		System.out.println(northing);
		
		String eastingString = UTMLiteral.substring(0,1).equals("X") ? "06" : "05";
		eastingString += easting + "000";
		System.out.println(eastingString);
		
		String northingString = "46";
		northingString += northing + "000";
		System.out.println(northingString);

		try {
			// valueOf(int longitudeZone, char latitudeZone, double easting, double northing, Unit<Length> unit)
			 UTM utm = UTM.valueOf(30, 'T', Float.parseFloat(eastingString), Float.parseFloat(northingString), SI.METRE);
			 CoordinatesConverter<UTM, LatLong> utmToLatLong =
			 UTM.CRS.getConverterTo(LatLong.CRS);
			 LatLong latLong = utmToLatLong.convert(utm);
			 
			 System.out.println("LAT " + latLong.getCoordinates()[0]);
			 System.out.println("LNG " + latLong.getCoordinates()[1]);		
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}
}

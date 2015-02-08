package demo;

import java.util.List;

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

}

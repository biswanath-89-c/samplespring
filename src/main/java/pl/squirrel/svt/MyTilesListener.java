package pl.squirrel.svt;

import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.web.startup.TilesListener;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

@SuppressWarnings("deprecation")
public class MyTilesListener extends TilesListener {

	
	public VelocityConfig velocityConfig() {
		VelocityConfigurer cfg = new VelocityConfigurer();
		cfg.setResourceLoaderPath("/WEB-INF/velocity/");
		//cfg.setConfigLocation(context.getResource("/WEB-INF/velocity.properties"));
		System.out.println(" -- Created VelocityConfig :"+cfg);
		return cfg;
	}
	
	@Override
	protected TilesInitializer createTilesInitializer() {
		System.out.println(" --- Returning Custom Init ---");
		return new MyCustomTilesInitializer(velocityConfig());		
	}

}

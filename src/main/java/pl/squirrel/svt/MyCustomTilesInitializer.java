package pl.squirrel.svt;

import java.util.List;
import java.util.Properties;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.startup.DefaultTilesInitializer;
import org.apache.tiles.velocity.context.VelocityTilesRequestContextFactory;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.web.servlet.view.velocity.VelocityConfig;

public class MyCustomTilesInitializer extends DefaultTilesInitializer {
	private VelocityConfig velocityConfig;
	private VelocityEngine ve;
	
	public MyCustomTilesInitializer(VelocityConfig velocityConfig) {
		this.velocityConfig = velocityConfig;
		System.out.println("-- Config set:"+velocityConfig);
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
	}

	@Override
	protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context) {
		System.out.println(" -- Inside createContainerFactory -- ");
		return new BasicTilesContainerFactory(){

			@Override
			protected List<TilesRequestContextFactory> getTilesRequestContextFactoriesToBeChained(
					ChainedTilesRequestContextFactory parent) {
				List<TilesRequestContextFactory> factories = super.getTilesRequestContextFactoriesToBeChained(parent);
				System.out.println(" -- getting factories -- "+factories);	
				System.out.println(" -- Calling Register Factory -- ");	
				registerRequestContextFactory(VelocityTilesRequestContextFactory.class.getName(),factories, parent);
				return factories;
			}

			@Override
			protected AttributeRenderer createTemplateAttributeRenderer(
					BasicRendererFactory rendererFactory,
					TilesApplicationContext applicationContext,
					TilesRequestContextFactory contextFactory,
					TilesContainer container,
					AttributeEvaluatorFactory attributeEvaluatorFactory) {
				System.out.println(" -- Inside createTemplateAttributeRenderer with velocityConfig --"+velocityConfig);
				ContextPassingVelocityAttributeRenderer var = new ContextPassingVelocityAttributeRenderer(
						ve);
				var.setApplicationContext(applicationContext);
				var.setRequestContextFactory(contextFactory);
				var.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
				var.commit();
				System.out.println(" -- ContextPassingVelocityAttributeRenderer: "+var);
				return var;
			}			
		};
	}

}

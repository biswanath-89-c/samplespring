package pl.squirrel.svt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.StrutsRequestWrapper;
import org.apache.tiles.Attribute;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.impl.InvalidTemplateException;
import org.apache.tiles.renderer.impl.AbstractTypeDetectingAttributeRenderer;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.util.IteratorEnumeration;
import org.apache.tiles.velocity.renderer.VelocityAttributeRenderer;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalWrapperContext;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.view.JeeConfig;
import org.apache.velocity.tools.view.VelocityView;

/**
 * Largely based on VelocityAttributeRenderer, with tho workarounds:
 * 
 * 1. Supports injecting a VelocityEngine (JIRA TILES-542)
 * 
 * 2. Forwards context received from tiles macros to VelocityView (JIRA
 * TILES-541)
 * 
 * @author Konrad Garus
 * 
 */
public class ContextPassingVelocityAttributeRenderer extends
		AbstractTypeDetectingAttributeRenderer {

	/**
	 * The VelocityView object to use.
	 */
	private VelocityView velocityView;

	/**
	 * The initialization parameters for VelocityView.
	 */
	private Map<String, String> params = new HashMap<String, String>();

	private VelocityEngine engine;

	public ContextPassingVelocityAttributeRenderer(VelocityEngine engine) {
		System.out.println(" -- Velocity engine: "+engine);
		this.engine = engine;
	}

	/**
	 * Sets a parameter for the internal servlet.
	 * 
	 * @param key
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @since 2.2.0
	 */
	public void setParameter(String key, String value) {
		params.put(key, value);
	}

	/**
	 * Commits the parameters and makes this renderer ready for the use.
	 * 
	 * @since 2.2.0
	 */
	public void commit() {
		velocityView = new VelocityView(new TilesApplicationContextJeeConfig());
		velocityView.setVelocityEngine(engine);
		System.out.println(" -- After Commit --");
	}

	/** {@inheritDoc} */
	@Override
	public void write(Object value, Attribute attribute,
			TilesRequestContext request) throws IOException {
		System.out.println(" -- write() request: "+request);
		System.out.println(" -- value: "+value);
		if (value != null) {
			System.out.println(" -- value is not null --");
			if (value instanceof String) {
				System.out.println(" -- value is String --");
                ServletTilesRequestContext servletRequest = ServletUtil.getServletRequest(request);
                System.out.println("-- got servletReq --"+servletRequest);
                // then get a context
                Context context = velocityView.createContext(servletRequest
                        .getRequest(), servletRequest.getResponse());
                System.out.println("-- got context --"+context);
                
                Vector loader=(Vector) velocityView.getVelocityEngine().getProperty(RuntimeConstants.RESOURCE_LOADER);
                velocityView.getVelocityEngine().clearProperty("springMacro");
				Template template = velocityView.getTemplate((String) value);
				System.out.println(" -- Template Accesible:"+template+" -- "+loader);
				System.out.println("-- Setting Context --");
				velocityView.merge(template, context, request.getWriter());
			} else {
				throw new InvalidTemplateException(
						"Cannot render a template that is not a string: "
								+ value.toString());
			}
		} else {
			throw new InvalidTemplateException("Cannot render a null template");
		}
	}

	/** {@inheritDoc} */
	public boolean isRenderable(Object value, Attribute attribute,
			TilesRequestContext request) {
		if (value instanceof String) {
			String string = (String) value;
			return string.startsWith("/") && string.endsWith(".vm");
		}
		return false;
	}

	/**
	 * Implements JeeConfig to use parameters set through
	 * {@link VelocityAttributeRenderer#setParameter(String, String)}.
	 * 
	 * @version $Rev: 821299 $ $Date: 2009-10-03 14:15:05 +0200 (sab, 03 ott
	 *          2009) $
	 * @since 2.2.0
	 */
	private class TilesApplicationContextJeeConfig implements JeeConfig {

		/** {@inheritDoc} */
		public String getInitParameter(String name) {
			return params.get(name);
		}

		/** {@inheritDoc} */
		public String findInitParameter(String key) {
			return params.get(key);
		}

		/** {@inheritDoc} */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Enumeration getInitParameterNames() {
			return new IteratorEnumeration(params.keySet().iterator());
		}

		/** {@inheritDoc} */
		public String getName() {
			return "Tiles Application Context JEE Config";
		}

		/** {@inheritDoc} */
		public ServletContext getServletContext() {
			return ServletUtil.getServletContext(applicationContext);
		}
	}


}

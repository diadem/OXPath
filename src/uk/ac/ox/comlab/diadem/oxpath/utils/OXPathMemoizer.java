

package uk.ac.ox.comlab.diadem.oxpath.utils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import diadem.common.web.dom.DOMDocument;
import diadem.common.web.dom.DOMNode;

/**
 * 
 * This class is a general java memoizer that was retrieved from <tt>http://onjava.com/pub/a/onjava/2003/08/20/memoization.html?page=2</tt>
 * on 24 Sept 2011.  It is useful in OXPath for both the memoization feature in eval_ as well as the extraction marker creation.
 * <p>
 * The class here has been adapted from its original verison to update generics to parameterized types (rather than the raws of 
 * pre-Java 1.5).  In addition, for efficiency, we memoize methods only whose first parameter
 * is a {@code DOMNode} instance (each such method signature, then, must be unique as we don't account for the name).
 * This way, we can cache by the containing document, rather than the method.
 * @author Tom White
 * @author AndrewJSel
 *
 */
public class OXPathMemoizer implements InvocationHandler,OXPathCache {
//	public static OXPathCache memoize(OXPathCache cache) {
//		return (OXPathCache) Proxy.newProxyInstance(
//				cache.getClass().getClassLoader(),
//				cache.getClass().getInterfaces(),
//				new OXPathMemoizer(cache)
//				);
//	}

	@SuppressWarnings("unchecked")
	public static <T extends OXPathCache> T memoize(T cache) {
		return (T) Proxy.newProxyInstance(
				cache.getClass().getClassLoader(),
				cache.getClass().getInterfaces(),
				new OXPathMemoizer(cache)
				);
	}
	
	private OXPathCache object;
	private Map<DOMDocument, Map<List<Object>, Object>> caches = new HashMap<DOMDocument, Map<List<Object>, Object>>();

	private OXPathMemoizer(OXPathCache object) {
		this.object = object;
	}

	public Object invoke(Object proxy, Method method,
			Object[] args) throws Throwable {
		if (method.getReturnType().equals(Void.TYPE)) {
	          // Don't cache void methods
	          return invoke(method, args);
		}
		//check for the signature for clear(DOMDocument) and override if it is called
		else if (method.getName().equals("clear")  && args.length==1 && args[0] instanceof DOMDocument) {
			return this.clear((DOMDocument)args[0]);
		}
		//only memoize for methods whose first argument is a DOMNode
		else if (args[0] instanceof DOMNode) {
			Map<List<Object>, Object> cache  = getCache((args[0] instanceof DOMDocument)?(DOMDocument)args[0]:((DOMNode) args[0]).getOwnerDocument());
			List<Object> key     = Arrays.asList(args);
			Object value = cache.get(key);

			if (value == null && !cache.containsKey(key)) {
				value = this.invoke(method, args);
				cache.put(key, value);
			} 
			return value;
		}
		else return this.invoke(method, args);
	}

	private Object invoke(Method method, Object[] args)
			throws Throwable {
		try {
			return method.invoke(object, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private synchronized Map<List<Object>, Object> getCache(DOMDocument m) {
		Map<List<Object>, Object> cache = caches.get(m);
		if (cache == null) {
			cache = Collections.synchronizedMap(
					new HashMap<List<Object>, Object>()
					);
			caches.put(m, cache);
		}
		return cache;
	}

	@Override
	public Boolean clear(DOMDocument page) {
		Object result = this.caches.remove(page);
		return (result!=null);
	}
}
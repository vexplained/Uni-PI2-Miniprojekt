package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.util.List;

// Note that "extends" here also include classes which implement IDynamicComponent.
// See https://docs.oracle.com/javase/tutorial/java/generics/bounded.html
/**
 * An interface describing a dynamic container. If the implementing container shall only accept certain subtypes of
 * {@link IDynamicComponent}, that restriction may be achieved using a generic type at construction time.
 * <b>Otherwise</b>, define the container using
 * 
 * <pre>
 * IDynamicContainer&lt;? extends IDynamicComponent&gt;
 * </pre>
 */
public interface IDynamicContainer<C extends IDynamicComponent>
{
	public void removeAllObjects();

	public void removeAllObjects(List<C> listToRemove);

	/**
	 * @param object
	 *            element to be removed from this list, if present
	 * @return {@code true} if this list contained the specified element
	 */
	public boolean removeObject(C object);

	public List<C> getObjects();

	public void addObject(C object);

	public void invalidate();
}

package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.util.List;

// Note that "extends" here also include classes which implement IDynamicComponent.
// See https://docs.oracle.com/javase/tutorial/java/generics/bounded.html
public interface IDynamicContainer<C extends IDynamicComponent>
{
	public void removeAllObjects();

	public void removeAllObjects(List<C> listToRemove);

	public void removeObject(C object);

	public List<C> getObjects();

	public void addObject(C object);

	public void invalidate();
}

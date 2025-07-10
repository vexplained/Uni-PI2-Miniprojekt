package de.grotefober.nbodysim.ui.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicContainer;

/**
 * Dynamic object containing multiple other dynamic components. The position of all child objects is determined by the
 * position of this wrapper.
 */
public class DynCompoundObject extends DynamicObject
{

	Set<DynamicObject> objSet;

	private boolean showOutline;

	public DynCompoundObject(Color color, double x, double y)
	{
		super(color, x, y);

		objSet = Collections.synchronizedSet(new LinkedHashSet<DynamicObject>());
	}

	@Override
	public void setParentCanvas(IDynamicContainer parent)
	{
		super.setParentCanvas(parent);

		for (DynamicObject obj : objSet)
		{
			obj.setParentCanvas(parent);
		}
	}

	public boolean isOutlineVisible()
	{
		return showOutline;
	}

	public void setOutlineVisible(boolean showOutline)
	{
		this.showOutline = showOutline;
	}

	/**
	 * See {@link Set#add(Object)}.
	 */
	public void addComponent(DynamicObject comp)
	{
		comp.setParentCanvas(parentCanvas);
		objSet.add(comp);
	}

	/**
	 * See {@link Set#remove(Object)}.
	 */
	public boolean removeComponent(DynamicObject comp)
	{
		return objSet.remove(comp);
	}

	public void clear()
	{
		objSet.clear();
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		for (DynamicObject obj : objSet)
		{
			if (obj.isInShape(x, y))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		for (DynamicObject obj : objSet)
		{
			// Synchronize obj's position. #moveTo automatically invalidates the object
			obj.setPositionNoUpdate(getX(), getY());
			if (!showOutline)
			{
				obj.draw(g2d);
			} else
			{
				drawOutline(g2d);
			}
		}

	}

	/**
	 * This call is expensive. Use sparingly.
	 */
	private void drawOutline(Graphics2D g2d)
	{
		Color colorTemp;
		for (DynamicObject obj : objSet)
		{
			colorTemp = obj.getColor();
			obj.setColor(color);
			obj.draw(g2d);
			obj.setColor(colorTemp);
		}
	}

}

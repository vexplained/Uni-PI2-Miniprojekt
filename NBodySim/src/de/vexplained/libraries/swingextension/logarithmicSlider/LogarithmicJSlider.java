package de.vexplained.libraries.swingextension.logarithmicSlider;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.UIResource;

import de.vexplained.libraries.swingextension.util.MathUtil;

/**
 * This JSlider subclass uses a custom UI to allow a slider to work in
 * logarithmic scale. Major and minor ticks are drawn for logarithmic scale as
 * well.
 *
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Apr 10, 2004
 * @author Piotr Różański (small adjustments)
 */
public class LogarithmicJSlider extends JSlider
{
	private static final long serialVersionUID = 2977848666930849791L;

	public static double logBase = Math.E;

	public LogarithmicJSlider(int orientation)
	{
		super(orientation);
		setLogSliderUI();
	}

	public LogarithmicJSlider(int min, int max)
	{
		super(min, max);
		setLogSliderUI();
	}

	public LogarithmicJSlider(int min, int max, int value)
	{
		super(min, max, value);
		setLogSliderUI();
	}

	public LogarithmicJSlider(int orientation, int min, int max, int value)
	{
		super(orientation, min, max, value);
		setLogSliderUI();
	}

	public LogarithmicJSlider(BoundedRangeModel brm)
	{
		super(brm);
		setLogSliderUI();
	}

	public LogarithmicJSlider()
	{
		setLogSliderUI();
	}

	public static class LogSliderCore
	{

		public static int xPositionForValue(int value, JSlider slider, boolean drawInverted, Rectangle trackRect)
		{
			final int min = (slider.getMinimum() <= 0) ? 1 : slider.getMinimum();
			int max = slider.getMaximum();
			int trackLength = trackRect.width;
			double valueRange = MathUtil.logN(max, logBase) - MathUtil.logN(min, logBase);
			double pixelsPerValue = trackLength / valueRange;
			int trackLeft = trackRect.x;
			int trackRight = trackRect.x + (trackRect.width - 1);
			int xPosition;

			if (!drawInverted)
			{
				xPosition = trackLeft;
				xPosition += Math.round(pixelsPerValue * (MathUtil.logN(value, logBase) - MathUtil.logN(min, logBase)));
			} else
			{
				xPosition = trackRight;
				xPosition -= Math.round(pixelsPerValue * (MathUtil.logN(value, logBase) - MathUtil.logN(min, logBase)));
			}

			xPosition = Math.max(trackLeft, xPosition);
			xPosition = Math.min(trackRight, xPosition);

			// System.out.println("xForV: " + xPosition);
			return xPosition;
		}

		/**
		 * wird verwendet um slider position auf getValue() Wert umzurechnen
		 */
		public static int valueForXPosition(int xPos, JSlider slider, boolean drawInverted, Rectangle trackRect)
		{
			int value;
			final int minValue = (slider.getMinimum() <= 0) ? 1 : slider.getMinimum();
			final int maxValue = slider.getMaximum();
			final int trackLength = trackRect.width;
			final int trackLeft = trackRect.x;
			final int trackRight = trackRect.x + (trackRect.width - 1);

			if (xPos <= trackLeft)
			{
				value = drawInverted ? maxValue : minValue;
			} else if (xPos >= trackRight)
			{
				value = drawInverted ? minValue : maxValue;
			} else
			{
				int distanceFromTrackLeft = drawInverted ? trackRight - xPos : xPos - trackLeft;
				double valueRange = MathUtil.logN(maxValue, logBase) - MathUtil.logN(minValue, logBase);
				// double valuePerPixel = (double)valueRange / (double)trackLength;
				// int valueFromTrackLeft =
				// (int)Math.round( Math.pow(3.5,(double)distanceFromTrackLeft *
				// (double)valuePerPixel));

				int valueFromTrackLeft = (int) Math.round(Math.pow(logBase,
						MathUtil.logN(minValue, logBase) + (((distanceFromTrackLeft) * valueRange) / trackLength)));

				value = (int) MathUtil.logN(minValue, logBase) + valueFromTrackLeft;
			}

			// System.out.println("vForX: " + value);
			return value;
		}

	}

	private void setLogSliderUI()
	{
		this.setUI(new LogarithmicSliderUI(this));
		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				addMouseWheelListener(new MouseWheelListener()
				{
					@Override
					public void mouseWheelMoved(MouseWheelEvent e)
					{
						if (isEnabled())
						{
							if (e.getPreciseWheelRotation() < 0.0)
							{
								setValue(getValue() + 1);
							} else
							{
								if (getValue() > 1)
								{
									setValue(getValue() - 1);
								}
							}
						}
					}
				});

				if (getOrientation() == HORIZONTAL)
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				} else
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				}
			}
		});
	}

	/**
	 * Creates a hashtable holding the text labels for this slider. This
	 * implementation uses the increment as a log-base.
	 *
	 * @return hash table
	 */
	@Override
	public Hashtable createStandardLabels(int increment, int start)
	{
		if (start > getMaximum() || start < getMinimum())
		{
			throw new IllegalArgumentException("Slider label start point out of range.");
		}

		if (increment <= 0)
		{
			throw new IllegalArgumentException("Label incremement must be > 0");
		}

		class LabelHashtable extends Hashtable implements PropertyChangeListener
		{

			private static final long serialVersionUID = 6849372349423483792L;

			int increment = 0;

			int start = 0;

			boolean startAtMin = false;

			public LabelHashtable(int increment, int start)
			{
				super();
				this.increment = increment;
				this.start = start;
				startAtMin = start == getMinimum();
				createLabels(this, increment, start, getMaximum());
			}

			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				if (e.getPropertyName().equals("minimum") && startAtMin)
				{
					start = getMinimum();
				}

				if (e.getPropertyName().equals("minimum") || e.getPropertyName().equals("maximum"))
				{

					Enumeration keys = getLabelTable().keys();
					Object key = null;
					Hashtable hashtable = new Hashtable();

					// Save the labels that were added by the developer
					while (keys.hasMoreElements())
					{
						key = keys.nextElement();
						Object value = getLabelTable().get(key);
						if (!(value instanceof LabelUIResource))
						{
							hashtable.put(key, value);
						}
					}

					clear();
					createLabels(this, increment, start, getMaximum());

					// Add the saved labels
					keys = hashtable.keys();
					while (keys.hasMoreElements())
					{
						key = keys.nextElement();
						put(key, hashtable.get(key));
					}
					((JSlider) e.getSource()).setLabelTable(this);
				}
			}
		}

		LabelHashtable table = new LabelHashtable(increment, start);

		if (getLabelTable() != null && (getLabelTable() instanceof PropertyChangeListener))
		{
			removePropertyChangeListener((PropertyChangeListener) getLabelTable());
		}

		addPropertyChangeListener(table);

		return table;
	}

	/**
	 * This method creates the table of labels that are used to label major ticks on
	 * the slider.
	 *
	 * @param table
	 * @param increment
	 * @param start
	 */
	protected void createLabels(Hashtable table, int increment, int start, int end)
	{
		boolean alreadyDoubled = false;
		for (int labelIndex = start; labelIndex <= getMaximum(); labelIndex += increment)
		{
			if (labelIndex >= 100 && !alreadyDoubled)
			{
				increment = (int) (increment * 2.5d);
			}

			if (labelIndex <= 0)
			{
				table.put(1, new LabelUIResource("1", JLabel.CENTER));
				continue;
			}

			table.put(labelIndex, new LabelUIResource("" + labelIndex, JLabel.CENTER));
		}

		table.put(end, new LabelUIResource("" + end, JLabel.CENTER));
	}

	protected class LabelUIResource extends JLabel implements UIResource
	{

		private static final long serialVersionUID = 6374399785069895978L;

		public LabelUIResource(String text, int alignment)
		{
			super(text, alignment);
			setName("Slider.label");
		}
	}

}
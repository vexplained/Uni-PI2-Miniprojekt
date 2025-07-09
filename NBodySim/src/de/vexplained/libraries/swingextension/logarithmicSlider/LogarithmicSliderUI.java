package de.vexplained.libraries.swingextension.logarithmicSlider;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import de.vexplained.libraries.swingextension.logarithmicSlider.LogarithmicJSlider.LogSliderCore;
import de.vexplained.libraries.swingextension.logarithmicSlider.weblafExt.GraphicsUtils;

/**
 * @author vExplained
 *
 */
public class LogarithmicSliderUI extends BasicSliderUI
{
	JSlider component;

	Color borderColor = new Color(170, 170, 170);

	Color thumbBgTop = new Color(253, 253, 253);
	Color thumbBgBottom = new Color(227, 227, 227);

	// org-modified
	// Color trackBgTop = new Color(255, 255, 255);
	// Color trackBgBottom = new Color(215, 215, 215);
	// lighter
	Color trackBgTop = new Color(255, 255, 255);
	Color trackBgBottom = new Color(235, 235, 235);

	Color progressBorderColor = new Color(128, 128, 128);
	// org-modified
	// Color progressTrackBgTop = new Color(252, 252, 252);
	// Color progressTrackBgBottom = new Color(226, 226, 226);

	// Saturated colors
	// Color progressTrackBgTop = new Color(3, 243, 10);
	// Color progressTrackBgBottom = new Color(243, 206, 32);
	// Lighter colors
	Color progressTrackBgTop = new Color(166, 243, 168);
	Color progressTrackBgBottom = new Color(243, 229, 166);

	private transient boolean upperDragging;

	protected Color getBorderColor()
	{
		return borderColor;
	}

	/**
	 * @param b
	 */
	public LogarithmicSliderUI(JSlider b)
	{
		super(b);
		component = b;
	}

	@Override
	protected int xPositionForValue(int value)
	{
		return LogSliderCore.xPositionForValue(value, slider, drawInverted(), trackRect);
	}

	@Override
	public int valueForXPosition(int xPos)
	{
		return LogSliderCore.valueForXPosition(xPos, slider, drawInverted(), trackRect);
	}

	@Override
	public void paint(Graphics g, JComponent c)
	{
		Graphics2D g2d = (Graphics2D) g;
		calculateGeometry();

		final Rectangle clip = g2d.getClipBounds();
		if (component.getPaintTrack() && clip.intersects(trackRect))
		{
			paintTrack(g2d);
		}
		if (component.getPaintTicks() && clip.intersects(tickRect))
		{
			paintTicks(g2d);
		}
		if (component.getPaintLabels() && clip.intersects(labelRect))
		{
			paintLabels(g2d);
		}
		if (clip.intersects(thumbRect))
		{
			paintThumb(g2d);
		}
	}

	@Override
	protected void calculateGeometry()
	{
		calculateFocusRect();
		calculateContentRect();
		calculateThumbSize();
		calculateTrackBuffer();
		calculateTrackRect();
		calculateTickRect();
		calculateLabelRect();
		calculateThumbLocation();
	}

	// @Override
	// protected Color getFocusColor()
	// {
	// return new Color(100, 0, 0);
	// }

	@Override
	protected void calculateTickRect()
	{
		if (component.getOrientation() == JSlider.HORIZONTAL)
		{
			tickRect.x = trackRect.x;
			tickRect.y = trackRect.y + trackRect.height;
			tickRect.width = trackRect.width;
			tickRect.height = component.getPaintTicks() ? getTickLength() : 0;
		} else
		{
			tickRect.width = component.getPaintTicks() ? getTickLength() : 0;
			if (ltr)
			{
				tickRect.x = trackRect.x + trackRect.width;
			} else
			{
				tickRect.x = trackRect.x - tickRect.width;
			}
			tickRect.y = trackRect.y;
			tickRect.height = trackRect.height;
		}
	}

	@Override
	protected void calculateLabelRect()
	{
		if (component.getPaintLabels())
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				labelRect.x = tickRect.x - trackBuffer;
				labelRect.y = tickRect.y + tickRect.height;
				labelRect.width = tickRect.width + trackBuffer * 2;
				labelRect.height = getHeightOfTallestLabel();
			} else
			{
				if (ltr)
				{
					labelRect.x = tickRect.x + tickRect.width;
					labelRect.width = getWidthOfWidestLabel();
				} else
				{
					labelRect.width = getWidthOfWidestLabel();
					labelRect.x = tickRect.x - labelRect.width;
				}
				labelRect.y = tickRect.y - trackBuffer;
				labelRect.height = tickRect.height + trackBuffer * 2;
			}
		} else
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				labelRect.x = tickRect.x;
				labelRect.y = tickRect.y + tickRect.height;
				labelRect.width = tickRect.width;
				labelRect.height = 0;
			} else
			{
				if (ltr)
				{
					labelRect.x = tickRect.x + tickRect.width;
				} else
				{
					labelRect.x = tickRect.x;
				}
				labelRect.y = tickRect.y;
				labelRect.width = 0;
				labelRect.height = tickRect.height;
			}
		}
	}

	@Override
	protected void calculateFocusRect()
	{
		final Insets insets = component.getInsets();
		focusRect.x = insets.left;
		focusRect.y = insets.top;
		focusRect.width = component.getWidth() - (insets.left + insets.right);
		focusRect.height = component.getHeight() - (insets.top + insets.bottom);
	}

	@Override
	protected void calculateThumbSize()
	{
		final Dimension size = getThumbSize();
		thumbRect.setSize(size.width, size.height);
	}

	@Override
	protected void calculateContentRect()
	{
		contentRect.x = focusRect.x;
		contentRect.y = focusRect.y;
		contentRect.width = focusRect.width;
		contentRect.height = focusRect.height;
	}

	@Override
	protected void calculateThumbLocation()
	{
		if (component.getSnapToTicks())
		{
			final int sliderValue = component.getValue();
			int snappedValue = sliderValue;
			final int majorTickSpacing = component.getMajorTickSpacing();
			final int minorTickSpacing = component.getMinorTickSpacing();
			int tickSpacing = 0;

			if (minorTickSpacing > 0)
			{
				tickSpacing = minorTickSpacing;
			} else if (majorTickSpacing > 0)
			{
				tickSpacing = majorTickSpacing;
			}

			if (tickSpacing != 0)
			{
				// If it's not on a tick, change the value
				if ((sliderValue - component.getMinimum()) % tickSpacing != 0)
				{
					final float temp = (float) (sliderValue - component.getMinimum()) / (float) tickSpacing;
					final int whichTick = Math.round(temp);
					snappedValue = component.getMinimum() + whichTick * tickSpacing;
				}

				if (snappedValue != sliderValue)
				{
					component.setValue(snappedValue);
				}
			}
		}

		if (component.getOrientation() == JSlider.HORIZONTAL)
		{
			final int valuePosition = xPositionForValue(component.getValue());

			thumbRect.x = valuePosition - thumbRect.width / 2;
			thumbRect.y = trackRect.y;
		} else
		{
			final int valuePosition = yPositionForValue(component.getValue());

			thumbRect.x = trackRect.x;
			thumbRect.y = valuePosition - thumbRect.height / 2;
		}
	}

	@Override
	protected void calculateTrackBuffer()
	{
		if (component.getPaintLabels() && component.getLabelTable() != null)
		{
			final Component highLabel = getHighestValueLabel();
			final Component lowLabel = getLowestValueLabel();

			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				trackBuffer = Math.max(highLabel.getBounds().width, lowLabel.getBounds().width) / 2;
				trackBuffer = Math.max(trackBuffer, thumbRect.width / 2);
			} else
			{
				trackBuffer = Math.max(highLabel.getBounds().height, lowLabel.getBounds().height) / 2;
				trackBuffer = Math.max(trackBuffer, thumbRect.height / 2);
			}
		} else
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				trackBuffer = thumbRect.width / 2;
			} else
			{
				trackBuffer = thumbRect.height / 2;
			}
		}
	}

	@Override
	protected void calculateTrackRect()
	{
		int centerSpacing; // used to center sliders added using BorderLayout.CENTER (bug 4275631)
		if (component.getOrientation() == JSlider.HORIZONTAL)
		{
			centerSpacing = thumbRect.height;
			if (component.getPaintTicks())
			{
				centerSpacing += getTickLength();
			}
			if (component.getPaintLabels())
			{
				centerSpacing += getHeightOfTallestLabel();
			}
			trackRect.x = contentRect.x + trackBuffer;
			trackRect.y = contentRect.y + (contentRect.height - centerSpacing - 1) / 2;
			trackRect.width = contentRect.width - trackBuffer * 2;
			trackRect.height = thumbRect.height;
		} else
		{
			centerSpacing = thumbRect.width;
			if (ltr)
			{
				if (component.getPaintTicks())
				{
					centerSpacing += getTickLength();
				}
				if (component.getPaintLabels())
				{
					centerSpacing += getWidthOfWidestLabel();
				}
			} else
			{
				if (component.getPaintTicks())
				{
					centerSpacing -= getTickLength();
				}
				if (component.getPaintLabels())
				{
					centerSpacing -= getWidthOfWidestLabel();
				}
			}
			trackRect.x = contentRect.x + (contentRect.width - centerSpacing - 1) / 2;
			trackRect.y = contentRect.y + trackBuffer;
			trackRect.width = thumbRect.width;
			trackRect.height = contentRect.height - trackBuffer * 2;
		}
	}

	int trackHeight = 9;
	int trackRound = 4;

	int progressShadeWidth = 2;
	int trackShadeWidth = 2;
	int progressRound = 2;

	protected Shape getProgressShape()
	{
		if (trackRound > 0)
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				final int x;
				final int w;
				if (ltr)
				{
					x = trackRect.x - trackRound + progressShadeWidth;
					w = thumbRect.x + thumbRect.width / 2 + progressRound - x;
				} else
				{
					x = thumbRect.x + thumbRect.width / 2 - progressRound;
					w = trackRect.x + trackRect.width + trackRound - progressShadeWidth - 1 - x;
				}
				return new RoundRectangle2D.Double(x,
						trackRect.y + trackRect.height / 2 - trackHeight / 2 + progressShadeWidth, w,
						trackHeight - progressShadeWidth * 2, progressRound * 2, progressRound * 2);
			} else
			{
				final int y = thumbRect.y + thumbRect.height / 2;
				final int h = trackRect.y + trackRect.height + trackRound - progressShadeWidth - y - 1;
				return new RoundRectangle2D.Double(
						trackRect.x + progressShadeWidth + trackRect.width / 2 - trackHeight / 2, y,
						trackHeight - progressShadeWidth * 2, h, progressRound * 2, progressRound * 2);
			}
		} else
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				final int x;
				final int w;
				if (ltr)
				{
					x = trackRect.x + progressShadeWidth;
					w = thumbRect.x + thumbRect.width / 2 - x;
				} else
				{
					x = thumbRect.x + thumbRect.width / 2;
					w = trackRect.x + trackRect.width - progressShadeWidth - 1 - x;
				}
				return new Rectangle2D.Double(x,
						trackRect.y + trackRect.height / 2 - trackHeight / 2 + progressShadeWidth, w,
						trackHeight - progressShadeWidth * 2);
			} else
			{
				final int y = trackRect.y + progressShadeWidth;
				final int h = thumbRect.y + thumbRect.height / 2 - y;
				return new Rectangle2D.Double(trackRect.x + progressShadeWidth + trackRect.width / 2 - trackHeight / 2,
						y, trackHeight - progressShadeWidth * 2, h);
			}
		}
	}

	boolean drawProgress = true;

	@Override
	public void paintTrack(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//
		// /**
		// * tb: trackBounds
		// */
		// Rectangle tb = trackRect;
		//
		// g2d.setColor(new Color(214, 214, 214));
		// g2d.drawRect(tb.x, (tb.height / 2) - 2 + tb.y, tb.width - 1, 2);
		// g2d.setColor(new Color(231, 231, 231));
		// g2d.drawLine(tb.x + 1, (tb.height / 2) - 1 + tb.y, tb.width + tb.x - 2,
		// (tb.height / 2) - 1 + tb.y);
		//
		// // g2d.drawLine(tb.x + 1, (tb.height / 2) - 1 + tb.y, upperX, (tb.height / 2)
		// -
		// // 1 + tb.y);
		//
		// Rectangle trackBounds = trackRect;
		// int lowerX = thumbRect.width / 2;
		// int upperX = thumbRect.x + (thumbRect.width / 2);
		// int cy = (trackBounds.height / 2) - 2;
		// g2d.translate(trackBounds.x, trackBounds.y + cy);
		// g2d.setColor(Color.black);
		// g2d.drawLine(lowerX - trackBounds.x, 2, upperX - trackBounds.x, 2);
		// g2d.translate(-trackBounds.x, -(trackBounds.y + cy));

		// Track shape
		final Shape ss = getTrackShape();

		// Track background & shade
		{
			// Track shade
			if (component.isEnabled())
			{
				GraphicsUtils.drawShade(g2d, ss,
						component.isFocusOwner() ? new Color(85, 142, 239) : new Color(210, 210, 210), trackShadeWidth);
			}

			// Track background
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				g2d.setPaint(new GradientPaint(0, trackRect.y, trackBgTop, 0, trackRect.y + trackRect.height,
						trackBgBottom));
			} else
			{
				g2d.setPaint(
						new GradientPaint(trackRect.x, 0, trackBgTop, trackRect.x + trackRect.width, 0, trackBgBottom));
			}
			g2d.fill(ss);
		}

		// Inner progress line
		// if (drawProgress)
		// {
		// Progress shape
		final Shape ps = getProgressShape();

		// Progress shade
		if (component.isEnabled())
		{
			GraphicsUtils.drawShade(g2d, ps, new Color(210, 210, 210), progressShadeWidth);
		}

		// Progress background
		final Rectangle bounds = ss.getBounds();
		if (component.getOrientation() == JSlider.HORIZONTAL)
		{
			// g2d.setPaint(new GradientPaint(0, bounds.y + progressShadeWidth,
			// progressTrackBgTop, 0,
			// bounds.y + bounds.height - progressShadeWidth, progressTrackBgBottom));

			g2d.setPaint(new GradientPaint(bounds.x, 0, progressTrackBgTop, bounds.x + bounds.width, 0,
					progressTrackBgBottom));
		} else
		{
			// g2d.setPaint(new GradientPaint(bounds.x + progressShadeWidth, 0,
			// progressTrackBgTop,
			// bounds.x + bounds.width - progressShadeWidth, 0, progressTrackBgBottom));

			g2d.setPaint(new GradientPaint(0, bounds.y, progressTrackBgBottom, 0, bounds.y + bounds.height,
					progressTrackBgTop));
		}
		g2d.fill(ps);

		// Progress border
		g2d.setPaint(component.isEnabled() ? progressBorderColor : Color.LIGHT_GRAY);
		g2d.draw(ps);
		// }

		// Track border & focus
		// {
		// Track border
		// g2d.setPaint(component.isEnabled()
		// ? rolloverDarkBorderOnly && !paintParameters.dragging ? getBorderColor() :
		// Color.GRAY
		// : Color.LIGHT_GRAY);
		// g2d.setPaint(Color.lightGray);
		g2d.draw(ss);
		// }
	}

	protected Shape getTrackShape()
	{
		if (trackRound > 0)
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				return new RoundRectangle2D.Double(trackRect.x - trackRound,
						trackRect.y + trackRect.height / 2 - trackHeight / 2, trackRect.width + trackRound * 2 - 1,
						trackHeight, trackRound * 2, trackRound * 2);
			} else
			{
				return new RoundRectangle2D.Double(trackRect.x + trackRect.width / 2 - trackHeight / 2,
						trackRect.y - trackRound, trackHeight, trackRect.height + trackRound * 2 - 1, trackRound * 2,
						trackRound * 2);
			}
		} else
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				return new Rectangle2D.Double(trackRect.x, trackRect.y + trackRect.height / 2 - trackHeight / 2,
						trackRect.width - 1, trackHeight);
			} else
			{
				return new Rectangle2D.Double(trackRect.x + trackRect.width / 2 - trackHeight / 2, trackRect.y,
						trackHeight, trackRect.height - 1);
			}
		}
	}

	@Override
	public void paintLabels(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (slider.isEnabled())
		{
			g2d.setColor(new Color(0, 0, 90));
		} else
		{
			g2d.setColor(new Color(172, 172, 172));
		}

		Rectangle labelBounds = labelRect;

		Dictionary dictionary = slider.getLabelTable();
		if (dictionary != null)
		{
			Enumeration keys = dictionary.keys();
			int minValue = slider.getMinimum();
			int maxValue = slider.getMaximum();
			boolean enabled = slider.isEnabled();
			while (keys.hasMoreElements())
			{
				Integer key = (Integer) keys.nextElement();
				int value = key.intValue();
				if (value >= minValue && value <= maxValue)
				{
					JLabel label = (JLabel) dictionary.get(key);
					label.setFont(new Font("Tahoma", Font.PLAIN, 10));
					label.setEnabled(enabled);

					Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();

					if (icon instanceof ImageIcon)
					{
						// Register Slider as an image observer. It allows to catch notifications about
						// image changes (e.g. gif animation)
						Toolkit.getDefaultToolkit().checkImage(((ImageIcon) icon).getImage(), -1, -1, slider);
					}

					if (slider.getOrientation() == JSlider.HORIZONTAL)
					{
						g2d.translate(0, labelBounds.y);

						int labelCenter = xPositionForValue(value);
						int labelLeft = labelCenter - (label.getPreferredSize().width / 2);
						g2d.translate(labelLeft, 0);
						g2d.drawString(label.getText(), 0, 10);
						// label.paint(g2d);
						g2d.translate(-labelLeft, 0);

						g2d.translate(0, -labelBounds.y);
					} else
					{
						int offset = 0;
						// if (!BasicGraphicsUtils.isLeftToRight(slider)) {
						offset = labelBounds.width - label.getPreferredSize().width;
						// }
						g2d.translate(labelBounds.x + offset, 0);

						int labelCenter = yPositionForValue(value);
						int labelTop = labelCenter - (label.getPreferredSize().height / 2);

						g2d.translate(0, labelTop);
						label.paint(g2d);
						g2d.translate(0, -labelTop);

						g2d.translate(-labelBounds.x - offset, 0);
					}
				}
			}
		}
	}

	@Override
	public void paintTicks(Graphics g)
	{
		Rectangle tickBounds = tickRect;

		// g.setColor(DefaultLookup.getColor(slider, this, "Slider.tickColor",
		// Color.black));

		if (slider.isEnabled())
		{
			g.setColor(new Color(0, 0, 0));
		} else
		{
			g.setColor(new Color(192, 192, 192));
		}

		if (slider.getOrientation() == JSlider.HORIZONTAL)
		{
			g.translate(0, tickBounds.y);

			if (slider.getMinorTickSpacing() > 0)
			{
				int value = slider.getMinimum();

				while (value <= slider.getMaximum())
				{
					int xPos = xPositionForValue(value);
					paintMinorTickForHorizSlider(g, tickBounds, xPos);

					// Overflow checking
					if (Integer.MAX_VALUE - slider.getMinorTickSpacing() < value)
					{
						break;
					}

					if (value >= 125)
					{
						value += Math.pow(LogarithmicJSlider.logBase, slider.getMinorTickSpacing());
					} else
					{
						value += slider.getMinorTickSpacing();
					}
				}
			}

			if (slider.getMajorTickSpacing() > 0)
			{
				int value = slider.getMinimum();

				while (value <= slider.getMaximum())
				{
					int xPos = xPositionForValue(value);
					paintMajorTickForHorizSlider(g, tickBounds, xPos);

					// Overflow checking
					if (Integer.MAX_VALUE - slider.getMajorTickSpacing() < value)
					{
						break;
					}

					value += slider.getMajorTickSpacing();
				}
			}

			g.translate(0, -tickBounds.y);
		} else
		{
			g.translate(tickBounds.x, 0);

			if (slider.getMinorTickSpacing() > 0)
			{
				int offset = 0;
				// if (!BasicGraphicsUtils.isLeftToRight(slider))
				// {
				offset = tickBounds.width - tickBounds.width / 2;
				g.translate(offset, 0);
				// }

				int value = slider.getMinimum();

				while (value <= slider.getMaximum())
				{
					int yPos = yPositionForValue(value);
					paintMinorTickForVertSlider(g, tickBounds, yPos);

					// Overflow checking
					if (Integer.MAX_VALUE - slider.getMinorTickSpacing() < value)
					{
						break;
					}

					value += slider.getMinorTickSpacing();
				}

				// if (!BasicGraphicsUtils.isLeftToRight(slider))
				// {
				g.translate(-offset, 0);
				// }
			}

			if (slider.getMajorTickSpacing() > 0)
			{
				// if (!BasicGraphicsUtils.isLeftToRight(slider))
				// {
				g.translate(2, 0);
				// }

				int value = slider.getMinimum();

				while (value <= slider.getMaximum())
				{
					int yPos = yPositionForValue(value);
					paintMajorTickForVertSlider(g, tickBounds, yPos);

					// Overflow checking
					if (Integer.MAX_VALUE - slider.getMajorTickSpacing() < value)
					{
						break;
					}

					value += slider.getMajorTickSpacing();
				}

				// if (!BasicGraphicsUtils.isLeftToRight(slider))
				// {
				g.translate(-2, 0);
				// }
			}
			g.translate(-tickBounds.x, 0);
		}
	}

	@Override
	protected void paintMinorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x)
	{
		g.drawLine(x, 0, x, tickBounds.height / 2 /* - 1 */);
	}

	@Override
	protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x)
	{
		g.drawLine(x, 0, x, tickBounds.height - 1);
	}

	@Override
	protected void paintMinorTickForVertSlider(Graphics g, Rectangle tickBounds, int y)
	{
		g.drawLine(0, y, tickBounds.width / 2 /* - 1 */, y);
	}

	@Override
	protected void paintMajorTickForVertSlider(Graphics g, Rectangle tickBounds, int y)
	{
		g.drawLine(0, y, tickBounds.width - 1, y);
	}

	// private Shape createThumbShape(int width, int height)
	// {
	// Ellipse2D shape = new Ellipse2D.Double(0, 0, width, height);
	// return shape;
	// }
	//
	// @Override
	// public void paintThumb(Graphics g)
	// {
	// Rectangle knobBounds = thumbRect;
	// int w = knobBounds.width;
	// int h = knobBounds.height;
	// Graphics2D g2d = (Graphics2D) g.create();
	// Shape thumbShape = createThumbShape(w - 1, h - 1);
	// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	// g2d.translate(knobBounds.x, knobBounds.y);
	//
	//
	// g2d.setColor(Color.orange);
	// // g2d.draw(thumbShape);
	// g2d.dispose();
	// }

	@Override
	public void paintThumb(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		thumbRect.setSize(thumbRect.width - 3, thumbRect.height - 2);
		thumbRect.setLocation(thumbRect.x + 1, thumbRect.y + 1);

		// Thumb shape
		final Shape ts = getThumbShape();

		// // Thumb shade
		// if ( slider.isEnabled () )
		// {
		// LafUtils.drawShade(g2d, ts, StyleConstants.shadeColor, thumbShadeWidth);
		// }

		// Thumb background
		if (component.getOrientation() == JSlider.HORIZONTAL)
		{
			g2d.setPaint(
					new GradientPaint(0, thumbRect.y, thumbBgTop, 0, thumbRect.y + thumbRect.height, thumbBgBottom));
		} else
		{
			g2d.setPaint(
					new GradientPaint(thumbRect.x, 0, thumbBgTop, thumbRect.x + thumbRect.width, 0, thumbBgBottom));
		}
		g2d.fill(ts);

		// Thumb border
		g2d.setPaint(component.isEnabled() ? Color.GRAY : Color.LIGHT_GRAY);
		g2d.draw(ts);

		// Thumb focus
		// LafUtils.drawCustomWebFocus ( g2d, slider, FocusType.fieldFocus, ts );
	}

	/** Creates a listener to handle track events in the specified slider. */
	@Override
	protected TrackListener createTrackListener(JSlider slider)
	{
		return new RangeTrackListener();
	}

	int thumbRound = 2;
	int thumbAngleLength = 4;
	boolean angledThumb = true;
	boolean sharpThumbAngle = true;
	boolean ltr = true;

	protected Shape getThumbShape()
	{

		Rectangle old = thumbRect;
		if (Toolkit.getDefaultToolkit().getScreenSize().width
				* Toolkit.getDefaultToolkit().getScreenSize().height > 2073600) // 2073600 = 1920 * 1080
		{
			// thumbRect.x /= 1.5;
			thumbRect.y /= 2.5;
			// thumbRect.y -= thumbRect.y / 4;

			thumbRect.x -= thumbRect.width / 3;

			thumbRect.width *= 1.5;
			thumbRect.height *= 1.5;
			// thumbAngleLength += 1;
			thumbRect.height -= 4;
		}

		// thumbRect.setLocation(thumbRect.x, thumbRect.y);
		// thumbRect.setSize(9, 19);
		if (angledThumb && (component.getPaintLabels() || component.getPaintTicks()))
		{
			if (component.getOrientation() == JSlider.HORIZONTAL)
			{
				final GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
				gp.moveTo(thumbRect.x, thumbRect.y + thumbRound);
				gp.quadTo(thumbRect.x, thumbRect.y, thumbRect.x + thumbRound, thumbRect.y);
				gp.lineTo(thumbRect.x + thumbRect.width - thumbRound, thumbRect.y);
				gp.quadTo(thumbRect.x + thumbRect.width, thumbRect.y, thumbRect.x + thumbRect.width,
						thumbRect.y + thumbRound);
				gp.lineTo(thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height - thumbAngleLength);
				if (sharpThumbAngle)
				{
					gp.lineTo(thumbRect.x + thumbRect.width / 2, thumbRect.y + thumbRect.height);
					gp.lineTo(thumbRect.x, thumbRect.y + thumbRect.height - thumbAngleLength);
				} else
				{
					gp.quadTo(thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height - thumbAngleLength / 2,
							thumbRect.x + thumbRect.width / 2, thumbRect.y + thumbRect.height);
					gp.quadTo(thumbRect.x, thumbRect.y + thumbRect.height - thumbAngleLength / 2, thumbRect.x,
							thumbRect.y + thumbRect.height - thumbAngleLength);
				}
				gp.closePath();
				return gp;
			} else
			{
				final GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
				if (ltr)
				{
					gp.moveTo(thumbRect.x, thumbRect.y + thumbRound);
					gp.quadTo(thumbRect.x, thumbRect.y, thumbRect.x + thumbRound, thumbRect.y);
					gp.lineTo(thumbRect.x + thumbRect.width - thumbAngleLength, thumbRect.y);
					if (sharpThumbAngle)
					{
						gp.lineTo(thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height / 2);
						gp.lineTo(thumbRect.x + thumbRect.width - thumbAngleLength, thumbRect.y + thumbRect.height);
					} else
					{
						gp.quadTo(thumbRect.x + thumbRect.width - thumbAngleLength / 2, thumbRect.y,
								thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height / 2);
						gp.quadTo(thumbRect.x + thumbRect.width - thumbAngleLength / 2, thumbRect.y + thumbRect.height,
								thumbRect.x + thumbRect.width - thumbAngleLength, thumbRect.y + thumbRect.height);
					}
					gp.lineTo(thumbRect.x + thumbRound, thumbRect.y + thumbRect.height);
					gp.quadTo(thumbRect.x, thumbRect.y + thumbRect.height, thumbRect.x,
							thumbRect.y + thumbRect.height - thumbRound);
				} else
				{
					gp.moveTo(thumbRect.x + thumbRect.width - thumbRound, thumbRect.y);
					gp.quadTo(thumbRect.x + thumbRect.width, thumbRect.y, thumbRect.x + thumbRect.width,
							thumbRect.y + thumbRound);
					gp.lineTo(thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height - thumbRound);
					gp.quadTo(thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height,
							thumbRect.x + thumbRect.width - thumbRound, thumbRect.y + thumbRect.height);
					gp.lineTo(thumbRect.x + thumbAngleLength, thumbRect.y + thumbRect.height);
					if (sharpThumbAngle)
					{
						gp.lineTo(thumbRect.x, thumbRect.y + thumbRect.height / 2);
						gp.lineTo(thumbRect.x + thumbAngleLength, thumbRect.y);
					} else
					{
						gp.quadTo(thumbRect.x + thumbAngleLength / 2, thumbRect.y + thumbRect.height, thumbRect.x,
								thumbRect.y + thumbRect.height / 2);
						gp.quadTo(thumbRect.x + thumbAngleLength / 2, thumbRect.y, thumbRect.x + thumbAngleLength,
								thumbRect.y);
					}
				}
				gp.closePath();

				thumbRect = old;
				return gp;
			}
		} else
		{
			thumbRect = old;
			return new RoundRectangle2D.Double(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height,
					thumbRound * 2, thumbRound * 2);
		}
	}

	/**
	 * Listener to handle model change events. This calculates the thumb locations
	 * and repaints the slider if the value change is not caused by dragging a
	 * thumb.
	 */
	public class ChangeHandler implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent arg0)
		{
			calculateThumbLocation();
			slider.repaint();
		}
	}

	/** Listener to handle mouse movements in the slider track. */
	public class RangeTrackListener extends TrackListener
	{

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (!slider.isEnabled())
			{
				return;
			}
			currentMouseX -= thumbRect.width / 2; // Because we want the mouse location correspond to middle of the
													 // "thumb", not left side of it.
			moveUpperThumb();
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (!slider.isEnabled())
			{
				return;
			}

			currentMouseX = e.getX();
			currentMouseY = e.getY();

			if (slider.isRequestFocusEnabled())
			{
				slider.requestFocus();
			}

			boolean upperPressed = false;
			if (slider.getMinimum() == slider.getValue())
			{
				if (thumbRect.contains(currentMouseX, currentMouseY))
				{
					upperPressed = true;
				}
			} else
			{
				if (thumbRect.contains(currentMouseX, currentMouseY))
				{
					upperPressed = true;
				}
			}

			if (upperPressed)
			{
				switch (slider.getOrientation())
				{
				case JSlider.VERTICAL:
					offset = currentMouseY - thumbRect.y;
					break;
				case JSlider.HORIZONTAL:
					offset = currentMouseX - thumbRect.x;
					break;
				}
				// upperThumbSelected = true;
				upperDragging = true;
				return;
			}

			upperDragging = false;
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{

			upperDragging = false;
			slider.setValueIsAdjusting(false);
			super.mouseReleased(e);
		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			if (!slider.isEnabled())
			{
				return;
			}

			currentMouseX = e.getX();
			currentMouseY = e.getY();

			if (upperDragging)
			{
				slider.setValueIsAdjusting(true);
				moveUpperThumb();

			}
		}

		@Override
		public boolean shouldScroll(int direction)
		{
			return false;
		}

		/**
		 * Moves the location of the upper thumb, and sets its corresponding value in
		 * the slider.
		 */
		public void moveUpperThumb()
		{
			int thumbMiddle = 0;
			switch (slider.getOrientation())
			{

			case JSlider.HORIZONTAL:
				int halfThumbWidth = thumbRect.width / 2;
				int thumbLeft = currentMouseX - offset;
				int trackLeft = trackRect.x;
				int trackRight = trackRect.x + (trackRect.width - 1);
				int hMax = xPositionForValue(slider.getMaximum() - slider.getExtent());

				if (drawInverted())
				{
					trackLeft = hMax;
				} else
				{
					trackRight = hMax;
				}
				thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
				thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

				setThumbLocation(thumbLeft, thumbRect.y);// setThumbLocation

				thumbMiddle = thumbLeft + halfThumbWidth;
				slider.setValue(valueForXPosition(thumbMiddle));
				break;

			default:
				return;
			}
		}
	}
}

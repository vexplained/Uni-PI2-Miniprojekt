package de.grotefober.nbodysim.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import de.grotefober.nbodysim.sim.PhysicsManager;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.grotefober.nbodysim.sim.physObjects.PhysPointMass;
import de.grotefober.nbodysim.ui.graphics.DynCompoundObject;
import de.grotefober.nbodysim.ui.graphics.PhysicsUniverse2D;
import de.grotefober.nbodysim.ui.graphics.dynObjects.DynCenteredEllipse;
import de.grotefober.nbodysim.ui.graphics.dynObjects.DynamicPhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicComponent;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects.DynArrow;

public class InteractionController
{
	private static final double DEFAULT_MASS = 1E20;
	private static final Color PSEUDO_COLOR = new Color(0x99FFFFFF, true);

	private PhysicsUniverse2D physUniverse;
	private PhysicsManager physMan;
	private MainGUI gui;

	private EInteractionAction currentAction;
	private int clickCounter;
	private double mouseWheelCounter;
	private double mass = DEFAULT_MASS;
	private int[][] positionCache;

	private DynCompoundObject pseudoObject;

	private DynArrow pseudoArrow;
	private DynCenteredEllipse pseudoBody;

	/**
	 * HashMap holding all objects marked for deletion as well as their previous color to restore their original color
	 * when no longer highlighting them.
	 */
	private HashMap<IDynamicComponent, Color> deleteCache;

	public InteractionController(PhysicsUniverse2D physicsUniverse, PhysicsManager physMan, MainGUI gui)
	{
		this.physUniverse = physicsUniverse;
		this.physMan = physMan;
		this.gui = gui;

		this.clickCounter = 0;
		this.mouseWheelCounter = 0;
		this.positionCache = new int[4][2]; // Cache can hold up to 4 mouse positions
		this.currentAction = EInteractionAction.NONE;

		this.deleteCache = new HashMap<>(4);

		PhysicsObject physShadow = new PhysPointMass(0);

		double diameter = MainGUI.radiusFromMass(DEFAULT_MASS) * 2;
		pseudoBody = new DynCenteredEllipse(PSEUDO_COLOR, 0, 0, diameter, diameter);
		pseudoBody.setFill(PSEUDO_COLOR);
		pseudoArrow = new DynArrow(MainGUI.COLOR_VELOCITY, 0, 0, 0, 0);
		pseudoArrow.setStroke(MainGUI.STROKE_ARROW);
		pseudoArrow.setVisible(false);

		DynCompoundObject pseudoObj = new DynCompoundObject(Color.BLACK, 0, 0);
		pseudoObj.setVisible(false);

		pseudoObj.addComponent(pseudoBody);
		pseudoObj.addComponent(pseudoArrow);

		this.pseudoObject = pseudoObj;
		physicsUniverse.addObject(pseudoObj);

		setupListeners();
	}

	private void setupListeners()
	{
		physUniverse.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					handleMouseClicked(e);
				}
			}
		});

		physUniverse.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				super.mouseMoved(e);
				handleMouseMoved(e);
			}
		});

		physUniverse.addMouseWheelListener(new MouseWheelListener()
		{

			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				handleAddMouseWheel(e);
			}
		});
	}

	public void setAction(EInteractionAction action)
	{
		this.currentAction = action;

		if (action == EInteractionAction.ADD_OBJECT)
		{
			// make object visible in MouseMotionListener
			// => otherwise object would float where it was last positioned
			// pseudoObject.setVisible(true);
		} else
		{
			pseudoObject.setVisible(false);
		}
	}

	public void resetAction()
	{
		setAction(EInteractionAction.NONE);
	}

	private void handleMouseClicked(MouseEvent e)
	{
		if (clickCounter == 0)
		{
			// don't reset; keep size
			// mouseWheelCounter = 0;
		}

		switch (currentAction)
		{
		default: // + NONE
			break;
		case ADD_OBJECT:
			handleAddMouseClicked(e);
			break;
		case DELETE_OBJECT:
			handleDeleteMouseClicked(e);
			break;
		}
	}

	private void handleMouseMoved(MouseEvent e)
	{
		if (clickCounter > positionCache.length - 1)
		{
			// failsave
			return;
		}

		switch (currentAction)
		{
		default:
			break;

		case ADD_OBJECT:
			pseudoObject.setVisible(true);
			positionCache[clickCounter][0] = e.getX();
			positionCache[clickCounter][1] = e.getY();

			pseudoObject.moveTo(positionCache[0][0], positionCache[0][1]);
			pseudoArrow.setTip(positionCache[1][0], positionCache[1][1]);
			break;
		case DELETE_OBJECT:
			synchronized (deleteCache)
			{
				deleteCache.clear();
			}

			for (DynamicPhysicsObject comp : physUniverse.getDynPhysicsObjects())
			{
				DynamicObject obj = comp.getDynamicObject();
				if (obj.isInShape(e.getX(), e.getY()))
				{
					System.out.println("in shape");
					deleteCache.put(comp, obj.getColor());
					obj.setColor(MainGUI.COLOR_HIGHLIGHT);
					if (obj instanceof DynCompoundObject)
					{
						((DynCompoundObject) obj).setOutlineVisible(true);
					}
				} else
				{
					System.out.println("n");
					if (obj instanceof DynCompoundObject)
					{
						((DynCompoundObject) obj).setOutlineVisible(false);
					}
				}
			}
			break;
		}
	}

	private void handleAddMouseWheel(MouseWheelEvent e)
	{
		mouseWheelCounter += -e.getPreciseWheelRotation();

		handleMassModified();
	}

	private void handleMassModified()
	{
		double scaleFactor = Math.pow(1.2, mouseWheelCounter);
		this.mass = DEFAULT_MASS * scaleFactor;
		double diameterNew = MainGUI.radiusFromMass(mass) * 2;
		pseudoBody.setSize(diameterNew, diameterNew);
	}

	private void handleAddMouseClicked(MouseEvent e)
	{
		clickCounter++;
		handleAdd();
	}

	private void handleAdd()
	{
		// clickCounter = 0: draw semi-transparent circle for new objet
		// clickCounter = 1: draw opaque circle, make velocity changeable
		// clickCounter = 2: place object & reset
		if (clickCounter == 0)
		{
			pseudoObject.setVisible(true);
			pseudoArrow.setVisible(false);
		} else if (clickCounter == 1)
		{
			// set arrow tip pos to match tail pos upon revealing it
			pseudoArrow.setTip(pseudoArrow.getX(), pseudoArrow.getY());
			pseudoArrow.setVisible(true);
		} else
		{
			// Add real object
			Vector2D pos = new Vector2D.Double(positionCache[0][0], positionCache[0][1]).scale(physMan.DISTANCE_FACTOR);
			Vector2D vel = new Vector2D.Double(positionCache[1][0] - positionCache[0][0],
					positionCache[1][1] - positionCache[0][1]).scale(physMan.DISTANCE_FACTOR);
			System.out.println("pos: %s \t vel: %s".formatted(pos, vel));
			gui.addPhysObject(mass, pos, vel.scale(1 / MainGUI.SCALE_FACTOR_VEL), false, MainGUI.COLOR_OBJECT_DEFAULT);

			// keep modified size until exiting "add object" mode
			// TODO: (hacky)
			double mouseWheelCounterTemp = mouseWheelCounter;
			handleReset();
			mouseWheelCounter = mouseWheelCounterTemp;
			handleMassModified();
		}
	}

	private void handleReset()
	{
		EInteractionAction previousAction = currentAction;
		clickCounter = 0;
		mouseWheelCounter = 0;
		handleAdd(); // reset object visibility
		handleMassModified(); // reset object properties
		resetAction();

		// "restart"
		setAction(previousAction);
	}

	private void handleDeleteMouseClicked(MouseEvent e)
	{
		// clickCounter = 0: highlight hovered-over object (colored outline)
		// clickCounter = 1: delete & reset

		handleReset();
	}

}

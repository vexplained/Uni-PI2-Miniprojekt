package de.grotefober.nbodysim.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.grotefober.nbodysim.sim.PhysicsManager;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.grotefober.nbodysim.sim.physObjects.PhysPointMass;
import de.grotefober.nbodysim.sim.physObjects.dynObjects.DynCenteredEllipse;
import de.grotefober.nbodysim.sim.physObjects.dynObjects.DynamicPhysicsObject;
import de.grotefober.nbodysim.ui.graphics.DynCompoundObject;
import de.grotefober.nbodysim.ui.graphics.DynPhysicsArrow;
import de.grotefober.nbodysim.ui.graphics.PhysicsUniverse2D;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicShape;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects.DynCrosshair;
import de.vexplained.libraries.swingextension.logarithmicSlider.LogarithmicJSlider;

public class MainGUI
{
	/**
	 * Factor to scale displayed vector by. This is purely for aesthetics.
	 */
	public static final double SCALE_FACTOR_ACC = 1000D,
			SCALE_FACTOR_VEL = 100D;

	public static Color COLOR_OBJECT_DEFAULT = new Color(0x02B0E6),
			COLOR_SUN = new Color(0xF5B952),
			COLOR_VELOCITY = Color.GREEN,
			COLOR_ACCELERATION = Color.RED;
	public static final Stroke STROKE_CROSSHAIR = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL),
			STROKE_ARROW = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	private JFrame frame;
	private final ButtonGroup btnGroupPlayPause = new ButtonGroup(), btnGroupTools = new NoneSelectableButtonGroup();
	private PhysicsUniverse2D physUniverse;
	private PhysicsManager physMan;
	private InteractionController interactionController;

	private boolean rdbtnPlaySelected = false;
	private LogarithmicJSlider sliderSimSpeed;
	private JRadioButton rdbtnPlay;
	private JRadioButton rdbtnPause;
	private JToggleButton tglbtnToolAddBody, tglbtnToolDeleteBody;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI()
	{
		// Enable font AA
		System.setProperty("awt.useSystemAAFontSettings", "on");

		initialize();

		initPhysics();

		registerListeners();
	}

	/**
	 * TODO: Refactor to call this method outside the Swing UI dispatch thread (currently this is called from inside
	 * EventQueue#invokeLater)
	 */
	private void initPhysics()
	{
		this.physMan = physUniverse.createPhysicsManager();
		setupExample();
	}

	private void setupExample()
	{
		double massSun = 1.98892E30;
		double massEarth = 5.9722E24;
		// addPhysObject(massSun, new Vector2D.Double(300 * physMan.DISTANCE_FACTOR, 300 * physMan.DISTANCE_FACTOR),
		// false,
		// COLOR_SUN);

		double speedFac = 0.008;

		double borderOffset = 100;
		double center = 400;
		double spacing = 400;
		double leftTop = borderOffset + center - spacing / 2;
		double rightBottom = borderOffset + center + spacing / 2;

		double initialVel = 200;

		addPhysObject(massEarth, new Vector2D.Double(leftTop, leftTop).scale(physMan.DISTANCE_FACTOR),
				new Vector2D.Double(initialVel, -initialVel).scale(physMan.DISTANCE_FACTOR * speedFac),
				false);
		addPhysObject(massEarth, new Vector2D.Double(rightBottom, leftTop).scale(physMan.DISTANCE_FACTOR),
				new Vector2D.Double(initialVel, initialVel).scale(physMan.DISTANCE_FACTOR * speedFac),
				false);
		addPhysObject(massEarth, new Vector2D.Double(rightBottom, rightBottom).scale(physMan.DISTANCE_FACTOR),
				new Vector2D.Double(-initialVel, initialVel).scale(physMan.DISTANCE_FACTOR * speedFac),
				false);
		addPhysObject(massEarth, new Vector2D.Double(leftTop, rightBottom).scale(physMan.DISTANCE_FACTOR),
				new Vector2D.Double(-initialVel, -initialVel).scale(physMan.DISTANCE_FACTOR * speedFac),
				false);

		addPhysObject(massSun,
				new Vector2D.Double(borderOffset + center, borderOffset + center).scale(physMan.DISTANCE_FACTOR), false,
				COLOR_SUN);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		final double preferredToolWidth;

		frame = new JFrame();
		frame.setBounds(100, 100, 720, 480);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolBarLeft = new JToolBar();
		toolBarLeft.setOrientation(SwingConstants.VERTICAL);
		toolBarLeft.setFocusable(false);
		frame.getContentPane().add(toolBarLeft, BorderLayout.WEST);

		tglbtnToolAddBody = new JToggleButton("<html>Add body</html>");
		tglbtnToolAddBody.setFocusable(false);
		tglbtnToolAddBody.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/plus_x32.png")));
		tglbtnToolAddBody.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/plus_green_x32.png")));
		tglbtnToolAddBody.setToolTipText("<html>Toggle \"Add body\" tool.<br><i>Shortcut: <kbd>Alt+N</kbd></i></html>");
		tglbtnToolAddBody.setMnemonic(KeyEvent.VK_N);
		toolBarLeft.add(tglbtnToolAddBody);

		tglbtnToolDeleteBody = new JToggleButton("<html>Delete body</html>");
		tglbtnToolDeleteBody.setFocusable(false);
		tglbtnToolDeleteBody.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/trash_x32.png")));
		tglbtnToolDeleteBody
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/trash_red_x32.png")));
		tglbtnToolDeleteBody
				.setToolTipText("<html>Toggle \"Add body\" tool.<br><i>Shortcut: <kbd>Alt+N</kbd></i></html>");
		tglbtnToolDeleteBody.setMnemonic(KeyEvent.VK_N);
		toolBarLeft.add(tglbtnToolDeleteBody);

		btnGroupTools.add(tglbtnToolAddBody);
		btnGroupTools.add(tglbtnToolDeleteBody);

		JLabel lblMassInfo = new JLabel("<html><i>Note: Scrolling while on the canvas changes the mass.</i></html>");
		lblMassInfo.setPreferredSize(tglbtnToolAddBody.getPreferredSize());
		toolBarLeft.add(lblMassInfo);

		toolBarLeft.addSeparator();

		rdbtnPlay = new JRadioButton("Play");
		rdbtnPlay.setFocusable(false);
		rdbtnPlay
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_active_x32.png")));
		rdbtnPlay.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32.png")));
		rdbtnPlay.setRolloverEnabled(true);
		rdbtnPlay.setRolloverIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_hover_x32.png")));
		toolBarLeft.add(rdbtnPlay);
		btnGroupPlayPause.add(rdbtnPlay);

		rdbtnPause = new JRadioButton("Pause");
		rdbtnPause.setSelected(true);
		rdbtnPause.setFocusable(false);
		rdbtnPause.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32.png")));
		rdbtnPause.setRolloverEnabled(true);
		rdbtnPause.setRolloverIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_hover_x32.png")));
		rdbtnPause
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_active_x32.png")));
		toolBarLeft.add(rdbtnPause);
		btnGroupPlayPause.add(rdbtnPause);

		toolBarLeft.addSeparator();

		JToggleButton tglbtnChangeRule = new JToggleButton("<html>Change magic rule</html>");
		tglbtnChangeRule.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/magic_icon_x32.png")));
		tglbtnChangeRule
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/magic_icon_colorful_x32.png")));
		toolBarLeft.add(tglbtnChangeRule);

		physUniverse = new PhysicsUniverse2D();
		frame.getContentPane().add(physUniverse, BorderLayout.CENTER);

		JToolBar toolBarTop = new JToolBar();
		frame.getContentPane().add(toolBarTop, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		toolBarTop.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblSimSpeed = new JLabel("Simulation speed (time step) multiplier:");
		lblSimSpeed.setToolTipText("Note that for higher speeds, the simulation accuracy decreases.");
		GridBagConstraints gbc_lblSimSpeed = new GridBagConstraints();
		gbc_lblSimSpeed.insets = new Insets(0, 0, 5, 0);
		gbc_lblSimSpeed.gridx = 0;
		gbc_lblSimSpeed.gridy = 0;
		panel.add(lblSimSpeed, gbc_lblSimSpeed);

		sliderSimSpeed = new LogarithmicJSlider();
		sliderSimSpeed.setMinimum(1);
		sliderSimSpeed.setMaximum(50);
		sliderSimSpeed.setValue(2);
		sliderSimSpeed.setPaintTicks(true);
		sliderSimSpeed.setPaintLabels(true);
		sliderSimSpeed.setMinorTickSpacing(1);
		sliderSimSpeed.setMajorTickSpacing(11);
		GridBagConstraints gbc_sliderSimSpeed = new GridBagConstraints();
		gbc_sliderSimSpeed.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderSimSpeed.gridx = 0;
		gbc_sliderSimSpeed.gridy = 1;
		panel.add(sliderSimSpeed, gbc_sliderSimSpeed);

		preferredToolWidth = tglbtnToolDeleteBody.getPreferredSize().getWidth();
		Dimension preferredToolSize = tglbtnToolDeleteBody.getPreferredSize();
		tglbtnToolAddBody.setPreferredSize(preferredToolSize);
		lblMassInfo.setPreferredSize(preferredToolSize);
		rdbtnPlay.setPreferredSize(preferredToolSize);
		rdbtnPause.setPreferredSize(preferredToolSize);
		tglbtnChangeRule.setPreferredSize(preferredToolSize);
	}

	/**
	 * Registers all listeners <i>after</i> initializing all components.
	 */
	private void registerListeners()
	{

		ActionListener alPlayPause = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnPlay.isSelected() && !rdbtnPlaySelected)
				{
					// only enable if previously disabled
					physMan.enableTickScheduler();
				} else
				{
					try
					{
						physMan.disableTickScheduler();
					} catch (InterruptedException | ExecutionException e1)
					{
						// Cull exception
					}
				}

				rdbtnPlaySelected = rdbtnPlay.isSelected();
			}
		};

		rdbtnPlay.addActionListener(alPlayPause);

		rdbtnPause.addActionListener(alPlayPause);

		sliderSimSpeed.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				physMan.setSimulationTimeStep(sliderSimSpeed.getValue());
				// // Restart tick scheduler to apply new time step
				// try
				// {
				// physMan.disableTickScheduler();
				// } catch (InterruptedException | ExecutionException e1)
				// {
				// e1.printStackTrace();
				// }
				// physMan.enableTickScheduler();
			}
		});

		// =====================================
		// Listeners for interacting with canvas
		// =====================================

		interactionController = new InteractionController(physUniverse, physMan, this);

		ActionListener alTool = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				interactionController.setAction(
						tglbtnToolAddBody.isSelected() ? EInteractionAction.ADD_OBJECT : EInteractionAction.NONE);
			}
		};

		tglbtnToolAddBody.addActionListener(alTool);
		tglbtnToolDeleteBody.addActionListener(alTool);

	}

	public void addPhysObject(double mass, int x, int y, boolean fixed)
	{
		addPhysObject(mass, new Vector2D.Double(x, y), fixed);
	}

	public void addPhysObject(double mass, Vector2D position, boolean fixed)
	{
		addPhysObject(mass, position, fixed, COLOR_OBJECT_DEFAULT);
	}

	public void addPhysObject(double mass, Vector2D position, boolean fixed, Color color)
	{
		addPhysObject(mass, position, new Vector2D.Double(), fixed, color);
	}

	public void addPhysObject(double mass, Vector2D position, Vector2D initialVelocity, boolean fixed)
	{
		addPhysObject(mass, position, initialVelocity, fixed, COLOR_OBJECT_DEFAULT);
	}

	public void addPhysObject(double mass, Vector2D position, Vector2D initalVelocity, boolean fixed, Color color)
	{
		// create new DynObj
		// create new PhysObj
		// link DynObj & PhysObj -> DynPhysObj
		// add DynPhysObj to canvas
		// register DynPhysObj at PhysicsManager
		// invalidate canvas?

		PhysicsObject physObj = new PhysPointMass(mass, position);
		physObj.setFixed(fixed);
		physObj.setVelocity(initalVelocity);

		// Scale objects with 10x mass to 1.5x the radius
		double radius = radiusFromMass(mass);
		// double radius = Math.pow(2, Math.log10(mass / 1E20));

		// Position of these objects doesn't matter; only set pos of DynCompoundObject
		DynamicShape sphere = new DynCenteredEllipse(color, 0, 0, radius * 2,
				radius * 2);
		sphere.setFill(color);
		DynCrosshair crosshair = new DynCrosshair(Color.RED, 0, 0, 1, 8, STROKE_CROSSHAIR);

		DynPhysicsArrow arrowAccel = new DynPhysicsArrow(Color.RED, 0, 0, 0, 0, physObj,
				DynPhysicsArrow.ELinkedProperty.ACCELERATION, physMan, SCALE_FACTOR_ACC);
		DynPhysicsArrow arrowSpeed = new DynPhysicsArrow(Color.GREEN, 0, 0, 0, 0, physObj,
				DynPhysicsArrow.ELinkedProperty.VELOCITY, physMan, SCALE_FACTOR_VEL);
		arrowAccel.setStroke(STROKE_ARROW);
		arrowSpeed.setStroke(STROKE_ARROW);

		DynCompoundObject visibleObj = new DynCompoundObject(Color.BLACK, position.getX(), position.getY());
		visibleObj.addComponent(sphere);
		visibleObj.addComponent(crosshair);
		visibleObj.addComponent(arrowAccel);
		visibleObj.addComponent(arrowSpeed);

		DynamicPhysicsObject<DynamicObject, PhysicsObject> dynPhysObj = new DynamicPhysicsObject<>(visibleObj, physObj,
				physMan.DISTANCE_FACTOR);
		physUniverse.addObject(dynPhysObj);
		physMan.addToTickScheduler(dynPhysObj);
	}

	public static double radiusFromMass(double mass)
	{
		return Math.pow(Math.log1p(mass / 1E5), 1.1);
	}
}

/**
 * ButtonGroup which permits to have no selection at all. Useful for groups of toggle buttons where <i>at most</i> one
 * button shall be selected.
 * 
 * @see https://stackoverflow.com/a/16731838
 */
class NoneSelectableButtonGroup extends ButtonGroup
{

	private static final long serialVersionUID = 8890764343014111689L;

	@Override
	public void setSelected(ButtonModel model, boolean selected)
	{
		if (selected)
		{
			super.setSelected(model, selected);
		} else
		{
			clearSelection();
		}
	}
}

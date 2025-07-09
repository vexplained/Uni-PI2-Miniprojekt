package de.grotefober.nbodysim.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
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

	public static Color COLOR_OBJECT_DEFAULT = new Color(0x02B0E6), COLOR_SUN = new Color(0xF5B952);
	private final Stroke crosshairStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL),
			arrowStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private PhysicsUniverse2D physUniverse;
	private PhysicsManager physMan;

	private boolean rdbtnPlaySelected = false;
	private LogarithmicJSlider sliderSimSpeed;
	private JRadioButton rdbtnPlay;
	private JRadioButton rdbtnPause;

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

		double massSun = 1.98892E30;
		double massEarth = 5.9722E24 * 10000;
		// addPhysObject(massSun, new Vector2D.Double(300 * physMan.DISTANCE_FACTOR, 300 * physMan.DISTANCE_FACTOR),
		// false,
		// COLOR_SUN);

		double speedFac = 0.008;

		double borderOffset = 100;
		double center = 400;
		double spacing = 400;
		double leftTop = borderOffset + center - spacing / 2;
		double rightBottom = borderOffset + center + spacing / 2;

		double initialVel = 70;

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

		addPhysObject(massEarth * 4,
				new Vector2D.Double(borderOffset + center, borderOffset + center).scale(physMan.DISTANCE_FACTOR), false,
				COLOR_SUN);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 720, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolBarLeft = new JToolBar();
		toolBarLeft.setOrientation(SwingConstants.VERTICAL);
		toolBarLeft.setFocusable(false);
		frame.getContentPane().add(toolBarLeft, BorderLayout.WEST);

		JToggleButton tglbtnToolAddBody = new JToggleButton("Add body");
		tglbtnToolAddBody.setFocusable(false);
		tglbtnToolAddBody.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/plus_x32.png")));
		tglbtnToolAddBody.setToolTipText("<html>Toggle \"Add body\" tool.<br><i>Shortcut: <kbd>Alt+N</kbd></i></html>");
		tglbtnToolAddBody.setMnemonic(KeyEvent.VK_N);
		toolBarLeft.add(tglbtnToolAddBody);

		JSeparator separator = new JSeparator();
		toolBarLeft.add(separator);

		rdbtnPlay = new JRadioButton("Play");
		rdbtnPlay.setFocusable(false);
		rdbtnPlay
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32_active.png")));
		rdbtnPlay.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32.png")));
		toolBarLeft.add(rdbtnPlay);
		buttonGroup.add(rdbtnPlay);

		rdbtnPause = new JRadioButton("Pause");
		rdbtnPause.setSelected(true);
		rdbtnPause.setFocusable(false);
		rdbtnPause.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32.png")));
		rdbtnPause
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32_active.png")));
		toolBarLeft.add(rdbtnPause);
		buttonGroup.add(rdbtnPause);

		JSeparator separator_1 = new JSeparator();
		toolBarLeft.add(separator_1);

		JToggleButton tglbtnChangeRule = new JToggleButton("Change magic rule");
		toolBarLeft.add(tglbtnChangeRule);

		// JSeparator separator_1 = new JSeparator();
		// toolBar.add(separator_1);

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
	}

	private void addPhysObject(double mass, int x, int y, boolean fixed)
	{
		addPhysObject(mass, new Vector2D.Double(x, y), fixed);
	}

	private void addPhysObject(double mass, Vector2D position, boolean fixed)
	{
		addPhysObject(mass, position, fixed, COLOR_OBJECT_DEFAULT);
	}

	private void addPhysObject(double mass, Vector2D position, boolean fixed, Color color)
	{
		addPhysObject(mass, position, new Vector2D.Double(), fixed, color);
	}

	private void addPhysObject(double mass, Vector2D position, Vector2D initialVelocity, boolean fixed)
	{
		addPhysObject(mass, position, initialVelocity, fixed, COLOR_OBJECT_DEFAULT);
	}

	private void addPhysObject(double mass, Vector2D position, Vector2D initalVelocity, boolean fixed, Color color)
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

		double radius = Math.cbrt(mass) / 1E8D;

		// Position of these objects doesn't matter; only set pos of DynCompoundObject
		DynamicShape sphere = new DynCenteredEllipse(color, 0, 0, radius * 2,
				radius * 2);
		sphere.setFill(color);
		DynCrosshair crosshair = new DynCrosshair(Color.RED, 0, 0, 1, 8, crosshairStroke);

		DynPhysicsArrow arrowAccel = new DynPhysicsArrow(Color.RED, 0, 0, 0, 0, physObj,
				DynPhysicsArrow.ELinkedProperty.ACCELERATION, physMan);
		DynPhysicsArrow arrowSpeed = new DynPhysicsArrow(Color.GREEN, 0, 0, 0, 0, physObj,
				DynPhysicsArrow.ELinkedProperty.VELOCITY, physMan, 100);
		arrowAccel.setStroke(arrowStroke);
		arrowSpeed.setStroke(arrowStroke);

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
}

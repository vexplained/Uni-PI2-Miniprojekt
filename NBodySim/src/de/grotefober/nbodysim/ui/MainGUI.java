package de.grotefober.nbodysim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.grotefober.nbodysim.sim.PhysicsManager;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.grotefober.nbodysim.sim.physObjects.PhysPointMass;
import de.grotefober.nbodysim.sim.physObjects.dynObjects.DynamicPhysicsObject;
import de.grotefober.nbodysim.ui.graphics.PhysicsUniverse2D;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicShape;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects.DynEllipse;

public class MainGUI
{

	public static Color COLOR_OBJECT_DEFAULT = new Color(0x02B0E6);

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private PhysicsUniverse2D physUniverse;
	private PhysicsManager physMan;

	private boolean rdbtnPlaySelected = false;

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
	}

	/**
	 * TODO: Refactor to call this method outside the Swing UI dispatch thread (currently this is called from inside
	 * EventQueue#invokeLater)
	 */
	private void initPhysics()
	{
		this.physMan = physUniverse.createPhysicsManager();

		addPhysObject(1230, new Vector2D.Double(150, 150));

		addPhysObject(1230, new Vector2D.Double(300, 300));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 720, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		toolBar.setFocusable(false);
		frame.getContentPane().add(toolBar, BorderLayout.WEST);

		JToggleButton tglbtnToolAddBody = new JToggleButton("Add body");
		tglbtnToolAddBody.setFocusable(false);
		tglbtnToolAddBody.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/plus_x32.png")));
		tglbtnToolAddBody.setToolTipText("<html>Toggle \"Add body\" tool.<br><i>Shortcut: <kbd>Alt+N</kbd></i></html>");
		tglbtnToolAddBody.setMnemonic(KeyEvent.VK_N);
		toolBar.add(tglbtnToolAddBody);

		JSeparator separator = new JSeparator();
		toolBar.add(separator);

		JRadioButton rdbtnPlay = new JRadioButton("Play");
		rdbtnPlay.setFocusable(false);
		rdbtnPlay
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32_active.png")));
		rdbtnPlay.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32.png")));
		toolBar.add(rdbtnPlay);
		buttonGroup.add(rdbtnPlay);

		JRadioButton rdbtnPause = new JRadioButton("Pause");
		rdbtnPause.setSelected(true);
		rdbtnPause.setFocusable(false);
		rdbtnPause.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32.png")));
		rdbtnPause
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32_active.png")));
		toolBar.add(rdbtnPause);
		buttonGroup.add(rdbtnPause);

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

		// JSeparator separator_1 = new JSeparator();
		// toolBar.add(separator_1);

		physUniverse = new PhysicsUniverse2D();
		frame.getContentPane().add(physUniverse, BorderLayout.CENTER);
	}

	private void addPhysObject(double mass, int x, int y)
	{
		addPhysObject(mass, new Vector2D.Double(x, y));
	}

	private void addPhysObject(double mass, Vector2D position)
	{
		addPhysObject(mass, position, new Vector2D.Double());
	}

	private void addPhysObject(double mass, Vector2D position, Vector2D initialVelocity)
	{
		// create new DynObj
		// create new PhysObj
		// link DynObj & PhysObj -> DynPhysObj
		// add DynPhysObj to canvas
		// register DynPhysObj at PhysicsManager
		// invalidate canvas?

		double radius = mass / 100D;

		DynamicShape sphere = new DynEllipse(COLOR_OBJECT_DEFAULT, position.getX(), position.getY(), radius * 2,
				radius * 2);
		sphere.setFill(COLOR_OBJECT_DEFAULT);

		PhysicsObject physObj = new PhysPointMass(mass, position);

		DynamicPhysicsObject<DynamicShape, PhysicsObject> dynPhysObj = new DynamicPhysicsObject<>(sphere, physObj);
		physUniverse.addObject(dynPhysObj);
		physMan.addToTickScheduler(dynPhysObj);
	}
}

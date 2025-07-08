package de.grotefober.nbodysim.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.grotefober.nbodysim.ui.graphics.PhysicsUniverse2D;

public class MainGUI
{

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();

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
		// try
		// {
		// ImageIO.read(getClass().getResource("/resources/images/plus.png"));
		// } catch (IOException e)
		// {
		// // This should never happen. But in case, who cares what happens with this exception?
		// e.printStackTrace();
		// }
		// tglbtnToolAddBody.setIcon(new ImageIcon(MainGUI.class.getResource("/images/plus.png")));
		tglbtnToolAddBody.setToolTipText("<html>Toggle \"Add body\" tool.<br><i>Shortcut: <kbd>Alt+N</kbd></i></html>");
		tglbtnToolAddBody.setMnemonic(KeyEvent.VK_N);
		toolBar.add(tglbtnToolAddBody);

		JSeparator separator = new JSeparator();
		toolBar.add(separator);

		JRadioButton rdbtnNewRadioButton = new JRadioButton("Play");
		rdbtnNewRadioButton.setFocusable(false);
		rdbtnNewRadioButton
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32_active.png")));
		rdbtnNewRadioButton.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/play_x32.png")));
		toolBar.add(rdbtnNewRadioButton);
		buttonGroup.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Pause");
		rdbtnNewRadioButton_1.setSelected(true);
		rdbtnNewRadioButton_1.setFocusable(false);
		rdbtnNewRadioButton_1.setIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32.png")));
		rdbtnNewRadioButton_1
				.setSelectedIcon(new ImageIcon(MainGUI.class.getResource("/rsc/images/pause_x32_active.png")));
		toolBar.add(rdbtnNewRadioButton_1);
		buttonGroup.add(rdbtnNewRadioButton_1);

		JSeparator separator_1 = new JSeparator();
		toolBar.add(separator_1);

		PhysicsUniverse2D panelCanvas = new PhysicsUniverse2D();
		frame.getContentPane().add(panelCanvas, BorderLayout.CENTER);
	}

}

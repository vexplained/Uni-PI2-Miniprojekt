package de.grotefober.nbodysim.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicCanvas;

public class MainGUI
{

	private JFrame frame;

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

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.9);
		splitPane.setDividerSize(8);
		splitPane.setOneTouchExpandable(true);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JPanel panelControls = new JPanel();
		splitPane.setLeftComponent(panelControls);
		panelControls.setLayout(new GridLayout(1, 0, 0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setFocusable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		panelControls.add(toolBar);

		DynamicCanvas panelCanvas = new DynamicCanvas();
		splitPane.setRightComponent(panelCanvas);
	}

}

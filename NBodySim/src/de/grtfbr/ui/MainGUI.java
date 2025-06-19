package de.grtfbr.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
		GridBagLayout gbl_panelControls = new GridBagLayout();
		gbl_panelControls.columnWidths = new int[] { 0 };
		gbl_panelControls.rowHeights = new int[] { 0 };
		gbl_panelControls.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panelControls.rowWeights = new double[] { Double.MIN_VALUE };
		panelControls.setLayout(gbl_panelControls);

		JPanel panelCanvas = new JPanel();
		splitPane.setRightComponent(panelCanvas);
	}

}

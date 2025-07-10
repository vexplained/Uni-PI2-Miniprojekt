package de.grotefober.nbodysim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class InfoFrame
{

	private JFrame frame;

	private JLabel lblIcon;

	private JLabel lblText;

	public InfoFrame(String title, String htmlText, Icon icon)
	{
		frame = new JFrame();
		frame.setTitle(title);
		frame.setBounds(100, 100, 720, 480);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		lblIcon = new JLabel(icon);
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(lblIcon, BorderLayout.NORTH);

		lblText = new JLabel("<html><p style='text-align: center;'>%s</p></html>".formatted(htmlText));
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setHorizontalAlignment(SwingConstants.CENTER);
		lblText.setAlignmentX(Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(lblText, BorderLayout.CENTER);

		frame.addFocusListener(new FocusAdapter()
		{

			@Override
			public void focusLost(FocusEvent e)
			{
				frame.setVisible(false);
				frame.dispose();
			}
		});
	}

	public void setVisible(boolean visible)
	{
		frame.pack();
		frame.setVisible(visible);
	}

	public void setVisibleRelativeTo(boolean visible, Component parent)
	{
		setVisible(visible);
		frame.setLocationRelativeTo(parent);
	}

	public boolean isVisible()
	{
		return frame.isVisible();
	}

	public String getHTMLText()
	{
		return lblText.getText();
	}

	public void setHTMLText(String htmlText)
	{
		lblText.setText("<html>%s</html>".formatted(htmlText));
		lblText.invalidate();
	}

	public Icon getIcon()
	{
		return lblIcon.getIcon();
	}

	public void setIcon(Icon icon)
	{
		lblIcon.setIcon(icon);
		lblIcon.invalidate();
	}

}

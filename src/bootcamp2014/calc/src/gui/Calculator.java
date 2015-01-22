package bootcamp2014.calc.src.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

import bootcamp2014.calc.src.service.EvaluatorService;

/*
 * A simple calculator using Swing.
 * Testing for Jenkins automated build
 */
public class Calculator extends JFrame {
	private static final long serialVersionUID = 3333L;
	private static int MAX_WIDTH = 275;
	private static int MAX_HEIGHT = 310;
	private static int EXTENDED_WIDTH = 500;
	private CalcTextField display = null;
	private JMenuBar menu = null;
	private JDialog aboutDialog = null;
	private HistoryPanel historyPanel = null;

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Calculator();
			}
		});
	}	
	
	Calculator() {
		init();

		display = new CalcTextField();
		historyPanel = new HistoryPanel();
		menu = initMenuBar();
		JPanel buttons = initButtons();
		JPanel operations = initOperations();

		setJMenuBar(menu);
		setLayout(null);
		
		display.getWidget().setBounds(10, 10, 250, 60);
		buttons.setBounds(10, 80, 170, 170);
		operations.setBounds(190, 80, 70, 170);
		historyPanel.setBounds(270, 10, 210, 240);
		
		add(buttons);
		add(display.getWidget());
		add(operations);
		add(historyPanel);
	}

	private void init() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, fall back to cross-platform
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
				// not worth my time
			}
		}

		setTitle("Simple Calc");
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setSize(MAX_WIDTH, MAX_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void toggleShowHistory() {
		if (getWidth() < 340) {
			setSize(EXTENDED_WIDTH, MAX_HEIGHT);
		} else {
			setSize(MAX_WIDTH, MAX_HEIGHT);
		}
	}

	private void showAbout() {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(this);
		}
		aboutDialog.setVisible(true);
	}

	private JMenuBar initMenuBar() {
		JMenuBar mainMenu = new JMenuBar();

		JMenu editMenu = new JMenu("Edit");
		JMenuItem history = new JMenuItem("History");
		history.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleShowHistory();
			}
		});
		editMenu.add(history);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		});
		helpMenu.add(about);

		mainMenu.add(editMenu);
		mainMenu.add(helpMenu);

		return mainMenu;
	}

	private JPanel initButtons() {
		TextAppender appender = new TextAppender();
		Evaluate evaluator = new Evaluate();

		JPanel numbers = new JPanel();
		numbers.setOpaque(false);
		numbers.setLayout(new GridLayout(4, 3, 3, 3));

		for (int i = 1; i < 10; i++) {
			JButton button = createButton(String.valueOf(i), appender);
			numbers.add(button);
		}
		numbers.add(createButton(".", appender));
		numbers.add(createButton("0", appender));
		numbers.add(createButton("=", evaluator));

		return numbers;
	}

	private JPanel initOperations() {
		TextAppender appender = new TextAppender();
		JPanel operations = new JPanel();
		operations.setOpaque(false);
		operations.setLayout(new GridLayout(5, 1));
		operations.add(createButton("CLR", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				display.clear();
			}
		}));
		operations.add(createButton("/", appender));
		operations.add(createButton("*", appender));
		operations.add(createButton("+", appender));
		operations.add(createButton("-", appender));
		return operations;
	}

	private JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		return button;
	}
	
	class TextAppender implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = e.getActionCommand();
			display.append(text);
		}
	}

	class Evaluate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = display.getWidget().getText();
			String result = "ERROR";
			try {
				result = String.valueOf(EvaluatorService.eval(text));
			} catch (ScriptException e1) {
				e1.printStackTrace();
			}
			display.setResult(result);
			historyPanel.append(text + " = " + result + System.getProperty("line.separator"));
		}
	}
	
	class HistoryPanel extends JPanel {
		private JTextArea text = null;
		
		public HistoryPanel() {
			text = new JTextArea();
			text.setEditable(false);
			
			JScrollPane scrollPane = new JScrollPane(text);
			
			text.setBackground(this.getBackground());
			scrollPane.setBackground(this.getBackground());
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 1), "History", TitledBorder.LEFT, TitledBorder.CENTER));
			setOpaque(false);
			setLayout(new BorderLayout());
			add(scrollPane, BorderLayout.CENTER);
		}
		
		private void append(String str) {
			text.append(str);
		}
		
		private void clear() {
			text.setText("");
		}
	}

	class AboutDialog extends JDialog {
		public AboutDialog(JFrame parent) {
			super(parent, "About", true);

			Box box = Box.createVerticalBox();
			box.add(Box.createGlue());
			box.add(new JLabel("Simple Calculator example using Swing."));
			box.add(new JLabel("It uses JavaScriptEngine to evaluate expressions."));
			box.add(Box.createGlue());
			box.setOpaque(false);
			getContentPane().add(box, "Center");
			
			getContentPane().setBackground(parent.getBackground());
			
			JPanel buttonPanel = new JPanel();
			JButton ok = new JButton("Close");
			buttonPanel.add(ok);
			getContentPane().add(buttonPanel, "South");

			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setVisible(false);
				}
			});
			setSize(250, 150);
			pack();
			setLocationRelativeTo(this);
		}
		
		
	}

	class CalcTextField {
		JTextField textField = null;
		boolean hasResult = false;

		private CalcTextField() {
			Font font = new Font("Verdana", Font.BOLD, 18);
			textField = new JTextField();
			textField.setHorizontalAlignment(JTextField.RIGHT);
			textField.setEditable(false);
			textField.setForeground(new Color(0x6699ff));
			textField.setFont(font);
		}

		public JTextField getWidget() {
			return textField;
		}

		public void clear() {
			textField.setText("");
			hasResult = false;
		}

		public void append(String text) {
			if (hasResult) {
				textField.setText(text);
				hasResult = false;
			} else {
				textField.setText(textField.getText() + text);
			}
		}

		public void setResult(String result) {
			textField.setText(result);
			hasResult = true;
		}
	}
}

package debug.gui.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 

public class InitProgress extends JFrame 
{

    public final static int ONE_SECOND = 1000;

    private JProgressBar progressBar;
    private Timer timer;
    private JTextArea taskOutput;
    private String newline = "\n";

    public InitProgress( final Task task )
    {
        super("Initializing");
       
       	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        //Create the demo's UI.
        progressBar = new JProgressBar(0, task.getLengthOfTask());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(progressBar);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        //Create a timer.
        timer = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                progressBar.setValue(task.getCurrent());
                taskOutput.append(task.getMessage() + newline);
                taskOutput.setCaretPosition(
                        taskOutput.getDocument().getLength());
                if (task.done()) {
//                    Toolkit.getDefaultToolkit().beep();
                    timer.stop();
//                    progressBar.setValue(progressBar.getMinimum());
						setVisible( false);
                }
            }
        });
        pack();
        
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getWidth();
		int h = getHeight();
		
       	setLocation( (dim.width -w)/2, (dim.height-h)/2 );

        setVisible(true);
        
        addWindowFocusListener( focusListener );
        
		task.go();
		timer.start();
    }
    
    protected WindowFocusListener focusListener = new WindowFocusListener()
    {
    	public void windowGainedFocus( WindowEvent e )
    	{
    	}
    	
    	public void windowLostFocus( WindowEvent e )
    	{
    		InitProgress.this.toFront();
    	}
    };

}

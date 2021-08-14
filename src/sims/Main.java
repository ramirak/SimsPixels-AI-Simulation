package sims;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import sims.ai.Simulation;
import sims.map.Building;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

public class Main implements GLEventListener {
	public int size;
	public Building building;
	public Simulation simulation;
	public static JTextArea textArea;
	public static boolean start = false, sound = true;
	public static Clip clip;
	public static int simCounter = 0;

	public static void main(String[] args) {
		// getting the capabilities object of GL2 profile
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The canvas
		final GLCanvas glcanvas = new GLCanvas(capabilities);
		Main main = new Main();
		glcanvas.addGLEventListener(main);
		glcanvas.setSize(1300, 1000);
		glcanvas.display();
		// Setting the main frame
		final JFrame frame = new JFrame("Sims PIXELS");
		// adding canvas to frame
		frame.getContentPane().add(glcanvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frame.getContentPane().getPreferredSize());
		// Show frame
		frame.setVisible(true);
		// Enable animation
		FPSAnimator animator = new FPSAnimator(glcanvas, 60);
		// Center the frame
		frame.setLocation(130, 30);
		// New frame which contains the Sim states and buttons.
		JFrame states = new JFrame("Sim states");
		// Panel which is divided into 2 parts
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setLayout(new FlowLayout());
		// Adding the panel to the frame
		states.add(mainPanel);
		// Buttons
		JButton startButton = new JButton("Start");
		JButton stopButton = new JButton("Stop");
		JButton audioOn = new JButton("Audio On");
		JButton audioOff = new JButton("Audio Off");
		JButton nextSim = new JButton("Next Sim");
		textArea = new JTextArea(5, 20);
		mainPanel.add(textArea);
		mainPanel.add(startButton);
		mainPanel.add(stopButton);
		mainPanel.add(audioOn);
		mainPanel.add(audioOff);
		mainPanel.add(nextSim);
		startButton.setLocation(stopButton.getX() + stopButton.getWidth(), stopButton.getY());
		// Text settings
		textArea.setFont(new Font("Monaco", Font.PLAIN, 20));
		textArea.setBackground(Color.black);
		textArea.setForeground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setBorder(BorderFactory.createCompoundBorder(textArea.getBorder(),
				BorderFactory.createEmptyBorder(30, 30, 30, 30)));
		states.setLocation(frame.getX() + frame.getWidth(), frame.getY());
		states.setSize(400, 570);
		states.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Show second frame
		states.setVisible(true);
		textArea.append(
				"~The Sims Pixels~ \n\nWelcome!\nPlease click on the start button\nto initiate the simulation :)");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("Loading ..");
				start = true;
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start = false;
				clip.stop();
			}
		});
		audioOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (clip != null) {
					clip.start();
					sound = true;
				}
			}
		});
		audioOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (clip != null) {
					clip.stop();
					sound = false;
				}
			}
		});
		nextSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (simCounter + 1 >= Simulation.numOfSims)
					simCounter = 0;
				else
					simCounter++;

			}
		});
		animator.start();
	}

	public void audio() {
		sound = true;
		try {
			// Open an audio input stream.
			URL url = this.getClass().getClassLoader().getResource("theSims.wav");
			AudioInputStream audioIn = null;
			try {
				audioIn = AudioSystem.getAudioInputStream(url);
			} catch (javax.sound.sampled.UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		double x, y;
		double dx, dy; // size of a square
		dx = 2.0 / size;
		dy = 2.0 / size;
		y = 1;
		for (int i = 0; i < size; i++, y -= dy) {
			x = -1;
			for (int j = 0; j < size; j++, x += dx) {
				switch (building.getHome()[i][j]) // set the right color
				{
				case DEAD_SIM:
					gl.glColor3d(0, 0.6, 0);
					break;
				case SIM1:
					gl.glColor3d(0.737, 0.56, 0.56);
					break;
				case SIM2:
					gl.glColor3d(0.537, 0.36, 0.36);
					break;
				case SIM3:
					gl.glColor3d(0.437, 0.26, 0.26);
					break;
				case DOOR:
					gl.glColor3d(0.3, 0.5, 0.4);
					break;
				case SPACE1:
					gl.glColor3d(1, 0.9, 0.8);
					break;
				case SPACE2:
					gl.glColor3d(1, 0.8, 0.7);
					break;
				case WALL:
					gl.glColor3d(0.4, 0.2, 0);
					break;
				case TABLE:
					gl.glColor3d(1, 0.7, 0.4);
					break;
				case CHAIR:
					gl.glColor3d(0.5, 0, 0);
					break;
				case PC_CHAIR:
					gl.glColor3d(0, 0, 0.5);
					break;
				case FRIDGE:
					gl.glColor3d(0.25, 0.25, 0.25);
					break;
				case FRIDGE_ADD:
					gl.glColor3d(0.35, 0.35, 0.35);
					break;
				case SOFA:
					gl.glColor3d(0.75, 0, 0);
					break;
				case SOPA_ADD:
					gl.glColor3d(0.4, 0, 0);
					break;
				case BED:
					gl.glColor3d(0.6, 0.6, 1);
					break;
				case PILLOWS:
					gl.glColor3d(0.5, 0.5, 0.8);
					break;
				case SALOON_TABLE:
					gl.glColor3d(0.6, 0.4, 0.2);
					break;
				case TV:
					gl.glColor3d(0.1, 0.1, 0.1);
					break;
				case PC:
					gl.glColor3d(0.2, 0.2, 0.2);
					break;
				case PHONE:
					gl.glColor3d(0.25, 0.25, 0.25);
					break;
				case BATHROOM1:
					gl.glColor3d(0.8, 0.8, 0.8);
					break;
				case BATHROOM2:
					gl.glColor3d(1, 1, 1);
					break;
				case GARDEN:
					gl.glColor3d(0, 0.5, 0);
					break;
				case POOL_WATER1:
					gl.glColor3d(0, 0.9, 1);
					break;
				case POOL_WATER2:
					gl.glColor3d(0, 1, 1);
					break;
				case POOL:
					gl.glColor3d(0, 0.4, 0);
					break;
				case COUNTER:
					gl.glColor3d(0.5, 0, 0);
					break;
				case MICROWAVE:
					gl.glColor3d(0.35, 0.35, 0.35);
					break;
				case MICROWAVE_ADD:
					gl.glColor3d(0.45, 0.45, 0.45);
					break;
				case SHOWER:
					gl.glColor3d(0.35, 0.35, 0.35);
					break;
				case SHOWER_F:
					gl.glColor3d(0.9, 0.9, 0.9);
					break;
				case SINK:
					gl.glColor3d(0.35, 0.35, 0.35);
					break;
				case TOILET:
					gl.glColor3d(0.35, 0.35, 0.35);
					break;
				case TOILET_ADD:
					gl.glColor3d(0.3, 0.3, 0.3);
					break;
				case TABLE_DECO:
					gl.glColor3d(0.82, 0.41, 0.11);
					break;
				default:
					break;

				}
				gl.glBegin(GL2.GL_POLYGON);// static field
				gl.glVertex2d(x, y); // left-top corner
				gl.glVertex2d(x + dx, y); // right-top corner
				gl.glVertex2d(x + dx, y - dy); // right-bottom corner
				gl.glVertex2d(x, y - dy); // left-bottom corner
				gl.glEnd();
			}
		}
		if (start) {
			this.simulation.run();
			if (sound && (clip == null || !clip.isRunning()))
				clip.start();

		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		this.building = new Building();
		this.simulation = new Simulation(building);
		audio();
		this.size = building.getMSZ();
		gl.glOrtho(-1, 1, -1, 1, -1, 1);
		gl.glBegin(GL2.GL_QUADS);
	}
}
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;


public class Main {
	public static void main(String[] args) throws IOException {
		// Setting up PC connection
		LCD.drawString("Available", 0, 0);
		USBConnection conn = USB.waitForConnection();
		DataInputStream dIn = conn.openDataInputStream();
		
		while (true) 
		{
            int b;
            try
            {
                b = dIn.readInt();
            }
            catch (EOFException e) 
            {
                break;
            }         
            // Show next move of robot
            LCD.drawString("Move is: ", 0, 1);
            LCD.drawString((char)((b/1000%10-1) + 'A') + " " + (char)((b/100%10) + '0') + "-" + (char)((b/10%10-1) + 'A') + " " + (char)(b%10 + '0'), 0, 2);
		}
        dIn.close();
        conn.close();
        
        /* 
         * Sample robot move (not configured to work with the received moves).
        */
        
		// motor A is for going up and down
		// motor B is for rotating left and right 
		// motor C is for going back and forth with the wheels
		// Motor.A.setSpeed(50); // set speed of A
		Motor.B.setSpeed(50); // set speed of B
		Motor.C.rotate(-800); // move C to a set position that the robot will always go to
		Motor.A.rotate(-120); // crane goes down
		Button.waitForAnyPress(); // robot waits for you to close the hand and press on any buttons to restart
		Motor.A.rotate(120); // robot goes back up after grabbing the pawn
		Motor.C.rotate(150); // moves the pawn to the next position
		Motor.A.rotate(-120); // puts the pawn on the board
		Button.waitForAnyPress(); // waits to open the hand
		Motor.A.rotate(120); // goes back up
		Motor.C.rotate(-150); // and goes back to its original position like everytime
	}
}

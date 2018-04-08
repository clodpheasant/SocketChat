package com.fs.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Useful constants and methods used throughout the app
 * @author FS
 *
 */
public class Constants {
	
	public static final int PORT_NUMBER = 8891;
	
	public static final Random r = new Random();
	public static final String[] adjectives = {"Good", "Dreamy", "Stubborn", "Sleepy", "Cute",
			"Playful", "Hyperactive", "Generous"};
	public static final String[] names = {"Dog", "Cat", "Giraffe", "Hippo", "Lion", "Monkey",
			"Capibara", "Cow", "Goose", "Pidgeon"};
	
	public static final String WELCOME = "someone new! :)";
	public static final String GOODBYE = "someone left :(";
	
	public static String getRandomName() {
		return adjectives[r.nextInt(adjectives.length)]
				+ names[r.nextInt(names.length)]
				+ (r.nextInt(100)+1);
	}
	
	public static String getHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
	
}

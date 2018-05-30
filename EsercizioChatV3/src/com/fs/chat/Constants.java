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
	private static final String[] adjectives = {"Handsome", "Dreamy", "Stubborn", "Sleepy", "Cute",
			"Playful", "Hyperactive", "Generous", "Loving", "Considerate", "Flamboyant"};
	private static final String[] names = {"Dog", "Cat", "Giraffe", "Hippo", "Lion", "Monkey",
			"Capibara", "Cow", "Goose", "Pidgeon", "Squid", "Whale", "Chicken"};
	
	public static final String WELCOME = "someone new! :)";
	public static final String GOODBYE = "someone left :(";
	public static final String NO_CONNECTION = "couldn't connect to the server.";
	
	public static String getRandomName() {
		return adjectives[r.nextInt(adjectives.length)]
				+ names[r.nextInt(names.length)]
				+ (r.nextInt(100)+1);
	}
	
	public static String getHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
	
}

package com.scfs.common.utils;

import java.net.InetAddress;
import java.util.Map;

/**
 * 该类从org.hibernate.id.UUIDHexGenerator.java中搬来
 * 算法为uuid.hex
 * 与java.util.UUID生成的uuid的不同之处：该类生成的id有序，java.util.UUID生成的完全无序
 * <b>uuid</b><br>
 * <br>
 * A <tt>UUIDGenerator</tt> that returns a string of length 32,
 * This string will consist of only hex digits. Optionally,
 * the string may be generated with separators between each
 * component of the UUID.
 *
 * Mapping parameters supported: separator.
 *
 * @author Gavin King
 */
public class UUIDHexGenerator {

	private static String sep = "";
	
	private static final int IP;
	
	private static String formatedIP = "";
	
	private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );
	
	private static String formatedJVM = "";
	
	static {
		int ipadd;
		try {
			ipadd = toInt( InetAddress.getLocalHost().getAddress() );
		}
		catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
		formatedIP = format( getIP());
		formatedJVM = format( getJVM());
	}
	private static short counter = (short) 0;
	

	public static String generate() {
		return formatedIP + sep
				+ formatedJVM + sep
				+ format( getHiTime() ) + sep
				+ format( getLoTime() ) + sep
				+ format( getCount() );
	}

	private static String format(int intValue) {
		String formatted = Integer.toHexString( intValue );
		StringBuilder buf = new StringBuilder( "00000000" );
		buf.replace( 8 - formatted.length(), 8, formatted );
		return buf.toString();
	}

	private static String format(short shortValue) {
		String formatted = Integer.toHexString( shortValue );
		StringBuilder buf = new StringBuilder( "0000" );
		buf.replace( 4 - formatted.length(), 4, formatted );
		return buf.toString();
	}
	
	/**
	 * Unique across JVMs on this machine (unless they load this class
	 * in the same quater second - very unlikely)
	 */
	private static int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there
	 * are > Short.MAX_VALUE instances created in a millisecond)
	 */
	protected static short getCount() {
		synchronized(UUIDHexGenerator.class) {
			if (counter<0) counter=0;
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	private static int getIP() {
		return IP;
	}

	/**
	 * Unique down to millisecond
	 */
	private static short getHiTime() {
		return (short) ( System.currentTimeMillis() >>> 32 );
	}
	private static int getLoTime() {
		return (int) System.currentTimeMillis();
	}
	
	/**
	 * Get the config value as a {@link String}
	 *
	 * @param name The config setting name.
	 * @param values The map of config values
	 *
	 * @return The value, or null if not found
	 */
	@SuppressWarnings("unused")
	private String getString(String name, Map<String, Object> values) {
		Object value = values.get( name );
		if ( value == null ) {
			return null;
		}
		if ( String.class.isInstance( value ) ) {
			return (String) value;
		}
		return value.toString();
	}
	
	/**
	 * Custom algorithm used to generate an int from a series of bytes.
	 * <p/>
	 * NOTE : this is different than interpreting the incoming bytes as an int value!
	 *
	 * @param bytes The bytes to use in generating the int.
	 *
	 * @return The generated int.
	 */
	private static int toInt(byte[] bytes) {
		int result = 0;
		for ( int i = 0; i < 4; i++ ) {
			result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(generate());
	}
}
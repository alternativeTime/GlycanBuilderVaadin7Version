/*
 * Copyright (c) 2014. Matthew Campbell <matthew.campbell@mq.edu.au>, David R. Damerell <david@nixbioinf.org>.
 *
 * This file is part of GlycanBuilder Vaadin Release and its affliated projects EUROCarbDB, UniCarb-DB and UniCarbKB.
 *
 * This program is free software free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GlycanBuilder Vaadin Release is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License (LICENSE.txt) for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GlycanBuilder Vaadin Release.  If not, see <http ://www.gnu.org/licenses/>.
 */
package ac.uk.icl.dell.vaadin.canvas.basiccanvas.font;

/**
 * Exception when a character value is out of range 
 *
 */
@SuppressWarnings("serial")
public class FontCharacterException extends Exception {

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public FontCharacterException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public FontCharacterException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of 
	 * (cause==null ? null : cause.toString()) 
	 * (which typically contains the class and detail message of cause). 
	 * This constructor is useful for exceptions that are little more than 
	 * wrappers for other throwables (for example, PrivilegedActionException). 
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). 
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public FontCharacterException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.<br>
	 * Note that the detail message associated with cause is not automatically incorporated 
	 * in this exception's detail message. 
	 * @param message the detail message (which is saved for later retrieval by 
	 * the Throwable.getMessage() method).
	 * @param cause the cause (which is saved for later retrieval by 
	 * the Throwable.getCause() method). 
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public FontCharacterException(String message, Throwable cause) {
		super(message, cause);
	}

}

// $Header: /home/cvs/jakarta-jmeter/src/jorphan/org/apache/jorphan/util/ClassContext.java,v 1.6 2005/07/12 20:51:04 mstover1 Exp $
/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package app.util.logging;

/**
 * Class to get access to the protected getClassContext() method of
 * SecurityManager, thus obtaining the call stack.
 * 
 * May not work with applications that install their own security managers.
 * 
 * @version $Revision: 1.6 $ last updated $Date: 2005/07/12 20:51:04 $
 */
public final class ClassContext extends SecurityManager {
	/**
	 * Private constructor to prevent instantiation.
	 */
	private ClassContext() {
	}

	private static ClassContext _instance = new ClassContext();

	/*
	 * N.B. Both static routines pick up the instance context directly This
	 * ensures that both return the same stack depth
	 */

	/**
	 * Gets the calling context as an array of classes Class[0] is this class.
	 * 
	 * @return Class[] - list of classes in the callers context
	 */
	public static Class[] getMyClassContext() {
		return _instance.getClassContext();
	}

	/**
	 * Get the name of the class at a particular stack depth i=0 gives this
	 * class
	 * 
	 * @param i -
	 *            stack depth
	 * @return String - name of class at depth i
	 */
	public static String getCallerClassNameAt(int i) {
		Class[] cs = _instance.getClassContext();
		return cs[i].getName();
	}
	
	/**
	 * Get the name of the class at a particular stack depth i=0 gives this
	 * class
	 * 
	 * @param i -
	 *            stack depth
	 * @return String - name of class at depth i
	 */
	public static Class getCallerClassName() {
		Class[] cs = _instance.getClassContext();
		return cs[2];
	}

}

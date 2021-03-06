/**
 * Project: a00967008_lab09
 * File: Lab9.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.database;

/**
 * @author Alan, A00967008
 *
 */
public interface DbConstants {

	String DB_PROPERTIES_FILENAME = "db.properties";
	String DB_DRIVER_KEY = "db.driver";
	String DB_URL_KEY = "db.url";
	String DB_USER_KEY = "db.user";
	String DB_PASSWORD_KEY = "db.password";
	String TABLE_ROOT = "A00967008_";
	String STUDENT_TABLE_NAME = TABLE_ROOT + "Student";
	String COURSE_TABLE_NAME = TABLE_ROOT + "Course";
	String ENROLLMENT_TABLE_NAME = TABLE_ROOT + "Enrollment";
}

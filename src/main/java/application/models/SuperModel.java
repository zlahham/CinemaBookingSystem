package application.models;

import java.time.format.DateTimeFormatter;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Ancestor/parent class for all models; meant to contain
 * 	<dd> fields and methods potentially useful to all models.
 * 	<dd> 
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public abstract class SuperModel {
    public static final DateTimeFormatter firebaseDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}

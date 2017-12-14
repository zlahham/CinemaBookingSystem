package application.models;

import java.time.format.DateTimeFormatter;

public abstract class SuperModel {
    public static final DateTimeFormatter firebaseDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}

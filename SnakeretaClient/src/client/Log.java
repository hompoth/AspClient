package client;

import javafx.application.Platform;

public class Log {
    //private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static void println(Object message){
        if(message != null) {
            //Calendar cal = Calendar.getInstance();
            //Platform.runLater(() -> Program.INSTANCE.textArea.appendText(dateFormat.format(cal.getTime()) + " : " + message + "\n"));
        	Platform.runLater(() -> System.out.println(message));
        }
    }
    public static void test(String message){
        //Calendar cal = Calendar.getInstance();
        //Platform.runLater(() -> Program.INSTANCE.textArea.appendText(dateFormat.format(cal.getTime()) + " : " + message + "\n"));
    	Platform.runLater(() -> System.out.println("Message: "+message));
    }
}
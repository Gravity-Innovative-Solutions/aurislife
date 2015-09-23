package in.gravitykerala.aurislife.foregroundservice;

/**
 * Created by Prakash on 8/22/2015.
 */
public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.truiton.foregroundservice.action.main";
        String PREV_ACTION = "com.truiton.foregroundservice.action.prev";
        String PLAY_ACTION = "com.truiton.foregroundservice.action.play";
        String NEXT_ACTION = "com.truiton.foregroundservice.action.next";
        String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int SERVICE_FOREGROUND = 101;
        int SERVICE_HEALTH_RECORD = 103;
        int SERVICE_HEALTH_RECORD_RESULT = 104;
        int SERVICE_RESULT = 102;
    }
}
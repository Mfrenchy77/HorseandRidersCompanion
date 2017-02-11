package com.frenchfriedtechnology.horseandriderscompanion.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constants for Horse and Riders Companion
 */

public class Constants {

    //firebase url keys
    public final static String RIDER_PROFILE = "Rider_Profile";
    public final static String HORSE_PROFILE = "Horse_Profile";

    public final static String RIDER_SKILL_TREE = "Rider_Skill_Tree";
    public final static String HORSE_SKILL_TREE = "Horse_Skill_Tree";

    public final static String CATEGORIES = "Categories";
    public static final String RESOURCES = "Resources";
    public static final String MESSAGES = "Messages";
    public final static String SKILLS = "Skills";
    public final static String LEVELS = "Levels";

    /**
     * Possible level states
     */
    @IntDef({NO_PROGRESS, LEARNING, COMPLETE, VERIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LevelState {
    }

    public static final int NO_PROGRESS = 0;
    public static final int LEARNING = 1;
    public static final int COMPLETE = 2;
    public static final int VERIFIED = 3;

    /**
     * Possible message state
     */
    @IntDef({UNREAD, READ, All})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageState {
    }

    public static final int UNREAD = 1;
    public static final int READ = 2;
    public static final int All = 3;

    /**
     * Possible message types
     */
    @IntDef({STUDENT_REQUEST, INSTRUCTOR_REQUEST, EDIT_REQUEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageType {
    }

    public static final int STUDENT_REQUEST = 1;
    public static final int INSTRUCTOR_REQUEST = 2;
    public static final int EDIT_REQUEST = 3;
}

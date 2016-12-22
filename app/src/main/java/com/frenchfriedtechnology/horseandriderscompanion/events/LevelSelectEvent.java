package com.frenchfriedtechnology.horseandriderscompanion.events;

import android.support.annotation.StringDef;
import android.support.annotation.StringRes;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class LevelSelectEvent {
    @StringDef({NEW_LEVEL, EDIT_LEVEL, RIDER_ADJUST, HORSE_ADJUST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tags {
    }

    public static final String NEW_LEVEL = "NEW_LEVEL";
    public static final String EDIT_LEVEL = "EDIT_LEVEL";
    public static final String RIDER_ADJUST = "RIDER_ADJUST";
    public static final String HORSE_ADJUST = "HORSE_ADJUST";

    private Level level;
    private String tag;
    private String skillId;

    public LevelSelectEvent(@StringRes String tag, Level level, String skillId) {
        this.tag = tag;
        this.level = level;
        this.skillId = skillId;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getTag() {
        return tag;
    }

    public Level getLevel() {
        return level;
    }
}

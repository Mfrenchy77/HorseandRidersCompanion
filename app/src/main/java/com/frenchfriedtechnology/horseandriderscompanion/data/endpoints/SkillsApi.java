package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.List;

/**
 * Crud interface for Firebase and SKills
 */

public class SkillsApi {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void createOrUpdateSkill(Skill skill, String path) {
        //create Skill
        databaseReference.child(path)
                .child(Constants.SKILLS)
                .child(skill.getId())
                .setValue(skill);
    }

    public void getSkills(String path, SkillCallback callback) {
        //get all Skills
        RxFirebaseDatabase.observeValueEvent(databaseReference
                .child(path)
                .child(Constants.SKILLS), DataSnapshotMapper.listOf(Skill.class))
                .subscribe(callback::onSuccess, callback::onError);
    }

    public void deleteSkill(String deletedSkillId, String path) {
        //delete Skill
        databaseReference.child(path)
                .child(Constants.SKILLS)
                .child(deletedSkillId)
                .removeValue();
    }


    public interface SkillCallback {

        void onSuccess(List<Skill> skills);

        void onError(final Throwable throwable);
    }
}

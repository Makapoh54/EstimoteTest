package estimote.com.estimotetest.database;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import estimote.com.estimotetest.model.Note;
import estimote.com.estimotetest.model.User;


public final class Snapshot {
    public static List<User> toUsers(DataSnapshot dataSnapshot) {
        ArrayList<User> users = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
            user.setKey(snapshot.getKey());
            users.add(user);
        }
        return users;
    }

    public static LinkedHashMap<String, Note> toPosts(DataSnapshot dataSnapshot) {
        LinkedHashMap<String, Note> postList = new LinkedHashMap<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Note note = snapshot.getValue(Note.class);
            postList.put(snapshot.getKey(), note);
        }
        return postList;
    }
}

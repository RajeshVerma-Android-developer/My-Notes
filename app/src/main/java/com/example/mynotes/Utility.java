package com.example.mynotes;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static DocumentReference getCollectionReferenceForNotes() {
        return FirebaseFirestore.getInstance().collection("notes").
                document("myNotes").collection("list").document();
    }

//    @SuppressLint("SimpleDateFormat")
//    static String timestampToString(Timestamp timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
//        String dateStr = sdf.format(timestamp);
//        return dateStr;
//    }
}

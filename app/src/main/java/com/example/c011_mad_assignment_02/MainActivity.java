package com.example.c011_mad_assignment_02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> tasks = new ArrayList<>();
    private List<String> docIds = new ArrayList<>(); // parallel list of document ids

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	// init
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TaskAdapter(tasks, docIds, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Real-time listener: order by numeric priorityValue ascending (1=High)
        Query query = db.collection("tasks").orderBy("priorityValue", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, "Listen failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                tasks.clear();
                docIds.clear();
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        // we'll just rebuild the whole list for simplicity
                    }
                    for (int i = 0; i < value.size(); i++) {
                        // convert each snapshot to Task
                        Task t = value.getDocuments().get(i).toObject(Task.class);
                        tasks.add(t);
                        docIds.add(value.getDocuments().get(i).getId());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        // Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override public boolean onMove(@NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            @NonNull RecyclerView.ViewHolder target) { return false; }
            @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                String docId = docIds.get(pos);
                db.collection("tasks").document(docId).delete()
                        .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddTaskActivity.class)));
    }
}

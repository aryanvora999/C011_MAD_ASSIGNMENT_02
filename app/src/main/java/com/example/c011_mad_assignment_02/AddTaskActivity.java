package com.example.c011_mad_assignment_02;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editDescription;
    private Spinner spinnerPriority;
    private Button btnSave;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editDescription = findViewById(R.id.editDescription);
        spinnerPriority = findViewById(R.id.prioritySpinner);
        btnSave = findViewById(R.id.btnSave);
        db = FirebaseFirestore.getInstance();

        String[] items = new String[] {"High", "Medium", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            String desc = editDescription.getText().toString().trim();
            String priority = spinnerPriority.getSelectedItem().toString();

            if (desc.isEmpty()) {
                Toast.makeText(this, "Enter description", Toast.LENGTH_SHORT).show();
                return;
            }

            // auto doc id
            String docId = db.collection("tasks").document().getId();
            int pv = priorityToValue(priority);
            Task task = new Task(docId, desc, priority, pv);

            db.collection("tasks").document(docId).set(task)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Add failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private int priorityToValue(String p) {
        switch (p) {
            case "High": return 1;
            case "Medium": return 2;
            default: return 3;
        }
    }
}

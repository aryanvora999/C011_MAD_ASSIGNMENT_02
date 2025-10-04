package com.example.c011_mad_assignment_02;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText editDescription;
    private Spinner spinnerPriority;
    private Button btnUpdate;
    private FirebaseFirestore db;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        editDescription = findViewById(R.id.editDescription);
        spinnerPriority = findViewById(R.id.prioritySpinner);
        btnUpdate = findViewById(R.id.btnUpdate);
        db = FirebaseFirestore.getInstance();

        String[] items = new String[] {"High", "Medium", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        docId = getIntent().getStringExtra("docId");
        if (docId == null) {
            Toast.makeText(this, "No doc id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("tasks").document(docId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Task t = documentSnapshot.toObject(Task.class);
                    if (t != null) {
                        editDescription.setText(t.getDescription());
                        if ("High".equals(t.getPriority())) spinnerPriority.setSelection(0);
                        else if ("Medium".equals(t.getPriority())) spinnerPriority.setSelection(1);
                        else spinnerPriority.setSelection(2);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Fetch failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        btnUpdate.setOnClickListener(v -> {
            String newDesc = editDescription.getText().toString().trim();
            String newPriority = spinnerPriority.getSelectedItem().toString();
            if (newDesc.isEmpty()) {
                Toast.makeText(this, "Enter description", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> updates = new HashMap<>();
            updates.put("description", newDesc);
            updates.put("priority", newPriority);
            updates.put("priorityValue", priorityToValue(newPriority));

            db.collection("tasks").document(docId).update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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

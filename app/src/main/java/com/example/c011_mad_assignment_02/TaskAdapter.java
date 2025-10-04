package com.example.c011_mad_assignment_02;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final List<Task> tasks;
    private final List<String> docIds;
    private final Context context;

    public TaskAdapter(List<Task> tasks, List<String> docIds, Context context) {
        this.tasks = tasks;
        this.docIds = docIds;
        this.context = context;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We are still inflating the same layout file, as it contains the new UI
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task t = tasks.get(position);
        holder.desc.setText(t.getDescription());
        holder.priority.setText(t.getPriority());

        // ** NEW: Color code the priority indicator bar instead of the text **
        int priorityColor;
        switch (t.getPriority()) {
            case "High":
                priorityColor = ContextCompat.getColor(context, R.color.priority_high);
                break;
            case "Medium":
                priorityColor = ContextCompat.getColor(context, R.color.priority_medium);
                break;
            case "Low":
            default:
                priorityColor = ContextCompat.getColor(context, R.color.priority_low);
                break;
        }
        holder.priorityIndicator.setBackgroundColor(priorityColor);

        // This remains the same
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateTaskActivity.class);
            intent.putExtra("docId", docIds.get(position));
            context.startActivity(intent);
        });
    }

    @Override public int getItemCount() { return tasks.size(); }

    // ** UPDATED ViewHolder to include the new priority indicator View **
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc, priority;
        View priorityIndicator; // <-- Add reference for the indicator bar

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.textDescription);
            priority = itemView.findViewById(R.id.textPriority);
            priorityIndicator = itemView.findViewById(R.id.priority_indicator); // <-- Find the view by its ID
        }
    }
}

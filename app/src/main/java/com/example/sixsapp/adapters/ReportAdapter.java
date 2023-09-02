package com.example.sixsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixsapp.R;
import com.example.sixsapp.pojo.Pair;
import com.example.sixsapp.pojo.ReportResponse;
import com.example.sixsapp.utilis.Global;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    public Context context;
    public List<ReportResponse> reportList;

    public ReportAdapter(Context context, List<ReportResponse> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportResponse response = reportList.get(position);

        holder.mAuditDate.setText(Global.FormatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", response.getAuditDate()));
        holder.mSubmittedDate.setText(Global.FormatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy - hh:mm a", response.getSubmittedDateTime()));
        holder.mDepartment.setText(response.getDeptName());
        holder.mSection.setText(response.getSectionName());
        holder.mRemarks.setText("Remarks: " + response.getRemarks());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAuditDate, mSubmittedDate, mDepartment, mSection, mRemarks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuditDate = itemView.findViewById(R.id.audit_date_tv);
            mSubmittedDate = itemView.findViewById(R.id.submitted_date_tv);
            mDepartment = itemView.findViewById(R.id.department_tv);
            mSection = itemView.findViewById(R.id.section_tv);
            mRemarks = itemView.findViewById(R.id.remarks_tv);
        }
    }
}

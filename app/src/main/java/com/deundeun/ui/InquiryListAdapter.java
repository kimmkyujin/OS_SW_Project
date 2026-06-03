package com.deundeun.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.deundeun.R;
import com.deundeun.models.Inquiry;
import java.util.List;

public class InquiryListAdapter extends RecyclerView.Adapter<InquiryListAdapter.ViewHolder> {

    private List<Inquiry> inquiries;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Inquiry inquiry);
    }

    public InquiryListAdapter(List<Inquiry> inquiries, OnItemClickListener listener) {
        this.inquiries = inquiries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inquiry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inquiry inquiry = inquiries.get(position);
        holder.tvNo.setText(String.valueOf(position + 1));
        holder.tvCategory.setText(inquiry.getCategory());
        
        if ("Completed".equals(inquiry.getStatus())) {
            holder.tvStatus.setText("완료");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed);
        } else {
            holder.tvStatus.setText("대기");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
        }

        if (holder.tvUser != null) {
            holder.tvUser.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(inquiry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inquiries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNo, tvCategory, tvStatus, tvUser;

        ViewHolder(View itemView) {
            super(itemView);
            tvNo = itemView.findViewById(R.id.tv_inquiry_no);
            tvCategory = itemView.findViewById(R.id.tv_inquiry_category);
            tvStatus = itemView.findViewById(R.id.tv_inquiry_status);
            tvUser = itemView.findViewById(R.id.tv_inquiry_user);
        }
    }
}

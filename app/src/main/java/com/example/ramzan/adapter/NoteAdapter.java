package com.example.ramzan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;

import com.bumptech.glide.Glide;
import com.example.ramzan.R;
import com.example.ramzan.model.Note;

import java.io.File;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    private OnItemClickListener listener;

    NoteAdapter adapter;
    private Context context;

    public NoteAdapter() {
        super(DIFF_CALLBACK);

    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getQuantity() == newItem.getQuantity();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        Uri uri = Uri.parse(currentNote.getImage());
        Context context = holder.imageView_productImage.getContext();


        holder.textView_title.setText("Товар:     " + currentNote.getTitle());
        holder.textView_description.setText("Тип:    " + currentNote.getDescription());
        holder.textView_quantity.setText(String.valueOf("Рейтинг:    " + currentNote.getQuantity()));
        holder.textView_price.setText(String.valueOf("Цена:    " + currentNote.getPrice() + " cом"));

        Glide.with(context).load(uri).into(holder.imageView_productImage);
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

     class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textView_title;
        private TextView textView_description;
        private TextView textView_quantity;
        private TextView textView_price;
        private ImageView imageView_productImage;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.text_view_title);
            textView_description = itemView.findViewById(R.id.text_view_description);
            textView_quantity = itemView.findViewById(R.id.text_view_quantity);
            textView_price = itemView.findViewById(R.id.text_view_price);
            imageView_productImage = itemView.findViewById(R.id.image_view_product);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

//        public void deleteItem() {
//            int position = getAdapterPosition();
//            notifyItemRemoved(position);
//        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}

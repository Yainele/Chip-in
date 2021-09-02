package recycleViewAdapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chipinv01.Event.ExtendedEvent;
import com.example.chipinv01.Event.Member;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import java.util.ArrayList;

public class ExtendedEventAdapter extends FirestoreRecyclerAdapter<Member, RecyclerView.ViewHolder> implements Filterable {
    ArrayList<Member> ChoosenContacts = new ArrayList<>();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ExtendedEventAdapter(@NonNull FirestoreRecyclerOptions<Member> options) {
        super(options);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Member model) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    private class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

        }
    }
    }


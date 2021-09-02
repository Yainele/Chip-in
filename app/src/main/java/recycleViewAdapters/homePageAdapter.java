package recycleViewAdapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chipinv01.Event.Event;
import com.example.chipinv01.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class homePageAdapter extends FirestoreRecyclerAdapter<Event,homePageAdapter.ViewHolder> implements Filterable {

    public homePageAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

    ArrayList<Event> creditsAd;
    List<Event> creditsFull;
    private homePageAdapter.OnItemListener onItemListener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
   //  * @param options
     */

    @Override
    public int getItemViewType(final int position) {
        return R.layout.fragment_home;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setCredits(ArrayList<Event> events) {

        creditsAd = new ArrayList<>();
        if (events.size() == 0){
            Event event = new Event();
            event.setCreditName("пусто");
            event.setCreditTime("99999");
            event.setDeadline("30.02.2042");
            event.setCreditorName("пусто");
            event.setMemberAmount("0/0");
            event.setFullamount("9999999");
            event.setUserAvatar(R.drawable.ic_baseline_check_circle_24);
            events.add(event);

        }
        else {
            for (Event item : events){
                creditsAd.add(item);
            };
        }
        creditsFull = new ArrayList<>(
                events
        );
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_row_homepage, parent, false);
        view.setOnClickListener(new homePageAdapter.RV_ItemListener());
        view.setOnLongClickListener(new homePageAdapter.RV_ItemListener());

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,@NonNull Event event) {
        holder.UserAvatar.setImageAlpha(event.getUserAvatar());
        if( holder.UserAvatar!=null){
            holder.UserAvatar.setImageAlpha(R.drawable.ic_baseline_person_outline_24);
        }
        holder.CreditorName.setText(event.getCreditorName());
        holder.CreditTime.setText(event.getCreditTime());
        holder.Deadline.setText(event.getDeadline());
        holder.CreditName.setText(event.getCreditName());
        holder.Fullamount.setText(event.getFullamount());
        holder.MemberAmount.setText(event.getMemberAmount());
        holder.itemView.setId(position);
    }


    @Override
    public Filter getFilter() {
        return ContactFlter;
    }
    private Filter ContactFlter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event>FilterderContactsList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                FilterderContactsList.addAll(creditsFull);
            }
            else {
                String FilterPattern = constraint.toString().toLowerCase().trim();
                for (Event item: creditsFull){
                    if (item.getCreditName().toLowerCase().contains(FilterPattern)){
                        FilterderContactsList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = FilterderContactsList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            creditsAd.clear();
            creditsAd.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CreditorName, CreditTime, Deadline, CreditName, Fullamount, MemberAmount ;
        ImageView UserAvatar;
        Drawable defaultImage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            UserAvatar = itemView.findViewById(R.id.avatarHomePage);
            CreditorName = itemView.findViewById(R.id.creditornameHomePage);
            CreditTime = itemView.findViewById(R.id.credittimeHomePage);
            Deadline = itemView.findViewById(R.id.DeadlineHomePage);
            CreditName = itemView.findViewById(R.id.creditNameHomePage);
            Fullamount = itemView.findViewById(R.id.fullAmountHomePage);
            MemberAmount = itemView.findViewById(R.id.memberAmountHomePage);



        }

    }
    public  interface  OnItemListener{
        void OnItemClickListener(View view, int position);
        void OnItemLongClickListener(View view, int position);
    }
    class RV_ItemListener implements View.OnClickListener, View.OnLongClickListener{

        @Override
        public void onClick(View view) {
            if (onItemListener != null){
                onItemListener.OnItemClickListener(view, view.getId());


            }
        }
        @Override
        public boolean onLongClick(View view) {
            if (onItemListener != null){
                onItemListener.OnItemLongClickListener(view,view.getId());
            }
            return true;
        }
    }
    public void setOnItemListenerListener(homePageAdapter.OnItemListener listener){
        this.onItemListener = listener;
    }
}

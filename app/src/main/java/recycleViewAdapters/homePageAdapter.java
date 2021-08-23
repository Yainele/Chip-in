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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chipinv01.Event.Credit;
import com.example.chipinv01.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class homePageAdapter extends FirestoreRecyclerAdapter<Credit ,homePageAdapter.ViewHolder> implements Filterable {
    public homePageAdapter(@NonNull FirestoreRecyclerOptions<Credit> options) {
        super(options);
    }

    ArrayList<Credit>CreditsAd;
    List<Credit>CreditsFull;
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
    public void setCredits(ArrayList<Credit> Credits) {

        CreditsAd = new ArrayList<>();
        if (Credits.size() == 0){
            Credit credit = new Credit();
            credit.setCreditName("пусто");
            credit.setCreditTime("99999");
            credit.setDeadline("30.02.2042");
            credit.setCreditorName("пусто");
            credit.setMemberAmount("0/0");
            credit.setFullamount("9999999");
            credit.setUserAvatar(R.drawable.ic_baseline_check_circle_24);
            Credits.add(credit);

        }
        else {
            for (Credit item : Credits){
                CreditsAd.add(item);
            };
        }
        CreditsFull = new ArrayList<>(
                Credits
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,@NonNull Credit credit) {
        holder.UserAvatar.setImageAlpha(credit.getUserAvatar());
        if( holder.UserAvatar!=null){
            holder.UserAvatar.setImageAlpha(R.drawable.ic_baseline_person_outline_24);
        }
        holder.CreditorName.setText(credit.getCreditorName());
        holder.CreditTime.setText(credit.getCreditTime());
        holder.Deadline.setText(credit.getDeadline());
        holder.CreditName.setText(credit.getCreditName());
        holder.Fullamount.setText(credit.getFullamount());
        holder.MemberAmount.setText(credit.getMemberAmount());
        holder.itemView.setId(position);
    }


    @Override
    public Filter getFilter() {
        return ContactFlter;
    }
    private Filter ContactFlter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Credit>FilterderContactsList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                FilterderContactsList.addAll(CreditsFull);
            }
            else {
                String FilterPattern = constraint.toString().toLowerCase().trim();
                for (Credit item: CreditsFull ){
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
            CreditsAd.clear();
            CreditsAd.addAll((List)results.values);
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

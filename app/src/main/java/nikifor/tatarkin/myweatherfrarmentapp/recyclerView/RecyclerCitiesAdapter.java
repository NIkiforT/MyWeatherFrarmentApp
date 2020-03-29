package nikifor.tatarkin.myweatherfrarmentapp.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nikifor.tatarkin.myweatherfrarmentapp.R;

public class RecyclerCitiesAdapter extends RecyclerView.Adapter<RecyclerCitiesAdapter.ViewHolder> {
    private String[] cities;
    private OnItemClickListener itemClickListener;

    public RecyclerCitiesAdapter(String[] cities){
        this.cities = cities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cities_layout, parent, false);
        return new RecyclerCitiesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemCitiesTextView.setText(cities[position]);
    }

    @Override
    public int getItemCount() {
        return cities == null? 0 : cities.length;
    }

    //публичный интерфейс для обработки нажатия.
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    //сеттер слушателя нажатий
    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        TextView listItemCitiesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);

            //Обработка нажатия.
            listItemCitiesTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }

        private void initViews(View itemView) {
            listItemCitiesTextView = itemView.findViewById(R.id.list_item_cities_text_view);
        }
    }
}

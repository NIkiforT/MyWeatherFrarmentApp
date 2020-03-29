package nikifor.tatarkin.myweatherfrarmentapp.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nikifor.tatarkin.myweatherfrarmentapp.R;

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.ViewHolder> {
    private String[] data;

    public TemperatureAdapter(String[] data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_temp_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemTemperatureTextView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data ==null? 0 : data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView listItemTemperatureTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            listItemTemperatureTextView = itemView.findViewById(R.id.list_item_temperature_text_view);
        }
    }
}

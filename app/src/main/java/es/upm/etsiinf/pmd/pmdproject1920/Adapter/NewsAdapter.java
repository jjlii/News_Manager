package es.upm.etsiinf.pmd.pmdproject1920.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<Article>  articles;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public NewsAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_items, parent,false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final  MyViewHolder holder = holders;
        Article model  = articles.get(position);
        holder.news_title.setText(model.getTitleText());
        holder.news_subtitle.setText(model.getSubtitleText());
        holder.news_abstract.setText(model.getAbstractText());
        holder.news_category.setText(model.getCategory());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;

    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class  MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView news_title, news_subtitle, news_abstract, news_category ;
        ImageView imageView;
        OnItemClickListener onItemClickListener;
        public MyViewHolder( View itemView, OnItemClickListener onItemClickListener){
            super(itemView);
            itemView.setOnClickListener(this);
            news_title = itemView.findViewById(R.id.tv_title);
            news_subtitle = itemView.findViewById(R.id.tv_subtitle);
            news_abstract = itemView.findViewById(R.id.tv_abstract);
            news_category = itemView.findViewById(R.id.tv_category);
            imageView = itemView.findViewById(R.id.iv_image);

            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

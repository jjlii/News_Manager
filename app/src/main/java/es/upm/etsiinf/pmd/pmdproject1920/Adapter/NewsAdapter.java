package es.upm.etsiinf.pmd.pmdproject1920.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import es.upm.etsiinf.pmd.pmdproject1920.utils.SerializationUtils;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<Article>  articles;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public NewsAdapter(List<Article> articles, Context context, OnItemClickListener onItemClickListener) {
        this.articles = articles;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
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
        Article article  = articles.get(position);
        holder.news_title.setText(article.getTitleText());
        holder.news_abstract.setText(article.getAbstractText());
        holder.news_category.setText(article.getCategory());
        Bitmap img = SerializationUtils.base64StringToImg(article.getThumbnail());
        holder.imageView.setImageBitmap(img);
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

        TextView news_title, news_abstract, news_category ;
        ImageView imageView;
        OnItemClickListener onItemClickListener;
        public MyViewHolder( View itemView, OnItemClickListener onItemClickListener){
            super(itemView);
            itemView.setOnClickListener(this);
            news_title = itemView.findViewById(R.id.tv_title);
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

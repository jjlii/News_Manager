package es.upm.etsiinf.pmd.pmdproject1920.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.Fragments.TechnologyFragmentDirections;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.SerializationUtils;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;

import static androidx.navigation.Navigation.findNavController;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<Article>  articles;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public NewsAdapter(List<Article> articles, Context context,
                       OnItemClickListener onItemClickListener) {
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
        final Article article  = articles.get(position);
        holder.news_title.setText(article.getTitleText());
        holder.news_abstract.setText(article.getAbstractText());
        holder.news_category.setText(article.getCategory());
        Bitmap img = SerializationUtils.base64StringToImg(article.getThumbnail());
        holder.imageView.setImageBitmap(img);
        holder.bt_rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onDeleteItemClick(v, article.getId(), holder.getAdapterPosition());
            }
        });
        holder.bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onEditItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onEditItemClick(View view, int position);
        void onDeleteItemClick(View view, int articleId, int position);
    }

    public class  MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView news_title, news_abstract, news_category ;
        ImageView imageView;
        ImageButton bt_edit, bt_rm;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(final View itemView, final OnItemClickListener onItemClickListener){
            super(itemView);
            itemView.setOnClickListener(this);
            news_title = itemView.findViewById(R.id.tv_title);
            news_abstract = itemView.findViewById(R.id.tv_abstract);
            news_category = itemView.findViewById(R.id.tv_category);
            imageView = itemView.findViewById(R.id.iv_image);
            bt_edit = itemView.findViewById(R.id.bt_edit);
            bt_rm = itemView.findViewById(R.id.bt_rm);
            if(ModelManager.isConnected()){
                bt_edit.setVisibility(View.VISIBLE);
                bt_rm.setVisibility(View.VISIBLE);
            }else {
                bt_edit.setVisibility(View.GONE);
                bt_rm.setVisibility(View.GONE);
            }
            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

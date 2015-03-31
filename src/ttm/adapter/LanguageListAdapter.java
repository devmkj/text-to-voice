package ttm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import in.mathakharap.ttm.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * List adapter of language selection dialog.
 * Created by shibaprasad on 3/19/2015.
 */
public class LanguageListAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Locale> data;

    /**
     * Public constructor.
     *
     * @param context       context
     * @param language_data language data.
     */
    public LanguageListAdapter(Context context, ArrayList<Locale> language_data) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        data = language_data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    @Deprecated
    public Object getItem(int position) {
        return null;
    }

    @Deprecated
    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<Locale> getData() {
        return this.data;
    }

    /**
     * Get language code by list item position.
     *
     * @param position list item position.
     * @return the language code.
     */
    public String getLanguageCode(int position) {
        String key;
        Locale locale = data.get(position);
        key = locale.toString();
        return key;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_list_row_language, null);

            //init the view holder.
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Locale locale = data.get(position);
        viewHolder.textView.setText(locale.getDisplayLanguage() +
                (locale.getDisplayCountry().length() > 1 ? "(" + locale.getDisplayCountry() + ")" : "" ));
        return convertView;
    }

    private static class ViewHolder {
        public TextView textView;
    }
}

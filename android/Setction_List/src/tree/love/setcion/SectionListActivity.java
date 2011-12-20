
package tree.love.setcion;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SectionListActivity extends Activity
{
    private static final String TAG = "SectionListActivity";
    
    private StandardArrayAdapter arrayAdapter = null;

    private SectionListAdapter sectionAdapter = null;

    private SectionListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return true;
        // return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // return super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.standard_list:
                arrayAdapter = new StandardArrayAdapter(this, R.id.example_text_view, exampleArray);
                sectionAdapter = new SectionListAdapter(getLayoutInflater(), arrayAdapter);
                listView.setAdapter(sectionAdapter);
                return true;
            case R.id.empty_list:
                arrayAdapter = new StandardArrayAdapter(this, R.id.example_text_view,
                        new SectionListItem[] {});
                sectionAdapter = new SectionListAdapter(getLayoutInflater(), arrayAdapter);
                listView.setAdapter(sectionAdapter);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews()
    {
        arrayAdapter = new StandardArrayAdapter(this, R.id.example_text_view, exampleArray);
        sectionAdapter = new SectionListAdapter(getLayoutInflater(), arrayAdapter);
        int listId = getResources().getIdentifier("section_list_view", "id",this.getClass().getPackage().getName());
        Log.d(TAG, "listID="+listId);
        listView = (SectionListView) findViewById(getResources().getIdentifier("section_list_view",
                "id", this.getClass().getPackage().getName()));
        listView.setAdapter(sectionAdapter);
    }

    private class StandardArrayAdapter extends ArrayAdapter<SectionListItem>
    {
        private final SectionListItem[] items;

        public StandardArrayAdapter(Context context, int textViewResourceId, SectionListItem[] items)
        {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // return super.getView(position, convertView, parent);
            View view = convertView;
            if (view == null)
            {
                final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.example_list_view, null);
            }
            final SectionListItem currentItem = items[position];
            if (currentItem != null)
            {
                final TextView textView = (TextView) view.findViewById(R.id.example_text_view);
                if (textView != null)
                {
                    textView.setText(currentItem.item.toString());
                    Log.i(TAG,"getView:"+currentItem.item.toString());
                }
            }
            return view;
        }
    }

    SectionListItem[] exampleArray = {
            new SectionListItem("Test 1 - A", "A"), // 
            new SectionListItem("Test 11 - A", "A"), // 
            new SectionListItem("Test 12 - A", "A"), // 
            new SectionListItem("Test 13 - A", "A"), // 
            new SectionListItem("Test 14 - A", "A"), // 
            new SectionListItem("Test 15 - A", "A"), // 
            new SectionListItem("Test 16- A", "A"), // 
            new SectionListItem("Test 2 - A", "A"), // 
            new SectionListItem("Test 3 - A", "A"), // 
            new SectionListItem("Test 4 - A", "A"), //
            new SectionListItem("Test 5 - A", "A"), //
            new SectionListItem("Test 6 - B", "B"), // 
            new SectionListItem("Test 61 - B", "B"), // 
            new SectionListItem("Test 62 - B", "B"), // 
            new SectionListItem("Test 63 - B", "B"), // 
            new SectionListItem("Test 64 - B", "B"), // 
            new SectionListItem("Test 65 - B", "B"), // 
            new SectionListItem("Test 66 - B", "B"), // 
            new SectionListItem("Test 67 - B", "B"), // 
            new SectionListItem("Test 68 - B", "B"), // 
            new SectionListItem("Test 69 - B", "B"), // 
            new SectionListItem("Test 7 - B", "B"), //
            new SectionListItem("Test 8 - B", "B"), // 
            new SectionListItem("Test 9 - Long",  "Long section"), // 
            new SectionListItem("Test 91 - Long",  "Long section"), // 
            new SectionListItem("Test 92 - Long",  "Long section"), // 
            new SectionListItem("Test 93 - Long",  "Long section"), // 
            new SectionListItem("Test 94 - Long",  "Long section"), // 
            new SectionListItem("Test 95 - Long",  "Long section"), // 
            new SectionListItem("Test 96 - Long",  "Long section"), // 
            new SectionListItem("Test 97 - Long",  "Long section"), // 
            new SectionListItem("Test 98 - Long",  "Long section"), // 
            new SectionListItem("Test 99 - Long",  "Long section"), // 
            new SectionListItem("Test 90 - Long",  "Long section"), // 
            new SectionListItem("Test 9- - Long",  "Long section"), // 
            new SectionListItem("Test 10 - Long", "Long section"), // 
            new SectionListItem("Test 11 - Long", "Long section"), // 
            new SectionListItem("Test 12 - Long", "Long section"), // 
            new SectionListItem("Test 13 - Long", "Long section"), // 
            new SectionListItem("Test 14 - A again", "A"), //         
            new SectionListItem("Test 15 - A again", "A"), //  
            new SectionListItem("Test 16 - A again", "A"), //   
            new SectionListItem("Test 17 - B again", "B"), //  
            new SectionListItem("Test 18 - B again", "B"), //  
            new SectionListItem("Test 19 - B again", "B"), //   
            new SectionListItem("Test 20 - B again", "B"), // 
            new SectionListItem("Test 21 - B again", "B"), //   
            new SectionListItem("Test 22 - B again", "B"), //  
            new SectionListItem("Test 23 - C", "C"), //       
            new SectionListItem("Test 24 - C", "C"), //      
            new SectionListItem("Test 25 - C", "C"), //  
            new SectionListItem("Test 26 - C", "C"), //  
    };
}

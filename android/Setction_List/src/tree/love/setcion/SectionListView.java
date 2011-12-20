package tree.love.setcion;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class SectionListView extends ListView implements OnScrollListener
{
	private static final String TAG = "SectionListView";
    private View transparentView = null ;

    public SectionListView(Context context)
    {
        super(context);
        commonInitialisation();
    }

    public SectionListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        commonInitialisation();
    }

    public SectionListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        commonInitialisation();
    }

    
    public void commonInitialisation()
    {
        setOnScrollListener(this);
        setVerticalFadingEdgeEnabled(false);
        setFadingEdgeLength(0);
    }
    
    @Override
    public void setAdapter(ListAdapter adapter)
    {
        if (!(adapter instanceof SectionListAdapter))
        {
            throw new IllegalArgumentException(
                    "The adapter needs to be of type"
                    +SectionListAdapter.class
                    +"and is"+adapter.getClass()
                    );
        }
        super.setAdapter(adapter);
        final ViewParent parent = getParent();
        if (!(parent instanceof FrameLayout))
        {
            throw new IllegalArgumentException(
                    "Section List should have Framlayout as parent!");
        }
        if (transparentView != null)
        {
            ((FrameLayout)parent).removeView(transparentView);
        }
        transparentView = ((SectionListAdapter)adapter).getTransparentSectionView();
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        ((FrameLayout)parent).addView(transparentView,lp);
        if (adapter.isEmpty())
        {
            transparentView.setVisibility(View.INVISIBLE);
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount)
    {
        final SectionListAdapter adapter = (SectionListAdapter) getAdapter();
        if (adapter != null)
        {
            adapter.makeSectionInvisibleIfFirstInList(firstVisibleItem);
            Log.i(TAG,"firstVisibleItem"+firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        //do nothing
    }

}

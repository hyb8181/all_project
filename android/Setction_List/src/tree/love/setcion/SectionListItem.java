package tree.love.setcion;

/**
 * @author Wuyexiong
 *  Item definition including the section
 */
public class SectionListItem
{
    public Object item = null ;
    public String section = null ;
    
    public SectionListItem(final Object item,final String section)
    {
        super();
        this.item = item;
        this.section = section;
    }
    @Override
    public String toString()
    {
        return item.toString();
    }
}

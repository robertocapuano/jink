package debug.label;

public abstract class AbstractLabel implements Label
{
	protected String label = "";
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel( String _label )
	{
		label = _label;
		LabelManager.getShared().putLabel( this );
	}
	
	public boolean hasLabel()
	{
		return label.length()>0;
	}
	
}

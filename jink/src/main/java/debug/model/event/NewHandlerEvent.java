package debug.model.event;

import debug.model.DetailModel;

public class NewHandlerEvent extends ModelEvent
{
	protected final DetailModel detailModel;
	
	public NewHandlerEvent( DetailModel _detailModel )
	{
		detailModel = _detailModel;
	}
	
	public DetailModel getDetailModel()
	{
		return detailModel;
	}
	
	public void fire( ModelListener listener )
	{
		listener.newHandler( this );
	}
	
	
}

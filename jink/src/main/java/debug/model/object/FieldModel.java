package debug.model.object;

import com.sun.jdi.*;

import debug.model.object.ObjectModel;
import debug.model.DetailModel;

public class FieldModel
{
	protected final ObjectModel object_m;
	protected final Field field;
	protected final DetailModel value;
	
	public FieldModel( ObjectModel _object_m, Field _field, DetailModel _value )
	{
		object_m = _object_m;
		field = _field;
		value = _value;
	}

	public String toString()
	{
		return longDescription();
	}

	public ObjectModel getObject()
	{
		return object_m;
	}
	
	public Field getField()
	{
		return field;
	}
	
	public DetailModel getValue()
	{
		return value;
	}

	public static String fieldShortDescription( Field field )
	{
		return field.toString();
	}
	
	public static String fieldlongDescription( Field field )
	{
		return field.toString();
	}
	
	
	public static String shortDescription( Field field, DetailModel value )
	{
		String desc = fieldShortDescription( field );
		desc += " ";
		desc += value.shortDescription();
		return desc;
	}
	
	public static String longDescription( Field field, DetailModel value )
	{
		String desc = fieldlongDescription( field );
		desc += " ";
		desc += value.longDescription();
		return desc;	
	}
	
	public String shortDescription()
	{
		return shortDescription( field, value );
	}
	
	public String longDescription()
	{
		return longDescription( field, value );
	}
}
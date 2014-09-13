package debug.exec;

import java.io.Serializable;

public interface Agent extends Serializable
{
	Reply perform( ExecManager exec );
}
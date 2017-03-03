package DataHandling;

import com.opera.core.systems.scope.protos.UmsProtos.Format;

public class DataFormat {
	public static String Format(String data){
		data=data.replace("'","");
		return data;
	}

}

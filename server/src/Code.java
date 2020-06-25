import java.io.UnsupportedEncodingException;

public class Code {
	 public static byte[] encode( String data ) {
		 //把字符串转为字节数组
	      byte[] b = data.getBytes();
	      System.out.println(data);
	      //遍历
	      for( int i=0 ; i < b.length ; i++ ) {
	    	  b[i]=(byte)(b[i] + 1 );//在原有的基础上+1
	      }
	      return b;
	  }
	 
	    public static String decode( byte[] data , int length ) {
	        //把字符串转为字节数组
	        byte[] b = new byte[length];
	        System.arraycopy(data, 0, b, 0, length);
	        //遍历
	        for(int i = 0 ; i < length ; i++ ) {
	        	b[i]=(byte)(b[i] - 1 );//在原有的基础上-1
	        }
	        String temp = new String(b);
	        String res = null;
//	        try {
//				//System.out.println(new String(temp.getBytes("gbk"),"utf-8"));
//				//res = new String(temp.getBytes("gbk"),"utf-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	  
	        return new String(b);
	    }
}

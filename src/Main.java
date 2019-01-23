public class Main {

	public static void main(String[] args) throws Exception {
		StringMultiplier m = new StringMultiplier();

		for(int x=-100; x<=2000; x++){
			for(int y=0; y<201; y++){
				int val = x*y;
				System.out.println("("+x+", "+y+")  " + val);
				String sval = m.multiply(""+x, ""+y);
				if(!sval.equals(""+val)){
					throw new Exception("Incorrect value: "+x+", "+y+" = "+val+" != ("+sval+")");
				}
			}
		}//*/
	}

}

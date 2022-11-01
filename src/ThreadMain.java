
public class ThreadMain extends Thread {
	IDMBCMessageService idm;
     
    public ThreadMain(int num){
    	idm = new IDMBCMessageService();
    }
    
    public void run(){
        for(int l = 0;l<1;l++){
           try{
        	   idm.Run();
           }catch(Exception exc){
               
           }
           
        }
    } 
}
